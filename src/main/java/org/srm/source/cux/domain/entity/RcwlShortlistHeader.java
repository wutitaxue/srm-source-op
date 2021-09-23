package org.srm.source.cux.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.srm.common.mybatis.domain.ExpandDomain;

/**
 * 入围单头表
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@ApiModel("入围单头表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "scux_rcwl_shortlist_header")
public class RcwlShortlistHeader extends ExpandDomain {

    public static final String FIELD_SHORTLIST_HEADER_ID = "shortlistHeaderId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_SAMPLE_ID = "sampleId";
    public static final String FIELD_SHORTLIST_NUM = "shortlistNum";
    public static final String FIELD_PROJECT_NAME = "projectName";
    public static final String FIELD_BUSINESS_COMPANY_ID = "businessCompanyId";
    public static final String FIELD_SOURCE_CATEGORY = "sourceCategory";
    public static final String FIELD_SHORTLIST_CATEGORY = "shortlistCategory";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_FINISH_DATE = "finishDate";
    public static final String FIELD_CAPITAL = "capital";
    public static final String FIELD_YEARS = "years";
    public static final String FIELD_ONE_PROFIT = "oneProfit";
    public static final String FIELD_TWO_PROFIT = "twoProfit";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_EXAMINE = "examine";
    public static final String FIELD_PUBLISH = "publish";
    public static final String FIELD_SAMPLE = "sample";
    public static final String FIELD_REQUEST_CONTENT = "requestContent";
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
    private Long shortlistHeaderId;
    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id")
    private Long tenantId;
    @ApiModelProperty(value = "公司ID")
    private Long companyId;
    @ApiModelProperty(value = "送样单id")
    private Long sampleId;
    @ApiModelProperty(value = "入围单号")
    private String shortlistNum;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "实体公司ID")
    private Long businessCompanyId;
    @ApiModelProperty(value = "寻源类别")
    private String sourceCategory;
    @ApiModelProperty(value = "入围方式")
    private String shortlistCategory;
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    @ApiModelProperty(value = "结束时间")
    private Date finishDate;
    @ApiModelProperty(value = "注册资本")
    private Long capital;
    @ApiModelProperty(value = "成立年限")
    private Long years;
    @ApiModelProperty(value = "近1年营业额（万元）")
    private BigDecimal oneProfit;
    @ApiModelProperty(value = "近2年营业额（万元）")
    private BigDecimal twoProfit;
    @ApiModelProperty(value = "状态")
    private String state;
    @ApiModelProperty(value = "是否审核")
    private Integer examine;
    @ApiModelProperty(value = "是否发布")
    private Integer publish;
    @ApiModelProperty(value = "是否送样")
    private Integer sample;
    @ApiModelProperty(value = "要求内容")
    private String requestContent;
    @ApiModelProperty(value = "启用标识", required = true)
    @NotNull
    private Integer enabledFlag;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "采购订单行")
    @Transient
    private List<Long> prLineIds;

    @ApiModelProperty(value = "寻源类别")
    @Transient
    private String sourceCategoryMeaning;

    @ApiModelProperty(value = "公司编码")
    @Transient
    private String companyNum;

    @ApiModelProperty(value = "公司名称")
    @Transient
    private String companyName;

    @ApiModelProperty(value = "创建人姓名")
    @Transient
    private String createdName;


    @ApiModelProperty(value = "业务实体名称")
    @Transient
    private String ouName;

    @ApiModelProperty(value = "业务实体编码")
    @Transient
    private String ouCode;

    @ApiModelProperty(value = "供应商状态")
    @Transient
    private String supplierStatus;

    @ApiModelProperty(value = "供应商状态")
    @Transient
    private Long supplierId;

    @ApiModelProperty(value = "供应商状态")
    @Transient
    private Integer selected;

    @ApiModelProperty(value = "入围单供应商头ID")
    @Transient
    private Long supplierHeaderId;

    @ApiModelProperty("bpmUrl")
    @Transient

    private String url;

    @ApiModelProperty("采购申请编号")
    @Transient

    private String prNum;

    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public String getPrNum() {
        return prNum;
    }

    public void setPrNum(String prNum) {
        this.prNum = prNum;
    }

    public String getSourceCategoryMeaning() {
        return sourceCategoryMeaning;
    }

    public void setSourceCategoryMeaning(String sourceCategoryMeaning) {
        this.sourceCategoryMeaning = sourceCategoryMeaning;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return 主键ID，PK
     */
    public Long getShortlistHeaderId() {
        return shortlistHeaderId;
    }

    public void setShortlistHeaderId(Long shortlistHeaderId) {
        this.shortlistHeaderId = shortlistHeaderId;
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
     * @return 公司ID
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
     * @return 入围单号
     */
    public String getShortlistNum() {
        return shortlistNum;
    }

    public void setShortlistNum(String shortlistNum) {
        this.shortlistNum = shortlistNum;
    }

    /**
     * @return 项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return 实体公司ID
     */
    public Long getBusinessCompanyId() {
        return businessCompanyId;
    }

    public void setBusinessCompanyId(Long businessCompanyId) {
        this.businessCompanyId = businessCompanyId;
    }

    /**
     * @return 寻源类别
     */
    public String getSourceCategory() {
        return sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }

    /**
     * @return 入围方式
     */
    public String getShortlistCategory() {
        return shortlistCategory;
    }

    public void setShortlistCategory(String shortlistCategory) {
        this.shortlistCategory = shortlistCategory;
    }

    /**
     * @return 开始时间
     */
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return 结束时间
     */
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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
    public BigDecimal getOneProfit() {
        return oneProfit;
    }

    public void setOneProfit(BigDecimal oneProfit) {
        this.oneProfit = oneProfit;
    }

    /**
     * @return 近2年营业额（万元）
     */
    public BigDecimal getTwoProfit() {
        return twoProfit;
    }

    public void setTwoProfit(BigDecimal twoProfit) {
        this.twoProfit = twoProfit;
    }

    /**
     * @return 状态
     */
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return 是否审核
     */
    public Integer getExamine() {
        return examine;
    }

    public void setExamine(Integer examine) {
        this.examine = examine;
    }

    /**
     * @return 是否发布
     */
    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }

    /**
     * @return 是否送样
     */
    public Integer getSample() {
        return sample;
    }

    public void setSample(Integer sample) {
        this.sample = sample;
    }

    /**
     * @return 要求内容
     */
    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
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

    public List<Long> getPrLineIds() {
        return prLineIds;
    }

    public void setPrLineIds(List<Long> prLineIds) {
        this.prLineIds = prLineIds;
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

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getOuName() {
        return ouName;
    }

    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    public String getOuCode() {
        return ouCode;
    }

    public void setOuCode(String ouCode) {
        this.ouCode = ouCode;
    }

    public String getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Long getSupplierHeaderId() {
        return supplierHeaderId;
    }

    public void setSupplierHeaderId(Long supplierHeaderId) {
        this.supplierHeaderId = supplierHeaderId;
    }
}
