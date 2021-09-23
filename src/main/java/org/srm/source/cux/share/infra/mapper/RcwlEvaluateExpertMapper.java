package org.srm.source.cux.share.infra.mapper;

import io.choerodon.mybatis.helper.ExtendMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.srm.source.cux.share.infra.constant.SourceBaseConstant;
import org.srm.source.share.api.dto.EvaluateScoreQueryDTO;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.infra.mapper.EvaluateExpertMapper;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/19 11:19
 * @version:1.0
 */
@Component
@Tenant(SourceBaseConstant.TENANT_NUM)
public interface RcwlEvaluateExpertMapper extends EvaluateExpertMapper, ExtendMapper<EvaluateExpert> {
    /**
     * 查询除去评分负责人之外未评分的数量
     * @param evaluateScoreQueryDTO
     * @return
     */
    @Override
    int selectExpertScoreNumByExpertIdAndSourceHeaderId(EvaluateScoreQueryDTO evaluateScoreQueryDTO);

    /**
     * 查询专家列表
     *
     * @param tenantId       租户id
     * @param sourceHeaderId 来源单据头id
     * @param sourceFrom     来源单据类型
     * @param expertStatus   专家状态
     * @param bidRuleType    标的规则
     * @return
     */
    @Override
    List<EvaluateExpert> queryEvaluateExpert(@Param("tenantId") Long tenantId, @Param("sourceHeaderId") Long sourceHeaderId, @Param("sourceFrom") String sourceFrom, @Param("expertStatus") String expertStatus, @Param("bidRuleType") String bidRuleType);
}
