package org.srm.source.cux.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.core.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.srm.boot.platform.message.MessageHelper;
import org.srm.boot.platform.message.entity.SpfmMessageSender;
import org.srm.source.cux.app.service.RcwlRoundHeaderService;
import org.srm.source.cux.domain.repository.RcwlRfxQuotationOtherRepository;
import org.srm.source.rfx.app.service.RfxQuotationHeaderService;
import org.srm.source.rfx.app.service.RfxQuotationLineService;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineSupplierRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
import org.srm.source.rfx.domain.service.IRfxActionDomainService;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.source.share.domain.entity.RoundHeaderDate;
import org.srm.source.share.domain.repository.RoundHeaderDateRepository;
import org.srm.source.share.domain.repository.RoundHeaderRepository;
import org.srm.source.share.domain.service.IRoundHeaderDomainService;
import org.srm.source.share.infra.utils.DateUtil;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/17 14:23
 * @version:1.0
 */
@Service
@Tenant("SRM-RCWL")
public class RcwlRoundHeaderServiceImpl implements RcwlRoundHeaderService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RoundHeaderRepository roundHeaderRepository;
    @Autowired
    private IRoundHeaderDomainService roundHeaderDomainService;
    @Autowired
    private MessageHelper messageHelper;
    @Autowired
    RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    IRfxActionDomainService rfxActionDomainService;
    @Autowired
    private SendMessageHandle sendMessageHandle;
    @Autowired
    private RoundHeaderDateRepository roundHeaderDateRepository;
    @Autowired
    private RfxLineSupplierRepository rfxLineSupplierRepository;
    @Autowired
    private CommonQueryRepository commonQueryRepository;
    @Autowired
    private RfxQuotationHeaderService rfxQuotationHeaderService;
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;
    @Autowired
    private RcwlRfxQuotationOtherRepository rcwlRfxQuotationOtherRepository;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startQuotation(Long tenantId, Long sourceHeaderId, Date roundQuotationEndDate, String startingReason, List<RfxQuotationHeader> rfxQuotationHeaderList) {

        ObjectMapper mapper = new ObjectMapper();
        for (RfxQuotationHeader e : rfxQuotationHeaderList
        ) {
            try {
                logger.info("-------------供应商列表：" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(e));
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        }
        //this.rfxQuotationHeaderRepository.batchUpdateOptional(rfxQuotationHeaderList, new String[]{"attributeVarchar2"});
        //修改为手动更新状态

        this.rcwlRfxQuotationOtherRepository.updateSuppierFlag(rfxQuotationHeaderList);
        logger.info("------------更新寻源--------多伦报价-------");
        RoundHeader roundHeaderDb = (RoundHeader) this.roundHeaderRepository.selectOne(new RoundHeader(tenantId, sourceHeaderId, "RFX"));
        Assert.isTrue(DateUtil.beforeNow(roundQuotationEndDate, (String) null), "error.round_quotation_end_date");
        Assert.isTrue(!DateUtil.beforeNow(roundHeaderDb.getRoundQuotationEndDate(), (String) null), "error.round_deadline_is_not_reached");
        roundHeaderDb.setQuotationRoundNumber(roundHeaderDb.getQuotationRoundNumber() + 1L);
        roundHeaderDb.setRoundQuotationEndDate(roundQuotationEndDate);
        roundHeaderDb.setStartingReason(startingReason);
        this.roundHeaderRepository.updateOptional(roundHeaderDb, new String[]{"quotationRoundNumber", "roundQuotationEndDate", "startingReason"});
        if ("RFX".equals(roundHeaderDb.getSourceFrom())) {
            this.roundHeaderDomainService.startRfxRoundQuotation(tenantId, sourceHeaderId, roundQuotationEndDate);
            RfxHeader rfxHeader = (RfxHeader) this.rfxHeaderRepository.selectByPrimaryKey(sourceHeaderId);
            this.rfxActionDomainService.insertAction(rfxHeader, "START_QUOTATION", startingReason);
            if (!"OFFLINE".equals(rfxHeader.getQuotationType())) {
                sendMessageForRoundQuotation(rfxHeader, DetailsHelper.getUserDetails(), rfxQuotationHeaderList);
            }

            if (!"NONE".equals(roundHeaderDb.getRoundQuotationRule()) || !"AUTO".equals(roundHeaderDb.getRoundQuotationRule())) {
                RoundHeaderDate roundHeaderDate = new RoundHeaderDate();
                roundHeaderDate.setRoundRemark(startingReason);
                roundHeaderDate.setAutoFlag(BaseConstants.Flag.NO);
                roundHeaderDate.setTenantId(rfxHeader.getTenantId());
                roundHeaderDate.setRoundQuotationStartDate(new Date());
                roundHeaderDate.setSourceHeaderId(rfxHeader.getRfxHeaderId());
                roundHeaderDate.setRoundNumber(rfxHeader.getRoundNumber());
                roundHeaderDate.setRoundQuotationEndDate(roundQuotationEndDate);
                roundHeaderDate.setSourceFrom("RFX");
                roundHeaderDate.setQuotationRound(roundHeaderDb.getQuotationRoundNumber());
                roundHeaderDate.setSummaryFlag(BaseConstants.Flag.NO);
                long quotationRunningMills = roundHeaderDate.getRoundQuotationEndDate().getTime() - roundHeaderDate.getRoundQuotationStartDate().getTime();
                roundHeaderDate.setRoundQuotationRunningDuration(new BigDecimal(quotationRunningMills / 60000L));
                this.roundHeaderDateRepository.insertSelective(roundHeaderDate);
            }
        } else {
            this.roundHeaderDomainService.startBidRoundQuotation(tenantId, sourceHeaderId, roundQuotationEndDate);
        }

    }

    @Async
    public void sendMessageForRoundQuotation(RfxHeader rfxHeader, CustomUserDetails userDetails, List<RfxQuotationHeader> rfxQuotationHeaderList) {
        try {
            DetailsHelper.setCustomUserDetails(userDetails);
            if (rfxHeader != null && rfxHeader.getRfxHeaderId() != null) {
                Map<String, String> map = new ConcurrentHashMap();
                rfxHeader.initSentMessagePara(map, this.format);
                String serverName = this.commonQueryRepository.selectServerName(rfxHeader.getTenantId());
                String linkString = getLinkString(DetailsHelper.getUserDetails());
                map.put("sourceAddress", "<a target='_blank' href='" + serverName + "/app/ssrc/inquiry-hall/rfx-detail/" + rfxHeader.getRfxHeaderId() + "'>" + linkString + "</a>");
                RfxLineSupplier temp = new RfxLineSupplier();
                temp.setRfxHeaderId(rfxHeader.getRfxHeaderId());
                temp.setTenantId(rfxHeader.getTenantId());
                temp.setFeedbackStatus("PARTICIPATED");
                List<RfxLineSupplier> rfxLineSupplierList = this.rfxLineSupplierRepository.select(temp);
                List<RfxLineSupplier> list = new ArrayList<>();
                for (RfxQuotationHeader e : rfxQuotationHeaderList
                ) {
                    if (!"1".equals(e.getAttributeVarchar2())) {
                        Optional<RfxLineSupplier> optional = rfxLineSupplierList.stream().filter(suppier -> suppier.getSupplierCompanyId().equals(e.getSupplierCompanyId())).findFirst();
                        if (optional.isPresent()) {
                            RfxLineSupplier rfxLineSupplier = optional.get();
                            list.add(rfxLineSupplier);
                            map.put("supplierTenantId", String.valueOf(rfxLineSupplier.getSupplierTenantId()));
                            map.put("supplierCompanyId", String.valueOf(rfxLineSupplier.getSupplierCompanyId()));
                            this.messageHelper.sendMessage(this.getSpfmMessageSender(rfxHeader.getTenantId(), "SSRC.RFX_ROUND_QUOTATION", map, DetailsHelper.getUserDetails()));
                        }
                    }
                }

                //校验供应商数量是否大于2
                if (null != list && list.size() > 1) {
                    for (RfxLineSupplier rfxLineSupplier : list
                    ) {
                        map.put("supplierTenantId", String.valueOf(rfxLineSupplier.getSupplierTenantId()));
                        map.put("supplierCompanyId", String.valueOf(rfxLineSupplier.getSupplierCompanyId()));
                        this.messageHelper.sendMessage(this.getSpfmMessageSender(rfxHeader.getTenantId(), "SSRC.RFX_ROUND_QUOTATION", map, DetailsHelper.getUserDetails()));
                    }
                } else {
                    throw new CommonException("供应商数量至少2个！");
                }

            }
        } finally {
            SecurityContextHolder.clearContext();
        }

    }

    public SpfmMessageSender getSpfmMessageSender(Long tenantId, String messageCode, Map<String, String> args, CustomUserDetails customUserDetails) {
        SpfmMessageSender spfmMessageSender = new SpfmMessageSender(tenantId, messageCode, args);
        spfmMessageSender.setLang(customUserDetails.getLanguage());
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("spfmMessageSender:{}", spfmMessageSender.toString());
        }

        return spfmMessageSender;
    }

    public static String getLinkString(CustomUserDetails userDetails) {
        String linkString;
        if ("en_US".equals(userDetails.getLanguage())) {
            linkString = "here";
        } else {
            linkString = "此处";
        }

        return linkString;
    }
}
