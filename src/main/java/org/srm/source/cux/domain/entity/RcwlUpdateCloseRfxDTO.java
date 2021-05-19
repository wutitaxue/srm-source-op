package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateCloseRfxDTO {
    @JsonProperty("RcwlUpdateDTO")
    private RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO;

    public RcwlUpdateRfxHeaderDataDTO getRcwlUpdateDataDTO() {
        return rcwlUpdateDataDTO;
    }

    public void setRcwlUpdateDataDTO(RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO) {
        this.rcwlUpdateDataDTO = rcwlUpdateDataDTO;
    }
}
