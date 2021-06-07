package org.srm.source.cux.shortlist.api.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/31 16:15
 * @version:1.0
 */
public class RcwlShortListSupplierDTO {
    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("供应商编码")
    private String supplierNum;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "RcwlShortListSupplierDTO{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierNum='" + supplierNum + '\'' +
                ", supplierName='" + supplierName + '\'' +
                '}';
    }
}
