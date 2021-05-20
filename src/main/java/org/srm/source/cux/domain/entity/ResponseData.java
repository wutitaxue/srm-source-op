package org.srm.source.cux.domain.entity;

import lombok.Data;

@Data
public class ResponseData {
    private String code;
    private String message;
    private String url;
}
