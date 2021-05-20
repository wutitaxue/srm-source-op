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
import java.util.*;
import java.util.stream.Collectors;

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
        Date quotationStartDate;
        Date quotationEndDate;
        if (!BaseConstants.Flag.YES.equals(header.getStartFlag()) || !"NEW".equals(header.getRfxStatus()) && !"RELEASE_APPROVING".equals(header.getRfxStatus())) {
            quotationStartDate = BaseConstants.Flag.YES.equals(header.getStartFlag()) ? new Date() : header.getQuotationStartDate();
            quotationEndDate = header.getQuotationEndDate();
            BigDecimal quotationRunningDuration;
            if (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(header.getSourceCategory())
                    ||RcwlShareConstants.CategoryType.RCBJ.equals(header.getSourceCategory())
                    ||RcwlShareConstants.CategoryType.RCZB.equals(header.getSourceCategory())
                    ||RcwlShareConstants.CategoryType.RCZW.equals(header.getSourceCategory())) {
                quotationRunningDuration = header.getStartQuotationRunningDuration();
                if (quotationStartDate != null) {
                    header.setQuotationStartDate(quotationStartDate);
                    if (Objects.nonNull(quotationRunningDuration) && !"NEW".equals(header.getRfxStatus()) && !"RELEASE_APPROVING".equals(header.getRfxStatus())) {
                        long quotationRunningMills = quotationRunningDuration.multiply(new BigDecimal(60000)).longValue();
                        quotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
                        header.setQuotationEndDate(quotationEndDate);
                    }

                    RfxLineItem rfxLineItem = new RfxLineItem(rfxHeaderId, quotationStartDate, quotationEndDate);
                    CustomizeHelper.ignore(() -> {
                        return this.rfxLineItemRepository.batchUpdateAllItemDateByHeaderId(rfxLineItem);
                    });
                }
            } else {
                if ("RFA".equals(header.getSourceCategory())) {
                    quotationRunningDuration = header.getQuotationRunningDuration();
                    if (quotationStartDate == null || quotationRunningDuration == null) {
                        return;
                    }

                    BigDecimal quotationInterval = header.getQuotationInterval() == null ? new BigDecimal(0) : header.getQuotationInterval();
                    long quotationRunningMills = quotationRunningDuration.multiply(new BigDecimal(60000)).longValue();
                    long quotationIntervalMills = quotationInterval.multiply(new BigDecimal(60000)).longValue();
                    Date newQuotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
                    List<RfxLineItem> sortedItemList = (List)itemList.stream().sorted(Comparator.comparing(RfxLineItem::getRfxLineItemNum)).collect(Collectors.toList());
                    if ("PARALLEL".equals(header.getQuotationOrderType())) {
                        sortedItemList.forEach((item) -> {
                            item.setQuotationStartDate(quotationStartDate);
                            item.setQuotationEndDate(newQuotationEndDate);
                        });
                    }

                    int i;
                    if ("STAGGER".equals(header.getQuotationOrderType())) {
                        for(i = 0; i < sortedItemList.size(); ++i) {
                            ((RfxLineItem)sortedItemList.get(i)).setQuotationStartDate(quotationStartDate);
                            ((RfxLineItem)sortedItemList.get(i)).setQuotationEndDate(new Date(quotationStartDate.getTime() + quotationRunningMills + (long)i * quotationIntervalMills));
                        }
                    }

                    if ("SEQUENCE".equals(header.getQuotationOrderType())) {
                        for(i = 0; i < sortedItemList.size(); ++i) {
                            if (i == 0) {
                                ((RfxLineItem)sortedItemList.get(i)).setQuotationStartDate(quotationStartDate);
                                ((RfxLineItem)sortedItemList.get(i)).setQuotationEndDate(newQuotationEndDate);
                            } else {
                                long startDateMills = ((RfxLineItem)sortedItemList.get(i - 1)).getQuotationEndDate().getTime() + quotationIntervalMills;
                                long endDateMills = startDateMills + quotationRunningMills;
                                ((RfxLineItem)sortedItemList.get(i)).setQuotationStartDate(new Date(startDateMills));
                                ((RfxLineItem)sortedItemList.get(i)).setQuotationEndDate(new Date(endDateMills));
                            }
                        }
                    }

                    if (null != sortedItemList.get(sortedItemList.size() - 1)) {
                        header.setQuotationStartDate(quotationStartDate);
                        header.setQuotationEndDate(((RfxLineItem)sortedItemList.get(sortedItemList.size() - 1)).getQuotationEndDate());
                    }

                    RfxLineItem rfxLineItem = new RfxLineItem(rfxHeaderId, quotationStartDate, quotationEndDate);
                    if ("PARALLEL".equals(header.getQuotationOrderType())) {
                        CustomizeHelper.ignore(() -> {
                            return this.rfxLineItemRepository.batchUpdateAllItemDateByHeaderId(rfxLineItem);
                        });
                    } else {
                        CustomizeHelper.ignore(() -> {
                            return this.rfxLineItemRepository.batchUpdateItemDate(itemList);
                        });
                    }
                }

                header.setHandDownDate(header.getQuotationEndDate());
                header.setLatestQuotationEndDate(header.getQuotationEndDate());
            }
        } else {
            quotationStartDate = null;
            quotationEndDate = null;
            Date finalQuotationEndDate = quotationEndDate;
            CustomizeHelper.ignore(() -> {
                return this.rfxLineItemRepository.batchUpdateAllItemDateByHeaderId(new RfxLineItem(rfxHeaderId, quotationStartDate, finalQuotationEndDate));
            });
        }
    }

    @Override
    public void initItemStartAndEndDateTimeAdjusted(RfxHeader header, List<RfxLineItem> itemList) {
        Date quotationStartDate = header.getQuotationStartDate();
        Date quotationEndDate = header.getQuotationEndDate();
        BigDecimal tempQuotationRunningDuration = header.getQuotationRunningDuration();
        BigDecimal tempQuotationInterval = header.getQuotationInterval();
        header = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(header.getRfxHeaderId());
        BigDecimal startQuotationRunningDuration;
        if (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCBJ.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(header.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(header.getSourceCategory())) {
            Iterator var7 = itemList.iterator();

            while(var7.hasNext()) {
                RfxLineItem item = (RfxLineItem)var7.next();
                item.setQuotationStartDate(quotationStartDate);
                item.setQuotationEndDate(quotationEndDate);
            }

            if (null != quotationEndDate) {
                long quotaionRunningMills = quotationEndDate.getTime() - quotationStartDate.getTime();
                startQuotationRunningDuration = new BigDecimal(quotaionRunningMills / 60000L);
                header.setStartQuotationRunningDuration(startQuotationRunningDuration);
            }

            RfxHeader finalHeader = header;
            CustomizeHelper.ignore(() -> {
                return this.rfxHeaderRepository.updateOptional(finalHeader, new String[]{"startQuotationRunningDuration"});
            });
        }

        header.setQuotationRunningDuration(tempQuotationRunningDuration);
        header.setQuotationInterval(tempQuotationInterval);
        if ("RFA".equals(header.getSourceCategory()) && null != header.getQuotationRunningDuration() && Objects.nonNull(header.getQuotationStartDate())) {
            startQuotationRunningDuration = header.getQuotationRunningDuration();
            BigDecimal quotationInterval = header.getQuotationInterval() == null ? new BigDecimal(0) : header.getQuotationInterval();
            long quotationRunningMills = startQuotationRunningDuration.multiply(new BigDecimal(60000)).longValue();
            long quotationIntervalMills = quotationInterval.multiply(new BigDecimal(60000)).longValue();
            Date newQuotationEndDate = new Date(quotationStartDate.getTime() + quotationRunningMills);
            List<RfxLineItem> sortedItemList = (List)itemList.stream().sorted(Comparator.comparing(RfxLineItem::getRfxLineItemNum)).collect(Collectors.toList());
            if ("PARALLEL".equals(header.getQuotationOrderType())) {
                sortedItemList.forEach((itemx) -> {
                    itemx.setQuotationStartDate(quotationStartDate);
                    itemx.setQuotationEndDate(newQuotationEndDate);
                });
            }

            int i;
            if ("STAGGER".equals(header.getQuotationOrderType())) {
                for(i = 0; i < sortedItemList.size(); ++i) {
                    ((RfxLineItem)sortedItemList.get(i)).setQuotationStartDate(quotationStartDate);
                    ((RfxLineItem)sortedItemList.get(i)).setQuotationEndDate(new Date(quotationStartDate.getTime() + quotationRunningMills + (long)i * quotationIntervalMills));
                }
            }

            if ("SEQUENCE".equals(header.getQuotationOrderType())) {
                for(i = 0; i < sortedItemList.size(); ++i) {
                    if (i == 0) {
                        ((RfxLineItem)sortedItemList.get(i)).setQuotationStartDate(quotationStartDate);
                        ((RfxLineItem)sortedItemList.get(i)).setQuotationEndDate(newQuotationEndDate);
                    } else {
                        long startDateMills = ((RfxLineItem)sortedItemList.get(i - 1)).getQuotationEndDate().getTime() + quotationIntervalMills;
                        long endDateMills = startDateMills + quotationRunningMills;
                        ((RfxLineItem)sortedItemList.get(i)).setQuotationStartDate(new Date(startDateMills));
                        ((RfxLineItem)sortedItemList.get(i)).setQuotationEndDate(new Date(endDateMills));
                    }
                }
            }

            if (null != sortedItemList.get(sortedItemList.size() - 1)) {
                header.setQuotationEndDate(((RfxLineItem)sortedItemList.get(sortedItemList.size() - 1)).getQuotationEndDate());
                header.setHandDownDate(header.getQuotationEndDate());
                header.setLatestQuotationEndDate(header.getQuotationEndDate());
                RfxHeader finalHeader1 = header;
                CustomizeHelper.ignore(() -> {
                    return this.rfxHeaderRepository.updateOptional(finalHeader1, new String[]{"quotationEndDate", "latestQuotationEndDate", "handDownDate"});
                });
            }
        } else if ("RFA".equals(header.getSourceCategory()) && null == header.getQuotationRunningDuration()) {
            itemList.forEach((rfxLineItem) -> {
                rfxLineItem.setQuotationStartDate(quotationStartDate);
            });
        }

        CustomizeHelper.ignore(() -> {
            return this.rfxLineItemRepository.batchUpdateOptional(itemList, new String[]{"quotationStartDate", "quotationEndDate"});
        });
    }

    @Override
    public void updateSupplierRoundRank(List<RfxQuotationLine> rfxQuotationLineList, RfxHeader rfxHeader) {
        if ((!rfxHeader.getSourceCategory().equals(ShareConstants.SourceTemplate.CategoryType.RFQ)
                && !RcwlShareConstants.CategoryType.RCBJ.equals(rfxHeader.getSourceCategory())
                && !RcwlShareConstants.CategoryType.RCZB.equals(rfxHeader.getSourceCategory())
                && !RcwlShareConstants.CategoryType.RCZW.equals(rfxHeader.getSourceCategory())) && !CollectionUtils.isEmpty(rfxQuotationLineList) && rfxHeader.getRoundQuotationRule().contains("AUTO") && BaseConstants.Flag.YES.equals(rfxHeader.getRoundQuotationRankFlag())) {
            List<Long> rfxItemIds = (List)rfxQuotationLineList.stream().map(RfxQuotationLine::getRfxLineItemId).collect(Collectors.toList());
            List<RfxQuotationLine> rfxQuotationLines = this.rfxQuotationLineRepository.selectAllQuotationByItemIds(rfxHeader.getRfxHeaderId(), rfxItemIds);
            List<RfxQuotationLine> effectiveList = (List)rfxQuotationLines.stream().filter((rfxQuotationLinex) -> {
                return "SUBMITTED".equals(rfxQuotationLinex.getQuotationLineStatus()) || "BARGAINED".equals(rfxQuotationLinex.getQuotationLineStatus()) || "FINISHED".equals(rfxQuotationLinex.getQuotationLineStatus()) || "ROUND_QUOTATION".equals(rfxQuotationLinex.getQuotationLineStatus());
            }).collect(Collectors.toList());
            Iterator var6 = rfxQuotationLines.iterator();

            while(true) {
                while(var6.hasNext()) {
                    RfxQuotationLine rfxQuotationLine = (RfxQuotationLine)var6.next();
                    if (!"SUBMITTED".equals(rfxQuotationLine.getQuotationLineStatus()) && !"BARGAINED".equals(rfxQuotationLine.getQuotationLineStatus()) && !"FINISHED".equals(rfxQuotationLine.getQuotationLineStatus()) && !"ROUND_QUOTATION".equals(rfxQuotationLine.getQuotationLineStatus())) {
                        rfxQuotationLine.setAutoRoundRank((Long)null);
                    } else {
                        long autoRoundRank;
                        if ("TAX_PRICE".equals(rfxHeader.getRoundQuotationRankRule())) {
                            autoRoundRank = effectiveList.stream().filter((roundQuotationLineParam) -> {
                                return rfxQuotationLine.getRfxLineItemId().equals(roundQuotationLineParam.getRfxLineItemId());
                            }).filter((roundQuotationLineParam) -> {
                                if ("REVERSE".equals(rfxHeader.getAuctionDirection())) {
                                    return rfxQuotationLine.getValidQuotationPrice().compareTo(roundQuotationLineParam.getValidQuotationPrice()) > 0;
                                } else {
                                    return rfxQuotationLine.getValidQuotationPrice().compareTo(roundQuotationLineParam.getValidQuotationPrice()) < 0;
                                }
                            }).count();
                            rfxQuotationLine.setAutoRoundRank(autoRoundRank + 1L);
                        }

                        if ("UNIT_PRICE".equals(rfxHeader.getRoundQuotationRankRule())) {
                            autoRoundRank = effectiveList.stream().filter((roundQuotationLineParam) -> {
                                return rfxQuotationLine.getRfxLineItemId().equals(roundQuotationLineParam.getRfxLineItemId());
                            }).filter((roundQuotationLineParam) -> {
                                if ("REVERSE".equals(rfxHeader.getAuctionDirection())) {
                                    return rfxQuotationLine.getValidNetPrice().compareTo(roundQuotationLineParam.getValidNetPrice()) > 0;
                                } else {
                                    return rfxQuotationLine.getValidNetPrice().compareTo(roundQuotationLineParam.getValidNetPrice()) < 0;
                                }
                            }).count();
                            rfxQuotationLine.setAutoRoundRank(autoRoundRank + 1L);
                        }
                    }
                }

                return;
            }
        }
    }
}
