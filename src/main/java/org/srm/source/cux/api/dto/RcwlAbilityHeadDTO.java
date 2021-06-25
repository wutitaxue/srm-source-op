package org.srm.source.cux.api.dto;

public class RcwlAbilityHeadDTO {

    private Long supplyAbilityId;
    private Long supplierCompanyId;
    private Long supplierTenantId;
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

    public Long getSupplyAbilityId() {
        return supplyAbilityId;
    }

    public void setSupplyAbilityId(Long supplyAbilityId) {
        this.supplyAbilityId = supplyAbilityId;
    }

    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
