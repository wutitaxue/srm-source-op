package org.srm.source.cux.rfx.domain.strategy;

import io.choerodon.core.exception.CommonException;
import org.springframework.stereotype.Service;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author kaibo.li
 * @date 2021-05-19 14:33
 */

@Service
public class RcwlAutoScoreStrategyService {
    private Map<String, IRcwlEvaluateIndicAutoScoreCalculator> calculatorMapMap = new HashMap();
    private Map<String, IRcwlAutoScoreBenchmarkPriceCalculator> benchmarkPriceCalculatorMap = new HashMap();

    public RcwlAutoScoreStrategyService(List<IRcwlEvaluateIndicAutoScoreCalculator> calculators, List<IRcwlAutoScoreBenchmarkPriceCalculator> benchmarkPriceCalculators) {
        calculators.forEach((e) -> {
            IRcwlEvaluateIndicAutoScoreCalculator var10000 = (IRcwlEvaluateIndicAutoScoreCalculator)this.calculatorMapMap.put(e.getFormulaType(), e);
        });
        benchmarkPriceCalculators.forEach((e) -> {
            IRcwlAutoScoreBenchmarkPriceCalculator var10000 = (IRcwlAutoScoreBenchmarkPriceCalculator)this.benchmarkPriceCalculatorMap.put(e.calcMethodName(), e);
        });
    }

    public BigDecimal calcScore(String calculatorType, BigDecimal supplierQuotationPriceTotal, EvaluateScoreLineDTO evaluateScoreLineDTO, EvaluateIndicDetail evaluateIndicDetail, BigDecimal benchmarkPrice) {
        IRcwlEvaluateIndicAutoScoreCalculator calculator = (IRcwlEvaluateIndicAutoScoreCalculator)this.calculatorMapMap.get(calculatorType);
        if (Objects.isNull(calculator)) {
            throw new CommonException("没有找到对应计算策略");
        } else {
            return calculator.calcScore(supplierQuotationPriceTotal, evaluateScoreLineDTO, evaluateIndicDetail, benchmarkPrice);
        }
    }

    public BigDecimal calcBenchmarkPrice(String methodName, String priceTypeCode, AutoScoreDTO autoScoreDTO, EvaluateIndicDetail evaluateIndicDetail) {
        IRcwlAutoScoreBenchmarkPriceCalculator calculator = (IRcwlAutoScoreBenchmarkPriceCalculator)this.benchmarkPriceCalculatorMap.get(methodName);
        if (Objects.isNull(calculator)) {
            throw new CommonException("没有找到对应计算策略");
        } else {
            return calculator.getBenchmarkPrice(priceTypeCode, autoScoreDTO, evaluateIndicDetail);
        }
    }
}
