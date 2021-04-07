package org.srm.source.cux.api.dto;

public class PrLineDTO {
    private Long prLineId;
    private Long prHeaderId;
    private Long tenantId;
    /**
     * 采购计划id
     */
    private Long attributeBigint1;

    public Long getPrLineId() {
        return prLineId;
    }

    public void setPrLineId(Long prLineId) {
        this.prLineId = prLineId;
    }

    public Long getPrHeaderId() {
        return prHeaderId;
    }

    public void setPrHeaderId(Long prHeaderId) {
        this.prHeaderId = prHeaderId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getAttributeBigint1() {
        return attributeBigint1;
    }

    public void setAttributeBigint1(Long attributeBigint1) {
        this.attributeBigint1 = attributeBigint1;
    }
}
