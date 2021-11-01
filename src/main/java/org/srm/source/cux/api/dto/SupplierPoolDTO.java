package org.srm.source.cux.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.format.annotation.DateTimeFormat;
import org.srm.common.mybatis.domain.ExpandDomain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: longjunquan 21420
 * @Date: 2021/11/1 15:52
 * @Description:
 */
@ApiModel("供应商汇总")
@ExcelSheet(
        promptKey = "sslm.supplierManage",
        promptCode = "sslm.supplierManage.view.title.supplierLifecycleQuery"
)
public class SupplierPoolDTO extends ExpandDomain {
    public static final String FIELD_SUPPLIER_COMPANY_NUM = "companyNum";
    public static final String FIELD_SUPPLIER_COMPANY_ID = "supplierCompanyId";
    @ApiModelProperty("生命周期id")
    @Encrypt
    private Long lifeCycleId;
    @ApiModelProperty("查询用户租户id")
    private Long tenantId;
    @ApiModelProperty("采购方公司id")
    @Encrypt
    private Long companyId;
    @ApiModelProperty("采购方公司编码")
    private String companyNum;
    @ApiModelProperty("供应商编码")
    @ExcelColumn(
            promptKey = "sslm.common",
            promptCode = "sslm.common.view.supplier.code"
    )
    private String supplierCompanyNum;
    @ApiModelProperty("供应商名称")
    @ExcelColumn(
            promptKey = "sslm.common",
            promptCode = "sslm.common.view.supplier.englishName"
    )
    private String supplierCompanyName;
    @ApiModelProperty("供应商简称")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.supplierShortName"
    )
    private String supplierCompanyShortName;
    @ApiModelProperty("采购方公司名")
    @ExcelColumn(
            promptKey = "sslm.common",
            promptCode = "sslm.common.view.company.name"
    )
    private String companyNameMeaning;
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.erpFlag"
    )
    private String erpFlagMeaning;
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.specialSupplier"
    )
    private String authorizeFlagMeaning;
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.blacklistFlag"
    )
    private String blacklistFlagMeaning;
    @ApiModelProperty("阶段名")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.lifeStage"
    )
    private String stageDescription;
    @ApiModelProperty("统一社会信用代码")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.unifiedSocialCode"
    )
    private String unifiedSocialCode;
    @ApiModelProperty("组织机构代码")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.organizeInsCode"
    )
    private String organizingInstitutionCode;
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.businessNature"
    )
    private String businessNature;
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.ForeignRelation"
    )
    private String domesticForeignRelationMeaning;
    @ApiModelProperty("邓白氏编码")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.dunsCode"
    )
    private String dunsCode;
    @ApiModelProperty("企业类型")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.companyType"
    )
    private String companyTypeMeaning;
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.taxpayerTypeMeans"
    )
    private String taxpayerTypeMeaning;
    @ApiModelProperty("是否是特准供应商")
    @LovValue(
            lovCode = "HPFM.FLAG"
    )
    private Integer specialSupplierFlag;
    private String specialSupplierFlagMeaning;
    @ApiModelProperty("境内外关系")
    @Range(
            min = 0L,
            max = 1L
    )
    @LovValue(
            lovCode = "SPFM.DOMESTIC_FOREIGN_RELATION"
    )
    private Integer domesticForeignRelation;
    @ApiModelProperty("企业类型")
    @LovValue(
            lovCode = "HPFM.COMPANY_TYPE",
            meaningField = "companyTypeMeaning"
    )
    private String companyType;
    @ApiModelProperty("是否是黑名单供应商")
    @Range(
            min = 0L,
            max = 1L
    )
    @LovValue(
            lovCode = "HPFM.FLAG"
    )
    private Integer blacklistFlag;
    @ApiModelProperty("纳税人标识")
    @LovValue(
            lovCode = "HPFM.TAXPAYER_TYPE",
            meaningField = "taxpayerTypeMeaning"
    )
    private String taxpayerType;
    @ApiModelProperty("制造商")
    private Integer manufacturerFlag;
    @LovValue(
            lovCode = "SPFM.BUSINESS_NATURE"
    )
    private String manufacturer;
    private String manufacturerMeaning;
    @ApiModelProperty("贸易商")
    private Integer traderFlag;
    @LovValue(
            lovCode = "SPFM.BUSINESS_NATURE"
    )
    private String trader;
    private String traderMeaning;
    @ApiModelProperty("服务商")
    private Integer serviceFlag;
    @LovValue(
            lovCode = "SPFM.BUSINESS_NATURE"
    )
    private String service;
    private String serviceMeaning;
    @ApiModelProperty("服务商")
    private Integer agentflag;
    @LovValue(
            lovCode = "SPFM.BUSINESS_NATURE"
    )
    private String agent;
    private String agentMeaning;
    @ApiModelProperty("是否是erp供应商")
    @LovValue(
            lovCode = "HPFM.FLAG"
    )
    private Integer erpFlag;
    @ApiModelProperty("供应商租户id")
    private Long supplierTenantId;
    @ApiModelProperty("供应商公司id")
    @Encrypt
    private Long supplierCompanyId;
    @ApiModelProperty("是否是特准供应商")
    @Range(
            min = 0L,
            max = 1L
    )
    @LovValue(
            lovCode = "HPFM.FLAG"
    )
    private Integer authorizeFlag;
    @ApiModelProperty("阶段id")
    @Encrypt
    private Long stageId;
    @ApiModelProperty("spfm公司id")
    @Encrypt
    private Long spfmCompanyId;
    @ApiModelProperty("spfm供应商公司id")
    @Encrypt
    private Long spfmSupplierCompanyId;
    @ApiModelProperty("是否是永久黑名单供应商")
    @Range(
            min = 0L,
            max = 1L
    )
    @LovValue(
            lovCode = "HPFM.FLAG"
    )
    private Integer foreverBlacklistFlag;
    private String foreverBlacklistFlagMeaning;
    @ApiModelProperty("供应商英文名称")
    private String supplierEnglishName;
    @ApiModelProperty("联系人姓名")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.contactName"
    )
    private String contactName;
    @ApiModelProperty("邮箱")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.contactMail"
    )
    private String contactMail;
    @ApiModelProperty("手机号码")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.contactPhone"
    )
    private String contactPhone;
    @ApiModelProperty("注册资本(万元)")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.registeredCapital"
    )
    private BigDecimal registeredCapital;
    @ApiModelProperty("生命周期经办人")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.lifeStageAgent"
    )
    private String chargeName;
    @ApiModelProperty("生命阶段创建时间")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.lifeStageCreation",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date creationDate;
    @ApiModelProperty("供应商erp编码")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.erpSupplierCode"
    )
    private String supplierErpNum;
    @ApiModelProperty("采购员")
    private String purchaseAgentName;
    @ApiModelProperty("供应商分类")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.categoryName"
    )
    private String categoryName;
    @ApiModelProperty("法定代表人")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.legalRepName"
    )
    private String legalRepName;
    @ApiModelProperty("成立日期")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.buildDate"
    )
    private String buildDate;
    @ApiModelProperty("详细地址")
    @ExcelColumn(
            promptKey = "sslm.supplierManage",
            promptCode = "sslm.supplierManage.model.supplierManage.addressDetail"
    )
    private String addressDetail;
    private Long supplierBasicId;

    public SupplierPoolDTO() {
    }

    public String getChargeName() {
        return this.chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSupplierErpNum() {
        return this.supplierErpNum;
    }

    public void setSupplierErpNum(String supplierErpNum) {
        this.supplierErpNum = supplierErpNum;
    }

    public String getSupplierEnglishName() {
        return this.supplierEnglishName;
    }

    public void setSupplierEnglishName(String supplierEnglishName) {
        this.supplierEnglishName = supplierEnglishName;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMail() {
        return this.contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public BigDecimal getRegisteredCapital() {
        return this.registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyNameMeaning() {
        return this.companyNameMeaning;
    }

    public void setCompanyNameMeaning(String companyNameMeaning) {
        this.companyNameMeaning = companyNameMeaning;
    }

    public String getSupplierCompanyNum() {
        return this.supplierCompanyNum;
    }

    public void setSupplierCompanyNum(String supplierCompanyNum) {
        this.supplierCompanyNum = supplierCompanyNum;
    }

    public String getSupplierCompanyName() {
        return this.supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public String getSupplierCompanyShortName() {
        return this.supplierCompanyShortName;
    }

    public void setSupplierCompanyShortName(String supplierCompanyShortName) {
        this.supplierCompanyShortName = supplierCompanyShortName;
    }

    public String getErpFlagMeaning() {
        return this.erpFlagMeaning;
    }

    public void setErpFlagMeaning(String erpFlagMeaning) {
        this.erpFlagMeaning = erpFlagMeaning;
    }

    public Integer getSpecialSupplierFlag() {
        return this.specialSupplierFlag;
    }

    public void setSpecialSupplierFlag(Integer specialSupplierFlag) {
        this.specialSupplierFlag = specialSupplierFlag;
    }

    public Integer getDomesticForeignRelation() {
        return this.domesticForeignRelation;
    }

    public void setDomesticForeignRelation(Integer domesticForeignRelation) {
        this.domesticForeignRelation = domesticForeignRelation;
    }

    public String getCompanyType() {
        return this.companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getOrganizingInstitutionCode() {
        return this.organizingInstitutionCode;
    }

    public void setOrganizingInstitutionCode(String organizingInstitutionCode) {
        this.organizingInstitutionCode = organizingInstitutionCode;
    }

    public Integer getBlacklistFlag() {
        return this.blacklistFlag;
    }

    public void setBlacklistFlag(Integer blacklistFlag) {
        this.blacklistFlag = blacklistFlag;
    }

    public String getUnifiedSocialCode() {
        return this.unifiedSocialCode;
    }

    public void setUnifiedSocialCode(String unifiedSocialCode) {
        this.unifiedSocialCode = unifiedSocialCode;
    }

    public String getDunsCode() {
        return this.dunsCode;
    }

    public void setDunsCode(String dunsCode) {
        this.dunsCode = dunsCode;
    }

    public String getCompanyTypeMeaning() {
        return this.companyTypeMeaning;
    }

    public void setCompanyTypeMeaning(String companyTypeMeaning) {
        this.companyTypeMeaning = companyTypeMeaning;
    }

    public String getTaxpayerType() {
        return this.taxpayerType;
    }

    public void setTaxpayerType(String taxpayerType) {
        this.taxpayerType = taxpayerType;
    }

    public String getTaxpayerTypeMeaning() {
        return this.taxpayerTypeMeaning;
    }

    public void setTaxpayerTypeMeaning(String taxpayerTypeMeaning) {
        this.taxpayerTypeMeaning = taxpayerTypeMeaning;
    }

    public Integer getErpFlag() {
        return this.erpFlag;
    }

    public void setErpFlag(Integer erpFlag) {
        this.erpFlag = erpFlag;
    }

    public Integer getManufacturerFlag() {
        return this.manufacturerFlag;
    }

    public void setManufacturerFlag(Integer manufacturerFlag) {
        this.manufacturerFlag = manufacturerFlag;
    }

    public Integer getTraderFlag() {
        return this.traderFlag;
    }

    public void setTraderFlag(Integer traderFlag) {
        this.traderFlag = traderFlag;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturerMeaning() {
        return this.manufacturerMeaning;
    }

    public void setManufacturerMeaning(String manufacturerMeaning) {
        this.manufacturerMeaning = manufacturerMeaning;
    }

    public String getTrader() {
        return this.trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getTraderMeaning() {
        return this.traderMeaning;
    }

    public void setTraderMeaning(String traderMeaning) {
        this.traderMeaning = traderMeaning;
    }

    public Integer getServiceFlag() {
        return this.serviceFlag;
    }

    public void setServiceFlag(Integer serviceFlag) {
        this.serviceFlag = serviceFlag;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceMeaning() {
        return this.serviceMeaning;
    }

    public void setServiceMeaning(String serviceMeaning) {
        this.serviceMeaning = serviceMeaning;
    }

    public String getDomesticForeignRelationMeaning() {
        return this.domesticForeignRelationMeaning;
    }

    public void setDomesticForeignRelationMeaning(String domesticForeignRelationMeaning) {
        this.domesticForeignRelationMeaning = domesticForeignRelationMeaning;
    }

    public Long getSupplierTenantId() {
        return this.supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public Long getSupplierCompanyId() {
        return this.supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public Long getLifeCycleId() {
        return this.lifeCycleId;
    }

    public void setLifeCycleId(Long lifeCycleId) {
        this.lifeCycleId = lifeCycleId;
    }

    public Integer getAuthorizeFlag() {
        return this.authorizeFlag;
    }

    public void setAuthorizeFlag(Integer authorizeFlag) {
        this.authorizeFlag = authorizeFlag;
    }

    public String getStageDescription() {
        return this.stageDescription;
    }

    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }

    public Long getStageId() {
        return this.stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public String getBusinessNature() {
        return this.businessNature;
    }

    public void setBusinessNature(String businessNature) {
        this.businessNature = businessNature;
    }

    public String getSpecialSupplierFlagMeaning() {
        return this.specialSupplierFlagMeaning;
    }

    public void setSpecialSupplierFlagMeaning(String specialSupplierFlagMeaning) {
        this.specialSupplierFlagMeaning = specialSupplierFlagMeaning;
    }

    public String getBlacklistFlagMeaning() {
        return this.blacklistFlagMeaning;
    }

    public void setBlacklistFlagMeaning(String blacklistFlagMeaning) {
        this.blacklistFlagMeaning = blacklistFlagMeaning;
    }

    public String getAuthorizeFlagMeaning() {
        return this.authorizeFlagMeaning;
    }

    public void setAuthorizeFlagMeaning(String authorizeFlagMeaning) {
        this.authorizeFlagMeaning = authorizeFlagMeaning;
    }

    public Long getSpfmCompanyId() {
        return this.spfmCompanyId;
    }

    public void setSpfmCompanyId(Long spfmCompanyId) {
        this.spfmCompanyId = spfmCompanyId;
    }

    public Long getSpfmSupplierCompanyId() {
        return this.spfmSupplierCompanyId;
    }

    public void setSpfmSupplierCompanyId(Long spfmSupplierCompanyId) {
        this.spfmSupplierCompanyId = spfmSupplierCompanyId;
    }

    public Integer getForeverBlacklistFlag() {
        return this.foreverBlacklistFlag;
    }

    public void setForeverBlacklistFlag(Integer foreverBlacklistFlag) {
        this.foreverBlacklistFlag = foreverBlacklistFlag;
    }

    public String getForeverBlacklistFlagMeaning() {
        return this.foreverBlacklistFlagMeaning;
    }

    public void setForeverBlacklistFlagMeaning(String foreverBlacklistFlagMeaning) {
        this.foreverBlacklistFlagMeaning = foreverBlacklistFlagMeaning;
    }

    public String getPurchaseAgentName() {
        return this.purchaseAgentName;
    }

    public void setPurchaseAgentName(String purchaseAgentName) {
        this.purchaseAgentName = purchaseAgentName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLegalRepName() {
        return this.legalRepName;
    }

    public void setLegalRepName(String legalRepName) {
        this.legalRepName = legalRepName;
    }

    public String getBuildDate() {
        return this.buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getAddressDetail() {
        return this.addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Integer getAgentflag() {
        return this.agentflag;
    }

    public void setAgentflag(Integer agentflag) {
        this.agentflag = agentflag;
    }

    public String getAgent() {
        return this.agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentMeaning() {
        return this.agentMeaning;
    }

    public void setAgentMeaning(String agentMeaning) {
        this.agentMeaning = agentMeaning;
    }

    public Long getSupplierBasicId() {
        return this.supplierBasicId;
    }

    public void setSupplierBasicId(Long supplierBasicId) {
        this.supplierBasicId = supplierBasicId;
    }

    public String getCompanyNum() {
        return this.companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    @Override
    public String toString() {
        return "SupplierPoolDTO{lifeCycleId=" + this.lifeCycleId + ", tenantId=" + this.tenantId + ", companyId=" + this.companyId + ", supplierCompanyNum='" + this.supplierCompanyNum + '\'' + ", supplierCompanyName='" + this.supplierCompanyName + '\'' + ", supplierCompanyShortName='" + this.supplierCompanyShortName + '\'' + ", companyNameMeaning='" + this.companyNameMeaning + '\'' + ", erpFlagMeaning='" + this.erpFlagMeaning + '\'' + ", authorizeFlagMeaning='" + this.authorizeFlagMeaning + '\'' + ", blacklistFlagMeaning='" + this.blacklistFlagMeaning + '\'' + ", stageDescription='" + this.stageDescription + '\'' + ", unifiedSocialCode='" + this.unifiedSocialCode + '\'' + ", organizingInstitutionCode='" + this.organizingInstitutionCode + '\'' + ", businessNature='" + this.businessNature + '\'' + ", domesticForeignRelationMeaning='" + this.domesticForeignRelationMeaning + '\'' + ", dunsCode='" + this.dunsCode + '\'' + ", companyTypeMeaning='" + this.companyTypeMeaning + '\'' + ", taxpayerTypeMeaning='" + this.taxpayerTypeMeaning + '\'' + ", specialSupplierFlag=" + this.specialSupplierFlag + ", specialSupplierFlagMeaning='" + this.specialSupplierFlagMeaning + '\'' + ", domesticForeignRelation=" + this.domesticForeignRelation + ", companyType='" + this.companyType + '\'' + ", blacklistFlag=" + this.blacklistFlag + ", taxpayerType='" + this.taxpayerType + '\'' + ", manufacturerFlag=" + this.manufacturerFlag + ", manufacturer='" + this.manufacturer + '\'' + ", manufacturerMeaning='" + this.manufacturerMeaning + '\'' + ", traderFlag=" + this.traderFlag + ", trader='" + this.trader + '\'' + ", traderMeaning='" + this.traderMeaning + '\'' + ", serviceFlag=" + this.serviceFlag + ", service='" + this.service + '\'' + ", serviceMeaning='" + this.serviceMeaning + '\'' + ", agentflag=" + this.agentflag + ", agent='" + this.agent + '\'' + ", agentMeaning='" + this.agentMeaning + '\'' + ", erpFlag=" + this.erpFlag + ", supplierTenantId=" + this.supplierTenantId + ", supplierCompanyId=" + this.supplierCompanyId + ", authorizeFlag=" + this.authorizeFlag + ", stageId=" + this.stageId + ", spfmCompanyId=" + this.spfmCompanyId + ", spfmSupplierCompanyId=" + this.spfmSupplierCompanyId + ", foreverBlacklistFlag=" + this.foreverBlacklistFlag + ", foreverBlacklistFlagMeaning='" + this.foreverBlacklistFlagMeaning + '\'' + ", supplierEnglishName='" + this.supplierEnglishName + '\'' + ", contactName='" + this.contactName + '\'' + ", contactMail='" + this.contactMail + '\'' + ", contactPhone='" + this.contactPhone + '\'' + ", registeredCapital=" + this.registeredCapital + ", chargeName='" + this.chargeName + '\'' + ", creationDate=" + this.creationDate + ", supplierErpNum='" + this.supplierErpNum + '\'' + ", purchaseAgentName='" + this.purchaseAgentName + '\'' + ", categoryName='" + this.categoryName + '\'' + ", legalRepName='" + this.legalRepName + '\'' + ", buildDate='" + this.buildDate + '\'' + ", addressDetail='" + this.addressDetail + '\'' + '}';
    }
}
