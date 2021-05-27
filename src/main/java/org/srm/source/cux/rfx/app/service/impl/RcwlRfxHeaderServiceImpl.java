package org.srm.source.cux.rfx.app.service.impl;

import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.source.bid.domain.entity.SourceNotice;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.rfx.api.dto.CopyAttachmentUuidDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.impl.RfxHeaderServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.service.IRfxHeaderDomainService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.util.RfxEventUtil;
import org.srm.source.share.app.service.SourceTemplateService;
import org.srm.source.share.domain.entity.PrequalHeader;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.service.IPrequelDomainService;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        //招采时复制历史单据时需要自动给表： ssrc_rfx_header
        //的字段：attribute_varchar2、attribute_varchar3、attribute_varchar4、attribute_varchar5、attribute_varchar6、attribute_varchar7自动赋值为0
        rfxHeader.setAttributeVarchar2("0");
        rfxHeader.setAttributeVarchar3("0");
        rfxHeader.setAttributeVarchar4("0");
        rfxHeader.setAttributeVarchar5("0");
        rfxHeader.setAttributeVarchar6("0");
        rfxHeader.setAttributeVarchar7("0");
        return ((RfxHeaderService)this.self()).saveOrUpdateHeader(rfxHeader);
    }
}
