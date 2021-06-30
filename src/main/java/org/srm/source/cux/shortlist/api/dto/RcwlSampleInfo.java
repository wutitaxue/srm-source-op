package org.srm.source.cux.shortlist.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.format.annotation.DateTimeFormat;
import org.srm.common.mybatis.domain.ExpandDomain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/28 15:26
 * @version:1.0
 */
@ApiModel("样品信息")
@VersionAudit
@ModifyAudit
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(
        name = "sslm_sample_info"
)
public class RcwlSampleInfo extends ExpandDomain {
    public static final String FIELD_SAMPLE_ID = "sampleId";
    public static final String FIELD_REQ_ID = "reqId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_ITEM_CODE = "itemCode";
    public static final String FIELD_ITEM_NAME = "itemName";
    public static final String FIELD_ITEM_DESC = "itemDesc";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_ITEM_CATEGORY_ID = "itemCategoryId";
    public static final String FIELD_ITEM_CATEGORY_CODE = "itemCategoryCode";
    public static final String FIELD_REQ_QUANTITY = "reqQuantity";
    public static final String FIELD_REQ_TIME = "reqTime";
    public static final String FIELD_EXPECTED_DELIVERY_DATE = "expectedDeliveryDate";
    public static final String FIELD_SEND_TYPE_CODE = "sendTypeCode";
    public static final String FIELD_TRACKING_NUMBER = "trackingNumber";
    public static final String FIELD_TRY_USE_DEPARTMENT = "tryUseDepartment";
    public static final String FIELD_TRY_USE_WORKSHOP = "tryUseWorkshop";
    public static final String FIELD_CERTIFICATION_FLAG = "certificationFlag";
    public static final String FIELD_ATTACHMENT_UUID = "attachmentUuid";
    public static final String FIELD_BUYER_ATTACHMENT_UUID = "buyerAttachmentUuid";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_SAMPLE_RESULT = "sampleResult";
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
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("物料品类Id")
    @Encrypt
    private Long itemCategoryId;
    @ApiModelProperty("物料品类")
    private String itemCategoryCode;
    @ApiModelProperty("需求数量")
    private BigDecimal reqQuantity;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty("需求时间")
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
    private String sampleResult;
    @Transient
    @ApiModelProperty("申请人姓名")
    private String reqUserName;
    @Transient
    @ApiModelProperty("送样需求时间从")
    private String reqTimeFrom;
    @Transient
    @ApiModelProperty("送样需求时间至")
    private String reqTimeTo;
    @Transient
    private String sendTypeCodeMeaning;

    public RcwlSampleInfo() {
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

    public String getItemCategoryCode() {
        return this.itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
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

    public Date getExpectedDeliveryDate() {
        return this.expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
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

    public String getSampleResult() {
        return this.sampleResult;
    }

    public void setSampleResult(String sampleResult) {
        this.sampleResult = sampleResult;
    }

    public String getReqUserName() {
        return this.reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    public String getReqTimeFrom() {
        return this.reqTimeFrom;
    }

    public void setReqTimeFrom(String reqTimeFrom) {
        this.reqTimeFrom = reqTimeFrom;
    }

    public String getReqTimeTo() {
        return this.reqTimeTo;
    }

    public void setReqTimeTo(String reqTimeTo) {
        this.reqTimeTo = reqTimeTo;
    }

    public Long getItemCategoryId() {
        return this.itemCategoryId;
    }

    public void setItemCategoryId(Long itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
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
