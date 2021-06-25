package org.srm.source.cux.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.srm.common.mybatis.domain.ExpandDomain;

/**
 * 入围单供应商
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@ApiModel("入围单供应商头信息")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "scux_rcwl_supplier_header")
public class RcwlSupplierHeader extends ExpandDomain {

    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_SHORTLIST_HEADER_ID = "shortlistHeaderId";
    public static final String FIELD_SAMPLE_ID = "sampleId";
    public static final String FIELD_SELECTED = "selected";
    public static final String FIELD_ENROLL = "enroll";
    public static final String FIELD_ENROLL_DATE = "enrollDate";
    public static final String FIELD_CONTACTS = "contacts";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_STAGE_CODE = "stageCode";
    public static final String FIELD_QUALIFICATION = "qualification";
    public static final String FIELD_QUALIFICATION_INFO = "qualificationInfo";
    public static final String FIELD_CAPITAL = "capital";
    public static final String FIELD_YEARS = "years";
    public static final String FIELD_ONE_PROFIT = "oneProfit";
    public static final String FIELD_TWO_PROFIT = "twoProfit";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键ID，PK")
    @Id
    @GeneratedValue
    private Long supplierHeaderId;
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id")
    private Long tenantId;
    @ApiModelProperty(value = "公司id")
    private Long companyId;
    @ApiModelProperty(value = "入围单id")
    private Long shortlistHeaderId;
    @ApiModelProperty(value = "送样单id")
    private Long sampleId;
    @ApiModelProperty(value = "是否入围")
    private Integer selected;
    @ApiModelProperty(value = "是否报名")
    private Integer enroll;
    @ApiModelProperty(value = "报名时间")
    private Date enrollDate;
    @ApiModelProperty(value = "联系人")
    private String contacts;
    @ApiModelProperty(value = "联系方式")
    private String phone;
    @ApiModelProperty(value = "生命周期")
    private Long stageId;
    @ApiModelProperty(value = "是否符合")
    private Integer qualification;
    @ApiModelProperty(value = "符合说明")
    private String qualificationInfo;
    @ApiModelProperty(value = "注册资本")
    @NotNull
    private Long capital;
    @ApiModelProperty(value = "成立年限")
    @NotNull
    private Long years;
    @ApiModelProperty(value = "近1年营业额（万元）")
    @NotNull
    private Long oneProfit;
    @ApiModelProperty(value = "近2年营业额（万元）")
    @NotNull
    private Long twoProfit;
    @ApiModelProperty(value = "启用标识", required = true)
    @NotNull
    private Integer enabledFlag;

    @ApiModelProperty(value = "状态")
    private String status;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "公司编码")
    @Transient
    private String companyNum;

    @ApiModelProperty(value = "公司名称")
    @Transient
    private String companyName;

    @ApiModelProperty(value = "供应商编码")
    @Transient
    private String supplierNum;

    @ApiModelProperty(value = "供应商名称")
    @Transient
    private String supplierName;

    @ApiModelProperty(value = "生命周期描述")
    @Transient
    private String stageDescription;

    @ApiModelProperty("bpm链接")
    @Transient
    private String url;


    @Transient
    private Long supplierTenantId;
    @Transient
    private Long supplierContactId;
    @Transient
    private String contactMail;
    //
    // getter/setter
    // ------------------------------------------------------------------------------




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return 主键ID，PK
     */
    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 租户ID, hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 供应商id
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return 入围单id
     */
    public Long getShortlistHeaderId() {
        return shortlistHeaderId;
    }

    public void setShortlistHeaderId(Long shortlistHeaderId) {
        this.shortlistHeaderId = shortlistHeaderId;
    }

    /**
     * @return 送样单id
     */
    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    /**
     * @return 是否入围
     */
    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    /**
     * @return 是否报名
     */
    public Integer getEnroll() {
        return enroll;
    }

    public void setEnroll(Integer enroll) {
        this.enroll = enroll;
    }

    /**
     * @return 报名时间
     */
    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    /**
     * @return 联系人
     */
    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * @return 联系方式
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return 生命周期
     */
    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    /**
     * @return 是否符合
     */
    public Integer getQualification() {
        return qualification;
    }

    public void setQualification(Integer qualification) {
        this.qualification = qualification;
    }

    /**
     * @return 符合说明
     */
    public String getQualificationInfo() {
        return qualificationInfo;
    }

    public void setQualificationInfo(String qualificationInfo) {
        this.qualificationInfo = qualificationInfo;
    }

    /**
     * @return 注册资本
     */
    public Long getCapital() {
        return capital;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    /**
     * @return 成立年限
     */
    public Long getYears() {
        return years;
    }

    public void setYears(Long years) {
        this.years = years;
    }

    /**
     * @return 近1年营业额（万元）
     */
    public Long getOneProfit() {
        return oneProfit;
    }

    public void setOneProfit(Long oneProfit) {
        this.oneProfit = oneProfit;
    }

    /**
     * @return 近2年营业额（万元）
     */
    public Long getTwoProfit() {
        return twoProfit;
    }

    public void setTwoProfit(Long twoProfit) {
        this.twoProfit = twoProfit;
    }

    /**
     * @return 启用标识
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getSupplierHeaderId() {
        return supplierHeaderId;
    }

    public void setSupplierHeaderId(Long supplierHeaderId) {
        this.supplierHeaderId = supplierHeaderId;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getStageDescription() {
        return stageDescription;
    }

    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public Long getSupplierContactId() {
        return supplierContactId;
    }

    public void setSupplierContactId(Long supplierContactId) {
        this.supplierContactId = supplierContactId;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }
}
