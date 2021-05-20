package org.srm.source.cux.app.service;


import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataDTO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;

import java.util.List;

public interface RcwlCalibrationApprovalService {
    ResponseCalibrationApprovalData connectBPM(String organizationId, Long rfxHeaderId);

    ResponseCalibrationApprovalData updateClarifyData(RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateDataDTO);

    Long getRfxHeaderIdByRfxNum(String rfxNum);

    List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId);
}
