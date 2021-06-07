package org.srm.source.cux.share.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.share.infra.constant.SourceBaseConstant;
import org.srm.source.cux.share.infra.mapper.RcwlEvaluateExpertMapper;
import org.srm.source.share.api.dto.EvaluateScoreQueryDTO;
import org.srm.source.share.infra.repository.impl.EvaluateExpertRepositoryImpl;
import org.srm.web.annotation.Tenant;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/19 11:17
 * @version:1.0
 */
@Component
@Tenant(SourceBaseConstant.TENANT_NUM)
public class RcwlEvaluateExpertRepositoryImpl extends EvaluateExpertRepositoryImpl {
    @Autowired
    private RcwlEvaluateExpertMapper evaluateExpertMapper;

    @Override
    public int selectExpertScoreNumByExpertIdAndSourceHeaderId(EvaluateScoreQueryDTO evaluateScoreQueryDTO) {
        return this.evaluateExpertMapper.selectExpertScoreNumByExpertIdAndSourceHeaderId(evaluateScoreQueryDTO);
    }

}
