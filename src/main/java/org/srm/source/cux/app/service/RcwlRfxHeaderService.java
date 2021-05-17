package org.srm.source.cux.app.service;


import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;

import java.util.List;

public interface RcwlRfxHeaderService {

    ResponseData newClose(Long tenantId, Long rfxHeaderId, String remark);

    ResponseData updateRfxHeaderData(RcwlUpdateDataDTO rcwlUpdateDataDTO);
}
