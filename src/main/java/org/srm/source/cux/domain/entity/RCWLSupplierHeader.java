package org.srm.source.cux.domain.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class RCWLSupplierHeader {
    private Long supplierId;
    private Long tenantId;
    private Long companyId;
    private Long shortlistHeaderId;
    private Long sampleId;

    //供应商信息
    private String companyNum;
    private String supplierName;
    private Integer selected;
    private Integer enroll;
    private Date enrollDate;
    private String contacts;
    private String phone;
    private String stageCode;
    private Integer qualification;
    private String qualificationInfo;
    private String sampleNum;
    private String annex;

    //要求内容
    private String announcement;

    private Long capital;
    private Long years;
    private Long oneProfit;
    private Long twoProfit;

    private Integer enabledFlag;
    private Long objectVersionNumber;
    private Date creationDate;
    private Long createdBy;
    private Long lastUpdatedBy;
    private Date lastUpdateDate;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getShortlistHeaderId() {
        return shortlistHeaderId;
    }

    public void setShortlistHeaderId(Long shortlistHeaderId) {
        this.shortlistHeaderId = shortlistHeaderId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Integer getEnroll() {
        return enroll;
    }

    public void setEnroll(Integer enroll) {
        this.enroll = enroll;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
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

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public Integer getQualification() {
        return qualification;
    }

    public void setQualification(Integer qualification) {
        this.qualification = qualification;
    }

    public String getQualificationInfo() {
        return qualificationInfo;
    }

    public void setQualificationInfo(String qualificationInfo) {
        this.qualificationInfo = qualificationInfo;
    }

    public String getSampleNum() {
        return sampleNum;
    }

    public void setSampleNum(String sampleNum) {
        this.sampleNum = sampleNum;
    }

    public String getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = annex;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public Long getCapital() {
        return capital;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    public Long getYears() {
        return years;
    }

    public void setYears(Long years) {
        this.years = years;
    }

    public Long getOneProfit() {
        return oneProfit;
    }

    public void setOneProfit(Long oneProfit) {
        this.oneProfit = oneProfit;
    }

    public Long getTwoProfit() {
        return twoProfit;
    }

    public void setTwoProfit(Long twoProfit) {
        this.twoProfit = twoProfit;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
