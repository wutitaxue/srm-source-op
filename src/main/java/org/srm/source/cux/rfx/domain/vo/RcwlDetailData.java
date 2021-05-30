package org.srm.source.cux.rfx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class RcwlDetailData {

    //行号
    @JSONField(name = "RFXLINEITEMNUM")
    private String RfxLineItemNum;

    //物料编码
    @JSONField(name = "ITEMCODE")
    private String ItemCode;

    //物料名称
    @JSONField(name = "ITEMNAME")
    private String ItemName;

    //规格
    @JSONField(name = "UOMID")
    private String UomId;

    //物料类别
    @JSONField(name = "ITEMCATEGORYNAME")
    private String ItemCategoryName;

    //需求数量
    @JSONField(name = "RFXQUANTITY")
    private String rfxQuanttity;

    //单位
    @JSONField(name = "UOMID1")
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
