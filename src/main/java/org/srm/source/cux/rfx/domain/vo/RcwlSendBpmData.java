package org.srm.source.cux.rfx.domain.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class RcwlSendBpmData {

    //标题
    @JSONField(name = "FSubject")
    private String FSubject;

    //项目单号
    @JSONField(name = "RFXNUM")
    private String RfxNum;

    //项目名称
    @JSONField(name = "RFXNAME")
    private String RfxName;

    //招采模式
    @JSONField(name = "BIDDINGMODE")
    private String BidDingMode;

    //招采方式
    @JSONField(name = "SHORTLISTCATEGORY")
    private String ShortListCategory;

    //合同形式
    @JSONField(name = "ATTRIBUTEVARCHAR9")
    private String AttributeVarchar9;

    //标段划分
    @JSONField(name = "SECTIONNUMBER")
    private String SectionNumber;

    //定标方式
    @JSONField(name = "ATTRIBUTEVARCHAR11")
    private String AttributeVarchar11;

    //招标经办人
    @JSONField(name = "CHECKEDBY")
    private String CheckedBy;

    //标底金额
    @JSONField(name = "ATTRIBUTEVARCHAR12")
    private String AttributeVarchar12;

    //回标标轮数
    @JSONField(name = "ROUNDNUMBER")
    private String RoundNumber;

    //评标方法
    @JSONField(name = "METHODREMARK")
    private String MethodRemark;

    //技术分得分比例
    @JSONField(name = "BUSINESSWEIGHT")
    private String BusinessWeight;

    //商务分得分比例
    @JSONField(name = "TECHNOLOGYWEIGHT")
    private String TechnologyWeight;

    //计划发标时间
    @JSONField(name = "ATTRIBUTEDATETIME10")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date AttributeDateTime10;

    //计划回标时间
    @JSONField(name = "QUOTATIONSTARTDATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date QuotationStartDate;

    //计划定标时间
    @JSONField(name = "ATTRIBUTEDATETIME11")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date AttributeDateTime11;

    //计划开标时间
    @JSONField(name = "QUOTATIONENDDATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date QuotationEndDate;

    //质疑截止时间
    @JSONField(name = "ATTRIBUTEDATETIME12")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date AttributeDateTime12;

    //中标供应商数
    @JSONField(name = "ATTRIBUTEVARCHAR13")
    private String AttributeVarchar13;

    //招标地址
    @JSONField(name = "ATTRIBUTEVARCHAR14")
    private String AttributeVarchar14;

    //招标联系人
    @JSONField(name = "PURCHASERNAME")
    private String PurchaserName;

    //联系人电话
    @JSONField(name = "PURCHASERPHONE")
    private String PurchaserPhone;

    //招标范围
    @JSONField(name = "ATTRIBUTEVARCHAR15")
    private String AttributeVarchar15;

    //投标保证金金额
    @JSONField(name = "BIDBOND")
    private String BidBond;

    //投标保证金开户行名称
    @JSONField(name = "ATTRIBUTEVARCHAR16")
    private String AttributeVarchar16;

    //投标保证金开户银行
    @JSONField(name = "ATTRIBUTEVARCHAR17")
    private String AttributeVarchar17;

    //投标保证金银行账号
    @JSONField(name = "ATTRIBUTEVARCHAR18")
    private String AttributeVarchar18;

    //招标小组组长
    @JSONField(name = "ATTRIBUTEVARCHAR19")
    private String AttributeVarchar19;

    //甄云链接
    @JSONField(name = "URL_MX")
    private String UrlMx;

    //评分小组
    @JSONField(name = "ZBLXPFXZ")
    private List<RcwlScoringTeamData> RcwlScoringTeamDataList;

    //供应商
    @JSONField(name = "ZBLXGYS")
    private List<RcwlSupplierData> RcwlSupplierDataList;

    //需求详细描述
    @JSONField(name = "ZBLXXQMS")
    private List<RcwlDetailData> RcwlDetailDataList;

    //附件
    @JSONField(name = "ATTACHMENTS1")
    private List<RcwlAttachmentData> RcwlAttachmentDataList;

    public String getUrlMx() {
        return UrlMx;
    }

    public void setUrlMx(String urlMx) {
        UrlMx = urlMx;
    }

    public List<RcwlScoringTeamData> getRcwlScoringTeamDataList() {
        return RcwlScoringTeamDataList;
    }

    public void setRcwlScoringTeamDataList(List<RcwlScoringTeamData> rcwlScoringTeamDataList) {
        RcwlScoringTeamDataList = rcwlScoringTeamDataList;
    }

    public List<RcwlSupplierData> getRcwlSupplierDataList() {
        return RcwlSupplierDataList;
    }

    public void setRcwlSupplierDataList(List<RcwlSupplierData> rcwlSupplierDataList) {
        RcwlSupplierDataList = rcwlSupplierDataList;
    }

    public List<RcwlDetailData> getRcwlDetailDataList() {
        return RcwlDetailDataList;
    }

    public void setRcwlDetailDataList(List<RcwlDetailData> rcwlDetailDataList) {
        RcwlDetailDataList = rcwlDetailDataList;
    }

    public List<RcwlAttachmentData> getRcwlAttachmentDataList() {
        return RcwlAttachmentDataList;
    }

    public void setRcwlAttachmentDataList(List<RcwlAttachmentData> rcwlAttachmentDataList) {
        RcwlAttachmentDataList = rcwlAttachmentDataList;
    }

    public String getFSubject() {
        return FSubject;
    }

    public void setFSubject(String FSubject) {
        this.FSubject = FSubject;
    }

    public String getRfxNum() {
        return RfxNum;
    }

    public void setRfxNum(String rfxNum) {
        RfxNum = rfxNum;
    }

    public String getRfxName() {
        return RfxName;
    }

    public void setRfxName(String rfxName) {
        RfxName = rfxName;
    }

    public String getBidDingMode() {
        return BidDingMode;
    }

    public void setBidDingMode(String bidDingMode) {
        BidDingMode = bidDingMode;
    }

    public String getShortListCategory() {
        return ShortListCategory;
    }

    public void setShortListCategory(String shortListCategory) {
        ShortListCategory = shortListCategory;
    }

    public String getAttributeVarchar9() {
        return AttributeVarchar9;
    }

    public void setAttributeVarchar9(String attributeVarchar9) {
        AttributeVarchar9 = attributeVarchar9;
    }

    public String getSectionNumber() {
        return SectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        SectionNumber = sectionNumber;
    }

    public String getAttributeVarchar11() {
        return AttributeVarchar11;
    }

    public void setAttributeVarchar11(String attributeVarchar11) {
        AttributeVarchar11 = attributeVarchar11;
    }

    public String getCheckedBy() {
        return CheckedBy;
    }

    public void setCheckedBy(String checkedBy) {
        CheckedBy = checkedBy;
    }

    public String getAttributeVarchar12() {
        return AttributeVarchar12;
    }

    public void setAttributeVarchar12(String attributeVarchar12) {
        AttributeVarchar12 = attributeVarchar12;
    }

    public String getRoundNumber() {
        return RoundNumber;
    }

    public void setRoundNumber(String roundNumber) {
        RoundNumber = roundNumber;
    }

    public String getMethodRemark() {
        return MethodRemark;
    }

    public void setMethodRemark(String methodRemark) {
        MethodRemark = methodRemark;
    }

    public String getBusinessWeight() {
        return BusinessWeight;
    }

    public void setBusinessWeight(String businessWeight) {
        BusinessWeight = businessWeight;
    }

    public String getTechnologyWeight() {
        return TechnologyWeight;
    }

    public void setTechnologyWeight(String technologyWeight) {
        TechnologyWeight = technologyWeight;
    }

    public Date getAttributeDateTime10() {
        return AttributeDateTime10;
    }

    public void setAttributeDateTime10(Date attributeDateTime10) {
        AttributeDateTime10 = attributeDateTime10;
    }

    public Date getQuotationStartDate() {
        return QuotationStartDate;
    }

    public void setQuotationStartDate(Date quotationStartDate) {
        QuotationStartDate = quotationStartDate;
    }

    public Date getAttributeDateTime11() {
        return AttributeDateTime11;
    }

    public void setAttributeDateTime11(Date attributeDateTime11) {
        AttributeDateTime11 = attributeDateTime11;
    }

    public Date getQuotationEndDate() {
        return QuotationEndDate;
    }

    public void setQuotationEndDate(Date quotationEndDate) {
        QuotationEndDate = quotationEndDate;
    }

    public Date getAttributeDateTime12() {
        return AttributeDateTime12;
    }

    public void setAttributeDateTime12(Date attributeDateTime12) {
        AttributeDateTime12 = attributeDateTime12;
    }

    public String getAttributeVarchar13() {
        return AttributeVarchar13;
    }

    public void setAttributeVarchar13(String attributeVarchar13) {
        AttributeVarchar13 = attributeVarchar13;
    }

    public String getAttributeVarchar14() {
        return AttributeVarchar14;
    }

    public void setAttributeVarchar14(String attributeVarchar14) {
        AttributeVarchar14 = attributeVarchar14;
    }

    public String getPurchaserName() {
        return PurchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        PurchaserName = purchaserName;
    }

    public String getPurchaserPhone() {
        return PurchaserPhone;
    }

    public void setPurchaserPhone(String purchaserPhone) {
        PurchaserPhone = purchaserPhone;
    }

    public String getAttributeVarchar15() {
        return AttributeVarchar15;
    }

    public void setAttributeVarchar15(String attributeVarchar15) {
        AttributeVarchar15 = attributeVarchar15;
    }

    public String getBidBond() {
        return BidBond;
    }

    public void setBidBond(String bidBond) {
        BidBond = bidBond;
    }

    public String getAttributeVarchar16() {
        return AttributeVarchar16;
    }

    public void setAttributeVarchar16(String attributeVarchar16) {
        AttributeVarchar16 = attributeVarchar16;
    }

    public String getAttributeVarchar17() {
        return AttributeVarchar17;
    }

    public void setAttributeVarchar17(String attributeVarchar17) {
        AttributeVarchar17 = attributeVarchar17;
    }

    public String getAttributeVarchar18() {
        return AttributeVarchar18;
    }

    public void setAttributeVarchar18(String attributeVarchar18) {
        AttributeVarchar18 = attributeVarchar18;
    }

    public String getAttributeVarchar19() {
        return AttributeVarchar19;
    }

    public void setAttributeVarchar19(String attributeVarchar19) {
        AttributeVarchar19 = attributeVarchar19;
    }
}
