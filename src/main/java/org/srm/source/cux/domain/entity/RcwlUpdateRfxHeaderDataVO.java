package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateRfxHeaderDataVO {
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("rfxNum")
    private String rfxNum;
    @JsonProperty("attributeVarchar6")
    private String attributeVarchar6;
    @JsonProperty("attributeVarchar7")
    private String attributeVarchar7;
    @JsonProperty("status")
    private String status;
}
