package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlGetDataCloseDTO {
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("rfxNum")
    private String rfxNum;
    @JsonProperty("tenantId")
    private Long tenantId;
}
