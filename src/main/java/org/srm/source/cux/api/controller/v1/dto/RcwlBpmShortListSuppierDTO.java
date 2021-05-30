package org.srm.source.cux.api.controller.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/21 14:46
 * @version:1.0
 */
public class RcwlBpmShortListSuppierDTO {
    @ApiModelProperty("供应商编码")
    @JsonProperty("COMPANYID")
    private String companyId;

    @ApiModelProperty("供应商名称")
    @JsonProperty("SUPPLIERNAME")
    private String supplierName;

    @ApiModelProperty("是否入围")
    @JsonProperty("SHORTLISTSTATUS")
    private String shortListStatus;

    @ApiModelProperty("是否考察")
    @JsonProperty("INVESTIGATE")
    private String investigate;

    @ApiModelProperty("不考察原因")
    @JsonProperty("INVESTIGATEREASONS")
    private String investigatereasons;

    @ApiModelProperty("报名情况")
    @JsonProperty("ENROLL")
    private String enroll;

    @ApiModelProperty("报名时间")
    @JsonProperty("ENROLLDATE")
    private String enrollDate;

    @ApiModelProperty("联系人")
    @JsonProperty("CONTACTS")
    private String contacts;

    @ApiModelProperty("联系方式")
    @JsonProperty("PHONE")
    private String phone;

    @ApiModelProperty("生命周期")
    @JsonProperty("PERIOD")
    private String period;

    @ApiModelProperty("是否符合")
    @JsonProperty("ENABLEDFLAG")
    private String enabledFlag;

    @ApiModelProperty("符合说明")
    @JsonProperty("PERIODINFO")
    private String periodInfo;

    @ApiModelProperty("送样单号")
    @JsonProperty("SHORTLISTNUM")
    private String shortListNum;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getShortListStatus() {
        return shortListStatus;
    }

    public void setShortListStatus(String shortListStatus) {
        this.shortListStatus = shortListStatus;
    }

    public String getInvestigate() {
        return investigate;
    }

    public void setInvestigate(String investigate) {
        this.investigate = investigate;
    }

    public String getInvestigatereasons() {
        return investigatereasons;
    }

    public void setInvestigatereasons(String investigatereasons) {
        this.investigatereasons = investigatereasons;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getPeriodInfo() {
        return periodInfo;
    }

    public void setPeriodInfo(String periodInfo) {
        this.periodInfo = periodInfo;
    }

    public String getShortListNum() {
        return shortListNum;
    }

    public void setShortListNum(String shortListNum) {
        this.shortListNum = shortListNum;
    }
}
