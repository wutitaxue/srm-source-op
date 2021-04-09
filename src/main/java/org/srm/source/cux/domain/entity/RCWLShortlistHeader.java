package org.srm.source.cux.domain.entity;




import org.srm.common.mybatis.domain.ExpandDomain;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "scux_rcwl_shortlist_header")
public class RCWLShortlistHeader extends ExpandDomain {

    private Long shortlistHeaderId;
    private Long tenantId;
    private Long companyId;
    private Long sampleId;
    private Long prLineId;

    //基础信息
    private String shortlistNum;
    private String projectName;
    private String company;
    private String businessEntity;
    private String sourceCategory;
    private String shortlistCategory;
    private Date startDate;
    private Date finishDate;
    private Long capital;
    private Long years;
    private Long oneProfit;
    private Long twoProfit;
    private String state;

    //附件要求
    private String accessoryName;
    private String files;
    private String accessoryInfo;
    private String uploader;
    private Date uploadDate;

    public Long getShortlistHeaderId() {
        return shortlistHeaderId;
    }

    public void setShortlistHeaderId(Long shortlistHeaderId) {
        this.shortlistHeaderId = shortlistHeaderId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getPrLineId() {
        return prLineId;
    }

    public void setPrLineId(Long prLineId) {
        this.prLineId = prLineId;
    }

    public String getShortlistNum() {
        return shortlistNum;
    }

    public void setShortlistNum(String shortlistNum) {
        this.shortlistNum = shortlistNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompanyName() {
        return company;
    }

    public void setCompanyName(String companyName) {
        this.company = companyName;
    }

    public String getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(String businessEntity) {
        this.businessEntity = businessEntity;
    }

    public String getSourceCategory() {
        return sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }

    public String getShortlistCategory() {
        return shortlistCategory;
    }

    public void setShortlistCategory(String shortlistCategory) {
        this.shortlistCategory = shortlistCategory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Long getCapital() {
        return capital;
    }

    public void setCapital(Long capital) {
        this.capital = capital;
    }

    public Long getYears() {
        return years;
    }

    public void setYears(Long years) {
        this.years = years;
    }

    public Long getOneProfit() {
        return oneProfit;
    }

    public void setOneProfit(Long oneProfit) {
        this.oneProfit = oneProfit;
    }

    public Long getTwoProfit() {
        return twoProfit;
    }

    public void setTwoProfit(Long twoProfit) {
        this.twoProfit = twoProfit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getAccessoryInfo() {
        return accessoryInfo;
    }

    public void setAccessoryInfo(String accessoryInfo) {
        this.accessoryInfo = accessoryInfo;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getPrHeaderId() {
        return prHeaderId;
    }

    public void setPrHeaderId(Long prHeaderId) {
        this.prHeaderId = prHeaderId;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getExamine() {
        return examine;
    }

    public void setExamine(Integer examine) {
        this.examine = examine;
    }

    public Integer getPublish() {
        return publish;
    }

    public void setPublish(Integer publish) {
        this.publish = publish;
    }

    public Integer getSample() {
        return sample;
    }

    public void setSample(Integer sample) {
        this.sample = sample;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    //申请信息
    private Long prHeaderId;
    private String lineNum;
    private String categoryName;
    private String itemCode;
    private String itemName;

    //状态
    private Integer examine;
    private Integer publish;
    private Integer sample;

    private Integer enabledFlag;
    private Long objectVersionNumber;
    private Long lastUpdatedBy;
    private Date lastUpdateDate;
}
