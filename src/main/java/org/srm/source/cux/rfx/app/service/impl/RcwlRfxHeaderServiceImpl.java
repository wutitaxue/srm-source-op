package org.srm.source.cux.rfx.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.impl.RfxHeaderServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.share.app.service.EvaluateIndicAssignService;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.domain.entity.EvaluateIndic;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxHeaderServiceImpl extends RfxHeaderServiceImpl {
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private EvaluateIndicAssignService evaluateIndicAssignService;
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
                throw new CommonException("error.evaluate_leader_flag_repeat", new Object[0]);
            }
        }
    }
}
