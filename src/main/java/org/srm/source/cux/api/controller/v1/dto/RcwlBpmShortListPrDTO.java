package org.srm.source.cux.api.controller.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/21 15:18
 * @version:1.0
 */
public class RcwlBpmShortListPrDTO {

    @ApiModelProperty("申请单号")
    @JsonProperty("PRHEADERID")
    private String prHeaderId;

    @ApiModelProperty("申请行号")
    @JsonProperty("LINENUM")
    private String lineNum;

    @ApiModelProperty("供应品类")
    @JsonProperty("CATEGORYID")
    private String categoryId;

    @ApiModelProperty("物料编码")
    @JsonProperty("ITEMCODE")
    private String itemCode;

    @ApiModelProperty("物料名称")
    @JsonProperty("ITEMNAME")
    private String itemName;

    @ApiModelProperty("甄云链接")
    @JsonProperty("URL_MX")
    private String urlMx;

    public String getPrHeaderId() {
        return prHeaderId;
    }

    public void setPrHeaderId(String prHeaderId) {
        this.prHeaderId = prHeaderId;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUrlMx() {
        return urlMx;
    }

    public void setUrlMx(String urlMx) {
        this.urlMx = urlMx;
    }
}
