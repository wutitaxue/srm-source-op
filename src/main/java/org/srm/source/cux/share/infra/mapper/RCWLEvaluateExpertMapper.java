package org.srm.source.cux.share.infra.mapper;

import org.springframework.stereotype.Component;
import org.srm.source.share.api.dto.EvaluateScoreQueryDTO;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/19 11:19
 * @version:1.0
 */
@Component
public interface RCWLEvaluateExpertMapper {
    /**
     * 查询除去评分负责人之外未评分的数量
     * @param evaluateScoreQueryDTO
     * @return
     */
    int selectExpertScoreNumByExpertIdAndSourceHeaderId(EvaluateScoreQueryDTO evaluateScoreQueryDTO);
}
