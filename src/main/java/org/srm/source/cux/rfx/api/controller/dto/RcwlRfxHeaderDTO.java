package org.srm.source.cux.rfx.api.controller.dto;

import org.srm.source.rfx.api.dto.RfxHeaderDTO;

public class RcwlRfxHeaderDTO extends RfxHeaderDTO {

    private Integer bargainRedactFlag;

    public Integer getBargainRedactFlag() {
        return bargainRedactFlag;
    }

    public void setBargainRedactFlag(Integer bargainRedactFlag) {
        this.bargainRedactFlag = bargainRedactFlag;
    }
}
