package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetData {
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("rfxHeaderId")
    private Long rfxHeaderId;
}
