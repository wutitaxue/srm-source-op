package org.srm.source.cux.share.infra.constant;

/**
 * @description: source通用常量
 * @author:yuanping.zhang
 * @createTime:2021/5/19 10:55
 * @version:1.0
 */
public interface SourceBaseConstant {
    /**
     * 核企租户
     */
    String TENANT_NUM = "SRM-RCWL";

    /**
     * 来源RFX/BID
     */
    interface SourceFrom {
        String RFX = "RFX";
        String BID = "BID";
    }

    /**
     * 文本
     */
    interface Text {
        String EXPERT_SUGGESTION = "系统自动计算平均分";
    }

    /**
     * 评分状态
     */
    interface ScoredStatus {
        String SCORED = "SCORED";
        String NEW = "NEW";
    }

    /**
     * RFX 状态
     */
    interface RfxStatus {
        String BID_EVALUATION_PENDING = "BID_EVALUATION_PENDING";
        String RFX_EVALUATION_PENDING = "RFX_EVALUATION_PENDING";
    }

    interface RfxCategory {
        String SSRC_EVALUATE = "SSRC_EVALUATE";
    }

    interface EventCode {
        String SSRC_EVALUATE_PENDING = "SSRC_EVALUATE_PENDING";
    }

    interface RfxAction {
        String SUMMARY_PENDING = "SUMMARY_PENDING";
    }

    /**
     * 寻源类别
     */
    interface SourceCategory {
        String RFQ = "RFQ";
    }

    /**
     * 评标方法
     */
    interface BidEvalMethod {
        String COMPREHENSIVE_SCORE = "综合评分法";
    }

    /**
     * 专家组
     */
    interface TeamMeaning {
        String BUSINESS_TECHNOLOGY_GROUP = "商务技术组";
        String BUSINESS_GROUP = "商务组";
        String TECHNOLOGY_GROUP = "技术组";
    }
}
