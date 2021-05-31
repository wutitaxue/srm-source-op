package org.srm.source.cux.rfx.domain.strategy;

import io.choerodon.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.srm.source.cux.share.domain.strategy.IRcwlAutoScoreBenchmarkPriceCalculator;
import org.srm.source.cux.share.infra.constant.Constant;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.web.annotation.Tenant;

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
@Tenant(Constant.TENANT_NUM)
//public class RcwlAutoScoreStrategyService extends AutoScoreStrategyService {
public class RcwlAutoScoreStrategyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlAutoScoreStrategyService.class);

    private Map<String, IRcwlEvaluateIndicAutoScoreCalculator> calculatorMapMap = new HashMap();

    private Map<String, IRcwlAutoScoreBenchmarkPriceCalculator> benchmarkPriceCalculatorMap = new HashMap();

    public RcwlAutoScoreStrategyService(List<IRcwlEvaluateIndicAutoScoreCalculator> calculators, List<IRcwlAutoScoreBenchmarkPriceCalculator> benchmarkPriceCalculators) {
        /*super(calculators.stream().map(item -> (IEvaluateIndicAutoScoreCalculator) item).collect(Collectors.toList()),
                benchmarkPriceCalculators.stream().map(item -> (AutoScoreBenchmarkPriceCalculator)item).collect(Collectors.toList()));*/
        calculators.forEach(e->calculatorMapMap.put(e.getFormulaType(),e));
        benchmarkPriceCalculators.forEach(e->benchmarkPriceCalculatorMap.put(e.calcMethodName(),e));
    }

    /**
     *
     * @param calculatorType 计算公式类型
     * @param supplierQuotationPriceTotal 供应商投标总价
     * @param evaluateScoreLineDTO 评分要素行（这里用于取单据配置的最低分和最高分）
     * @param evaluateIndicDetail 评分细则（配置的计算公式相关的信息）
     * @param benchmarkPrice 基准价
     * @return score 得分
     */
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

    public BigDecimal calcBenchmarkPrice(String methodName, String priceTypeCode, AutoScoreDTO autoScoreDTO, EvaluateIndicDetail evaluateIndicDetail) {
        IRcwlAutoScoreBenchmarkPriceCalculator calculator = this.benchmarkPriceCalculatorMap.get(methodName);
        if (Objects.isNull(calculator)) {
            throw new CommonException("没有找到对应计算策略");
        } else {
            return calculator.getBenchmarkPrice(priceTypeCode, autoScoreDTO, evaluateIndicDetail);
        }
    }
}
