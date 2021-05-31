package org.srm.source.cux.shortlist.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.export.annotation.ExcelColumn;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description 入围单批量送样dto
 * @date 2021/5/28 17:17
 * @version:1.0
 */
public class RcwlShortListSampleDTO {

    @ApiModelProperty("入围单号")
    private String shortListNum;

    @ApiModelProperty("入围单id")
    private Long ShortListId;

    @ApiModelProperty("库存组织Id")
    private Long invOrganizationId;

    @ApiModelProperty("库存组织名称")
    private String organizationName;

    @ApiModelProperty("库存组织编码")
    private String organizationCode;

    @ApiModelProperty("供应商编码")
    private String supplierNum;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("公司id")
    @NotNull
    private Long companyId;

    @ApiModelProperty("公司编码")
    private String companyNum;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("业务实体Id")
    private Long ouId;

    @ApiModelProperty("业务实体编码")
    private String ouCode;

    @ApiModelProperty("业务实体名称")
    private String ouName;

    @ApiModelProperty("申请人id")
    @NotNull
    private Long reqUserId;
    @ApiModelProperty("申请人姓名")
    private String reqUserName;
    @ApiModelProperty("申请人电话")
    private String reqUserPhone;
    @ApiModelProperty("接样人")
    private String recUserName;
    @ApiModelProperty("接样人电话")
    private String recUserPhone;
    @ApiModelProperty("样品接收部门id")
    private Long receiveUnitId;
    @ApiModelProperty("样品接收部门名称")
    private String receiveUnitName;
    @ApiModelProperty("送样地址")
    private String sampleSendAddress;
    @ApiModelProperty("送样人")
    private String sendUserName;
    @ApiModelProperty("送样人电话")
    private String sendUserPhone;
    @ApiModelProperty("备注说明")
    private String remark;

    private List<RcwlShortListSupplierDTO> rcwlShortListSupplierDTOList;

    public List<RcwlShortListSupplierDTO> getRcwlShortListSupplierDTOList() {
        return rcwlShortListSupplierDTOList;
    }

    public void setRcwlShortListSupplierDTOList(List<RcwlShortListSupplierDTO> rcwlShortListSupplierDTOList) {
        this.rcwlShortListSupplierDTOList = rcwlShortListSupplierDTOList;
    }

    public String getShortListNum() {
        return shortListNum;
    }

    public void setShortListNum(String shortListNum) {
        this.shortListNum = shortListNum;
    }

    public Long getShortListId() {
        return ShortListId;
    }

    public void setShortListId(Long shortListId) {
        ShortListId = shortListId;
    }

    public Long getInvOrganizationId() {
        return invOrganizationId;
    }

    public void setInvOrganizationId(Long invOrganizationId) {
        this.invOrganizationId = invOrganizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getOuId() {
        return ouId;
    }

    public void setOuId(Long ouId) {
        this.ouId = ouId;
    }

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getOuName() {
        return ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public Long getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(Long reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getReqUserName() {
        return reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    public String getReqUserPhone() {
        return reqUserPhone;
    }

    public void setReqUserPhone(String reqUserPhone) {
        this.reqUserPhone = reqUserPhone;
    }

    public String getRecUserName() {
        return recUserName;
    }

    public void setRecUserName(String recUserName) {
        this.recUserName = recUserName;
    }

    public String getRecUserPhone() {
        return recUserPhone;
    }

    public void setRecUserPhone(String recUserPhone) {
        this.recUserPhone = recUserPhone;
    }

    public Long getReceiveUnitId() {
        return receiveUnitId;
    }

    public void setReceiveUnitId(Long receiveUnitId) {
        this.receiveUnitId = receiveUnitId;
    }

    public String getReceiveUnitName() {
        return receiveUnitName;
    }

    public void setReceiveUnitName(String receiveUnitName) {
        this.receiveUnitName = receiveUnitName;
    }

    public String getSampleSendAddress() {
        return sampleSendAddress;
    }

    public void setSampleSendAddress(String sampleSendAddress) {
        this.sampleSendAddress = sampleSendAddress;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserPhone() {
        return sendUserPhone;
    }

    public void setSendUserPhone(String sendUserPhone) {
        this.sendUserPhone = sendUserPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public String toString() {
        return "RcwlShortListSampleDTO{" +
                "shortListNum='" + shortListNum + '\'' +
                ", ShortListId=" + ShortListId +
                ", invOrganizationId=" + invOrganizationId +
                ", organizationName='" + organizationName + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", supplierNum='" + supplierNum + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", companyId=" + companyId +
                ", companyNum='" + companyNum + '\'' +
                ", companyName='" + companyName + '\'' +
                ", ouId=" + ouId +
                ", ouCode='" + ouCode + '\'' +
                ", ouName='" + ouName + '\'' +
                ", reqUserId=" + reqUserId +
                ", reqUserName='" + reqUserName + '\'' +
                ", reqUserPhone='" + reqUserPhone + '\'' +
                ", recUserName='" + recUserName + '\'' +
                ", recUserPhone='" + recUserPhone + '\'' +
                ", receiveUnitId=" + receiveUnitId +
                ", receiveUnitName='" + receiveUnitName + '\'' +
                ", sampleSendAddress='" + sampleSendAddress + '\'' +
                ", sendUserName='" + sendUserName + '\'' +
                ", sendUserPhone='" + sendUserPhone + '\'' +
                ", remark='" + remark + '\'' +
                ", rcwlShortListSupplierDTOList=" + rcwlShortListSupplierDTOList +
                '}';
    }
}