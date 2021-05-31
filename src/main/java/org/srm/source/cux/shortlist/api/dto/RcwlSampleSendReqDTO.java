package org.srm.source.cux.shortlist.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.format.annotation.DateTimeFormat;
import org.srm.common.mybatis.domain.ExpandDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/28 15:19
 * @version:1.0
 */
public class RcwlSampleSendReqDTO extends ExpandDomain {
    @ApiModelProperty("表ID")
    @Id
    @GeneratedValue
    @Encrypt
    private Long reqId;
    @ApiModelProperty("租户id")
    @NotNull
    private Long tenantId;
    @LovValue(
            lovCode = "SSLM.PROCESS_STATUS",
            meaningField = "reqStatusMeaning"
    )
    private String reqStatus;
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.reqStatusMeaning"
    )
    private String reqStatusMeaning;
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.reqNum"
    )
    @ApiModelProperty("申请单号")
    @NotBlank
    private String reqNum;
    @ApiModelProperty("库存组织Id")
    private Long invOrganizationId;
    @ApiModelProperty("库存组织名称")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.organizationName"
    )
    private String organizationName;
    @ApiModelProperty("库存组织编码")
    private String organizationCode;
    @ApiModelProperty("供应商编码")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.supplierNum"
    )
    private String supplierNum;
    @ApiModelProperty("供应商名称")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.supplierName"
    )
    private String supplierName;
    @ApiModelProperty("公司id")
    @NotNull
    private Long companyId;
    @ApiModelProperty("公司编码")
    private String companyNum;
    @ApiModelProperty("公司名称")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.companyName"
    )
    private String companyName;
    @ApiModelProperty("业务实体Id")
    private Long ouId;
    @ApiModelProperty("业务实体编码")
    private String ouCode;
    @ApiModelProperty("业务实体名称")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.ouName"
    )
    private String ouName;
    @ApiModelProperty("供应商类型")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.supplierTypeCodeMeaning"
    )
    private String supplierTypeCodeMeaning;
    @ApiModelProperty("原厂名称")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.originFactoryName"
    )
    private String originFactoryName;
    @LovValue(
            lovCode = "SSLM.TYPE_CODE",
            meaningField = "typeCodeMeaning"
    )
    private String typeCode;
    @ApiModelProperty("送样类型")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.typeCodeMeaning"
    )
    private String typeCodeMeaning;
    @ApiModelProperty("申请人id")
    @NotNull
    private Long reqUserId;
    @ApiModelProperty("申请人姓名")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.reqUserName"
    )
    private String reqUserName;
    @ApiModelProperty("申请人电话")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.reqUserPhone"
    )
    private String reqUserPhone;
    @ApiModelProperty("接样人")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.recUserName"
    )
    private String recUserName;
    @ApiModelProperty("接样人电话")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.recUserPhone"
    )
    private String recUserPhone;
    @ApiModelProperty("样品接收部门id")
    private Long receiveUnitId;
    @ApiModelProperty("样品接收部门名称")
    private String receiveUnitName;
    @ApiModelProperty("送样地址")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.sampleSendAddress"
    )
    private String sampleSendAddress;
    @ApiModelProperty("送样人")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.sendUserName"
    )
    private String sendUserName;
    @ApiModelProperty("送样人电话")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.sendUserPhone"
    )
    private String sendUserPhone;
    @ApiModelProperty("送样方式")
    @LovValue(
            lovCode = "SSLM.SEND_TYPE_CODE",
            meaningField = "sendTypeCodeMeaning"
    )
    private String sendTypeCode;
    @ApiModelProperty("送样方式")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.sendTypeCodeMeaning"
    )
    private String sendTypeCodeMeaning;
    @ApiModelProperty("快递单号")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.trackingNumber"
    )
    private String trackingNumber;
    @ApiModelProperty("预计送达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @NotNull
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.expectedDeliveryDate"
    )
    private Date expectedDeliveryDate;
    @ApiModelProperty("紧急程度")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.urgencyDegreeMeaning"
    )
    private String urgencyDegreeMeaning;
    @Transient
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty("创建时间")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.creationDate"
    )
    private Date creationDate;
    @ApiModelProperty("发布时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.releaseDate"
    )
    private Date releaseDate;
    @ApiModelProperty("反馈时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.feedbackDate"
    )
    private Date feedbackDate;
    @ApiModelProperty("备注说明")
    @ExcelColumn(
            promptKey = "sslm.sampleHead",
            promptCode = "sslm.sampleHead.model.remark"
    )
    private String remark;
    @ApiModelProperty("物料编码")
    private String itemCode;
    @ApiModelProperty("物料名称")
    private String itemName;
    @ApiModelProperty("物料说明")
    private String itemDesc;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("供应商id")
    private Long supplierId;
    @ApiModelProperty("供应商租户id")
    private Long supplierTenantId;
    @LovValue(
            lovCode = "SSLM.SUPPLIER_TYPE",
            meaningField = "supplierTypeCodeMeaning"
    )
    private String supplierTypeCode;
    @LovValue(
            lovCode = "SSLM.SAMPLE_URGENCY_DEGREE"
    )
    private String urgencyDegree;
    @ApiModelProperty("供应商备注")
    private String supplierRemark;
    @ApiModelProperty("确认说明")
    private String confirmRemark;
    @Transient
    @ApiModelProperty("创建时间从")
    private String creationTimeFrom;
    @Transient
    @ApiModelProperty("创建时间至")
    private String creationTimeTo;
    @Transient
    @ApiModelProperty("预计送达时间从")
    private String expectedDeliveryTimeFrom;
    @Transient
    @ApiModelProperty("预计送达时间至")
    private String expectedDeliveryTimeTo;
    @Transient
    @ApiModelProperty("发布时间从")
    private String releaseTimeFrom;
    @Transient
    @ApiModelProperty("发布时间至")
    private String releaseTimeTo;
    @Transient
    @ApiModelProperty("反馈时间从")
    private String feedbackTimeFrom;
    @Transient
    @ApiModelProperty("反馈时间至")
    private String feedbackTimeTo;
    @ApiModelProperty("送样申请行")
    @Transient
    private List<RcwlSampleInfo> infoList;
    private Long objectVersionNumber;
    private String sampleResult;
    private String sampleResultMeaning;
    @ApiModelProperty("试用部门")
    private String tryUseDepartment;
    @ApiModelProperty("试用车间")
    private String tryUseWorkshop;
    @ApiModelProperty("送样申请行DTO")
    @Transient
    private List<RcwlSampleInfoDTO> infoDtoList;



    public List<RcwlSampleInfo> getInfoList() {
        return this.infoList;
    }

    public void setInfoList(List<RcwlSampleInfo> infoList) {
        this.infoList = infoList;
    }

    public Long getReqId() {
        return this.reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getReqNum() {
        return this.reqNum;
    }

    public void setReqNum(String reqNum) {
        this.reqNum = reqNum;
    }

    public Long getReqUserId() {
        return this.reqUserId;
    }

    public void setReqUserId(Long reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getReqUserPhone() {
        return this.reqUserPhone;
    }

    public void setReqUserPhone(String reqUserPhone) {
        this.reqUserPhone = reqUserPhone;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getOuId() {
        return this.ouId;
    }

    public void setOuId(Long ouId) {
        this.ouId = ouId;
    }

    public Long getInvOrganizationId() {
        return this.invOrganizationId;
    }

    public void setInvOrganizationId(Long invOrganizationId) {
        this.invOrganizationId = invOrganizationId;
    }

    public Long getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getOriginFactoryName() {
        return this.originFactoryName;
    }

    public void setOriginFactoryName(String originFactoryName) {
        this.originFactoryName = originFactoryName;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getUrgencyDegree() {
        return this.urgencyDegree;
    }

    public void setUrgencyDegree(String urgencyDegree) {
        this.urgencyDegree = urgencyDegree;
    }

    public String getRecUserName() {
        return this.recUserName;
    }

    public void setRecUserName(String recUserName) {
        this.recUserName = recUserName;
    }

    public String getRecUserPhone() {
        return this.recUserPhone;
    }

    public void setRecUserPhone(String recUserPhone) {
        this.recUserPhone = recUserPhone;
    }

    public String getSampleSendAddress() {
        return this.sampleSendAddress;
    }

    public void setSampleSendAddress(String sampleSendAddress) {
        this.sampleSendAddress = sampleSendAddress;
    }

    public String getSendUserName() {
        return this.sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserPhone() {
        return this.sendUserPhone;
    }

    public void setSendUserPhone(String sendUserPhone) {
        this.sendUserPhone = sendUserPhone;
    }

    public String getSendTypeCode() {
        return this.sendTypeCode;
    }

    public void setSendTypeCode(String sendTypeCode) {
        this.sendTypeCode = sendTypeCode;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Date getExpectedDeliveryDate() {
        return this.expectedDeliveryDate;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getFeedbackDate() {
        return this.feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getCreationTimeFrom() {
        return this.creationTimeFrom;
    }

    public void setCreationTimeFrom(String creationTimeFrom) {
        this.creationTimeFrom = creationTimeFrom;
    }

    public String getCreationTimeTo() {
        return this.creationTimeTo;
    }

    public void setCreationTimeTo(String creationTimeTo) {
        this.creationTimeTo = creationTimeTo;
    }

    public String getExpectedDeliveryTimeFrom() {
        return this.expectedDeliveryTimeFrom;
    }

    public void setExpectedDeliveryTimeFrom(String expectedDeliveryTimeFrom) {
        this.expectedDeliveryTimeFrom = expectedDeliveryTimeFrom;
    }

    public String getExpectedDeliveryTimeTo() {
        return this.expectedDeliveryTimeTo;
    }

    public void setExpectedDeliveryTimeTo(String expectedDeliveryTimeTo) {
        this.expectedDeliveryTimeTo = expectedDeliveryTimeTo;
    }

    public String getReleaseTimeFrom() {
        return this.releaseTimeFrom;
    }

    public void setReleaseTimeFrom(String releaseTimeFrom) {
        this.releaseTimeFrom = releaseTimeFrom;
    }

    public String getReleaseTimeTo() {
        return this.releaseTimeTo;
    }

    public void setReleaseTimeTo(String releaseTimeTo) {
        this.releaseTimeTo = releaseTimeTo;
    }

    public String getFeedbackTimeFrom() {
        return this.feedbackTimeFrom;
    }

    public void setFeedbackTimeFrom(String feedbackTimeFrom) {
        this.feedbackTimeFrom = feedbackTimeFrom;
    }

    public String getFeedbackTimeTo() {
        return this.feedbackTimeTo;
    }

    public void setFeedbackTimeTo(String feedbackTimeTo) {
        this.feedbackTimeTo = feedbackTimeTo;
    }

    public String getReqStatus() {
        return this.reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSupplierRemark() {
        return this.supplierRemark;
    }

    public void setSupplierRemark(String supplierRemark) {
        this.supplierRemark = supplierRemark;
    }

    public String getSupplierNum() {
        return this.supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getReqStatusMeaning() {
        return this.reqStatusMeaning;
    }

    public void setReqStatusMeaning(String reqStatusMeaning) {
        this.reqStatusMeaning = reqStatusMeaning;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSupplierTypeCode() {
        return this.supplierTypeCode;
    }

    public void setSupplierTypeCode(String supplierTypeCode) {
        this.supplierTypeCode = supplierTypeCode;
    }

    public String getSupplierTypeCodeMeaning() {
        return this.supplierTypeCodeMeaning;
    }

    public void setSupplierTypeCodeMeaning(String supplierTypeCodeMeaning) {
        this.supplierTypeCodeMeaning = supplierTypeCodeMeaning;
    }

    public String getOuCode() {
        return this.ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getOuName() {
        return this.ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationCode() {
        return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getCompanyNum() {
        return this.companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getUomCode() {
        return this.uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUrgencyDegreeMeaning() {
        return this.urgencyDegreeMeaning;
    }

    public void setUrgencyDegreeMeaning(String urgencyDegreeMeaning) {
        this.urgencyDegreeMeaning = urgencyDegreeMeaning;
    }

    public String getTypeCodeMeaning() {
        return this.typeCodeMeaning;
    }

    public void setTypeCodeMeaning(String typeCodeMeaning) {
        this.typeCodeMeaning = typeCodeMeaning;
    }

    public String getReqUserName() {
        return this.reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    public Long getSupplierTenantId() {
        return this.supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    @Override
    public Long getObjectVersionNumber() {
        return this.objectVersionNumber;
    }

    @Override
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getSendTypeCodeMeaning() {
        return this.sendTypeCodeMeaning;
    }

    public void setSendTypeCodeMeaning(String sendTypeCodeMeaning) {
        this.sendTypeCodeMeaning = sendTypeCodeMeaning;
    }

    public String getSampleResult() {
        return this.sampleResult;
    }

    public void setSampleResult(String sampleResult) {
        this.sampleResult = sampleResult;
    }

    public String getTryUseDepartment() {
        return this.tryUseDepartment;
    }

    public void setTryUseDepartment(String tryUseDepartment) {
        this.tryUseDepartment = tryUseDepartment;
    }

    public String getTryUseWorkshop() {
        return this.tryUseWorkshop;
    }

    public void setTryUseWorkshop(String tryUseWorkshop) {
        this.tryUseWorkshop = tryUseWorkshop;
    }

    public String getSampleResultMeaning() {
        return this.sampleResultMeaning;
    }

    public void setSampleResultMeaning(String sampleResultMeaning) {
        this.sampleResultMeaning = sampleResultMeaning;
    }

    public List<RcwlSampleInfoDTO> getInfoDtoList() {
        return this.infoDtoList;
    }

    public void setInfoDtoList(List<RcwlSampleInfoDTO> infoDtoList) {
        this.infoDtoList = infoDtoList;
    }

    public String getConfirmRemark() {
        return this.confirmRemark;
    }

    public void setConfirmRemark(String confirmRemark) {
        this.confirmRemark = confirmRemark;
    }

    public Long getReceiveUnitId() {
        return this.receiveUnitId;
    }

    public void setReceiveUnitId(Long receiveUnitId) {
        this.receiveUnitId = receiveUnitId;
    }

    public String getReceiveUnitName() {
        return this.receiveUnitName;
    }

    public void setReceiveUnitName(String receiveUnitName) {
        this.receiveUnitName = receiveUnitName;
    }
}
