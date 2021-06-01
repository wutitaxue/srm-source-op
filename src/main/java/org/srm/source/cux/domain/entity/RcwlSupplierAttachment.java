package org.srm.source.cux.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.srm.common.mybatis.domain.ExpandDomain;

/**
 * 入围供应商单附件
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@ApiModel("入围供应商单附件")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "scux_rcwl_supplier_attachment")
public class RcwlSupplierAttachment extends ExpandDomain {

    public static final String FIELD_RCWL_SUPPLIER_ATTACHMENT_ID = "rcwlSupplierAttachmentId";
    public static final String FIELD_RCWL_SUPPLIER_HEADER_ID = "rcwlSupplierHeaderId";
    public static final String FIELD_RCWL_SHORTLIST_ATTACHMENT_ID = "rcwlShortlistAttachmentId";
    public static final String FIELD_ATTACHMENT_NAME = "attachmentName";
    public static final String FIELD_ATTACHMENT_URL = "attachmentUrl";
    public static final String FIELD_ATTACHMENT_DESC = "attachmentDesc";
    public static final String FIELD_UPLOAD_USER_ID = "uploadUserId";
    public static final String FIELD_TENANT_ID = "tenantId";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    private Long rcwlSupplierAttachmentId;
    @ApiModelProperty(value = "入围单供应商ID")
    private Long rcwlSupplierHeaderId;
    @ApiModelProperty(value = "入围单附件模板ID")
    private Long rcwlShortlistAttachmentId;
    @ApiModelProperty(value = "附件名称")
    private Long attachmentName;
    @ApiModelProperty(value = "附件URL")
    private String attachmentUrl;
    @ApiModelProperty(value = "附件说明")
    private String attachmentDesc;
    @ApiModelProperty(value = "上传人", required = true)
    @NotNull
    private Long uploadUserId;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @ApiModelProperty(value = "附件模板地址")
    private String tempAttachmentUrl;


    @Transient
    @ApiModelProperty(value = "上传姓名")
    private String uploadUserName;
    @Transient
    @ApiModelProperty(value = "入围单头id")
    private Long shortListId;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
    public Long getRcwlSupplierAttachmentId() {
        return rcwlSupplierAttachmentId;
    }

    public void setRcwlSupplierAttachmentId(Long rcwlSupplierAttachmentId) {
        this.rcwlSupplierAttachmentId = rcwlSupplierAttachmentId;
    }

    public Long getShortListId() {
        return shortListId;
    }

    public void setShortListId(Long shortListId) {
        this.shortListId = shortListId;
    }

    /**
     * @return 入围单供应商ID
     */
    public Long getRcwlSupplierHeaderId() {
        return rcwlSupplierHeaderId;
    }

    public void setRcwlSupplierHeaderId(Long rcwlSupplierHeaderId) {
        this.rcwlSupplierHeaderId = rcwlSupplierHeaderId;
    }

    /**
     * @return 入围单附件模板ID
     */
    public Long getRcwlShortlistAttachmentId() {
        return rcwlShortlistAttachmentId;
    }

    public void setRcwlShortlistAttachmentId(Long rcwlShortlistAttachmentId) {
        this.rcwlShortlistAttachmentId = rcwlShortlistAttachmentId;
    }

    /**
     * @return 附件名称
     */
    public Long getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(Long attachmentName) {
        this.attachmentName = attachmentName;
    }

    /**
     * @return 附件URL
     */
    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    /**
     * @return 附件说明
     */
    public String getAttachmentDesc() {
        return attachmentDesc;
    }

    public void setAttachmentDesc(String attachmentDesc) {
        this.attachmentDesc = attachmentDesc;
    }

    /**
     * @return 上传人
     */
    public Long getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }


    public String getTempAttachmentUrl() {
        return tempAttachmentUrl;
    }

    public void setTempAttachmentUrl(String tempAttachmentUrl) {
        this.tempAttachmentUrl = tempAttachmentUrl;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }
}
