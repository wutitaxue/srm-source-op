package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateDataDTO {
    @JsonProperty("tenantid")
    private String tenantid;
    @JsonProperty("clarifyNum")
    private String clarifyNum;
    @JsonProperty("processInstanceId")
    private String processInstanceId;
    @JsonProperty("webserviceUrl")
    private String webserviceUrl;
    @JsonProperty("clarifyStatus")
    private String clarifyStatus;
}
