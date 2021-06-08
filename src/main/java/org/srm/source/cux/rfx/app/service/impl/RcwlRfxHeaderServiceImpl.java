package org.srm.source.cux.rfx.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.customize.service.CustomizeClient;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.lov.handler.LovValueHandle;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.boot.event.service.sender.EventSender;
import org.srm.boot.platform.group.GroupApproveHelper;
import org.srm.boot.platform.group.feign.HwfpRemoteService;
import org.srm.boot.pr.app.service.PrManageDomainService;
import org.srm.source.bid.domain.repository.SourceNoticeRepository;
import org.srm.source.bid.domain.service.ISourceNoticeDomainService;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxHeaderDTO;
import org.srm.source.priceLib.app.service.PriceLibServiceService;
import org.srm.source.priceLib.infra.util.LovUtil;
import org.srm.source.rfx.api.dto.HeaderQueryDTO;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.srm.source.rfx.app.service.*;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.app.service.impl.RfxHeaderServiceImpl;
import org.srm.source.rfx.app.service.v2.RfxHeaderServiceV2;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxMember;
import org.srm.source.rfx.domain.repository.*;
import org.srm.source.rfx.domain.service.*;
import org.srm.source.rfx.domain.service.v2.RfxHeaderDomainService;
import org.srm.source.rfx.domain.strategy.CheckSelectionStrategyService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.feign.SitfRemoteService;
import org.srm.source.rfx.infra.mapper.RfxHeaderMapper;
import org.srm.source.rfx.infra.mapper.RfxLineItemMapper;
import org.srm.source.rfx.infra.util.RfxEventUtil;
import org.srm.source.share.app.service.*;
import org.srm.source.share.domain.entity.PrequalHeader;
import org.srm.source.share.domain.entity.ProjectLineSection;
import org.srm.source.share.domain.entity.RoundHeaderDate;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.*;
import org.srm.source.share.domain.service.*;
import org.srm.source.share.infra.feign.HfleRemoteService;
import org.srm.source.share.infra.feign.SmdmRemoteService;
import org.srm.source.share.infra.feign.SpfmRemoteService;
import org.srm.source.share.infra.feign.SsmlRemoteService;
import org.srm.source.share.infra.handle.CommonMultilingualHandle;
import org.srm.source.share.infra.utils.ExcelUtil;
import org.srm.source.share.infra.utils.PromptUtil;
import org.srm.web.annotation.Tenant;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderServiceImpl extends RfxHeaderServiceImpl {

    @Autowired
    private SourceTemplateService sourceTemplateService;
    @Autowired
    private IRfxHeaderDomainService rfxHeaderDomainService;
    @Autowired
    @Lazy
    private IPrequelDomainService prequelDomainService;

    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;
    @Autowired
    private RfxMemberRepository rfxMemberRepository;
    @Autowired
    private CommonQueryRepository commonQueryRepository;
    @Autowired
    @Lazy
    private RfxMemberService rfxMemberService;
    @Autowired
    private RoundHeaderDateRepository roundHeaderDateRepository;
    @Autowired
    private ProjectLineSectionRepository projectLineSectionRepository;

    @Override
    public void releaseRfx(Long organizationId, RfxFullHeader rfxFullHeader) {
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
        Assert.notNull(rfxHeader.getRfxHeaderId(), "header.not.presence");
        SourceTemplate sourceTemplate = this.sourceTemplateService.selectByPrimaryKey(rfxHeader.getTemplateId());
        Assert.notNull(sourceTemplate, "source.template.not.found");
        rfxHeader.beforeReleaseCheck(rfxFullHeader, sourceTemplate);
        rfxHeader.initRfxReleaseInfo(sourceTemplate.getReleaseApproveType());
        rfxHeader.initTotalCoast(rfxFullHeader.getRfxLineItemList());
        RfxFullHeader rtnFullHeader = ((RfxHeaderService)this.self()).saveOrUpdateFullHeader(rfxFullHeader);
        this.rfxHeaderDomainService.validateLineItemTaxRate(rfxFullHeader.getRfxHeader());
        if (BaseConstants.Flag.NO.equals(sourceTemplate.getScoreIndicFlag())) {
            this.checkExpertScore(sourceTemplate, rfxHeader, rfxFullHeader);
        }

        this.validateLadderInquiry(rtnFullHeader);
        this.rfxHeaderDomainService.removeOrValidRfxItemSupAssign(rfxFullHeader);
        PrequalHeader prequalHeader = rfxFullHeader.getPrequalHeader();
        if (null != prequalHeader) {
            prequalHeader.preData(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX");
        }

        this.prequelDomainService.checkPrequalHeader(sourceTemplate, rfxFullHeader.getPrequalHeader());
//        this.chooseReleaseApproveType(organizationId, rfxHeader.getRfxHeaderId(), sourceTemplate);
//        if (!"SELF".equals(sourceTemplate.getReleaseApproveType())) {
//            this.rfxEventUtil.eventSend("SSRC_RFX_RELEASE", "RELEASE", rfxHeader);
//        }
    }

    @Override
    public RfxHeaderDTO selectOneRfxHeader(HeaderQueryDTO headerQueryDTO) {
        RfxHeaderDTO rfxHeaderDTO = this.rfxHeaderRepository.selectOneRfxHeader(headerQueryDTO);
        SourceTemplate sourceTemplate = this.sourceTemplateService.selectByPrimaryKey(rfxHeaderDTO.getTemplateId());
        rfxHeaderDTO.setFastBidding(sourceTemplate.getFastBidding());
        int count = this.rfxMemberRepository.selectCount(new RfxMember(rfxHeaderDTO.getTenantId(), rfxHeaderDTO.getRfxHeaderId(), "OPENED_BY", BaseConstants.Flag.YES));
        if (BaseConstants.Flag.YES.equals(sourceTemplate.getOpenerFlag()) && BaseConstants.Flag.YES.equals(rfxHeaderDTO.getSealedQuotationFlag()) && count == 0) {
            rfxHeaderDTO.setQuotationEndDateChangeFlag(BaseConstants.Flag.YES);
        } else {
            rfxHeaderDTO.setQuotationEndDateChangeFlag(BaseConstants.Flag.NO);
        }

        String allowSourceSupplierStages = (String)this.commonQueryRepository.getAllowSourceSupplierStages(rfxHeaderDTO.getTenantId()).stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
        rfxHeaderDTO.setAllowSourceSupplierStages(allowSourceSupplierStages);
        this.rfxMemberService.handleRfxHeaderPermission(Collections.singletonList(rfxHeaderDTO));
        if (rfxHeaderDTO.getCompanyId() == -1L) {
            rfxHeaderDTO.setCompanyId((Long)null);
        }

        if (StringUtils.isNotBlank(sourceTemplate.getRoundQuotationRule()) && sourceTemplate.getRoundQuotationRule().contains("AUTO")) {
            List<RoundHeaderDate> roundHeaderDates = this.roundHeaderDateRepository.selectByCondition(Condition.builder(RoundHeaderDate.class).where(Sqls.custom().andEqualTo("sourceHeaderId", rfxHeaderDTO.getRfxHeaderId()).andEqualTo("tenantId", rfxHeaderDTO.getTenantId()).andEqualTo("autoFlag", BaseConstants.Flag.YES).andEqualTo("roundNumber", rfxHeaderDTO.getRoundNumber())).build());
            rfxHeaderDTO.setRoundHeaderDates(roundHeaderDates);
            if (!"NEW".equals(rfxHeaderDTO.getRfxStatus()) && !"ROUNDED".equals(rfxHeaderDTO.getRfxStatus()) && !"RELEASE_APPROVING".equals(rfxHeaderDTO.getRfxStatus()) && !"RELEASE_REJECTED".equals(rfxHeaderDTO.getRfxStatus())) {
                RoundHeaderDate roundHeaderDateParam = new RoundHeaderDate(rfxHeaderDTO.getRfxHeaderId(), rfxHeaderDTO.getTenantId(), rfxHeaderDTO.getRoundNumber());
                roundHeaderDateParam.setRoundQuotationEndDate(new Date());
                roundHeaderDateParam.setAutoFlag(BaseConstants.Flag.YES);
                int finishedCount = this.roundHeaderDateRepository.selectFinishedRoundCount(roundHeaderDateParam);
                if ((long)finishedCount < rfxHeaderDTO.getQuotationRounds()) {
                    rfxHeaderDTO.setCurrentQuotationRound((long)finishedCount + 1L);
                }
            }
        }

        if (BaseConstants.Flag.YES.equals(rfxHeaderDTO.getMultiSectionFlag())) {
            ProjectLineSection projectLineSectionParam = new ProjectLineSection();
            projectLineSectionParam.setTenantId(rfxHeaderDTO.getTenantId());
            projectLineSectionParam.setTempSourceHeaderId(rfxHeaderDTO.getRfxHeaderId());
            projectLineSectionParam.setSourceProjectId(rfxHeaderDTO.getSourceProjectId());
            List<ProjectLineSection> projectLineSections = this.projectLineSectionRepository.listProjectLineSections(projectLineSectionParam);
            Iterator var13 = projectLineSections.iterator();

            while(var13.hasNext()) {
                ProjectLineSection projectLineSection = (ProjectLineSection)var13.next();
                int itemCount = this.rfxLineItemRepository.selectCountByCondition(Condition.builder(RfxLineItem.class).where(Sqls.custom().andEqualTo("rfxHeaderId", rfxHeaderDTO.getRfxHeaderId()).andEqualTo("projectLineSectionId", projectLineSection.getProjectLineSectionId())).build());
                projectLineSection.setProjectItemCount((long)itemCount);
            }

            rfxHeaderDTO.setProjectLineSections(projectLineSections);
        }
        //返回DTO增加字段
        RcwlRfxHeaderDTO rcwlRfxHeaderDTO = new RcwlRfxHeaderDTO();
        RfxHeader rfxHeader = new RfxHeader();
        rfxHeader.setRfxHeaderId(headerQueryDTO.getRfxHeaderId());
        rfxHeader.setTenantId(headerQueryDTO.getTenantId());
        BeanUtils.copyProperties(rfxHeaderDTO,rcwlRfxHeaderDTO);
        rcwlRfxHeaderDTO.setBargainRedactFlag(rfxHeaderRepository.selectOne(rfxHeader).getBargainRedactFlag());

        return rcwlRfxHeaderDTO;
    }
}
