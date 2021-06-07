package org.srm.source.cux.rfx.app.service.v2.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.customize.service.CustomizeClient;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.lov.handler.LovValueHandle;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.bid.app.service.SourceNoticeService;
import org.srm.source.bid.domain.entity.SourceNotice;
import org.srm.source.bid.domain.service.ISourceNoticeDomainService;
import org.srm.source.rfx.app.service.*;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.app.service.v2.RfxHeaderServiceV2;
import org.srm.source.rfx.app.service.v2.impl.RfxHeaderServiceV2Impl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;
import org.srm.source.rfx.domain.entity.RfxMember;
import org.srm.source.rfx.domain.repository.*;
import org.srm.source.rfx.domain.service.*;
import org.srm.source.rfx.domain.service.v2.RfxHeaderDomainService;
import org.srm.source.rfx.domain.strategy.CheckSaveStrategyContext;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.util.RfxEventUtil;
import org.srm.source.share.api.dto.PrequalHeaderDTO;
import org.srm.source.share.app.service.*;
import org.srm.source.share.domain.entity.*;
import org.srm.source.share.domain.repository.*;
import org.srm.source.share.domain.service.IEvaluateDomainService;
import org.srm.source.share.domain.service.IPrequelDomainService;
import org.srm.source.share.infra.feign.HfleRemoteService;
import org.srm.source.share.infra.feign.HiamRemoteService;
import org.srm.source.share.infra.feign.SpfmRemoteService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RcwlRfxHeaderServiceV2Impl extends RfxHeaderServiceV2Impl {

    @Autowired
    private SourceTemplateService sourceTemplateService;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxMemberService rfxMemberService;
    @Autowired
    private IRfxActionDomainService rfxActionDomainService;
    @Autowired
    private RfxEventUtil rfxEventUtil;
    @Autowired
    private IRfxHeaderDomainService rfxHeaderDomainService;
    @Autowired
    private org.srm.source.share.domain.service.v2.IPrequelDomainService prequelDomainServiceV2;
    @Autowired
    private IEvaluateDomainService evaluateDomainService;
    @Autowired
    private ISourceNoticeDomainService sourceNoticeDomainService;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private RfxMemberRepository rfxMemberRepository;
    @Autowired
    private PrequalMemberService prequalNumberService;
    @Autowired
    private RfxHeaderService rfxHeaderServiceV1;
    @Autowired
    private PrequalHeaderService prequalHeaderService;
    @Autowired
    private PrequalHeaderRepository prequalHeaderRepository;
    @Autowired
    private PrequalScoreAssignRepository prequalScoreAssignRepository;
    @Autowired
    private RfxLineItemService rfxLineItemService;
    @Autowired
    private RfxHeaderDomainService rfxHeaderDomainServiceV2;

    @Override
    public RfxFullHeader saveOrUpdateFullHeader(RfxFullHeader rfxFullHeader) {
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
        if (rfxHeader.getCompanyId() == null) {
            rfxHeader.setCompanyId(-1L);
        }

        if (StringUtils.isBlank(rfxHeader.getCurrencyCode())) {
            rfxHeader.setCurrencyCode("CNY");
        }

        Assert.notNull(rfxHeader.getTemplateId(), "error.source_template_not_selected");
        SourceTemplate sourceTemplate = this.sourceTemplateService.selectByPrimaryKey(rfxHeader.getTemplateId());
        Assert.notNull(sourceTemplate, "error.source_template_id_not_found");
        this.redisHelper.strSet("ssrc:rfx:source:template:" + rfxHeader.getTemplateId(), this.redisHelper.toJson(sourceTemplate), 3L, TimeUnit.MINUTES);
        PrequalHeader prequalHeader = rfxFullHeader.getPrequalHeader();
        List<EvaluateIndic> evaluateIndics = rfxFullHeader.getEvaluateIndics();
        List<EvaluateIndic> initialReviewIndicList = rfxFullHeader.getInitialReviewIndicList();
        List<RfxLineItem> rfxLineItemList = rfxFullHeader.getRfxLineItemList();
        List<RfxLineSupplier> rfxLineSupplierList = rfxFullHeader.getRfxLineSupplierList();
        List<RfxMember> rfxMemberList = rfxFullHeader.getRfxMemberList();
        List<PrequalMember> prequalMemberList = rfxFullHeader.getPrequalMemberList();
        Integer newFlag = 0;
        if (rfxHeader.getRfxHeaderId() == null) {
            newFlag = 1;
            rfxHeader = ((RfxHeaderServiceV2)this.self()).createHeader(rfxHeader);
            Long rfxHeaderId = rfxHeader.getRfxHeaderId();
            evaluateIndics.forEach((evaluateIndic) -> {
                evaluateIndic.setSourceHeaderId(rfxHeaderId);
                evaluateIndic.setSourceFrom("RFX");
            });
            initialReviewIndicList.forEach((initialReviewIndic) -> {
                initialReviewIndic.setSourceHeaderId(rfxHeaderId);
                initialReviewIndic.setSourceFrom("RFX");
            });
        } else {
            this.rfxHeaderServiceV1.changeTemplate(rfxFullHeader);
            this.rfxMemberRepository.delete(new RfxMember(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId()));
            rfxFullHeader.getRfxMemberList().forEach((rfxMember) -> {
                rfxMember.setRfxMemberId((Long)null);
            });
        }

//        rfxMemberList.stream().filter((e) -> {
//            return "OPENED_BY".equals(e.getRfxRole());
//        }).forEach((rfxMember) -> {
//            rfxMember.setPasswordFlag(rfxFullHeader.getRfxHeader().getPasswordFlag() == null ? BaseConstants.Flag.NO : rfxFullHeader.getRfxHeader().getPasswordFlag());
//        });
        this.rfxMemberService.save(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), rfxMemberList);
        SourceNotice sourceNotice = rfxFullHeader.getSourceNotice();
        if (!Objects.isNull(sourceNotice)) {
            sourceNotice.setPurName(rfxHeader.getPurName());
            sourceNotice.setPurEmail(rfxHeader.getPurEmail());
            sourceNotice.setPurPhone(rfxHeader.getPurPhone());
        }

        this.sourceNoticeDomainService.processRfxNotice(rfxHeader, rfxFullHeader.getSourceNotice());
        this.prequelDomainServiceV2.createOrUpdateRfxPrequelHeader(sourceTemplate, rfxFullHeader.getRfxHeader().getQuotationStartDate(), prequalHeader, rfxHeader);
        PrequalHeaderDTO prequalHeaderDTO = this.prequalHeaderRepository.selectPrequalHeaderByRfxHeaderId(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId());
        if (prequalHeaderDTO != null) {
            if (BaseConstants.Flag.YES.equals(prequalHeaderDTO.getEnableScoreFlag())) {
                this.prequalHeaderService.createOrUpdateScoreIndicate(rfxFullHeader.getPrequalScoreAssigns(), prequalHeaderDTO.getPrequalHeaderId(), rfxHeader.getTenantId());
            } else {
                this.prequalScoreAssignRepository.delete(new PrequalScoreAssign(prequalHeaderDTO.getPrequalHeaderId(), prequalHeaderDTO.getTenantId()));
            }
        }

        this.prequalNumberService.createOrUpdatePrequalMembers(rfxHeader.getTenantId(), "RFX", rfxHeader.getRfxHeaderId(), prequalMemberList);
        this.rfxHeaderDomainService.processRfxLineItem(rfxHeader, rfxLineItemList);
        this.rfxHeaderDomainService.processRfxLineSupplier(rfxHeader, rfxLineSupplierList);
        this.rfxLineItemService.checkRfxSuppliers(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId());
        BiddingWorkDTO evaluateExperts = this.getBiddingWorkDTO(rfxFullHeader, rfxHeader);
        this.evaluateDomainService.createOrUpdateEvaluateExpert(rfxFullHeader.getRfxHeader().getTenantId(), evaluateExperts, "SUBMITTED");
        this.evaluateDomainService.createOrUpdateEvaluateEvaluateIndic(rfxFullHeader.getRfxHeader().getTenantId(), evaluateIndics, "SUBMITTED");
        this.evaluateDomainService.createOrUpdateInitialReviewIndic(rfxFullHeader.getRfxHeader().getTenantId(), rfxFullHeader.getInitialReviewIndicList(), "SUBMITTED");
        this.rfxHeaderServiceV1.initBudget(rfxHeader);
        this.rfxHeaderDomainServiceV2.processRoundQuotationDate(sourceTemplate, rfxHeader, rfxFullHeader.getRoundHeaderDates());
        this.rfxHeaderDomainServiceV2.processMultiSections(rfxFullHeader.getProjectLineSections());
        if (BaseConstants.Flag.YES.equals(newFlag)) {
            this.rfxActionDomainService.insertAction(rfxHeader, "CREATE", (String)null);
            this.rfxEventUtil.eventSend("SSRC_RFX_CREATE", "CREATE", rfxHeader);
        } else {
            this.rfxHeaderRepository.updateRfxHeader(rfxHeader);
        }

        this.redisHelper.delKey("ssrc:rfx:source:template:" + rfxHeader.getTemplateId());
        return rfxFullHeader;
    }
}
