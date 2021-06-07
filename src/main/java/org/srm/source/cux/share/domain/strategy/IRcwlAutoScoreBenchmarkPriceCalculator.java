package org.srm.source.cux.share.domain.strategy;

import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.strategy.AutoScoreBenchmarkPriceCalculator;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;

/**
 * @author kaibo.li
 * @date 2021-05-25 12:32
 */
@Tenant("SRM-RCWL")
public interface IRcwlAutoScoreBenchmarkPriceCalculator extends AutoScoreBenchmarkPriceCalculator {
    @Override
    BigDecimal getBenchmarkPrice(String priceTypeCode, AutoScoreDTO autoScoreDTO, EvaluateIndicDetail evaluateIndicDetail);

    @Override
    String calcMethodName();
}
