package org.srm.source.cux.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.cux.rfx.domain.repository.IRcwlRfxQuotationLineRepository;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.cux.share.infra.constant.Constant;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.app.service.EvaluateScoreLineService;
import org.srm.source.share.app.service.impl.RoundHeaderServiceImpl;
import org.srm.source.share.domain.entity.EvaluateSummary;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.source.share.domain.repository.EvaluateSummaryRepository;
import org.srm.source.share.domain.repository.RoundHeaderRepository;
import org.srm.source.share.domain.service.IRoundHeaderDomainService;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * RCWL 多轮报价
 * @author kaibo.li
 * @date 2021-05-27 11:13
 */
@Service
@Tenant(Constant.TENANT_NUM)
public class RcwlRoundHeaderServiceImpl extends RoundHeaderServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlRoundHeaderServiceImpl.class);

    /**
     * 原始的
     */
    @Autowired
    RoundHeaderRepository roundHeaderRepository;
    @Autowired
    private IRoundHeaderDomainService roundHeaderDomainService;
    @Autowired
    RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private EvaluateScoreLineService evaluateScoreLineService;
    @Autowired
    private EvaluateSummaryRepository evaluateSummaryRepository;

    /**
     * 新写的
     */
    @Autowired
    private IRcwlEvaluateScoreLineService rcwlEvaluateScoreLineService;

    @Autowired
    private IRcwlRfxQuotationLineRepository rcwlRfxQuotationLineRepository;

    public RcwlRoundHeaderServiceImpl() {
    }

    /**
     * RCWL 确认终轮报价结束
     * @param tenantId 租户
     * @param sourceHeaderId RFX 头 ID
     */
    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void endQuotation(Long tenantId, Long sourceHeaderId) {
        RoundHeader roundHeaderDb = this.roundHeaderRepository.selectOne(new RoundHeader(tenantId, sourceHeaderId, "RFX"));
        roundHeaderDb.setRoundHeaderStatus("CLOSED");
        this.roundHeaderRepository.updateOptional(roundHeaderDb, "roundHeaderStatus");
        this.roundHeaderDomainService.handleRfxQuotationEndDate(tenantId, sourceHeaderId, new Date());
        RfxHeader rfxHeader = this.rfxHeaderRepository.selectByPrimaryKey(new RfxHeader(tenantId, sourceHeaderId));
        if (!Constant.RfxStatus.CHECK_PENDING.equals(rfxHeader.getRfxStatus())) {
            this.evaluateScoreLineService.startAutoScore(rfxHeader.getRfxHeaderId(), rfxHeader.getTenantId());
        }

        /**
         * 新增逻辑
         * 多论报价重算评分
         */
        if (!Long.valueOf(1).equals(rfxHeader.getRoundNumber())) {
            // 获取专家评分汇总信息
            List<EvaluateSummary> evaluateSummarys =  this.rcwlRfxQuotationLineRepository.queryEvaluateSummary(new EvaluateSummary(rfxHeader.getTenantId(),rfxHeader.getRfxHeaderId(),"RFX",rfxHeader.getRoundNumber()));
            for (EvaluateSummary evaluateSummary: evaluateSummarys) {
                // 无效投标
                evaluateSummary.setBusinessScore(BigDecimal.ZERO);
                evaluateSummary.setScore(BigDecimal.ZERO);
            }
            LOGGER.info("BusinessScore : {} ", evaluateSummarys.get(0).getBusinessScore());
            // 重算商务分之前，清空原商务分，总分，排名
            this.evaluateSummaryRepository.batchUpdateOptional(evaluateSummarys,"businessScore","score","scoreRank");

            List<Long> invalidQuotationHeaderIdList = this.rcwlEvaluateScoreLineService.getInvalidQuotationHeaderIdList(rfxHeader);
            this.rcwlEvaluateScoreLineService.multipleRoundAutoEvaluateScore(new AutoScoreDTO(tenantId, "RFX", sourceHeaderId, invalidQuotationHeaderIdList));
        }
    }
}
