package org.srm.source.cux.app.service;


import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
import org.srm.source.cux.domain.entity.ResponseData;

public interface RcwlBPMRfxHeaderService {

    ResponseData newClose(Long tenantId, Long rfxHeaderId, String remark,String attributeVarchar20);

    ResponseData updateRfxHeaderData(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO);

    Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId);

    void chooseRfxCloseApproveType(Long tenantId, Long rfxHeaderId, String remark);

    void updateSubmitBy(long l, Long rfxHeaderIds);

    void updateTerminatedBy(Long rfxHeaderIds);

    void updateCheckedBy(Long rfxHeaderId);
}
