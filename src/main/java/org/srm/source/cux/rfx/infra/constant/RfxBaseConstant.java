package org.srm.source.cux.rfx.infra.constant;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/28 9:53
 * @version:1.0
 */
public interface RfxBaseConstant {
    /**
     * 币种
     */
    interface CurrencyCode{
        String CNY = "CNY";
    }

    /**
     * 来源
     */
    interface SourceFrom{
        String RFX = "RFX";
        String BID = "BID";
    }

    interface RfxRole{
        String OPENED_BY = "OPENED_BY";
    }
    interface ScoreStatus{
        String SUBMITTED = "SUBMITTED";
    }

    interface ProcessAction{
        String CREATE = "CREATE";
    }

    interface EventCode{
        String SSRC_RFX_CREATE = "SSRC_RFX_CREATE";
    }
}
