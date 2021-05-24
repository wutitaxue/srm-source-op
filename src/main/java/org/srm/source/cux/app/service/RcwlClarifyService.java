package org.srm.source.cux.app.service;

import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataVO;
import org.srm.source.cux.domain.entity.ResponseData;

import java.util.List;

public interface RcwlClarifyService {

    ResponseData releaseClarifyByBPM(RcwlClarifyForBPM clarify);

    ResponseData updateClarifyData(RcwlUpdateDataVO rcwlUpdateDTO);

    Long getClarifyIdByClarifyNum(String clarifyNum);

    List<String> getTenantIdByclarifyNum(String clarifyNum);
}
