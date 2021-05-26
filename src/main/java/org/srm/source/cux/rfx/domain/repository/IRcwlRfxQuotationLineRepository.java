package org.srm.source.cux.rfx.domain.repository;

import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.share.domain.entity.EvaluateSummary;

import java.util.List;

/**
 * @author kaibo.li
 * @date 2021-05-19 16:15
 */
public interface IRcwlRfxQuotationLineRepository {

    /**
     * 获取报价行，net_amount（不含税金额）或total_amount（含税金额）
     * @param rfxHeaderId rfx 头 ID
     * @return
     */
    List<RfxQuotationLine> querySumQuotationByRfxHeaderId(Long rfxHeaderId, Long tenantId);

    /**
     * 获取专家评分汇总信息
     * @param evaluateSummary EvaluateSummary
     * @return 专家评分汇总列表
     */
    List<EvaluateSummary> queryEvaluateSummary(EvaluateSummary evaluateSummary);
}
