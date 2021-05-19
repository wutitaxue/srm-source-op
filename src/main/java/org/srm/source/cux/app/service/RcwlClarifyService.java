package org.srm.source.cux.app.service;

import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;

public interface RcwlClarifyService {

    ResponseData releaseClarifyByBPM(RcwlClarifyForBPM clarify);

    ResponseData updateClarifyData(RcwlUpdateDataDTO rcwlUpdateDTO);

    Long getClarifyIdByClarifyNum(String clarifyNum);
}
