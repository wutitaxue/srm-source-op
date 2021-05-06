package org.srm.source.cux.rfx.api.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import org.srm.common.mybatis.domain.ExpandDomain;

import java.math.BigDecimal;

/**
 * @Author hand_ghq
 * @Date 2021/3/24
 * @Version V1.0
 */
public class RcwlRfxSupplierQueryDTO {

    @ApiModelProperty("寻源单头Id")
    private Long rfxHeaderId;
    @ApiModelProperty("寻源单行Id")
    private Long rfxLineSupplierId;
    @ApiModelProperty("寻源单号")
    private String rfxNum;
    @ApiModelProperty("寻源事项")
    private String rfxTitle;
    @ApiModelProperty("采购组织名称")
    private String organizationName;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("寻源类别")
    private String sourceCategoryName;
    @ApiModelProperty("创建人")
    private String createName;
    @ApiModelProperty("报价总金额")
    private BigDecimal totalPrice;
    @ApiModelProperty("轮次")
    private Integer roundNumber;

    public Long getRfxHeaderId() {
        return rfxHeaderId;
    }

    public void setRfxHeaderId(Long rfxHeaderId) {
        this.rfxHeaderId = rfxHeaderId;
    }

    public Long getRfxLineSupplierId() {
        return rfxLineSupplierId;
    }

    public void setRfxLineSupplierId(Long rfxLineSupplierId) {
        this.rfxLineSupplierId = rfxLineSupplierId;
    }

    public String getRfxNum() {
        return rfxNum;
    }

    public void setRfxNum(String rfxNum) {
        this.rfxNum = rfxNum;
    }

    public String getRfxTitle() {
        return rfxTitle;
    }

    public void setRfxTitle(String rfxTitle) {
        this.rfxTitle = rfxTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSourceCategoryName() {
        return sourceCategoryName;
    }

    public void setSourceCategoryName(String sourceCategoryName) {
        this.sourceCategoryName = sourceCategoryName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }
}
