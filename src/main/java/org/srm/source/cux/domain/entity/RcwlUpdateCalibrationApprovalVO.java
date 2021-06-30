package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateCalibrationApprovalVO {
    @JsonProperty("rcwlUpdateCalibrationApprovalDataVO")
    private RcwlUpdateCalibrationApprovalDataVO rcwlUpdateCalibrationApprovalDataVO;

    public RcwlUpdateCalibrationApprovalDataVO getRcwlUpdateCalibrationApprovalDataVO() {
        return rcwlUpdateCalibrationApprovalDataVO;
    }

    public void setRcwlUpdateCalibrationApprovalDataVO(RcwlUpdateCalibrationApprovalDataVO rcwlUpdateCalibrationApprovalDataVO) {
        this.rcwlUpdateCalibrationApprovalDataVO = rcwlUpdateCalibrationApprovalDataVO;
    }
}
