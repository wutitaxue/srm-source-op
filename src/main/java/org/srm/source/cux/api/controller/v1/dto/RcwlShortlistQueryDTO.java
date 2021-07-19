package org.srm.source.cux.api.controller.v1.dto;

import io.swagger.annotations.ApiModelProperty;


import java.util.Date;

/**
 * 入围单查询条件
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public class RcwlShortlistQueryDTO {

    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id")
    private Long tenantId;
    @ApiModelProperty(value = "公司ID")
    private Long companyId;
    @ApiModelProperty(value = "入围单号")
    private String shortlistNum;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "寻源类别")
    private String sourceCategory;
    @ApiModelProperty(value = "入围方式")
    private String shortlistCategory;
    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "开始时间从")
    private Date startDateFrom;
    @ApiModelProperty(value = "结束时间至")
    private Date finishDateTo;

    private String sourceFrom;

    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStartDateFrom() {
        return startDateFrom;
    }

    public void setStartDateFrom(Date startDateFrom) {
        this.startDateFrom = startDateFrom;
    }

    public Date getFinishDateTo() {
        return finishDateTo;
    }

    public void setFinishDateTo(Date finishDateTo) {
        this.finishDateTo = finishDateTo;
    }
}
