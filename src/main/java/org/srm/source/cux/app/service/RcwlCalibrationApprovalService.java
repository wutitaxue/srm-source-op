package org.srm.source.cux.app.service;


import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataVO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;

import java.util.List;

public interface RcwlCalibrationApprovalService {
    ResponseCalibrationApprovalData connectBPM(String organizationId, Long rfxHeaderId);

    ResponseCalibrationApprovalData updateClarifyData(RcwlUpdateCalibrationApprovalDataVO rcwlUpdateDataDTO);

    Long getRfxHeaderIdByRfxNum(String rfxNum);

    List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId);

    List<Long> getRfxLineItemIdByRfxHeaderId(Long rfxHeaderId);

    List<RfxQuotationLine> getQuotationLineListByQuotationHeaderID(Long id);
}
