package org.srm.source.cux.api.dto;

import io.swagger.annotations.ApiModelProperty;

public class RcwlAbilityLineDTO {

    private Long abilityLineId;
    private String categoryCode;
    private Long categoryId;
    private Long supplyAbilityId;
    private Long tenantId;
    private Long createdBy;
    private Long lastUpdatedBy;


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

    public Long getAbilityLineId() {
        return abilityLineId;
    }

    public void setAbilityLineId(Long abilityLineId) {
        this.abilityLineId = abilityLineId;
    }

    public Long getSupplyAbilityId() {
        return supplyAbilityId;
    }

    public void setSupplyAbilityId(Long supplyAbilityId) {
        this.supplyAbilityId = supplyAbilityId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
