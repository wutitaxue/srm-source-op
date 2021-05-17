package org.srm.source.cux.app.service;


import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;

public interface RcwlRfxHeaderService {

    ResponseData newClose(Long tenantId, Long rfxHeaderId, String remark);

    ResponseData updateRfxHeaderData(RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO);
}
