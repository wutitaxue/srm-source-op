package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlGetData {
    @JsonProperty("getData")
    private GetData getData;
}
