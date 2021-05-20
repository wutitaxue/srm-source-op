package org.srm.source.cux.rfx.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RcwlScoringTeamData {

    //成员姓名
    @JsonProperty(value = "EXPERTNAME")
    private String ExpertName;

    //手机号
    @JsonProperty(value = "EXPERTPHONE")
    private String ExpertPhone;

    //角色
    @JsonProperty(value = "EXPERTCATEGORYMEANING")
    private String ExpertCategoryMeaning;

    public String getExpertName() {
        return ExpertName;
    }

    public void setExpertName(String expertName) {
        ExpertName = expertName;
    }

    public String getExpertPhone() {
        return ExpertPhone;
    }

    public void setExpertPhone(String expertPhone) {
        ExpertPhone = expertPhone;
    }

    public String getExpertCategoryMeaning() {
        return ExpertCategoryMeaning;
    }

    public void setExpertCategoryMeaning(String expertCategoryMeaning) {
        ExpertCategoryMeaning = expertCategoryMeaning;
    }
}
