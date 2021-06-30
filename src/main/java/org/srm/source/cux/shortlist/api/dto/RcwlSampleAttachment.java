package org.srm.source.cux.shortlist.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.starter.keyencrypt.core.Encrypt;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/28 15:39
 * @version:1.0
 */

@ApiModel("样品附件表")
@VersionAudit
@ModifyAudit
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(
        name = "sslm_sample_attachment"
)
public class RcwlSampleAttachment extends AuditDomain {
    public static final String FIELD_ATTACHMENT_ID = "attachmentId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SAMPLE_ID = "sampleId";
    public static final String FIELD_ATTACHMENT_TYPE = "attachmentType";
    public static final String FIELD_ATTACHMENT_DESC = "attachmentDesc";
    public static final String FIELD_REQUIRED_FLAG = "requiredFlag";
    public static final String FIELD_ATTACHMENT_UUID = "attachmentUuid";
    @ApiModelProperty("表ID")
    @Id
    @GeneratedValue
    @Encrypt
    private Long attachmentId;
    @ApiModelProperty(
            value = "租户id",
            required = true
    )
    @NotNull
    private Long tenantId;
    @ApiModelProperty(
            value = "样品信息id",
            required = true
    )
    @NotNull
    @Encrypt
    private Long sampleId;
    @ApiModelProperty(
            value = "附件类型",
            required = true
    )
    @NotBlank
    @LovValue(
            lovCode = "SSLM.ATTACHMENT_TYPE",
            meaningField = "attachmentTypeMeaning"
    )
    private String attachmentType;
    @ExcelColumn(
            title = "附件类型"
    )
    @Transient
    private String attachmentTypeMeaning;
    @ApiModelProperty("附件描述")
    private String attachmentDesc;
    @ApiModelProperty(
            value = "是否必传0/1",
            required = true
    )
    @NotNull
    private Integer requiredFlag;
    @ApiModelProperty("附件uuid")
    private String attachmentUuid;

    public RcwlSampleAttachment() {
    }

    public Long getAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSampleId() {
        return this.sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getAttachmentType() {
        return this.attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentTypeMeaning() {
        return this.attachmentTypeMeaning;
    }

    public void setAttachmentTypeMeaning(String attachmentTypeMeaning) {
        this.attachmentTypeMeaning = attachmentTypeMeaning;
    }

    public static String getFieldAttachmentId() {
        return "attachmentId";
    }

    public static String getFieldTenantId() {
        return "tenantId";
    }

    public static String getFieldSampleId() {
        return "sampleId";
    }

    public static String getFieldAttachmentType() {
        return "attachmentType";
    }

    public static String getFieldAttachmentDesc() {
        return "attachmentDesc";
    }

    public static String getFieldRequiredFlag() {
        return "requiredFlag";
    }

    public static String getFieldAttachmentUuid() {
        return "attachmentUuid";
    }

    public String getAttachmentDesc() {
        return this.attachmentDesc;
    }

    public void setAttachmentDesc(String attachmentDesc) {
        this.attachmentDesc = attachmentDesc;
    }

    public Integer getRequiredFlag() {
        return this.requiredFlag;
    }

    public void setRequiredFlag(Integer requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public String getAttachmentUuid() {
        return this.attachmentUuid;
    }

    public void setAttachmentUuid(String attachmentUuid) {
        this.attachmentUuid = attachmentUuid;
    }
}
