package org.srm.source.cux.rfx.api.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import org.srm.source.rfx.api.dto.RfxDTO;

import java.util.Date;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/21 13:53
 * @version:1.0
 */
public class RcwlRfxDTO extends RfxDTO {
    @ApiModelProperty("质疑截至时间")
    private Date attributeDatetime7;

    public Date getAttributeDatetime7() {
        return attributeDatetime7;
    }

    public void setAttributeDatetime7(Date attributeDatetime7) {
        this.attributeDatetime7 = attributeDatetime7;
    }
}
