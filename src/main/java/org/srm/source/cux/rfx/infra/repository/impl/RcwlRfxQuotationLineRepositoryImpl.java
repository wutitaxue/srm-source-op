package org.srm.source.cux.rfx.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.domain.repository.IRcwlRfxQuotationLineRepository;
import org.srm.source.cux.rfx.infra.mapper.IRcwlRfxQuotationLineMapper;
import org.srm.source.cux.share.infra.constant.Constant;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.share.domain.entity.EvaluateSummary;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * @author kaibo.li
 * @date 2021-05-19 16:17
 */
@Component
@Tenant(Constant.TENANT_NUM)
public class RcwlRfxQuotationLineRepositoryImpl implements IRcwlRfxQuotationLineRepository {

    @Autowired
    private IRcwlRfxQuotationLineMapper rcwlRfxQuotationLineMapper;

    @Override
    public List<RfxQuotationLine> querySumQuotationByRfxHeaderId(Long rfxHeaderId, Long tenantId) {
        return this.rcwlRfxQuotationLineMapper.querySumQuotationByRfxHeaderId(rfxHeaderId, tenantId);
    }

    @Override
    public List<EvaluateSummary> queryEvaluateSummary(EvaluateSummary evaluateSummary) {
        return this.rcwlRfxQuotationLineMapper.queryEvaluateSummary(evaluateSummary);
    }
}
