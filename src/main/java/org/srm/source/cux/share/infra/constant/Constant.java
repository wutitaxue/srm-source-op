package org.srm.source.cux.share.infra.constant;

/**
 * 常量定义
 * @author kaibo.li
 * @date 2021-05-27 11:13
 */
public interface Constant {
    /**
     * 核企租户
     */
    String TENANT_NUM = "SRM-RCWL";
    /**
     * 多轮
     */
    String MULTIPLE_ROUNDS = "MULTIPLE_ROUNDS";

    /**
     * RFX 状态
     */
    interface RfxStatus {
        String CHECK_PENDING = "CHECK_PENDING"; // 待核价
    }
}
