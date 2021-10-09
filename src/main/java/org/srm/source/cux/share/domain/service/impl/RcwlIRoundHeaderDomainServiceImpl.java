package org.srm.source.cux.share.domain.service.impl;

import java.util.List;

import com.esotericsoftware.minlog.Log;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.source.share.domain.entity.RoundHeaderDate;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.RoundHeaderDateRepository;
import org.srm.source.share.domain.repository.RoundHeaderRepository;
import org.srm.source.share.domain.repository.SourceTemplateRepository;
import org.srm.source.share.domain.service.impl.IRoundHeaderDomainServiceImpl;
import org.srm.web.annotation.Tenant;

/**
 * @author: lmr
 * @date: 2021/10/9 10:20
 */
@Service
@Tenant("SRM-RCWL")
public class RcwlIRoundHeaderDomainServiceImpl extends IRoundHeaderDomainServiceImpl {
    @Autowired
    private RoundHeaderRepository roundHeaderRepository;
    @Autowired
    private SourceTemplateRepository sourceTemplateRepository;
    @Autowired
    private RoundHeaderDateRepository roundHeaderDateRepository;
    @Autowired
    private RfxHeaderService rfxHeaderService;

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void updateRoundQuotationEndDate(RfxHeader realRfxHeader, String roundQuotationRule) {
        if (roundQuotationRule != null && !"NONE".equals(roundQuotationRule) && !"AUTO".equals(roundQuotationRule)) {
            RoundHeader roundHeaderDb = (RoundHeader)this.roundHeaderRepository.selectOne(new RoundHeader(realRfxHeader.getTenantId(), realRfxHeader.getRfxHeaderId(), "RFX"));
            SourceTemplate sourceTemplate = (SourceTemplate)this.sourceTemplateRepository.selectByPrimaryKey(realRfxHeader.getTemplateId());
            if (null != realRfxHeader.getQuotationEndDate() || BaseConstants.Flag.YES.equals(sourceTemplate.getQuotationEndDateFlag())) {
                roundHeaderDb.setRoundQuotationEndDate(realRfxHeader.getQuotationEndDate());
                this.roundHeaderRepository.updateOptional(roundHeaderDb, new String[]{"roundQuotationEndDate"});
                if (realRfxHeader.getCurrentQuotationRound() == null || CollectionUtils.isEmpty(realRfxHeader.getRoundHeaderDates())) {
                    RoundHeaderDate roundHeaderDateParam = new RoundHeaderDate(realRfxHeader.getRfxHeaderId(), realRfxHeader.getTenantId(), realRfxHeader.getRoundNumber());
                    roundHeaderDateParam.setAutoFlag(BaseConstants.Flag.NO);
                    List<RoundHeaderDate> roundHeaderDates = this.roundHeaderDateRepository.selectRoundHeaderDatesOrderByRoundsAsc(roundHeaderDateParam);
                    if (CollectionUtils.isNotEmpty(roundHeaderDates)) {
                        RoundHeaderDate roundHeaderDate = (RoundHeaderDate)roundHeaderDates.get(roundHeaderDates.size() - 1);
                        roundHeaderDate.setRoundQuotationStartDate(realRfxHeader.getQuotationStartDate());
                        roundHeaderDate.setRoundQuotationEndDate(realRfxHeader.getQuotationEndDate());
                        roundHeaderDate.setRoundQuotationRunningDuration(this.rfxHeaderService.getCalculateRunningDuration(realRfxHeader.getQuotationStartDate(), realRfxHeader.getQuotationEndDate()));
                        this.roundHeaderDateRepository.updateByPrimaryKey(roundHeaderDate);
                    }
                }
            }

        }
    }


}
