package org.srm.source.cux.api.controller.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/21 14:38
 * @version:1.0
 */
public class RcwlBpmShortListHeaderDTO {
    @ApiModelProperty("标题：")
    @JsonProperty("FSubject")
    private String fSubject;

    @ApiModelProperty("入围单号")
    @JsonProperty("SHORTLISTNUM")
    private String shortListNum;

    @ApiModelProperty("项目名称")
    @JsonProperty("PROJECTNAME")
    private String projectname;

    @ApiModelProperty("公司")
    @JsonProperty("COMPANY")
    private String company;

    @ApiModelProperty("业务实体")
    @JsonProperty("BUSINESSENTITY")
    private String businessentity;

    @ApiModelProperty("招采方式")
    @JsonProperty("SHORTLISTCATEGORY")
    private String shortlistcategory;

    @ApiModelProperty("开始时间")
    @JsonProperty("STARTDATE")
    private String startdate;

    @ApiModelProperty("结束时间")
    @JsonProperty("FINISHDATE")
    private String finishdate;

    @ApiModelProperty("注册资金（万元）")
    @JsonProperty("CAPITAL")
    private String capital;

    @ApiModelProperty("成立年限")
    @JsonProperty("YEARS")
    private String years;

    @ApiModelProperty("最近1年营业额")
    @JsonProperty("ONEPROFIT")
    private String oneprofit;

    @ApiModelProperty("最近2年营业额")
    @JsonProperty("TWOPROFIT")
    private String twoprofit;

    @ApiModelProperty("要求内容")
    @JsonProperty("REQUIREMENTS")
    private String requirements;

    @ApiModelProperty("甄云链接")
    @JsonProperty("URL_MX")
    private String urlMx;

    @JsonProperty("RWGYS")
    private List<RcwlBpmShortListSuppierDTO> rcwlBpmShortListSuppierDTOList;

    @JsonProperty("RWSQXX")
    private List<RcwlBpmShortListPrDTO> rcwlBpmShortListPrDTOList;

    @JsonProperty("ATTACHMENTS1")
    private List<RcwlBpmShortListFilesDto> rcwlBpmShortListFilesDtoList;

    public List<RcwlBpmShortListSuppierDTO> getRcwlBpmShortListSuppierDTOList() {
        return rcwlBpmShortListSuppierDTOList;
    }

    public void setRcwlBpmShortListSuppierDTOList(List<RcwlBpmShortListSuppierDTO> rcwlBpmShortListSuppierDTOList) {
        this.rcwlBpmShortListSuppierDTOList = rcwlBpmShortListSuppierDTOList;
    }

    public List<RcwlBpmShortListPrDTO> getRcwlBpmShortListPrDTOList() {
        return rcwlBpmShortListPrDTOList;
    }

    public void setRcwlBpmShortListPrDTOList(List<RcwlBpmShortListPrDTO> rcwlBpmShortListPrDTOList) {
        this.rcwlBpmShortListPrDTOList = rcwlBpmShortListPrDTOList;
    }

    public List<RcwlBpmShortListFilesDto> getRcwlBpmShortListFilesDtoList() {
        return rcwlBpmShortListFilesDtoList;
    }

    public void setRcwlBpmShortListFilesDtoList(List<RcwlBpmShortListFilesDto> rcwlBpmShortListFilesDtoList) {
        this.rcwlBpmShortListFilesDtoList = rcwlBpmShortListFilesDtoList;
    }

    public String getfSubject() {
        return fSubject;
    }

    public void setfSubject(String fSubject) {
        this.fSubject = fSubject;
    }

    public String getShortListNum() {
        return shortListNum;
    }

    public void setShortListNum(String shortListNum) {
        this.shortListNum = shortListNum;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBusinessentity() {
        return businessentity;
    }

    public void setBusinessentity(String businessentity) {
        this.businessentity = businessentity;
    }

    public String getShortlistcategory() {
        return shortlistcategory;
    }

    public void setShortlistcategory(String shortlistcategory) {
        this.shortlistcategory = shortlistcategory;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getOneprofit() {
        return oneprofit;
    }

    public void setOneprofit(String oneprofit) {
        this.oneprofit = oneprofit;
    }

    public String getTwoprofit() {
        return twoprofit;
    }

    public void setTwoprofit(String twoprofit) {
        this.twoprofit = twoprofit;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getUrlMx() {
        return urlMx;
    }

    public void setUrlMx(String urlMx) {
        this.urlMx = urlMx;
    }
}
