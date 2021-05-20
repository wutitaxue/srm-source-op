package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateCalibrationApprovalDataDTO {
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("rfxNum")
    private String rfxNum;
    @JsonProperty("attribute_varchar4")
    private String attribute_varchar4;
    @JsonProperty("attribute_varchar5")
    private String attribute_varchar5;
}
