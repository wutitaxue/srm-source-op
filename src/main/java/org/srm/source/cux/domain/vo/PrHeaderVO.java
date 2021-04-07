package org.srm.source.cux.domain.vo;

import org.srm.common.mybatis.domain.ExpandDomain;

/**
 * @author 15640
 */
public class PrHeaderVO extends ExpandDomain {
    private Long prHeaderId;
    private String prNum;
    private String lineNum;
    private Long prLineId;

    public Long getPrHeaderId() {
        return prHeaderId;
    }

    public void setPrHeaderId(Long prHeaderId) {
        this.prHeaderId = prHeaderId;
    }

    public String getPrNum() {
        return prNum;
    }

    public void setPrNum(String prNum) {
        this.prNum = prNum;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public Long getPrLineId() {
        return prLineId;
    }

    public void setPrLineId(Long prLineId) {
        this.prLineId = prLineId;
    }
}
