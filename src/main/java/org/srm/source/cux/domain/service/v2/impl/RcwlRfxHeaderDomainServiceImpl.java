package org.srm.source.cux.domain.service.v2.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.service.v2.impl.RfxHeaderDomainServiceImpl;
import org.srm.source.share.domain.entity.RoundHeaderDate;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.RoundHeaderDateRepository;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderDomainServiceImpl extends RfxHeaderDomainServiceImpl {

    @Autowired
    private RoundHeaderDateRepository roundHeaderDateRepository;

    @Override
    public void initHeaderDateByAutoRoundQuotation(RfxHeader rfxHeader, SourceTemplate sourceTemplate) {
        if (!ShareConstants.SourceTemplate.CategoryType.RFQ.equals(rfxHeader.getSourceCategory())
                &&!RcwlShareConstants.CategoryType.RCBJ.equals(rfxHeader.getSourceCategory())
                &&!RcwlShareConstants.CategoryType.RCZB.equals(rfxHeader.getSourceCategory())
                &&!RcwlShareConstants.CategoryType.RCZW.equals(rfxHeader.getSourceCategory())) {
            rfxHeader.setQuotationRounds(null);
            return;
        }
        if (StringUtils.isBlank(sourceTemplate.getRoundQuotationRule()) || !sourceTemplate.getRoundQuotationRule().contains(ShareConstants.RoundQuotationRule.AUTO)) {
            rfxHeader.setQuotationRounds(null);
            return;
        }
        List<RoundHeaderDate> roundHeaderDates = roundHeaderDateRepository.selectByCondition(Condition.builder(RoundHeaderDate.class)
                .where(Sqls.custom()
                        .andEqualTo(RoundHeaderDate.FIELD_SOURCE_HEADER_ID, rfxHeader.getRfxHeaderId())
                        .andEqualTo(RoundHeaderDate.FIELD_TENANT_ID, rfxHeader.getTenantId())
                        .andEqualTo(RoundHeaderDate.FIELD_ROUND_NUMBER, rfxHeader.getRoundNumber()))
                .orderBy(RoundHeaderDate.FIELD_QUOTATION_ROUND).build());
        if (CollectionUtils.isNotEmpty(roundHeaderDates)) {
            Long quotationRounds = rfxHeader.getQuotationRounds();
            List<RoundHeaderDate> delRoundHeaderDates = new ArrayList<>();
            Date quotationStartDate = new Date();
            Date quotationEndDate;
            for (RoundHeaderDate roundHeaderDate : roundHeaderDates) {
                if (quotationRounds < roundHeaderDate.getQuotationRound()) {
                    delRoundHeaderDates.add(roundHeaderDate);
                    continue;
                }
                if (BaseConstants.Flag.YES.equals(rfxHeader.getStartFlag())) {
                    long quotationRunningMills = roundHeaderDate.getRoundQuotationRunningDuration().multiply(new BigDecimal(1000 * 60)).longValue();
                    quotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
                    roundHeaderDate.setRoundQuotationStartDate(quotationStartDate);
                    roundHeaderDate.setRoundQuotationEndDate(quotationEndDate);
                    quotationStartDate = quotationEndDate;
                } else {
                    long quotationRunningMills = roundHeaderDate.getRoundQuotationEndDate().getTime() - roundHeaderDate.getRoundQuotationStartDate().getTime();
                    roundHeaderDate.setRoundQuotationRunningDuration( new BigDecimal(quotationRunningMills / 60000));
                }
            }
            roundHeaderDateRepository.batchUpdateByPrimaryKey(roundHeaderDates);
            if (CollectionUtils.isNotEmpty(delRoundHeaderDates)) {
                roundHeaderDateRepository.batchDeleteByPrimaryKey(delRoundHeaderDates);
            }
            // 第一轮的开始时间为询价单报价开始时间
            // 最后一轮的截止时间为询价单报价截止时间
            Date firstRoundQuotationStartDate = roundHeaderDates.get(0).getRoundQuotationStartDate();
            Date lastRoundQuotationEndDate = roundHeaderDates.get(rfxHeader.getQuotationRounds().intValue() - 1).getRoundQuotationEndDate();
            rfxHeader.setQuotationStartDate(firstRoundQuotationStartDate);
            rfxHeader.setQuotationEndDate(lastRoundQuotationEndDate);
            rfxHeader.setLatestQuotationEndDate(lastRoundQuotationEndDate);
            rfxHeader.setHandDownDate(lastRoundQuotationEndDate);
        }
    }
}
