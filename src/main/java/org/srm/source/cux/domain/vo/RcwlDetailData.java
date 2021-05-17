package org.srm.source.cux.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RcwlDetailData {

    //行号
    @JsonProperty(value = "RFXLINEITEMNUM")
    private String RfxLineItemNum;

    //物料编码
    @JsonProperty(value = "ITEMCODE")
    private String ItemCode;

    //物料名称
    @JsonProperty(value = "ITEMNAME")
    private String ItemName;

    //规格
    @JsonProperty(value = "UOMID")
    private String UomId;

    //物料类别
    @JsonProperty(value = "ITEMCATEGORYNAME")
    private String ItemCategoryName;

    //需求数量
    @JsonProperty(value = "RFXQUANTITY")
    private String rfxQuanttity;

    //单位
    @JsonProperty(value = "UOMID1")
    private String UomId1;

    public String getRfxLineItemNum() {
        return RfxLineItemNum;
    }

    public void setRfxLineItemNum(String rfxLineItemNum) {
        RfxLineItemNum = rfxLineItemNum;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getUomId() {
        return UomId;
    }

    public void setUomId(String uomId) {
        UomId = uomId;
    }

    public String getItemCategoryName() {
        return ItemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        ItemCategoryName = itemCategoryName;
    }

    public String getRfxQuanttity() {
        return rfxQuanttity;
    }

    public void setRfxQuanttity(String rfxQuanttity) {
        this.rfxQuanttity = rfxQuanttity;
    }

    public String getUomId1() {
        return UomId1;
    }

    public void setUomId1(String uomId1) {
        UomId1 = uomId1;
    }
}
