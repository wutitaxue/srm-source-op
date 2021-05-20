package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateCalibrationApprovalDataDTO {
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("rfxNum")
    private String rfxNum;
    @JsonProperty("attributeVarchar4")
    private String attributeVarchar4;
    @JsonProperty("attributeVarchar5")
    private String attributeVarchar5;
}
