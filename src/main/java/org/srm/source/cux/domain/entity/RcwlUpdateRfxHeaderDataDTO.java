package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateRfxHeaderDataDTO {
    @JsonProperty("rfxHeaderId")
    private String rfxHeaderId;
    @JsonProperty("boId")
    private String boId;
    @JsonProperty("boIdUrl")
    private String boIdUrl;
}
