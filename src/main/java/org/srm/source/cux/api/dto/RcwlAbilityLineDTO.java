package org.srm.source.cux.api.dto;

import io.swagger.annotations.ApiModelProperty;

public class RcwlAbilityLineDTO {

    private String categoryCode;
    private Long categoryId;
    private Long supplyAbilityId;
    private Long tenantId;

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
