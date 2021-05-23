package org.srm.source.cux.rfx.domain.strategy;

import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;

import java.math.BigDecimal;

/**
 * 自动评分计算接口
 * @author kaibo.li
 * @date 2021-05-19 15:06
 */
public interface IRcwlEvaluateIndicAutoScoreCalculator {
    /**
     *
     * @param supplierQuotationPriceTotal 供应商报价
     * @param evaluateScoreLineDTO 评分行
     * @param evaluateIndicDetail 自动评分计算逻辑
     * @param benchmarkPrice 基准价
     * @return 得分
     */
    BigDecimal calcScore(BigDecimal supplierQuotationPriceTotal, EvaluateScoreLineDTO evaluateScoreLineDTO, EvaluateIndicDetail evaluateIndicDetail, BigDecimal benchmarkPrice);

    String getFormulaType();
}
