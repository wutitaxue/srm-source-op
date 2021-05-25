package org.srm.source.cux.share.app.service;

import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.app.service.EvaluateScoreLineService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author kaibo.li
 * @date 2021-05-18 20:53
 */
//public interface IRcwlEvaluateScoreLineService extends EvaluateScoreLineService {
public interface IRcwlEvaluateScoreLineService  {

    /**
     * 商务，自动评分
     * @param autoScoreDTO 自动评分信息记录DTO
     */
//    void autoEvaluateScore(AutoScoreDTO autoScoreDTO);

    void _autoEvaluateScore(AutoScoreDTO autoScoreDTO);

    /**
     * 获取报价头 ID，与价格组成的 Map
     * @param autoScoreDTO 自动评分信息记录 DTO
     * @param priceTypeCode 价格类型（未使用）
     * @return
     */
    Map<Long, BigDecimal> getRfxQuotationLineMaps(AutoScoreDTO autoScoreDTO, String priceTypeCode);
}
