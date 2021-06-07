package org.srm.source.cux.rfx.domain.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.domain.strategy.IRcwlEvaluateIndicAutoScoreCalculator;
import org.srm.source.cux.share.infra.constant.Constant;
import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.strategy.impl.ProportionCalculator;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * RCWL 比例法，计算逻辑
 * @author kaibo.li
 * @date 2021-05-12 16:28
 */

@Component
@Tenant(Constant.TENANT_NUM)
//public class RcwlProportionCalculator implements IRcwlEvaluateIndicAutoScoreCalculator {
public class RcwlProportionCalculator extends ProportionCalculator implements IRcwlEvaluateIndicAutoScoreCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlProportionCalculator.class);

    public RcwlProportionCalculator() {
        super();
    }

    @Override
    public BigDecimal calcScore(BigDecimal supplierQuotationPriceTotal, EvaluateScoreLineDTO evaluateScoreLineDTO, EvaluateIndicDetail evaluateIndicDetail, BigDecimal benchmarkPrice) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("24769----ProportionCalculator---scoreParam:supplierQuotationPriceTotal:{},benchmarkPrice:{}", supplierQuotationPriceTotal, benchmarkPrice);
        }

        BigDecimal res = BigDecimal.valueOf(100).subtract(((supplierQuotationPriceTotal.subtract(benchmarkPrice)).divide(benchmarkPrice, 4, RoundingMode.HALF_UP).multiply(evaluateIndicDetail.getMaxScore())));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("24769----ProportionCalculator---scoreResult:{}", res);
        }

        return res;
    }

    @Override
    public String getFormulaType() {
        return "PROPORTION";
    }
}
