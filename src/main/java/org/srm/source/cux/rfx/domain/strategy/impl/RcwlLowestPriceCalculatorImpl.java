package org.srm.source.cux.rfx.domain.strategy.impl;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.app.IRcwlEvaluateScoreLineService;
import org.srm.source.cux.rfx.domain.strategy.IRcwlAutoScoreBenchmarkPriceCalculator;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.strategy.impl.LowestPriceCalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author kaibo.li
 * @date 2021-05-19 14:52
 */
@Component
public class RcwlLowestPriceCalculatorImpl implements IRcwlAutoScoreBenchmarkPriceCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(LowestPriceCalculator.class);

    @Autowired
    private IRcwlEvaluateScoreLineService evaluateScoreLineService;

    public RcwlLowestPriceCalculatorImpl() {
    }

    @Override
    public BigDecimal getBenchmarkPrice(String priceTypeCode, AutoScoreDTO autoScoreDTO, EvaluateIndicDetail evaluateIndicDetail) {
        List<Long> invalidQuotationHeaderIdList = autoScoreDTO.getInvalidQuotationHeaderIdList();
//        Map quotationLineMaps;
        Map<Long, BigDecimal> quotationLineMaps = new HashMap<>();
        if ("RFX".equals(autoScoreDTO.getSourceFrom())) {
            quotationLineMaps = this.evaluateScoreLineService.getRfxQuotationLineMaps(autoScoreDTO, priceTypeCode);
        }

        Map<Long, BigDecimal> validQuotationLineMaps = (Map)quotationLineMaps.entrySet().stream().filter((map) -> {
            return CollectionUtils.isEmpty(invalidQuotationHeaderIdList) || !invalidQuotationHeaderIdList.contains(map.getKey());
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        BigDecimal min = (BigDecimal)validQuotationLineMaps.values().stream().filter((a) -> {
            return BigDecimal.ZERO.compareTo(a) < 0;
        }).min(BigDecimal::compareTo).get();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("AvgDownCalculator---min={},benchmarkPrice={}", min, min);
        }

        return min;
    }

    @Override
    public String calcMethodName() {
        return "LOWEST_PRICE";
    }
}
