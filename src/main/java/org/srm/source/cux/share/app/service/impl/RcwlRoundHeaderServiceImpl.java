package org.srm.source.cux.share.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.cux.share.infra.constant.Constant;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.repository.RfxLineSupplierRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationLineRepository;
import org.srm.source.rfx.domain.service.IRfxActionDomainService;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.app.service.EvaluateScoreLineService;
import org.srm.source.share.app.service.impl.RoundHeaderServiceImpl;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.source.share.domain.repository.ProjectLineSectionRepository;
import org.srm.source.share.domain.repository.RoundHeaderDateRepository;
import org.srm.source.share.domain.repository.RoundHeaderRepository;
import org.srm.source.share.domain.repository.RoundQuotationLineRepository;
import org.srm.source.share.domain.service.IRoundHeaderDomainService;
import org.srm.source.share.infra.mapper.RoundHeaderMapper;
import org.srm.web.annotation.Tenant;

import java.util.Date;
import java.util.List;

/**
 * @author kaibo.li
 * @date 2021-05-27 11:13
 */
@Service
@Tenant(Constant.TENANT_NUM)
public class RcwlRoundHeaderServiceImpl extends RoundHeaderServiceImpl {
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

    /**
     * 新写的
     */
    @Autowired
    private IRcwlEvaluateScoreLineService rcwlEvaluateScoreLineService;

    public RcwlRoundHeaderServiceImpl() {
    }

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
            List<Long> invalidQuotationHeaderIdList = this.rcwlEvaluateScoreLineService.getInvalidQuotationHeaderIdList(rfxHeader);
            this.evaluateScoreLineService._autoEvaluateScore(new AutoScoreDTO(tenantId, "RFX", sourceHeaderId, invalidQuotationHeaderIdList));
        }
    }
}
