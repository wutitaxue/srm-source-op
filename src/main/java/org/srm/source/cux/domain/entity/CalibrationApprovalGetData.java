package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalibrationApprovalGetData {
    @JsonProperty("rfxHeaderId")
    private Long rfxHeaderId;
}
