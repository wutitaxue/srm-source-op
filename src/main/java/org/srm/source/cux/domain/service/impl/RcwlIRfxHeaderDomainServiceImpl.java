package org.srm.source.cux.domain.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.repository.RfxMemberRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationLineRepository;
import org.srm.source.rfx.domain.service.impl.IRfxHeaderDomainServiceImpl;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Tenant("SRM-RCWL")
public class RcwlIRfxHeaderDomainServiceImpl extends IRfxHeaderDomainServiceImpl {

    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;

    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;

    @Autowired
    private RfxQuotationLineRepository rfxQuotationLineRepository;
    @Autowired
    RfxMemberRepository rfxMemberRepository;

    @Override
    public void initItemStartAndEndDate(RfxHeader header, List<RfxLineItem> itemList) {
        Long rfxHeaderId = header.getRfxHeaderId();
        if (BaseConstants.Flag.YES.equals(header.getStartFlag()) && (SourceConstants.RfxStatus.NEW.equals(header.getRfxStatus())
                || SourceConstants.RfxStatus.RELEASE_APPROVING.equals(header.getRfxStatus()))) {
            //物料行报价开始和结束时间置为空，为了避免以后构造函数的参数也是三个，第一个为Long,当第二第三参数为null时无法区分对应哪一个构造函数,所以定义两个空变量
            Date quotationStartDate = null;
            Date quotationEndDate = null;
            //批量更新物料行，因为并行更新的开始时间和结束时间所有行一样，所以直接根据询价头id更新
            CustomizeHelper.ignore(()-> rfxLineItemRepository.batchUpdateAllItemDateByHeaderId(new RfxLineItem(rfxHeaderId, quotationStartDate, quotationEndDate)));
            return;
        }
        Date quotationStartDate = BaseConstants.Flag.YES.equals(header.getStartFlag()) ? new Date() : header.getQuotationStartDate();
        Date quotationEndDate = header.getQuotationEndDate();
        // 行开始时间与结束时间计算
        if (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(header.getSourceCategory())) {
            BigDecimal startQuotationRunningDuration = header.getStartQuotationRunningDuration();
            //判空
            if(quotationStartDate == null){
                return;
            }
            header.setQuotationStartDate(quotationStartDate);
            //只有寻源发布时才计算报价截止时间
            if (Objects.nonNull(startQuotationRunningDuration) && !(SourceConstants.RfxStatus.NEW.equals(header.getRfxStatus())
                    || SourceConstants.RfxStatus.RELEASE_APPROVING.equals(header.getRfxStatus()))) {
                long quotationRunningMills = startQuotationRunningDuration.multiply(new BigDecimal(1000 * 60)).longValue();
                quotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
                header.setQuotationEndDate(quotationEndDate);
            }
            RfxLineItem rfxLineItem = new RfxLineItem(rfxHeaderId, quotationStartDate, quotationEndDate);
            //批量更新物料行，因为并行更新的开始时间和结束时间所有行一样，所以直接根据询价头id更新
            CustomizeHelper.ignore(()-> rfxLineItemRepository.batchUpdateAllItemDateByHeaderId(rfxLineItem));
            return;
        }
        if (ShareConstants.SourceTemplate.CategoryType.RFA.equals(header.getSourceCategory())) {
            // 初始化报价运行时间和报价间隔时间
            BigDecimal quotationRunningDuration = header.getQuotationRunningDuration();
            //判空
            if(quotationStartDate == null || quotationRunningDuration == null){
                return;
            }
            BigDecimal quotationInterval = header.getQuotationInterval() == null ? new BigDecimal(0) : header.getQuotationInterval();
            long quotationRunningMills = quotationRunningDuration.multiply(new BigDecimal(1000 * 60)).longValue();
            long quotationIntervalMills = quotationInterval.multiply(new BigDecimal(1000 * 60)).longValue();
            Date newQuotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
            List<RfxLineItem> sortedItemList = itemList.stream().sorted(Comparator.comparing(RfxLineItem::getRfxLineItemNum)).collect(toList());
            // 并行
            // 开始时间为询价单开始时间
            // 结束时间为报价开始时间+运行时间
            if (SourceConstants.QuotationOrderType.PARALLEL.equals(header.getQuotationOrderType())) {
                sortedItemList.forEach(item -> {
                    item.setQuotationStartDate(quotationStartDate);
                    item.setQuotationEndDate(newQuotationEndDate);
                });
            }
            // 交错
            // 开始时间为询价单开始时间
            // 结束时间为行开始时间+运行时间+（n-1）间隔时间
            if (SourceConstants.QuotationOrderType.STAGGER.equals(header.getQuotationOrderType())) {
                for (int i = 0; i < sortedItemList.size(); i++) {
                    sortedItemList.get(i).setQuotationStartDate(quotationStartDate);
                    sortedItemList.get(i).setQuotationEndDate(new Date(quotationStartDate.getTime() + quotationRunningMills + (i * quotationIntervalMills)));
                }
            }
            // 序列
            // 开始时间为上一行物料的结束时间+间隔时间
            // 结束时间为本行开始时间+运行时间
            if (SourceConstants.QuotationOrderType.SEQUENCE.equals(header.getQuotationOrderType())) {
                for (int i = 0; i < sortedItemList.size(); i++) {
                    if (i == 0) {
                        sortedItemList.get(i).setQuotationStartDate(quotationStartDate);
                        sortedItemList.get(i).setQuotationEndDate(newQuotationEndDate);
                    } else {
                        long startDateMills = sortedItemList.get(i - 1).getQuotationEndDate().getTime() + quotationIntervalMills;
                        long endDateMills = startDateMills + quotationRunningMills;
                        sortedItemList.get(i).setQuotationStartDate(new Date(startDateMills));
                        sortedItemList.get(i).setQuotationEndDate(new Date(endDateMills));
                    }
                }
            }
            // 竞价时询价单的截止时间为物料最后一行的截止时间
            if (null != sortedItemList.get(sortedItemList.size() - 1)) {
                header.setQuotationStartDate(quotationStartDate);
                header.setQuotationEndDate(sortedItemList.get(sortedItemList.size() - 1).getQuotationEndDate());
            }
            RfxLineItem rfxLineItem = new RfxLineItem(rfxHeaderId, quotationStartDate, quotationEndDate);
            //批量更新物料行
            if(SourceConstants.QuotationOrderType.PARALLEL.equals(header.getQuotationOrderType())){
                //因为并行更新的开始时间和结束时间所有行一样，所以直接根据询价头id更新
                CustomizeHelper.ignore(()-> rfxLineItemRepository.batchUpdateAllItemDateByHeaderId(rfxLineItem));
            }else{
                CustomizeHelper.ignore(()-> rfxLineItemRepository.batchUpdateItemDate(itemList));
            }
        }
        header.setHandDownDate(header.getQuotationEndDate());
        header.setLatestQuotationEndDate(header.getQuotationEndDate());
    }

    @Override
    public void initItemStartAndEndDateTimeAdjusted(RfxHeader header, List<RfxLineItem> itemList) {
        Date quotationStartDate = header.getQuotationStartDate();
        Date quotationEndDate = header.getQuotationEndDate();
        //运行时间
        BigDecimal tempQuotationRunningDuration = header.getQuotationRunningDuration();
        //间隔时间
        BigDecimal tempQuotationInterval = header.getQuotationInterval();
        header = rfxHeaderRepository.selectByPrimaryKey(header.getRfxHeaderId());

        // 行开始时间与结束时间计算
        if (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(header.getSourceCategory())) {

            for (RfxLineItem item : itemList) {
                item.setQuotationStartDate(quotationStartDate);
                item.setQuotationEndDate(quotationEndDate);
            }
            if(null != quotationEndDate) {
                BigDecimal startQuotationRunningDuration;
                long quotaionRunningMills = quotationEndDate.getTime() - quotationStartDate.getTime();
                startQuotationRunningDuration = new BigDecimal(quotaionRunningMills / 60000);
                header.setStartQuotationRunningDuration(startQuotationRunningDuration);
            }
            RfxHeader finalHeader = header;
            CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(finalHeader,
                    RfxHeader.FIELD_START_QUOTATION_RUNNING_DURATION
            ));
        }
        //设置运行时间和间隔时间，因为如果修改了运行时间和间隔时间他用的还是原来的，并没有保存修改后的
        header.setQuotationRunningDuration(tempQuotationRunningDuration);
        header.setQuotationInterval(tempQuotationInterval);
        if (ShareConstants.SourceTemplate.CategoryType.RFA.equals(header.getSourceCategory()) && null != header.getQuotationRunningDuration() && Objects.nonNull(header.getQuotationStartDate())){
            // 初始化报价运行时间和报价间隔时间
            BigDecimal quotationRunningDuration = header.getQuotationRunningDuration();
            BigDecimal quotationInterval = header.getQuotationInterval() == null ? new BigDecimal(0) : header.getQuotationInterval();
            long quotationRunningMills = quotationRunningDuration.multiply(new BigDecimal(1000 * 60)).longValue();
            long quotationIntervalMills = quotationInterval.multiply(new BigDecimal(1000 * 60)).longValue();
            Date newQuotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
            List<RfxLineItem> sortedItemList = itemList.stream().sorted(Comparator.comparing(RfxLineItem::getRfxLineItemNum)).collect(toList());
            // 并行
            // 开始时间为询价单开始时间
            // 结束时间为报价开始时间+运行时间
            if (SourceConstants.QuotationOrderType.PARALLEL.equals(header.getQuotationOrderType())) {
                sortedItemList.forEach(item -> {
                    item.setQuotationStartDate(quotationStartDate);
                    item.setQuotationEndDate(newQuotationEndDate);
                });
            }
            // 交错
            // 开始时间为询价单开始时间
            // 结束时间为行开始时间+运行时间+（n-1）间隔时间
            if (SourceConstants.QuotationOrderType.STAGGER.equals(header.getQuotationOrderType())) {
                for (int i = 0; i < sortedItemList.size(); i++) {
                    sortedItemList.get(i).setQuotationStartDate(quotationStartDate);
                    sortedItemList.get(i).setQuotationEndDate(new Date(quotationStartDate.getTime() + quotationRunningMills + (i * quotationIntervalMills)));
                }
            }
            // 序列
            // 开始时间为上一行物料的结束时间+间隔时间
            // 结束时间为本行开始时间+运行时间
            if (SourceConstants.QuotationOrderType.SEQUENCE.equals(header.getQuotationOrderType())) {
                for (int i = 0; i < sortedItemList.size(); i++) {
                    if (i == 0) {
                        sortedItemList.get(i).setQuotationStartDate(quotationStartDate);
                        sortedItemList.get(i).setQuotationEndDate(newQuotationEndDate);
                    } else {
                        long startDateMills = sortedItemList.get(i - 1).getQuotationEndDate().getTime() + quotationIntervalMills;
                        long endDateMills = startDateMills + quotationRunningMills;
                        sortedItemList.get(i).setQuotationStartDate(new Date(startDateMills));
                        sortedItemList.get(i).setQuotationEndDate(new Date(endDateMills));
                    }
                }
            }
            // 竞价时询价单的截止时间为物料最后一行的截止时间
            if (null != sortedItemList.get(sortedItemList.size() - 1)) {
                header.setQuotationEndDate(sortedItemList.get(sortedItemList.size() - 1).getQuotationEndDate());
                header.setHandDownDate(header.getQuotationEndDate());
                header.setLatestQuotationEndDate(header.getQuotationEndDate());
                RfxHeader finalHeader1 = header;
                CustomizeHelper.ignore(() -> this.rfxHeaderRepository.updateOptional(finalHeader1, RfxHeader.FIELD_QUOTATION_END_DATE, RfxHeader.FIELD_LATEST_QUOTATION_END_DATE, RfxHeader.FIELD_HAND_DOWN_DATE));
            }
        } else if(ShareConstants.SourceTemplate.CategoryType.RFA.equals(header.getSourceCategory()) && null == header.getQuotationRunningDuration()) {
            itemList.forEach(rfxLineItem -> rfxLineItem.setQuotationStartDate(quotationStartDate));
        }
        CustomizeHelper.ignore(() -> this.rfxLineItemRepository.batchUpdateOptional(itemList, RfxLineItem.FIELD_QUOTATION_START_DATE, RfxLineItem.FIELD_QUOTATION_END_DATE));
    }

    @Override
    public void updateSupplierRoundRank(List<RfxQuotationLine> rfxQuotationLineList, RfxHeader rfxHeader) {
        if ((!rfxHeader.getSourceCategory().equals(ShareConstants.SourceTemplate.CategoryType.RFQ)
                && !RcwlShareConstants.CategoryType.RCBJ.equals(rfxHeader.getSourceCategory())
                && !RcwlShareConstants.CategoryType.RCZB.equals(rfxHeader.getSourceCategory())
                && !RcwlShareConstants.CategoryType.RCZW.equals(rfxHeader.getSourceCategory()))
                || CollectionUtils.isEmpty(rfxQuotationLineList)
                || !rfxHeader.getRoundQuotationRule().contains(ShareConstants.RoundQuotationRule.AUTO)
                || !BaseConstants.Flag.YES.equals(rfxHeader.getRoundQuotationRankFlag())) {
            return;
        }
        // 获取询价单下所有已报价的报价行
        List<Long> rfxItemIds = rfxQuotationLineList.stream().map(RfxQuotationLine::getRfxLineItemId).collect(toList());
        List<RfxQuotationLine> rfxQuotationLines = rfxQuotationLineRepository.selectAllQuotationByItemIds(rfxHeader.getRfxHeaderId(), rfxItemIds);
        List<RfxQuotationLine> effectiveList = rfxQuotationLines.stream().filter(rfxQuotationLine ->
                SourceConstants.RfxQuotationLineStatus.SUBMITTED.equals(rfxQuotationLine.getQuotationLineStatus())
                        || SourceConstants.RfxQuotationLineStatus.BARGAINED.equals(rfxQuotationLine.getQuotationLineStatus())
                        || SourceConstants.RfxQuotationLineStatus.FINISHED.equals(rfxQuotationLine.getQuotationLineStatus())
                        || SourceConstants.RfxQuotationLineStatus.ROUND_QUOTATION.equals(rfxQuotationLine.getQuotationLineStatus())).collect(toList());
        for (RfxQuotationLine rfxQuotationLine : rfxQuotationLines) {
            if (!SourceConstants.RfxQuotationLineStatus.SUBMITTED.equals(rfxQuotationLine.getQuotationLineStatus())
                    && !SourceConstants.RfxQuotationLineStatus.BARGAINED.equals(rfxQuotationLine.getQuotationLineStatus())
                    && !SourceConstants.RfxQuotationLineStatus.FINISHED.equals(rfxQuotationLine.getQuotationLineStatus())
                    && !SourceConstants.RfxQuotationLineStatus.ROUND_QUOTATION.equals(rfxQuotationLine.getQuotationLineStatus())) {
                rfxQuotationLine.setAutoRoundRank(null);
                continue;
            }
            // 按含税
            if (ShareConstants.RoundQuotationRankRule.TAX_PRICE.equals(rfxHeader.getRoundQuotationRankRule())) {
                long autoRoundRank = effectiveList.stream().filter(roundQuotationLineParam -> rfxQuotationLine.getRfxLineItemId().equals(roundQuotationLineParam.getRfxLineItemId()))
                        .filter(roundQuotationLineParam -> {
                            if (ShareConstants.SourceTemplate.AuctionDirection.REVERSE.equals(rfxHeader.getAuctionDirection())) {
                                return rfxQuotationLine.getValidQuotationPrice().compareTo(roundQuotationLineParam.getValidQuotationPrice()) > 0;
                            } else {
                                return rfxQuotationLine.getValidQuotationPrice().compareTo(roundQuotationLineParam.getValidQuotationPrice()) < 0;
                            }
                        }).count();
                rfxQuotationLine.setAutoRoundRank(autoRoundRank + 1);
            }
            // 按未税
            if (ShareConstants.RoundQuotationRankRule.UNIT_PRICE.equals(rfxHeader.getRoundQuotationRankRule())) {
                long autoRoundRank = effectiveList.stream().filter(roundQuotationLineParam -> rfxQuotationLine.getRfxLineItemId().equals(roundQuotationLineParam.getRfxLineItemId()))
                        .filter(roundQuotationLineParam -> {
                            if (ShareConstants.SourceTemplate.AuctionDirection.REVERSE.equals(rfxHeader.getAuctionDirection())) {
                                return rfxQuotationLine.getValidNetPrice().compareTo(roundQuotationLineParam.getValidNetPrice()) > 0;
                            } else {
                                return rfxQuotationLine.getValidNetPrice().compareTo(roundQuotationLineParam.getValidNetPrice()) < 0;
                            }
                        }).count();
                rfxQuotationLine.setAutoRoundRank(autoRoundRank + 1);
            }
        }
    }
}
