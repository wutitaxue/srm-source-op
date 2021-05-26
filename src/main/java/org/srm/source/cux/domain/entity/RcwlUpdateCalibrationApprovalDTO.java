package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateCalibrationApprovalDTO {
    @JsonProperty("rcwlUpdateCalibrationApprovalDataVO")
    private RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateCalibrationApprovalDataDTO;

    public RcwlUpdateCalibrationApprovalDataDTO getRcwlUpdateCalibrationApprovalDataDTO() {
        return rcwlUpdateCalibrationApprovalDataDTO;
    }

    public void setRcwlUpdateCalibrationApprovalDataDTO(RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateCalibrationApprovalDataDTO) {
        this.rcwlUpdateCalibrationApprovalDataDTO = rcwlUpdateCalibrationApprovalDataDTO;
    }
}
