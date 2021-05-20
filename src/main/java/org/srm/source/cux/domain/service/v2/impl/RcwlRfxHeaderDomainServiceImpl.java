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
import java.util.Iterator;
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
            rfxHeader.setQuotationRounds((Long)null);
        } else if (!StringUtils.isBlank(sourceTemplate.getRoundQuotationRule()) && sourceTemplate.getRoundQuotationRule().contains("AUTO")) {
            List<RoundHeaderDate> roundHeaderDates = this.roundHeaderDateRepository.selectByCondition(Condition.builder(RoundHeaderDate.class).where(Sqls.custom().andEqualTo("sourceHeaderId", rfxHeader.getRfxHeaderId()).andEqualTo("tenantId", rfxHeader.getTenantId()).andEqualTo("roundNumber", rfxHeader.getRoundNumber())).orderBy(new String[]{"quotationRound"}).build());
            if (CollectionUtils.isNotEmpty(roundHeaderDates)) {
                Long quotationRounds = rfxHeader.getQuotationRounds();
                List<RoundHeaderDate> delRoundHeaderDates = new ArrayList();
                Date quotationStartDate = new Date();
                Iterator var8 = roundHeaderDates.iterator();

                while(var8.hasNext()) {
                    RoundHeaderDate roundHeaderDate = (RoundHeaderDate)var8.next();
                    if (quotationRounds < roundHeaderDate.getQuotationRound()) {
                        delRoundHeaderDates.add(roundHeaderDate);
                    } else {
                        long quotationRunningMills;
                        if (BaseConstants.Flag.YES.equals(rfxHeader.getStartFlag())) {
                            quotationRunningMills = roundHeaderDate.getRoundQuotationRunningDuration().multiply(new BigDecimal(60000)).longValue();
                            Date quotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
                            roundHeaderDate.setRoundQuotationStartDate(quotationStartDate);
                            roundHeaderDate.setRoundQuotationEndDate(quotationEndDate);
                            quotationStartDate = quotationEndDate;
                        } else {
                            quotationRunningMills = roundHeaderDate.getRoundQuotationEndDate().getTime() - roundHeaderDate.getRoundQuotationStartDate().getTime();
                            roundHeaderDate.setRoundQuotationRunningDuration(new BigDecimal(quotationRunningMills / 60000L));
                        }
                    }
                }

                this.roundHeaderDateRepository.batchUpdateByPrimaryKey(roundHeaderDates);
                if (CollectionUtils.isNotEmpty(delRoundHeaderDates)) {
                    this.roundHeaderDateRepository.batchDeleteByPrimaryKey(delRoundHeaderDates);
                }

                Date firstRoundQuotationStartDate = ((RoundHeaderDate)roundHeaderDates.get(0)).getRoundQuotationStartDate();
                Date lastRoundQuotationEndDate = ((RoundHeaderDate)roundHeaderDates.get(rfxHeader.getQuotationRounds().intValue() - 1)).getRoundQuotationEndDate();
                rfxHeader.setQuotationStartDate(firstRoundQuotationStartDate);
                rfxHeader.setQuotationEndDate(lastRoundQuotationEndDate);
                rfxHeader.setLatestQuotationEndDate(lastRoundQuotationEndDate);
                rfxHeader.setHandDownDate(lastRoundQuotationEndDate);
            }

        } else {
            rfxHeader.setQuotationRounds((Long)null);
        }
    }
}
