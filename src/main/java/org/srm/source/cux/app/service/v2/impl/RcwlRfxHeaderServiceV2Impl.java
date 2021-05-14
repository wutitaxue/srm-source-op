package org.srm.source.cux.app.service.v2.impl;

import org.hzero.core.base.BaseConstants;
import org.springframework.stereotype.Service;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.rfx.api.dto.FieldPropertyDTO;
import org.srm.source.rfx.api.dto.HeaderAdjustDateDTO;
import org.srm.source.rfx.api.dto.SourceHeaderDTO;
import org.srm.source.rfx.app.service.v2.impl.RfxHeaderServiceV2Impl;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.web.annotation.Tenant;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderServiceV2Impl extends RfxHeaderServiceV2Impl {
    @Override
    public void processQuotationEndDateField(List<FieldPropertyDTO> fieldPropertyDTOList, HeaderAdjustDateDTO headerAdjustDateDTO, SourceHeaderDTO sourceHeaderDTO) {
        Date now = new Date();
        boolean flag = Arrays.asList("AUTO", "AUTO_SCORE", "AUTO_CHECK").contains(sourceHeaderDTO.getRoundQuotationRule());
        if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
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
