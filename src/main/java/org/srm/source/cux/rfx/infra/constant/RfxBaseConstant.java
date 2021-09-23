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
        /**
         * 自动生成专家（评分负责人）
         */
        String AUTO = "AUTO";/**
         /**
         * 线上专家评分
         */
        String ONLINE = "ONLINE";
    }

    interface RfxRole{
        String OPENED_BY = "OPENED_BY";
    }
    interface ScoreStatus{
        String SUBMITTED = "SUBMITTED";
        String NEW = "NEW";
    }

    interface ProcessAction{
        String CREATE = "CREATE";
    }

    interface EventCode{
        String SSRC_RFX_CREATE = "SSRC_RFX_CREATE";
    }
}
