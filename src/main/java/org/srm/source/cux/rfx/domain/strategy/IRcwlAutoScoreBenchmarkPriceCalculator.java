package org.srm.source.cux.rfx.domain.strategy;

import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;

import java.math.BigDecimal;

/**
 * 自动评分基准价计算
 * @author kaibo.li
 * @date 2021-05-19 14:42
 */
public interface IRcwlAutoScoreBenchmarkPriceCalculator {
    BigDecimal getBenchmarkPrice(String priceTypeCode, AutoScoreDTO autoScoreDTO, EvaluateIndicDetail evaluateIndicDetail);

    String calcMethodName();
}
