package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlDBSPTGDTO {
    @JsonProperty("rfxNum")
    String rfxNum;
    @JsonProperty("tenantId")
    Long tenantId;
}
