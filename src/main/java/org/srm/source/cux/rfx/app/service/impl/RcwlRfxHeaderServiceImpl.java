package org.srm.source.cux.rfx.app.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.rfx.api.dto.BargainProcessDTO;
import org.srm.source.rfx.api.dto.HeaderQueryDTO;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.srm.source.rfx.api.dto.SupplierItemDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxMemberService;
import org.srm.source.rfx.app.service.impl.RfxHeaderServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxMember;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.repository.RfxMemberRepository;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.share.app.service.EvaluateIndicAssignService;
import org.srm.source.share.app.service.SourceTemplateService;
import org.srm.source.share.domain.entity.*;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
import org.srm.source.share.domain.repository.ProjectLineSectionRepository;
import org.srm.source.share.domain.repository.RoundHeaderDateRepository;
import org.srm.source.share.domain.repository.SourceTemplateRepository;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.web.annotation.Tenant;

@Service
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxHeaderServiceImpl extends RfxHeaderServiceImpl {
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private EvaluateIndicAssignService evaluateIndicAssignService;
    @Autowired
    private SourceTemplateRepository sourceTemplateRepository;
    @Autowired
    private SourceTemplateService sourceTemplateService;
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
    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;
    @Override
    public RfxHeader copyRfxHeader(Long rfxHeaderId) {
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
        Assert.notNull(rfxHeader, "error.rfx_header.not_found");
        rfxHeader.setCopyRfxHeaderId(rfxHeaderId);
        rfxHeader.setRfxHeaderId((Long)null);
        rfxHeader.setObjectVersionNumber((Long)null);
        rfxHeader.setRfxStatus("NEW");
        rfxHeader.setQuotationStartDate((Date)null);
        rfxHeader.setQuotationEndDate((Date)null);
        rfxHeader.setRoundNumber(1L);
        rfxHeader.setVersionNumber(1L);
        rfxHeader.setReleasedDate((Date)null);
        rfxHeader.setReleasedBy((Long)null);
        rfxHeader.setTerminatedDate((Date)null);
        rfxHeader.setTerminatedBy((Long)null);
        rfxHeader.setTerminatedRemark((String)null);
        rfxHeader.setApprovedDate((Date)null);
        rfxHeader.setApprovedBy((Long)null);
        rfxHeader.setApprovedRemark((String)null);
        rfxHeader.setTimeAdjustedDate((Date)null);
        rfxHeader.setTimeAdjustedBy((Long)null);
        rfxHeader.setTimeAdjustedRemark((String)null);
        rfxHeader.setPretrailRemark((String)null);
        rfxHeader.setPretrialUserId((Long)null);
        rfxHeader.setPretrialUuid((String)null);
        rfxHeader.setPreAttachmentUuid((String)null);
        rfxHeader.setBackPretrialRemark((String)null);
        rfxHeader.setPretrialStatus((String)null);
        rfxHeader.setHandDownDate((Date)null);
        rfxHeader.setStartQuotationRunningDuration((BigDecimal)null);
        rfxHeader.setLatestQuotationEndDate((Date)null);
        rfxHeader.setBargainStatus((String)null);
        rfxHeader.setBargainEndDate((Date)null);
        rfxHeader.setCheckRemark((String)null);
        rfxHeader.setCheckAttachmentUuid((String)null);
        rfxHeader.setCheckedBy((Long)null);
        rfxHeader.setCheckFinishedDate((Date)null);
        rfxHeader.setTotalCost((BigDecimal)null);
        rfxHeader.setCostRemark((String)null);
        rfxHeader.setCurrentSequenceNum(BidConstants.BidHeader.CurrentSequenceNum.FIRST);
        rfxHeader.setSourceFrom("MANUAL");
        rfxHeader.setScoreProcessFlag(BaseConstants.Flag.NO);
        rfxHeader.setProjectLineSectionId((Long)null);
        rfxHeader.setSourceProjectId((Long)null);
        rfxHeader.setMultiSectionFlag(BaseConstants.Flag.NO);
        rfxHeader.setAttributeVarchar2("0");
        rfxHeader.setAttributeVarchar3("0");
        rfxHeader.setAttributeVarchar4("0");
        rfxHeader.setAttributeVarchar5("0");
        rfxHeader.setAttributeVarchar6("0");
        rfxHeader.setAttributeVarchar7("0");
        return ((RfxHeaderService)this.self()).saveOrUpdateHeader(rfxHeader);
    }

    @Override
    public void checkExpertScore(SourceTemplate sourceTemplate, RfxHeader rfxHeader, RfxFullHeader rfxFullHeader) {
        if ("ONLINE".equals(sourceTemplate.getExpertScoreType())) {
            List<EvaluateIndic> evaluateIndics = this.checkIndic(rfxHeader.getRfxHeaderId(), sourceTemplate, rfxHeader.getTenantId(), (String)null);
            if (!CollectionUtils.isNotEmpty(evaluateIndics)) {
                if (BaseConstants.Flag.YES.equals(rfxFullHeader.getOpenSourceFlag())) {
                    throw new CommonException("error.evaluate_indicate_can_not_null_open", new Object[0]);
                }

                throw new CommonException("error.evaluate_indicate_can_not_null", new Object[0]);
            }

            rfxFullHeader.setEvaluateIndics(evaluateIndics);
            Iterator var5 = rfxFullHeader.getEvaluateIndics().iterator();

            while(var5.hasNext()) {
                EvaluateIndic evaluateIndic = (EvaluateIndic)var5.next();
                if ("AUTO".equals(evaluateIndic.getCalculateType()) && !"ALL_QUOTATION".equals(rfxHeader.getQuotationScope())) {
                    if (BaseConstants.Flag.YES.equals(rfxFullHeader.getOpenSourceFlag())) {
                        throw new CommonException("error.quotation_type_open", new Object[0]);
                    }

                    throw new CommonException("error.quotation_type", new Object[0]);
                }
            }

            BiddingWorkDTO biddingWorkDto = rfxFullHeader.getEvaluateExperts();
            if (BaseConstants.Flag.YES.equals(rfxFullHeader.getOpenSourceFlag())) {
                Assert.notNull(biddingWorkDto, "error.leader_flag_at_least_one_open");
            } else {
                Assert.notNull(biddingWorkDto, "error.leader_flag_at_least_one");
            }

            List<EvaluateExpert> evaluateExperts = this.rfxHeaderRepository.checkExpertIndicAssign(sourceTemplate.getInitialReview(), rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId());
            Iterator var7 = evaluateExperts.iterator();

            while(var7.hasNext()) {
                EvaluateExpert evaluateExpert = (EvaluateExpert)var7.next();
                if (0 == evaluateExpert.getAssignCount()) {
                    if (BaseConstants.Flag.YES.equals(rfxFullHeader.getOpenSourceFlag())) {
                        throw new CommonException("error.expert_indic_assign_open", new Object[]{evaluateExpert.getExpertName()});
                    }

                    throw new CommonException("error.expert_indic_assign", new Object[]{evaluateExpert.getExpertName()});
                }
            }

            this.evaluateIndicAssignService.checkExpertAssignCorrectIndic(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX");
            biddingWorkDto.setSourceOpenFlag(rfxFullHeader.getOpenSourceFlag());
            this.validateEvaluateLeaderFlag(biddingWorkDto, rfxHeader.getTenantId());
            sourceTemplate.setSourceOpenFlag(rfxFullHeader.getOpenSourceFlag());
            biddingWorkDto.checkoutWeight(rfxFullHeader.getEvaluateIndics(), sourceTemplate);
        }

    }

    @Override
    public void validateEvaluateLeaderFlag(BiddingWorkDTO biddingWorkDTO, Long organizationId) {
        List<EvaluateExpert> evaluateExpertList = biddingWorkDTO.getEvaluateExpertList();
        if (BaseConstants.Flag.YES.equals(biddingWorkDTO.getSourceOpenFlag())) {
            Assert.notEmpty(evaluateExpertList, "error.leader_flag_at_least_one_open");
        } else {
            Assert.notEmpty(evaluateExpertList, "error.leader_flag_at_least_one");
        }

        int selectCount = this.evaluateExpertRepository.selectCountByCondition(Condition.builder(EvaluateExpert.class).andWhere(Sqls.custom().andEqualTo("tenantId", organizationId).andEqualTo("sourceHeaderId", ((EvaluateExpert)evaluateExpertList.get(0)).getSourceHeaderId()).andEqualTo("sourceFrom", ((EvaluateExpert)evaluateExpertList.get(0)).getSourceFrom()).andEqualTo("evaluateLeaderFlag", 1)).build());
        if (selectCount != 1) {
            if (BaseConstants.Flag.YES.equals(biddingWorkDTO.getSourceOpenFlag())) {
                throw new CommonException("error.evaluate_leader_flag_repeat_open", new Object[0]);
            } else {
                throw new CommonException("error.evaluate_at_least_one_leader", new Object[0]);
            }
        }
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
        //当寻源模板为直委，去掉对注册的筛选
        if("直委".equals(rfxHeaderDTO.getTemplateName())) allowSourceSupplierStages+=",注册";
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

        return rfxHeaderDTO;
    }
    @Override
    public List<BargainProcessDTO> listBargainProcess(Long organizationId, Long rfxLineItemId) {
        SupplierItemDTO supplierItemDTO = new SupplierItemDTO();
        supplierItemDTO.setTenantId(organizationId);
        supplierItemDTO.setRfxLineItemId(rfxLineItemId);
        List<SupplierItemDTO> supplierItemDTOList = rfxHeaderRepository.listBargainProcess(supplierItemDTO);
        if (CollectionUtils.isEmpty(supplierItemDTOList)) {
            return Collections.emptyList();
        }
        List<BargainProcessDTO> processDTOList = new ArrayList<>();
        //supplierCompanyId = null 的供应商，说明是手动录入的，不是从srm中选出来的，就算供应商名字跟srm中的供应商名字一样，也认为是两家不同的供应商
        Map<Long, List<SupplierItemDTO>> collect = supplierItemDTOList.stream().filter(e -> e.getSupplierCompanyId() != null).collect(Collectors.groupingBy(SupplierItemDTO::getSupplierCompanyId));
        collect.forEach((key, val) -> {
            val.forEach(SupplierItemDTO::findQuotationName);
            BargainProcessDTO bargainProcessDTO = new BargainProcessDTO();
            bargainProcessDTO.setSupplierItemDTOList(val);
            bargainProcessDTO.setSupplierCompanyName(val.get(0).getSupplierCompanyName());
            bargainProcessDTO.setSupplierCompanyId(key);
            processDTOList.add(bargainProcessDTO);
        });
        return processDTOList;
    }

    @Override
    public List<BargainProcessDTO> listBargainProcessHistogram(Long organizationId, SupplierItemDTO supplierItemDTO) {
        Assert.notNull(supplierItemDTO.getRfxHeaderId(),BaseConstants.ErrorCode.NOT_NULL);
        supplierItemDTO.setTenantId(organizationId);
        List<SupplierItemDTO> supplierItemDTOList = rfxHeaderRepository.listBargainProcess(supplierItemDTO);
        if (CollectionUtils.isEmpty(supplierItemDTOList)) {
            return Collections.emptyList();
        }
        //获取基准价配置
        RfxHeader rfxHeader = rfxHeaderRepository.selectByPrimaryKey(supplierItemDTOList.get(BaseConstants.Digital.ZERO).getRfxHeaderId());
        Assert.notNull(rfxHeader, ShareConstants.ErrorCode.ERROR_NOT_NULL);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("company",rfxHeader.getCompanyId().toString());
        parameter.put("sourceCategory", rfxHeader.getSourceCategory());
        SourceTemplate sourceTemplate = sourceTemplateRepository.selectByPrimaryKey(rfxHeader.getTemplateId());
        parameter.put("sourceTemplate", sourceTemplate.getTemplateNum());
        String priceTypeCode = CnfHelper.select(rfxHeader.getTenantId(), ShareConstants.ConfigCenterCode.QUOTATION_SET, String.class).invokeWithParameter(parameter);
        List<SupplierItemDTO> resultSupplierItemDTO = new ArrayList<>();
        //根据报价头,报价次数,轮次分租
        Map<String, List<SupplierItemDTO>> supplierItemDTOMap = supplierItemDTOList.stream().collect(Collectors.groupingBy(SupplierItemDTO::initGroupKey));
        for (Map.Entry<String, List<SupplierItemDTO>> supplierItemEntry : supplierItemDTOMap.entrySet()) {
            List<SupplierItemDTO> supplierItemDTOS = supplierItemEntry.getValue();
            if (CollectionUtils.isNotEmpty(supplierItemDTOS)){

                //计算每一个供应商每次报价对所有物品报价的总金额
                BigDecimal totalQuotationNetAmount = supplierItemDTOS.stream().map(SupplierItemDTO::getQuotationNetAmount).reduce(BigDecimal::add).orElse(null);
                BigDecimal totalQuotationTaxAmount = supplierItemDTOS.stream().map(SupplierItemDTO::getQuotationAmount).reduce(BigDecimal::add).orElse(null);
                BigDecimal totalQuotationAmount = SourceConstants.PriceTypeCode.TAX_INCLUDED_PRICE.equals(priceTypeCode) ? totalQuotationTaxAmount : totalQuotationNetAmount;
                SupplierItemDTO firstSupplierItemDTO = supplierItemDTOS.get(BaseConstants.Digital.ZERO);
                firstSupplierItemDTO.setTotalQuotationTaxAmount(totalQuotationTaxAmount);
                firstSupplierItemDTO.setTotalQuotationNetAmount(totalQuotationNetAmount);
                firstSupplierItemDTO.setTotalQuotationAmount(totalQuotationAmount);
                resultSupplierItemDTO.add(firstSupplierItemDTO);
            }
        }
        List<BargainProcessDTO> processDTOList = new ArrayList<>();
        //supplierCompanyId = null 的供应商，说明是手动录入的，不是从srm中选出来的，就算供应商名字跟srm中的供应商名字一样，也认为是两家不同的供应商
        Map<Long, List<SupplierItemDTO>> collect = resultSupplierItemDTO.stream().filter(e -> e.getSupplierCompanyId() != null).collect(Collectors.groupingBy(SupplierItemDTO::getSupplierCompanyId));
        collect.forEach((key, val) -> {
            val = val.stream().sorted(Comparator.comparing(SupplierItemDTO::getQuotationCount)).collect(Collectors.toList());
            val.forEach(SupplierItemDTO::findQuotationName);
            BargainProcessDTO bargainProcessDTO = new BargainProcessDTO();
            bargainProcessDTO.setSupplierItemDTOList(val);
            bargainProcessDTO.setSupplierCompanyName(val.get(0).getSupplierCompanyName());
            bargainProcessDTO.setSupplierCompanyId(key);
            processDTOList.add(bargainProcessDTO);
        });
        return processDTOList;

    }

}
