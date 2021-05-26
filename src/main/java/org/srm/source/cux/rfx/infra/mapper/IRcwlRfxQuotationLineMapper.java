package org.srm.source.cux.rfx.infra.mapper;

import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.share.domain.entity.EvaluateSummary;

import java.util.List;

/**
 * @author kaibo.li
 * @date 2021-05-19 16:20
 */
public interface IRcwlRfxQuotationLineMapper {
    /**
     * 获取报价单行，含税价、未税价
     * @param rfxHeaderId rfxHeaderId
     * @param tenantId 租户ID
     * @return 报价行列表
     */
    List<RfxQuotationLine> querySumQuotationByRfxHeaderId(Long rfxHeaderId, Long tenantId);

    /**
     * 获取专家评分汇总信息
     * @param evaluateSummary EvaluateSummary
     * @return 专家评分汇总列表
     */
    List<EvaluateSummary> queryEvaluateSummary(EvaluateSummary evaluateSummary);
}
