package org.srm.source.cux.rfx.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RcwlSupplierData {

    //供应商编码
    @JsonProperty(value = "SUPPLIERCOMPANYNUM")
    private String SupplierCompanyNum;

    //供应商名称
    @JsonProperty(value = "SUPPLIERCOMPANYNAME")
    private String SupplierCompanyName;

    //注册资金
    @JsonProperty(value = "REGISTEREDCAPITAL")
    private String RegisteredCapital;

    //第一联系人姓名
    @JsonProperty(value = "CONTACTNAME")
    private String ContactName;

    //联系方式
    @JsonProperty(value = "CONTACTMOBILEPHONE")
    private String ContactMobilePhone;

    public String getSupplierCompanyNum() {
        return SupplierCompanyNum;
    }

    public void setSupplierCompanyNum(String supplierCompanyNum) {
        SupplierCompanyNum = supplierCompanyNum;
    }

    public String getSupplierCompanyName() {
        return SupplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        SupplierCompanyName = supplierCompanyName;
    }

    public String getRegisteredCapital() {
        return RegisteredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        RegisteredCapital = registeredCapital;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactMobilePhone() {
        return ContactMobilePhone;
    }

    public void setContactMobilePhone(String contactMobilePhone) {
        ContactMobilePhone = contactMobilePhone;
    }
}