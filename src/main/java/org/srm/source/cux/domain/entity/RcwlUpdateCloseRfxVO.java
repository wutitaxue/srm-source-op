package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateCloseRfxVO {
    @JsonProperty("rcwlUpdateRfxHeaderDataVO")
    private RcwlUpdateRfxHeaderDataVO rcwlUpdateRfxHeaderDataVO;

    public RcwlUpdateRfxHeaderDataVO getRcwlUpdateDataDTO() {
        return rcwlUpdateRfxHeaderDataVO;
    }

    public void setRcwlUpdateDataDTO(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO) {
        this.rcwlUpdateRfxHeaderDataVO = rcwlUpdateDataDTO;
    }
}
