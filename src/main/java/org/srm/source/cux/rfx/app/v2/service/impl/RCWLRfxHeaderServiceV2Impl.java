package org.srm.source.cux.rfx.app.v2.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.customize.service.CustomizeClient;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.lov.handler.LovValueHandle;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.bid.app.service.SourceNoticeService;
import org.srm.source.bid.domain.entity.SourceNotice;
import org.srm.source.bid.domain.service.ISourceNoticeDomainService;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.cux.share.infra.constant.SourceBaseConstant;
import org.srm.source.rfx.api.dto.FieldPropertyDTO;
import org.srm.source.rfx.api.dto.HeaderAdjustDateDTO;
import org.srm.source.rfx.api.dto.SourceHeaderDTO;
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
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.source.share.infra.feign.HfleRemoteService;
import org.srm.source.share.infra.feign.HiamRemoteService;
import org.srm.source.share.infra.feign.SpfmRemoteService;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/21 10:12
 * @version:1.0
 */
@Service
@Tenant(SourceBaseConstant.TENANT_NUM)
public class RCWLRfxHeaderServiceV2Impl extends RfxHeaderServiceV2Impl {

    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private SourceTemplateService sourceTemplateService;
    @Autowired
    private SourceMatterConfService sourceMatterConfService;
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
    private IPrequelDomainService prequelDomainService;
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
    private IFlowNodeDomainService iFlowNodeDomainService;
    @Autowired
    private SourceTemplateRepository sourceTemplateRepository;
    @Autowired
    private PrequalHeaderService prequalHeaderService;
    @Autowired
    private PrequalHeaderRepository prequalHeaderRepository;
    @Autowired
    private PrequalScoreAssignRepository prequalScoreAssignRepository;
    @Autowired
    private RfxLineItemService rfxLineItemService;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private EvaluateIndicRepository evaluateIndicRepository;
    @Autowired
    private RfxHeaderDomainService rfxHeaderDomainServiceV2;
    @Autowired
    private CheckSaveStrategyContext checkSaveStrategyContext;
    @Autowired
    private SpfmRemoteService spfmRemoteService;
    @Autowired
    private HfleRemoteService hfleRemoteService;
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private LovValueHandle lovValueHandle;
    @Autowired
    private SourceNoticeService sourceNoticeService;
    @Autowired
    private HiamRemoteService hiamRemoteService;
    @Autowired
    private UserUnitService userUnitService;
    @Autowired
    private CommonQueryRepository commonQueryRepository;
    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;
    @Autowired
    private RfxLineSupplierRepository rfxLineSupplierRepository;
    @Autowired
    private ProjectLineSectionRepository projectLineSectionRepository;
    @Autowired
    private RfxLineSupplierService rfxLineSupplierService;
    @Autowired
    private IRfxLineSupplierDomainService rfxLineSupplierDomainService;
    @Autowired
    private IRfxLineItemDomainService rfxLineItemDomainService;
    @Autowired
    private ProjectLineItemRepository projectLineItemRepository;
    @Autowired
    private ProjectLineItemService projectLineItemService;
    @Autowired
    private SourceProjectRepository sourceProjectRepository;
    @Autowired
    private RoundHeaderDateRepository roundHeaderDateRepository;
    @Autowired
    private CustomizeClient customizeClient;
    @Autowired
    private SendMessageHandle sendMessageHandle;
    private static final Logger LOGGER = LoggerFactory.getLogger(RfxHeaderServiceV2Impl.class);

    public RCWLRfxHeaderServiceV2Impl() {
    }

    /**
     *
     * @param rfxFullHeader
     * @return
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public RfxFullHeader saveOrUpdateFullHeader(RfxFullHeader rfxFullHeader) {
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
        if (rfxHeader.getCompanyId() == null) {
            rfxHeader.setCompanyId(-1L);
        }

        if (StringUtils.isBlank(rfxHeader.getCurrencyCode())) {
            rfxHeader.setCurrencyCode("CNY");
        }
        //发布即开始 时间校验
        Assert.notNull(rfxHeader.getStartQuotationRunningDuration(), "error.no.start_quotation_running_duration");
        //发布即开始小于三天则报错
        BigDecimal threeDays = new BigDecimal(3 * 24 * 60);
        if (threeDays.compareTo(rfxHeader.getStartQuotationRunningDuration()) == 1) {
            throw new CommonException("error.start_quotation_running_duration.less.then.three_days");
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

        rfxMemberList.stream().filter((e) -> {
            return "OPENED_BY".equals(e.getRfxRole());
        }).forEach((rfxMember) -> {
            rfxMember.setPasswordFlag(rfxFullHeader.getRfxHeader().getPasswordFlag() == null ? BaseConstants.Flag.NO : rfxFullHeader.getRfxHeader().getPasswordFlag());
        });
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


    @Override
    public void processQuotationEndDateField(List<FieldPropertyDTO> fieldPropertyDTOList, HeaderAdjustDateDTO headerAdjustDateDTO, SourceHeaderDTO sourceHeaderDTO) {
        Date now = new Date();
        boolean flag = Arrays.asList("AUTO", "AUTO_SCORE", "AUTO_CHECK").contains(sourceHeaderDTO.getRoundQuotationRule());
        if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                || RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(sourceHeaderDTO.getSourceCategory())) && BaseConstants.Flag.YES.equals(sourceHeaderDTO.getQuotationEndDateFlag()) && !flag && null != sourceHeaderDTO.getQuotationEndDate() && now.compareTo(sourceHeaderDTO.getQuotationEndDate()) < 0 && Arrays.asList("IN_PREQUAL", "PENDING_PREQUAL", "NOT_START", "IN_QUOTATION").contains(sourceHeaderDTO.getRfxStatus())) {
            this.processFieldProperty(fieldPropertyDTOList, "quotationEndDate", headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.YES);
        } else if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(sourceHeaderDTO.getSourceCategory())) && !flag && null != sourceHeaderDTO.getQuotationEndDate() && now.compareTo(sourceHeaderDTO.getQuotationEndDate()) >= 0 && Arrays.asList("LACK_QUOTED").contains(sourceHeaderDTO.getRfxStatus())) {
            this.processFieldProperty(fieldPropertyDTOList, "quotationEndDate", headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.YES);
        } else if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(sourceHeaderDTO.getSourceCategory())) && BaseConstants.Flag.NO.equals(sourceHeaderDTO.getQuotationEndDateFlag()) && !flag && (null == sourceHeaderDTO.getQuotationEndDate() || now.compareTo(sourceHeaderDTO.getQuotationEndDate()) < 0) && Arrays.asList("IN_PREQUAL", "PENDING_PREQUAL", "NOT_START", "IN_QUOTATION").contains(sourceHeaderDTO.getRfxStatus())) {
            this.processFieldProperty(fieldPropertyDTOList, "quotationEndDate", headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.YES);
        } else {
            this.processFieldProperty(fieldPropertyDTOList, "quotationEndDate", headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.NO);
        }
    }
}
