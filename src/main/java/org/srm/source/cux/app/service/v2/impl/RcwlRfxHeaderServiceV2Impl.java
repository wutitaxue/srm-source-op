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
        //1.询价
        //2.勾选允许设置报价截止时间
        //3.当前时间小于报价截止时间
        //4.资格预审中、待预审审批、未开始、报价中
        boolean flag = Arrays.asList(ShareConstants.RoundQuotationRule.AUTO, ShareConstants.RoundQuotationRule.AUTO_SCORE, ShareConstants.RoundQuotationRule.AUTO_CHECK).contains(sourceHeaderDTO.getRoundQuotationRule());
        if((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(sourceHeaderDTO.getSourceCategory()))
                && BaseConstants.Flag.YES.equals(sourceHeaderDTO.getQuotationEndDateFlag())
                && !flag
                && null != sourceHeaderDTO.getQuotationEndDate()
                && now.compareTo(sourceHeaderDTO.getQuotationEndDate()) < 0
                && Arrays.asList(SourceConstants.RfxStatus.IN_PREQUAL,SourceConstants.RfxStatus.PENDING_PREQUAL,SourceConstants.RfxStatus.NOT_START,SourceConstants.RfxStatus.IN_QUOTATION).contains(sourceHeaderDTO.getRfxStatus())){
            this.processFieldProperty(fieldPropertyDTOList, HeaderAdjustDateDTO.FIELD_QUOTATION_END_DATE, headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.YES);
        }else if((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(sourceHeaderDTO.getSourceCategory()))
                && BaseConstants.Flag.YES.equals(sourceHeaderDTO.getQuotationEndDateFlag())
                && !flag
                && null != sourceHeaderDTO.getQuotationEndDate()
                && now.compareTo(sourceHeaderDTO.getQuotationEndDate()) >= 0
                && Arrays.asList(SourceConstants.RfxStatus.LACK_QUOTED).contains(sourceHeaderDTO.getRfxStatus())){
            //1.询价
            //2.勾选允许设置报价截止时间
            //3.当前时间不小于报价截止时间
            //4.报价响应不足

            //1.询价
            //2.不勾选允许设置报价截止时间
            //3.当前时间不小于报价截止时间
            //4.报价响应不足
            this.processFieldProperty(fieldPropertyDTOList, HeaderAdjustDateDTO.FIELD_QUOTATION_END_DATE, headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.YES);
        }else if((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(sourceHeaderDTO.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(sourceHeaderDTO.getSourceCategory()))
                && BaseConstants.Flag.YES.equals(sourceHeaderDTO.getQuotationEndDateFlag())
                && BaseConstants.Flag.NO.equals(sourceHeaderDTO.getQuotationEndDateFlag())
                && !flag
                && (null == sourceHeaderDTO.getQuotationEndDate() || now.compareTo(sourceHeaderDTO.getQuotationEndDate()) < 0)
                && Arrays.asList(SourceConstants.RfxStatus.IN_PREQUAL,SourceConstants.RfxStatus.PENDING_PREQUAL,SourceConstants.RfxStatus.NOT_START,SourceConstants.RfxStatus.IN_QUOTATION).contains(sourceHeaderDTO.getRfxStatus())){
            //1.询价
            //2.不勾选设置报价截止时间
            //3.当前时间小于报价截止时间或报价截止时间为空
            //4.资格预审中、待预审审批、未开始、报价中
            this.processFieldProperty(fieldPropertyDTOList, HeaderAdjustDateDTO.FIELD_QUOTATION_END_DATE, headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.YES);
        }else{
            this.processFieldProperty(fieldPropertyDTOList, HeaderAdjustDateDTO.FIELD_QUOTATION_END_DATE, headerAdjustDateDTO.getQuotationEndDate(), BaseConstants.Flag.NO);
        }
    }
}
