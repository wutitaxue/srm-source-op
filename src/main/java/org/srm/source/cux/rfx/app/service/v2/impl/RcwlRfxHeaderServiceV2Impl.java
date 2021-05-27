package org.srm.source.cux.rfx.app.service.v2.impl;

import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.source.cux.infra.constant.Constants;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.bid.domain.entity.SourceNotice;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.rfx.api.dto.CopyAttachmentUuidDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.v2.RfxHeaderServiceV2;
import org.srm.source.rfx.app.service.v2.impl.RfxHeaderServiceV2Impl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.service.IRfxHeaderDomainService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.share.app.service.SourceTemplateService;
import org.srm.source.share.domain.entity.PrequalHeader;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.service.IPrequelDomainService;
import org.srm.web.annotation.Tenant;

import java.util.Objects;

@Service("RcwlRfxHeaderServiceImpl.v2")
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderServiceV2Impl extends RfxHeaderServiceV2Impl {

    @Autowired
    private SourceTemplateService sourceTemplateService;
    @Autowired
    private IRfxHeaderDomainService rfxHeaderDomainService;
    @Autowired
    @Lazy
    private IPrequelDomainService prequelDomainService;
    @Autowired
    private RfxHeaderService rfxHeaderServiceV1;

    @Override
    public void releaseRfx(Long organizationId, RfxFullHeader rfxFullHeader) {
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();Assert.notNull(rfxHeader.getRfxHeaderId(), "header.not.presence");
        SourceTemplate sourceTemplate = this.sourceTemplateService.selectByPrimaryKey(rfxHeader.getTemplateId());
        Assert.notNull(sourceTemplate, "source.template.not.found");
        rfxHeader.beforeReleaseCheck(rfxFullHeader, sourceTemplate);
        if (Constants.SELF.equals(sourceTemplate.getReleaseApproveType())) {
            rfxHeader.setRfxStatus(Constants.IN_QUOTATION) ;
        } else {
            rfxHeader.setRfxStatus(Constants.RELEASE_APPROVING);
        }
        RfxFullHeader rtnFullHeader = ((RfxHeaderServiceV2)this.self()).saveOrUpdateFullHeader(rfxFullHeader);
        this.rfxHeaderDomainService.validRfxHeaderBeforeSave(rfxHeader, sourceTemplate);
        this.rfxHeaderDomainService.validateLineItemTaxRate(rfxFullHeader.getRfxHeader());
        if (BaseConstants.Flag.NO.equals(sourceTemplate.getScoreIndicFlag())) {
            this.rfxHeaderServiceV1.checkExpertScore(sourceTemplate, rfxHeader, rfxFullHeader);
        }

        this.rfxHeaderServiceV1.validateLadderInquiry(rtnFullHeader);
        this.rfxHeaderDomainService.removeOrValidRfxItemSupAssign(rfxFullHeader);
        PrequalHeader prequalHeader = rfxFullHeader.getPrequalHeader();
        if (null != prequalHeader) {
            prequalHeader.preData(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX");
        }

        if ("PRE".equals(sourceTemplate.getQualificationType()) || "PRE_POST".equals(sourceTemplate.getQualificationType())) {
            Assert.notNull(rfxHeader.getQuotationStartDate(), "error.quotation_start_time_not_found");
            prequalHeader.checkBeforeUpdate(Objects.isNull(rfxHeader.getQuotationStartDate()) ? rfxHeader.getEstimatedStartTime() : rfxHeader.getQuotationStartDate());
        }

        this.prequelDomainService.checkPrequalHeader(sourceTemplate, rfxFullHeader.getPrequalHeader());


    }



}
