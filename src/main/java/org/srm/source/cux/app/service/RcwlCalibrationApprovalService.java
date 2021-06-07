package org.srm.source.cux.app.service;


import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataVO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;
import org.srm.source.rfx.api.dto.CheckPriceHeaderDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;

import java.util.List;

public interface RcwlCalibrationApprovalService {
    ResponseCalibrationApprovalData connectBPM(String organizationId, Long rfxHeaderId);

    ResponseCalibrationApprovalData updateClarifyData(RcwlUpdateCalibrationApprovalDataVO rcwlUpdateDataDTO);

    Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId);

    List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId);

    List<Long> getRfxLineItemIdByRfxHeaderId(Long rfxHeaderId);

    List<RfxQuotationLine> getQuotationLineListByQuotationHeaderID(Long id);

    RfxQuotationLine getRfxQuotationLineDataByQuotationHeaderIDs(String id);

    Long getRoundNumber(Long rfxHeaderId, Long tenantId);

    void checkSubmitCommon(Long organizationId, Long rfxHeaderId, CheckPriceHeaderDTO checkPriceHeaderDTO);

    void checkPriceApproved(Long organizationId, Long rfxHeaderId);

}
