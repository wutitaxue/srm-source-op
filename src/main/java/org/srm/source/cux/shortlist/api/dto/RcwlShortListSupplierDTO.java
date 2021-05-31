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
    private String supplierId;

    @ApiModelProperty("供应商编码")
    private String supplierNum;

    @ApiModelProperty("供应商名称")
    private String companyName;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "RcwlShortListSupplierDTO{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierNum='" + supplierNum + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
