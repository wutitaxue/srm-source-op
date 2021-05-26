package org.srm.source.cux.share.domain.strategy.impl;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.cux.share.domain.strategy.IRcwlAutoScoreBenchmarkPriceCalculator;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.strategy.impl.LowestPriceCalculator;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author kaibo.li
 * @date 2021-05-24 18:26
 */
@Component
@Tenant("SRM-RCWL")
//public class RcwlLowestPriceCalculator extends LowestPriceCalculator {
public class RcwlLowestPriceCalculator extends LowestPriceCalculator implements IRcwlAutoScoreBenchmarkPriceCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlLowestPriceCalculator.class);

    /**
     * 新写的
     */
    @Autowired
    private IRcwlEvaluateScoreLineService rcwlEvaluateScoreLineService;

    public RcwlLowestPriceCalculator() {
        super();
    }

    @Override
    public BigDecimal getBenchmarkPrice(String priceTypeCode, AutoScoreDTO autoScoreDTO, EvaluateIndicDetail evaluateIndicDetail) {
        List<Long> invalidQuotationHeaderIdList = autoScoreDTO.getInvalidQuotationHeaderIdList();
        Map<Long, BigDecimal> quotationLineMaps;
        if ("RFX".equals(autoScoreDTO.getSourceFrom())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("24769  getBenchmarkPrice : {}", priceTypeCode);
            }
            quotationLineMaps = this.rcwlEvaluateScoreLineService.getRfxQuotationLineMaps(autoScoreDTO, priceTypeCode);
        } else {
            quotationLineMaps = this.rcwlEvaluateScoreLineService.getBidQuotationLineMaps(autoScoreDTO, priceTypeCode);
        }

        Map<Long, BigDecimal> validQuotationLineMaps = quotationLineMaps.entrySet().stream().filter((map) -> {
            return CollectionUtils.isEmpty(invalidQuotationHeaderIdList) || !invalidQuotationHeaderIdList.contains(map.getKey());
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        BigDecimal min = validQuotationLineMaps.values().stream().filter((a) -> {
            return BigDecimal.ZERO.compareTo(a) < 0;
        }).min(BigDecimal::compareTo).get();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("24769     AvgDownCalculator---min={},benchmarkPrice={}", min, min);
        }

        return min;
    }

    @Override
    public String calcMethodName() {
        return "LOWEST_PRICE";
    }
}
