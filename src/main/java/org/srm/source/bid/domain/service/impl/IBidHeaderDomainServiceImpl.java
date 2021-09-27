//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.srm.source.bid.domain.service.impl;

import com.alibaba.fastjson.JSON;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseConstants.Flag;
import org.hzero.message.domain.repository.MessageRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.srm.boot.adaptor.client.AdaptorMappingHelper;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.boot.pr.app.service.PrManageDomainService;
import org.srm.boot.pr.domain.vo.PrChangeResultVO;
import org.srm.boot.pr.domain.vo.PrChangeVO;
import org.srm.source.bid.api.dto.BidMemberDTO;
import org.srm.source.bid.api.dto.BidQuotationHeaderDTO;
import org.srm.source.bid.app.service.BidHeaderService;
import org.srm.source.bid.app.service.common.BidSendMessageHandle;
import org.srm.source.bid.domain.entity.BidHeader;
import org.srm.source.bid.domain.entity.BidLineItem;
import org.srm.source.bid.domain.entity.BidLineSupplier;
import org.srm.source.bid.domain.repository.BidHeaderRepository;
import org.srm.source.bid.domain.repository.BidLineItemRepository;
import org.srm.source.bid.domain.repository.BidLineSupplierRepository;
import org.srm.source.bid.domain.service.IBidHeaderDomainService;
import org.srm.source.bid.domain.service.IBidLineItemDomainService;
import org.srm.source.bid.domain.service.IBidLineSupplierDomainService;
import org.srm.source.bid.infra.constant.MessageCode;
import org.srm.source.cux.bid.api.dto.RfxHeaderInfoDTO;
import org.srm.source.cux.bid.infra.mapper.RcwlIBidHeaderMapper;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.share.api.dto.PrHeader;
import org.srm.source.share.api.dto.PrLine;
import org.srm.source.share.api.dto.PrLineDTO;
import org.srm.source.share.api.dto.PrRelationDTO;
import org.srm.source.share.app.service.PrManageService;
import org.srm.source.share.domain.entity.ProjectLineItemDist;
import org.srm.source.share.domain.repository.MessageMarkRepository;
import org.srm.source.share.domain.repository.ProjectLineItemDistRepository;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.share.infra.convertor.CommonConvertor;
import org.srm.source.share.infra.feign.SpucRemoteService;
import org.srm.source.share.infra.utils.DateUtil;
import org.srm.source.share.infra.utils.PasswordCreate;

@Component
public class IBidHeaderDomainServiceImpl implements IBidHeaderDomainService {
    @Autowired
    private BidHeaderRepository bidHeaderRepository;
    @Autowired
    private BidLineItemRepository bidLineItemRepository;
    @Autowired
    private BidLineSupplierRepository bidLineSupplierRepository;
    @Autowired
    private IBidLineSupplierDomainService bidLineSupplierDomainService;
    @Autowired
    private IBidLineItemDomainService bidLineItemDomainService;
    @Autowired
    private SpucRemoteService spucRemoteService;
    @Autowired
    @Lazy
    private BidSendMessageHandle bidSendMessageHandle;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PrManageDomainService prManageDomainService;
    @Autowired
    private MessageMarkRepository messageMarkRepository;
    @Autowired
    private BidHeaderService bidHeaderService;
    @Autowired
    private PrManageService prManageService;
    @Autowired
    private ProjectLineItemDistRepository projectLineItemDistRepository;
    @Autowired
    private RcwlIBidHeaderMapper rcwlIBidHeaderMapper;
    @Autowired
    private MessageClient messageClient;
    @Autowired
    private CommonQueryRepository commonQueryRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(IBidHeaderDomainServiceImpl.class);

    public IBidHeaderDomainServiceImpl() {
    }

    public void revertHeaderToCreate(BidHeader bidHeader) {
        bidHeader.initAgainInquiryStataus();
        this.bidHeaderRepository.updateByPrimaryKeySelective(bidHeader);
    }

    public void revertItemToCreate(Long rfxHeaderId, BidHeader bidHeader) {
        List<BidLineItem> itemList = this.bidLineItemRepository.selectByCondition(Condition.builder(BidLineItem.class).andWhere(Sqls.custom().andEqualTo("bidHeaderId", bidHeader)).build());
        this.bidLineItemDomainService.initItemForInquiryAgain(itemList, bidHeader.getRoundNumber());
        this.bidLineItemRepository.batchUpdateByPrimaryKeySelective(itemList);
    }

    public void revertSupplierToCreate(Long rfxHeaderId) {
        List<BidLineSupplier> supplierList = this.bidLineSupplierRepository.selectByCondition(Condition.builder(BidLineSupplier.class).andWhere(Sqls.custom().andEqualTo("bidHeaderId", rfxHeaderId)).build());
        this.bidLineSupplierDomainService.initSupplierForInquiryAgain(supplierList);
        this.bidLineSupplierRepository.batchUpdateByPrimaryKey(supplierList);
    }

    public List<BidLineItem> createBidLineItem(BidHeader bidHeader, Long organizationId, List<PrLineVO> prLineList) {
        LongAdder longAdder = new LongAdder();
        longAdder.add(0L);
        List<BidLineItem> bidLineItems = new ArrayList();
        Iterator var6 = prLineList.iterator();

        while(var6.hasNext()) {
            PrLineVO prLineVO = (PrLineVO)var6.next();
            longAdder.increment();
            BidLineItem bidLineItem = (BidLineItem)CommonConvertor.beanConvert(BidLineItem.class, prLineVO);
            bidLineItem.setBidHeaderId(bidHeader.getBidHeaderId());
            bidLineItem.setTenantId(organizationId);
            bidLineItem.setBidQuantity(prLineVO.getOccupiedQuantity());
            bidLineItem.setBidLineItemNum(longAdder.longValue());
            bidLineItem.setItemCategoryId(prLineVO.getCategoryId());
            bidLineItem.setDemandDate(prLineVO.getNeededDate());
            bidLineItem.setPrLineNum(Integer.parseInt(prLineVO.getDisplayLineNum()));
            bidLineItem.setPrNum(prLineVO.getDisplayPrNum());
            bidLineItem.setTaxIncludedFlag(bidLineItem.getTaxId() != null ? Flag.YES : null);
            bidLineItem.setExecutedBy(DetailsHelper.getUserDetails().getUserId());
            bidLineItem.setExecutedDate(new Date());
            bidLineItem.setExecutionStatusCode("FINISHED");
            bidLineItem.setExecutionBillId(bidHeader.getBidHeaderId());
            bidLineItem.setExecutionBillNum("SOURCE");
            bidLineItem.setExecutionBillData(bidHeader.getBidNum());
            bidLineItem.setFreightIncludedFlag(bidHeader.getFreightIncludedFlag());
            bidLineItems.add(bidLineItem);
        }

        if ("PACK".equals(bidHeader.getSubjectMatterRule())) {
            BidLineItem bidSectionLine = new BidLineItem();
            bidSectionLine.setBidHeaderId(bidHeader.getBidHeaderId());
            bidSectionLine.setSectionNum("01");
            bidSectionLine.setSectionName("标段1");
            bidSectionLine.setBidLineItemNum(1L);
            bidSectionLine.setTenantId(organizationId);
            bidSectionLine.setSectionFlag(1);
            this.bidLineItemRepository.insertSelective(bidSectionLine);
            bidLineItems.forEach((bidLineItemx) -> {
                bidLineItemx.setParentSectionId(bidSectionLine.getBidLineItemId());
                bidLineItemx.setParentSectionNum(bidSectionLine.getBidLineItemNum());
            });
        }

        this.bidLineItemRepository.batchInsertSelective(bidLineItems);
        return bidLineItems;
    }

    public BidHeader createBidHeaderAndLineItem(BidHeader bidHeader, Long organizationId, List<PrLineVO> prLineList) {
        LongAdder longAdder = new LongAdder();
        longAdder.add(0L);
        List<BidLineItem> bidLineItems = new ArrayList();
        Iterator var6 = prLineList.iterator();

        while(var6.hasNext()) {
            PrLineVO prLineVO = (PrLineVO)var6.next();
            longAdder.increment();
            BidLineItem bidLineItem = (BidLineItem)CommonConvertor.beanConvert(BidLineItem.class, prLineVO);
            bidLineItem.setBidHeaderId(bidHeader.getBidHeaderId());
            bidLineItem.setTenantId(organizationId);
            bidLineItem.setBidQuantity(prLineVO.getOccupiedQuantity());
            bidLineItem.setBidLineItemNum(longAdder.longValue());
            bidLineItem.setItemCategoryId(prLineVO.getCategoryId());
            bidLineItem.setDemandDate(prLineVO.getNeededDate());
            bidLineItem.setPrLineNum(Integer.parseInt(prLineVO.getDisplayLineNum()));
            bidLineItem.setPrNum(prLineVO.getDisplayPrNum());
            bidLineItem.setTaxIncludedFlag(bidLineItem.getTaxId() != null ? Flag.YES : null);
            bidLineItem.setExecutedBy(DetailsHelper.getUserDetails().getUserId());
            bidLineItem.setExecutedDate(new Date());
            bidLineItem.setExecutionStatusCode("FINISHED");
            bidLineItem.setExecutionBillId(bidHeader.getBidHeaderId());
            bidLineItem.setExecutionBillNum("SOURCE");
            bidLineItem.setExecutionBillData(bidHeader.getBidNum());
            bidLineItems.add(bidLineItem);
        }

        List<BidHeader> results = this.fieldMap(organizationId, bidHeader, bidLineItems);
        List<BidLineItem> resultLines = new ArrayList();
        if (CollectionUtils.isNotEmpty(results)) {
            bidHeader = (BidHeader)results.get(0);
            results.forEach((r) -> {
                resultLines.add(r.getLine());
            });
        }

        if (CollectionUtils.isNotEmpty(resultLines)) {
            bidLineItems = resultLines;
        }

        this.bidHeaderService.saveOrUpdateBidHeader(bidHeader);
        Long bidHeaderId = bidHeader.getBidHeaderId();
        bidLineItems.forEach((line) -> {
            line.setBidHeaderId(bidHeaderId);
        });
        if ("PACK".equals(bidHeader.getSubjectMatterRule())) {
            BidLineItem bidSectionLine = new BidLineItem();
            bidSectionLine.setBidHeaderId(bidHeader.getBidHeaderId());
            bidSectionLine.setSectionNum("01");
            bidSectionLine.setSectionName("标段1");
            bidSectionLine.setBidLineItemNum(1L);
            bidSectionLine.setTenantId(organizationId);
            bidSectionLine.setSectionFlag(1);
            this.bidLineItemRepository.insertSelective(bidSectionLine);
            bidLineItems.forEach((bidLineItemx) -> {
                bidLineItemx.setParentSectionId(bidSectionLine.getBidLineItemId());
                bidLineItemx.setParentSectionNum(bidSectionLine.getBidLineItemNum());
            });
        }

        this.bidLineItemRepository.batchInsertSelective(bidLineItems);
        this.batchHoldPrLine(bidHeader, bidLineItems);
        return bidHeader;
    }

    private List<BidHeader> fieldMap(Long tenantId, BidHeader bidHeader, List<BidLineItem> bidLineItems) {
        Set<Long> lineIds = (Set)bidLineItems.stream().map((line) -> {
            return line.getPrLineId();
        }).collect(Collectors.toSet());
        List<PrHeader> prHeaders = (List)this.spucRemoteService.listPrByLineIds(tenantId, new PrLineDTO(lineIds)).getBody();
        List<BidHeader> results = new ArrayList();
        if (CollectionUtils.isEmpty(prHeaders)) {
            return results;
        } else {
            Iterator var7 = prHeaders.iterator();

            label37:
            while(true) {
                PrHeader prHeader;
                List prHeaderLines;
                do {
                    if (!var7.hasNext()) {
                        return results;
                    }

                    prHeader = (PrHeader)var7.next();
                    prHeaderLines = prHeader.getPrLineList();
                } while(CollectionUtils.isEmpty(prHeaderLines));

                int j = 0;
                List<PrLine> prLines = new ArrayList();
                int batch = 10;
                int size = prHeaderLines.size();
                Iterator var14 = prHeaderLines.iterator();

                while(true) {
                    do {
                        if (!var14.hasNext()) {
                            continue label37;
                        }

                        PrLine prLine = (PrLine)var14.next();
                        prLines.add(prLine);
                        ++j;
                    } while(j % batch != 0 && j != size);

                    LOGGER.debug("prLines size: " + prLines.size());
                    LOGGER.debug("prLines: " + JSON.toJSONString(prLines));
                    List<BidHeader> batchResults = this.batchTranslateData(tenantId, bidHeader, bidLineItems, prHeader, prLines);
                    results.addAll(batchResults);
                    prLines = new ArrayList();
                }
            }
        }
    }

    private List<BidHeader> batchTranslateData(Long tenantId, BidHeader bidHeader, List<BidLineItem> bidLineItems, PrHeader prHeader, List<PrLine> prLines) {
        List<BidHeader> results = new ArrayList();
        List<Long> prLineIds = (List)prLines.stream().map(PrLine::getPrLineId).collect(Collectors.toList());
        List<BidLineItem> bidHeaderLines = (List)bidLineItems.stream().filter((line) -> {
            return prLineIds.contains(line.getPrLineId());
        }).collect(Collectors.toList());
        Map<String, String> param = new HashMap(8);
        param.put("prTypeName", String.valueOf(prHeader.getPrTypeId()));
        param.put("company", String.valueOf(prHeader.getCompanyId()));
        param.put("purchaseOrganization", String.valueOf(prHeader.getPurchaseOrgId()));
        String fieldMapTemplate = (String)CnfHelper.select(tenantId, "SITE.SSRC.BID_FIELD_MAP_TEMPLATE", String.class).invokeWithParameter(param);
        LOGGER.debug("fieldMapTemplate " + fieldMapTemplate);
        if (StringUtils.isBlank(fieldMapTemplate)) {
            bidHeaderLines.forEach((line) -> {
                BidHeader header = new BidHeader();
                BeanUtils.copyProperties(bidHeader, header);
                header.setLine(line);
                results.add(header);
            });
            return results;
        } else {
            List<Object> request = new ArrayList();
            prLines.sort(Comparator.comparing(PrLine::getPrLineId));
            prLines.forEach((line) -> {
                PrHeader prRequest = new PrHeader();
                BeanUtils.copyProperties(prHeader, prRequest);
                prRequest.setPrLineList((List)null);
                prRequest.setPrLine(line);
                request.add(prRequest);
            });
            List<Object> response = new ArrayList();
            bidHeaderLines.sort(Comparator.comparing(BidLineItem::getPrLineId));
            Iterator var13 = bidHeaderLines.iterator();

            while(var13.hasNext()) {
                BidLineItem bidHeaderLine = (BidLineItem)var13.next();
                BidHeader header = new BidHeader();
                BeanUtils.copyProperties(bidHeader, header);
                header.setLine(bidHeaderLine);
                response.add(header);
            }

            List<Object> fieldMapResults = AdaptorMappingHelper.batchTranslateData(tenantId, "SSRC_SCENE_CODE_PR_TO_BID", fieldMapTemplate, request, response);
            LOGGER.debug("fieldMapResults " + fieldMapResults);
            fieldMapResults.forEach((r) -> {
                results.add((BidHeader)r);
            });
            return results;
        }
    }

    public void batchPurchaseExecutionInLine(List<BidLineItem> bidLineItems) {
        if (!CollectionUtils.isEmpty(bidLineItems)) {
            List<PrLineDTO> paramList = new ArrayList();
            bidLineItems.stream().filter((bidLineItem) -> {
                return null != bidLineItem.getPrNum();
            }).forEach((bidLineItem) -> {
                paramList.add(CommonConvertor.beanConvert(PrLineDTO.class, bidLineItem));
            });
            if (CollectionUtils.isNotEmpty(paramList)) {
                this.spucRemoteService.batchPurchaseExecutionInLine(((PrLineDTO)paramList.get(0)).getTenantId(), paramList);
            }

        }
    }

    public void batchHoldPrLine(BidHeader bidHeader, List<BidLineItem> bidLineItems) {
        List<PrChangeVO> prChangeVOS = bidHeader.initPrChangeVOS(bidLineItems);
        List<PrRelationDTO> prRelationDTOS = bidHeader.initPrRelationDTOS(bidLineItems);
        if (CollectionUtils.isNotEmpty(prChangeVOS) && CollectionUtils.isNotEmpty(prRelationDTOS)) {
            PrChangeResultVO prChangeResultVO = this.prManageDomainService.holdPr(prChangeVOS);
            this.prManageService.holdPrBid(bidHeader.getTenantId(), prRelationDTOS);
            Assert.isTrue(prChangeResultVO.isSuccess(), prChangeResultVO.getResultMsg());
        }

    }

    public void batchCancelLines(Long tenantId, List<BidLineItem> bidLineItems) {
        if (!CollectionUtils.isEmpty(bidLineItems)) {
            List<PrLine> prLineList = new ArrayList();
            bidLineItems.forEach((item) -> {
                if (item.getPrNum() != null) {
                    prLineList.add(new PrLine(item.getPrHeaderId(), item.getPrLineId(), Flag.NO, Flag.YES, Flag.NO));
                }

            });
            List<PrRelationDTO> prRelationDTOS = this.initPrRelationDTOS(bidLineItems);
            if (CollectionUtils.isNotEmpty(bidLineItems) && CollectionUtils.isNotEmpty(prRelationDTOS)) {
                this.spucRemoteService.batchCancelLines(tenantId, prLineList);
                this.prManageService.changeQuantityBid(tenantId, prRelationDTOS);
            }

        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void batchReleasePrLines(BidHeader bidHeader, List<BidLineItem> bidLineItems) {
        if (!CollectionUtils.isEmpty(bidLineItems)) {
            List<PrChangeVO> prChangeVOS = new ArrayList();
            Iterator var4 = bidLineItems.iterator();

            while(var4.hasNext()) {
                BidLineItem bidLineItem = (BidLineItem)var4.next();
                if (null != bidLineItem.getPrHeaderId()) {
                    if (null == bidLineItem.getProjectLineItemId()) {
                        if (null != bidLineItem.getPrLineId()) {
                            prChangeVOS.add(new PrChangeVO(bidLineItem.getPrHeaderId(), bidLineItem.getPrLineId(), "SOURCE_BID", bidLineItem.getBidHeaderId(), bidLineItem.getBidLineItemId(), String.valueOf(bidLineItem.getBidLineItemNum()), bidHeader.getBidNum(), (String)null, DetailsHelper.getUserDetails().getUserId()));
                        }
                    } else if (null != bidLineItem.getPrLineId()) {
                        ProjectLineItemDist projectLineItemDist = (ProjectLineItemDist)this.projectLineItemDistRepository.selectOne(new ProjectLineItemDist(bidLineItem.getTenantId(), bidLineItem.getBidLineItemId(), bidLineItem.getProjectLineItemId(), "BID"));
                        BigDecimal prUpdateExecuteQuantity = bidLineItem.getBidQuantity().subtract(projectLineItemDist.getQuantity());
                        if (prUpdateExecuteQuantity.compareTo(BigDecimal.ZERO) > 0) {
                            prChangeVOS.add(new PrChangeVO(bidLineItem.getPrHeaderId(), bidLineItem.getPrLineId(), "SOURCE_BID", bidLineItem.getBidHeaderId(), bidLineItem.getBidLineItemId(), String.valueOf(bidLineItem.getBidLineItemNum()), bidHeader.getBidNum(), (String)null, DetailsHelper.getUserDetails().getUserId()));
                        }
                    }
                }
            }

            List<PrRelationDTO> prRelationDTOS = bidHeader.initPrRelationDTOS(bidLineItems);
            if (CollectionUtils.isNotEmpty(prChangeVOS)) {
                PrChangeResultVO prChangeResultVO = this.prManageDomainService.releasePr(prChangeVOS);
                Assert.isTrue(prChangeResultVO.isSuccess(), prChangeResultVO.getResultMsg());
            }

            if (CollectionUtils.isNotEmpty(prRelationDTOS)) {
                this.prManageService.releasePrBid(bidHeader.getTenantId(), prRelationDTOS);
            }

        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void sendOpenPasswordMailToOpener() {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Date tenMinuteBefore = DateUtils.truncate(new Date(System.currentTimeMillis() + 1200000L), 13);
        List<RfxHeaderInfoDTO> rfxHeaderInfoDTOList = new ArrayList<>();
        rfxHeaderInfoDTOList = rcwlIBidHeaderMapper.selectRfxInfo(tenMinuteBefore);
        rfxHeaderInfoDTOList.forEach((rfxHeaderInfoDTO) -> {
            sendMessageToOpener(rfxHeaderInfoDTO, userDetails);
        });
    }


    public void sendMessageToOpener(RfxHeaderInfoDTO rfxHeaderInfoDTO, CustomUserDetails userDetails) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new ConcurrentHashMap(16);
        map.put("BID_NUMBER", rfxHeaderInfoDTO.getRfxNum());
        map.put("BID_OPEN_DATE", format.format(rfxHeaderInfoDTO.getQuotationEndDate()));
        map.put("BID_TITLE", rfxHeaderInfoDTO.getRfxTitle());
        map.put("currentTime", format.format(new Date()));
        map.put("bidHeaderId", String.valueOf(rfxHeaderInfoDTO.getRfxHeaderId()));
        String serverName = "http://" + this.commonQueryRepository.selectServerName(rfxHeaderInfoDTO.getTenantId());
        map.put("sourceAddress", "<a target='_blank' href='" + serverName + "/app/ssrc/new-inquiry-hall/rfx-detail/"+rfxHeaderInfoDTO.getRfxHeaderId()+"?current=newInquiryHall" + "'>" + SendMessageHandle.getLinkString(userDetails) + "</a>");

        List<Receiver> receivers = new ArrayList<>();
        List<RfxHeaderInfoDTO> openerList = new ArrayList<>();
        openerList = rcwlIBidHeaderMapper.selectRfxOpeners(rfxHeaderInfoDTO.getRfxHeaderId(),rfxHeaderInfoDTO.getTenantId());

        Receiver receiver = new Receiver();
        openerList.forEach(x->{
            receiver.setEmail(x.getEmail());
            receiver.setPhone(x.getPhone());
            receiver.setTargetUserTenantId(0L);
            receivers.add(receiver);
        });

        if (BaseConstants.Flag.YES.equals(rfxHeaderInfoDTO.getPasswordFlag())) {
            String bidPassword;
            for(bidPassword = PasswordCreate.createPassword(); !Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)(?![a-z]+$)(?![!@#$%^&*=]+$)[0-9A-Za-z!@#$%^&*=]{6,30}$", bidPassword); bidPassword = PasswordCreate.createPassword()) {
            }
            map.put("OPEN_PASSWORD", bidPassword);
            messageClient.sendMessage(0L, MessageCode.BID_OPEN_PWD.getMessageCode(), receivers, map);
        } else {
            messageClient.sendMessage(0L,  MessageCode.BID_OPEN_NONE_PWD.getMessageCode(), receivers, map);
        }

        rcwlIBidHeaderMapper.updateSendFlag(rfxHeaderInfoDTO.getTenantId(), rfxHeaderInfoDTO.getRfxHeaderId());
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void quoEndTimeSentMessage(Integer sendMethodFlag) {
        Date date;
        if (Flag.YES.equals(sendMethodFlag)) {
            date = DateUtil.addDay(new Date(System.currentTimeMillis()), 1);
        } else {
            date = DateUtil.addHourOrMin(new Date(System.currentTimeMillis()), 1, 10);
        }

        List<BidQuotationHeaderDTO> bidQuotationHeaderDTOS = this.bidHeaderRepository.selectQuotationEndTimeComingSuppliers(date, sendMethodFlag);
        this.sendMessageToSuppliers(bidQuotationHeaderDTOS, sendMethodFlag);
        List<BidHeader> bidHeaders = this.bidHeaderRepository.selectQuotationEndTimeComingBid(date, sendMethodFlag);
        this.sendMessageToBidCreatedBy(bidHeaders, sendMethodFlag);
    }

    private void sendMessageToBidCreatedBy(List<BidHeader> bidHeaders, Integer sendMethodFlag) {
        if (!CollectionUtils.isEmpty(bidHeaders)) {
            bidHeaders.forEach((bidHeader) -> {
                this.bidSendMessageHandle.sendMessageToBidCreatedBy(bidHeader, sendMethodFlag);
            });
        }
    }

    private void sendMessageToSuppliers(List<BidQuotationHeaderDTO> bidQuotationHeaderDTOS, Integer sendMethodFlag) {
        if (!CollectionUtils.isEmpty(bidQuotationHeaderDTOS)) {
            bidQuotationHeaderDTOS.forEach((bidQuotationHeaderDTO) -> {
                this.bidSendMessageHandle.sendMessageToSuppler(bidQuotationHeaderDTO, sendMethodFlag);
            });
        }
    }

    private List<PrRelationDTO> initPrRelationDTOS(List<BidLineItem> bidLineItems) {
        if (CollectionUtils.isEmpty(bidLineItems)) {
            return null;
        } else {
            List<PrRelationDTO> prRelationDTOS = new ArrayList();
            bidLineItems.forEach((bidLineItem) -> {
                if (null != bidLineItem.getPrHeaderId()) {
                    prRelationDTOS.add(new PrRelationDTO(bidLineItem.getTenantId(), bidLineItem.getPrHeaderId(), bidLineItem.getPrLineId(), "SOURCE_BID", Flag.NO, bidLineItem.getBidHeaderId(), bidLineItem.getBidLineItemId(), (String)null, String.valueOf(bidLineItem.getBidLineItemNum()), bidLineItem.getBidQuantity()));
                }

            });
            return prRelationDTOS;
        }
    }
}
