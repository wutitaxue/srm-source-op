package org.srm.source.api.dto;


import java.util.Date;


/**
 * @author bin.zhang
 */
public class HeaderQueryDTO {
    private Long planId;
    private Long tenantId;
    private String planNum;
    private String state;
    private String format;
    private String companyName;
    private String prCategory;
    private String prWay;
    private String bidMethod;
    private String demanders;
    private Date creationDateFrom;
    private Date creationDateTo;
    private Long companyId;
    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getPlanNum() {
        return planNum;
    }

    public void setPlanNum(String planNum) {
        this.planNum = planNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrCategory() {
        return prCategory;
    }

    public void setPrCategory(String prCategory) {
        this.prCategory = prCategory;
    }

    public String getPrWay() {
        return prWay;
    }

    public void setPrWay(String prWay) {
        this.prWay = prWay;
    }

    public String getBidMethod() {
        return bidMethod;
    }

    public void setBidMethod(String bidMethod) {
        this.bidMethod = bidMethod;
    }

    public String getDemanders() {
        return demanders;
    }

    public void setDemanders(String demanders) {
        this.demanders = demanders;
    }

    public Date getCreationDateFrom() {
        return creationDateFrom;
    }

    public void setCreationDateFrom(Date creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    public Date getCreationDateTo() {
        return creationDateTo;
    }

    public void setCreationDateTo(Date creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "HeaderQueryDTO{" +
                "planId=" + planId +
                ", tenantId=" + tenantId +
                ", planNum='" + planNum + '\'' +
                ", state='" + state + '\'' +
                ", format='" + format + '\'' +
                ", companyName='" + companyName + '\'' +
                ", prCategory='" + prCategory + '\'' +
                ", prWay='" + prWay + '\'' +
                ", bidMethod='" + bidMethod + '\'' +
                ", demanders='" + demanders + '\'' +
                ", creationDateFrom=" + creationDateFrom +
                ", creationDateTo=" + creationDateTo +
                ", companyId=" + companyId +
                '}';
    }
}