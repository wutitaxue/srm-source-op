package org.srm.source.cux.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RCWLSample {
    private Long sampleId;
    private Long tenantId;
    private Long companyId;
    private Long shortlistHeaderId;
    private Long supplierId;

    //基本信息
    private String sampleName;
    private Long createdBy;
    private Date creationDate;
    private String phone;
    private String companyName;
    private String businessEntity;
    private String inventory;
    private String supplierName;
    private String supplierType;
    private String originalFactory;
    private String sampleType;
    private String tension;
    private String sampledBy;
    private String sampledPhone;
    private String sampledAddress;
    private String receivedDepartment;
    private String remarks;
    private String charge;

    private Integer enabledFlag;
    private Long objectVersionNumber;
    private Long lastUpdatedBy;
    private Date lastUpdateDate;

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(String businessEntity) {
        this.businessEntity = businessEntity;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getOriginalFactory() {
        return originalFactory;
    }

    public void setOriginalFactory(String originalFactory) {
        this.originalFactory = originalFactory;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getSampledBy() {
        return sampledBy;
    }

    public void setSampledBy(String sampledBy) {
        this.sampledBy = sampledBy;
    }

    public String getSampledPhone() {
        return sampledPhone;
    }

    public void setSampledPhone(String sampledPhone) {
        this.sampledPhone = sampledPhone;
    }

    public String getSampledAddress() {
        return sampledAddress;
    }

    public void setSampledAddress(String sampledAddress) {
        this.sampledAddress = sampledAddress;
    }

    public String getReceivedDepartment() {
        return receivedDepartment;
    }

    public void setReceivedDepartment(String receivedDepartment) {
        this.receivedDepartment = receivedDepartment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
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
