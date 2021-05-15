package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateDTO {
    @JsonProperty("RcwlUpdateDTO")
    private RcwlUpdateDataDTO rcwlUpdateDataDTO;

    public RcwlUpdateDataDTO getRcwlUpdateDataDTO() {
        return rcwlUpdateDataDTO;
    }

    public void setRcwlUpdateDataDTO(RcwlUpdateDataDTO rcwlUpdateDataDTO) {
        this.rcwlUpdateDataDTO = rcwlUpdateDataDTO;
    }
}
