package org.srm.source.cux.rfx.domain.strategy;

import io.choerodon.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.app.service.impl.EvaluateScoreLineServiceImpl;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.strategy.AutoScoreBenchmarkPriceCalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author kaibo.li
 * @date 2021-05-23 10:52
 */
@Service
public class RcwlAutoScoreStrategyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlAutoScoreStrategyService.class);

    private Map<String, IRcwlEvaluateIndicAutoScoreCalculator> calculatorMapMap = new HashMap();

    private Map<String, AutoScoreBenchmarkPriceCalculator> benchmarkPriceCalculatorMap = new HashMap();

    public RcwlAutoScoreStrategyService(List<IRcwlEvaluateIndicAutoScoreCalculator> calculators, List<AutoScoreBenchmarkPriceCalculator> benchmarkPriceCalculators) {
        calculators.forEach((e) -> {
            IRcwlEvaluateIndicAutoScoreCalculator var10000 = this.calculatorMapMap.put(e.getFormulaType(), e);
        });
        benchmarkPriceCalculators.forEach((e) -> {
            AutoScoreBenchmarkPriceCalculator var10000 = this.benchmarkPriceCalculatorMap.put(e.calcMethodName(), e);
        });
    }

    public BigDecimal calcScore(String calculatorType, BigDecimal supplierQuotationPriceTotal, EvaluateScoreLineDTO evaluateScoreLineDTO, EvaluateIndicDetail evaluateIndicDetail, BigDecimal benchmarkPrice) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("24769 RCWL calcScore : {}", calculatorType);
        }
        IRcwlEvaluateIndicAutoScoreCalculator calculator = this.calculatorMapMap.get(calculatorType);
        if (Objects.isNull(calculator)) {
            throw new CommonException("没有找到对应计算策略");
        } else {
            return calculator.calcScore(supplierQuotationPriceTotal, evaluateScoreLineDTO, evaluateIndicDetail, benchmarkPrice);
        }
    }
}
