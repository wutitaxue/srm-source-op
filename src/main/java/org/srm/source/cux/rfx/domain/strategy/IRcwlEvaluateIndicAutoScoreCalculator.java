package org.srm.source.cux.rfx.domain.strategy;

import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;

/**
 * @author kaibo.li
 * @date 2021-05-19 15:06
 */
@Tenant("SRM-RCWL")
public interface IRcwlEvaluateIndicAutoScoreCalculator {
    BigDecimal calcScore(BigDecimal supplierQuotationPriceTotal, EvaluateScoreLineDTO evaluateScoreLineDTO, EvaluateIndicDetail evaluateIndicDetail, BigDecimal benchmarkPrice);

    String getFormulaType();
}
