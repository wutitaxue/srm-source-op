package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlUpdateVO {
    @JsonProperty("RcwlUpdateVO")
    private RcwlUpdateDataVO rcwlUpdateDataVO;

    public RcwlUpdateDataVO getRcwlUpdateDataVO() {
        return rcwlUpdateDataVO;
    }

    public void setRcwlUpdateDataVO(RcwlUpdateDataVO rcwlUpdateDataVO) {
        this.rcwlUpdateDataVO = rcwlUpdateDataVO;
    }
}
