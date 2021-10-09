package org.srm.source.cux.rfx.infra.repository.impl;

import java.util.Date;
import java.util.List;

import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
import org.srm.source.rfx.domain.service.impl.IRfxHeaderDomainServiceImpl;
import org.srm.web.annotation.Tenant;

/**
 * @author: lmr
 * @date: 2021/10/9 10:19
 */
@Component
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlIRfxHeaderDomainServiceImpl extends IRfxHeaderDomainServiceImpl {
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;

    @Override
    public void updateBargainEndDate(RfxHeader realRfxHeader) {
        if ("BARGAINING_ONLINE".equals(realRfxHeader.getBargainStatus())) {
            Assert.isTrue((new Date()).compareTo(realRfxHeader.getBargainEndDate()) < 0, "error.round_quotation_end_date");
            RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(realRfxHeader.getRfxHeaderId());
            rfxHeader.setBargainEndDate(realRfxHeader.getBargainEndDate());
            CustomizeHelper.ignore(() -> {
                return this.rfxHeaderRepository.updateOptional(rfxHeader, new String[]{"bargainEndDate"});
            });
            RfxQuotationHeader temp = new RfxQuotationHeader(realRfxHeader.getTenantId(), realRfxHeader.getRfxHeaderId());
            temp.setBargainFlag(BaseConstants.Flag.YES);
            List<RfxQuotationHeader> rfxQuotationHeaders = this.rfxQuotationHeaderRepository.select(temp);
            rfxQuotationHeaders.forEach((e) -> {
                e.setBargainEndDate(realRfxHeader.getBargainEndDate());
            });
            this.rfxQuotationHeaderRepository.batchUpdateOptional(rfxQuotationHeaders, new String[]{"bargainEndDate"});
        }
    }
}
