package org.srm.source.cux.shortlist.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.format.annotation.DateTimeFormat;
import org.srm.common.mybatis.domain.ExpandDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/28 15:24
 * @version:1.0
 */
public class RcwlSampleInfoDTO extends ExpandDomain {
    @ApiModelProperty("表ID")
    @Id
    @GeneratedValue
    @Encrypt
    private Long sampleId;
    @ApiModelProperty(
            value = "样品申请单",
            required = true
    )
    @NotNull
    @Encrypt
    private Long reqId;
    @ApiModelProperty(
            value = "租户id",
            required = true
    )
    @NotNull
    private Long tenantId;
    @ApiModelProperty(
            value = "行号",
            required = true
    )
    @NotBlank
    private String lineNum;
    @ApiModelProperty("物料code")
    private String itemCode;
    @ApiModelProperty(
            value = "物料名称",
            required = true
    )
    @NotBlank
    private String itemName;
    @ApiModelProperty("物料说明")
    private String itemDesc;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("单位名称")
    private String uomName;
    @ApiModelProperty("品类代码DTO")
    private RcwlSampleInfoItemDTO itemCategoryCode;
    @ApiModelProperty("品类名称DTO")
    private RcwlSampleInfoItemDTO itemCategoryName;
    private Long categoryId;
    private String categoryCodes;
    private String categoryNames;
    @ApiModelProperty("需求数量")
    private BigDecimal reqQuantity;
    @ApiModelProperty("需求时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date reqTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty("预计送达时间")
    private Date expectedDeliveryDate;
    @ApiModelProperty("送样方式")
    @LovValue(
            lovCode = "SSLM.SEND_TYPE_CODE",
            meaningField = "sendTypeCodeMeaning"
    )
    private String sendTypeCode;
    @ApiModelProperty("快递单号")
    private String trackingNumber;
    @ApiModelProperty("试用部门")
    private String tryUseDepartment;
    @ApiModelProperty("试用车间")
    private String tryUseWorkshop;
    @ApiModelProperty("物料是否认证")
    private Integer certificationFlag;
    @ApiModelProperty("附件uid")
    private String attachmentUuid;
    @ApiModelProperty("采购方附件uid")
    private String buyerAttachmentUuid;
    @ApiModelProperty("备注说明")
    private String remark;
    @ApiModelProperty("样品结果")
    @LovValue(
            lovCode = "SSLM_SAMPLE_RESULT",
            meaningField = "sampleResultMeaning"
    )
    private String sampleResult;
    private String sampleResultMeaning;
    @ApiModelProperty("送样申请供方附件")
    @Transient
    private List<RcwlSampleAttachment> sampleAttachmentList;
    @Transient
    private String sendTypeCodeMeaning;

    public RcwlSampleInfoDTO() {
    }


    public RcwlSampleInfoItemDTO getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(RcwlSampleInfoItemDTO itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public RcwlSampleInfoItemDTO getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(RcwlSampleInfoItemDTO itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public Long getSampleId() {
        return this.sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
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

    public String getLineNum() {
        return this.lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
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


    public BigDecimal getReqQuantity() {
        return this.reqQuantity;
    }

    public void setReqQuantity(BigDecimal reqQuantity) {
        this.reqQuantity = reqQuantity;
    }

    public Date getReqTime() {
        return this.reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
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

    public Integer getCertificationFlag() {
        return this.certificationFlag;
    }

    public void setCertificationFlag(Integer certificationFlag) {
        this.certificationFlag = certificationFlag;
    }

    public String getAttachmentUuid() {
        return this.attachmentUuid;
    }

    public void setAttachmentUuid(String attachmentUuid) {
        this.attachmentUuid = attachmentUuid;
    }

    public String getBuyerAttachmentUuid() {
        return this.buyerAttachmentUuid;
    }

    public void setBuyerAttachmentUuid(String buyerAttachmentUuid) {
        this.buyerAttachmentUuid = buyerAttachmentUuid;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCategoryCodes() {
        return this.categoryCodes;
    }

    public void setCategoryCodes(String categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

    public String getUomName() {
        return this.uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }



    public String getCategoryNames() {
        return this.categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSampleResult() {
        return this.sampleResult;
    }

    public void setSampleResult(String sampleResult) {
        this.sampleResult = sampleResult;
    }

    public List<RcwlSampleAttachment> getSampleAttachmentList() {
        return this.sampleAttachmentList;
    }

    public void setSampleAttachmentList(List<RcwlSampleAttachment> sampleAttachmentList) {
        this.sampleAttachmentList = sampleAttachmentList;
    }

    public String getSampleResultMeaning() {
        return this.sampleResultMeaning;
    }

    public void setSampleResultMeaning(String sampleResultMeaning) {
        this.sampleResultMeaning = sampleResultMeaning;
    }

    public Date getExpectedDeliveryDate() {
        return this.expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
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

    public String getSendTypeCodeMeaning() {
        return this.sendTypeCodeMeaning;
    }

    public void setSendTypeCodeMeaning(String sendTypeCodeMeaning) {
        this.sendTypeCodeMeaning = sendTypeCodeMeaning;
    }
}
