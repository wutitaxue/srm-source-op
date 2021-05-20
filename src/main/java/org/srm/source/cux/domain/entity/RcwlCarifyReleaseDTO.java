package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlCarifyReleaseDTO {
    @JsonProperty("tenantid")
    private Long tenantid;
    @JsonProperty("clarifyNum")
    private String clarifyNum;
}
