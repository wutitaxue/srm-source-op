package org.srm.source.cux.bid.api.dto;

import java.util.Date;

/**
 * description
 *
 * @author xiubing.wang@hand-china.com 2021/09/27 18:09
 */
public class RfxHeaderInfoDTO {
    private Long rfxHeaderId;
    private Long tenantId;
    private String rfxNum;
    private String rfxTitle;
    private Date quotationEndDate;
    private Long userId;
    private String phone;
    private String email;
    private Integer passwordFlag;

    public Long getRfxHeaderId() {
        return rfxHeaderId;
    }

    public void setRfxHeaderId(Long rfxHeaderId) {
        this.rfxHeaderId = rfxHeaderId;
    }

    public String getRfxNum() {
        return rfxNum;
    }

    public void setRfxNum(String rfxNum) {
        this.rfxNum = rfxNum;
    }

    public String getRfxTitle() {
        return rfxTitle;
    }

    public void setRfxTitle(String rfxTitle) {
        this.rfxTitle = rfxTitle;
    }

    public Date getQuotationEndDate() {
        return quotationEndDate;
    }

    public void setQuotationEndDate(Date quotationEndDate) {
        this.quotationEndDate = quotationEndDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPasswordFlag() {
        return passwordFlag;
    }

    public void setPasswordFlag(Integer passwordFlag) {
        this.passwordFlag = passwordFlag;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
