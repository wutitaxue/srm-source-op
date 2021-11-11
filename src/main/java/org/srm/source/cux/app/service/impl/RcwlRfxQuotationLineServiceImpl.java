package org.srm.source.cux.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.boot.event.service.sender.EventSender;
import org.srm.source.bid.infra.util.InetAddressUtil;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.rfx.api.dto.QuotationDetailQueryDTO;
import org.srm.source.rfx.api.dto.QuotationSubmitDTO;
import org.srm.source.rfx.api.dto.RfxSelectQuotationLineDTO;
import org.srm.source.rfx.app.service.RfxLineItemService;
import org.srm.source.rfx.app.service.RfxQuotationHeaderService;
import org.srm.source.rfx.app.service.RfxQuotationLineService;
import org.srm.source.rfx.app.service.RfxQuotationRecordService;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.app.service.impl.RfxQuotationLineServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationLineRepository;
import org.srm.source.rfx.domain.service.IRfxHeaderDomainService;
import org.srm.source.rfx.domain.service.IRfxQuotationDomainService;
import org.srm.source.rfx.domain.service.IRfxQuotationRecordDomainService;
import org.srm.source.share.app.service.RoundQuotationLineService;
import org.srm.source.share.app.service.SourceTemplateService;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.source.share.domain.entity.RoundHeaderDate;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.RoundHeaderDateRepository;
import org.srm.source.share.domain.repository.RoundHeaderRepository;
import org.srm.source.share.domain.service.IQuotationColumnDomainService;
import org.srm.source.share.domain.service.IRoundHeaderDomainService;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.source.share.infra.utils.ShareCommonUtil;
import org.srm.web.annotation.Tenant;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Tenant("SRM-RCWL")
public class RcwlRfxQuotationLineServiceImpl extends RfxQuotationLineServiceImpl {

    @Autowired
    private RfxQuotationLineRepository rfxQuotationLineRepository;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;
    @Autowired
    private IRfxQuotationDomainService iRfxQuotationDomainService;
    @Autowired
    private RfxQuotationRecordService rfxQuotationRecordService;
    @Autowired
    private RfxQuotationHeaderService rfxQuotationHeaderService;
    @Autowired
    private SourceTemplateService sourceTemplateService;
    @Autowired
    RfxLineItemRepository rfxLineItemRepository;
    @Autowired
    RfxLineItemService rfxLineItemService;
    @Autowired
    InetAddressUtil inetAddressUtil;
    @Autowired
    FileClient fileClient;
    @Autowired
    private IQuotationColumnDomainService iQuotationColumnDomainService;
    @Autowired
    private RoundHeaderDateRepository roundHeaderDateRepository;
    @Autowired
    private RoundHeaderRepository roundHeaderRepository;
    @Autowired
    private IRoundHeaderDomainService roundHeaderDomainService;
    @Autowired
    private EventSender eventSender;
    @Autowired
    private IRfxQuotationRecordDomainService iRfxQuotationRecordDomainService;
    @Autowired
    private SendMessageHandle sendMessageHandle;
    @Autowired
    private IRfxHeaderDomainService rfxHeaderDomainService;
    @Autowired
    private RoundQuotationLineService roundQuotationLineService;

    @Override
    public Page<RfxQuotationLine> quotationSubmit(Long tenantId, RfxSelectQuotationLineDTO rfxSelectQuotationLine) {
        Date date = new Date();
        if (CollectionUtils.isNotEmpty(rfxSelectQuotationLine.getRfxQuotationLines())) {
            rfxSelectQuotationLine.getRfxQuotationLines().forEach((rfxQuotationLine) -> {
                rfxQuotationLine.setAbandonedFlag(rfxQuotationLine.getAbandonedFlag() == null ? 0 : rfxQuotationLine.getAbandonedFlag());
            });
        }

        List<RfxQuotationLine> rfxQuotationLinesUpdate = rfxSelectQuotationLine.getRfxQuotationLines();
        List<Long> submitLineIds = rfxSelectQuotationLine.getSelectedRowKeys();
        if (CollectionUtils.isEmpty(rfxQuotationLinesUpdate) && CollectionUtils.isEmpty(submitLineIds)) {
            return null;
        } else {
            if (!Objects.isNull(rfxSelectQuotationLine.getRfxQuotationHeader())) {
                rfxSelectQuotationLine.getRfxQuotationHeader().setTenantId(tenantId);
                this.rfxQuotationHeaderRepository.updateOptional(rfxSelectQuotationLine.getRfxQuotationHeader(), new String[]{"paymentTypeId", "paymentTermId"});
            }

            if (CollectionUtils.isNotEmpty(rfxQuotationLinesUpdate)) {
                this.iRfxQuotationDomainService.supplierQuotationSave(tenantId, rfxQuotationLinesUpdate);
            }

            List<RfxQuotationLine> rfxQuotationLineList;
            if (BaseConstants.Flag.NO.equals(rfxSelectQuotationLine.getAllSubmitFlag())) {
                if (CollectionUtils.isEmpty(submitLineIds)) {
                    rfxQuotationLineList = this.rfxQuotationLineRepository.quotationLine(tenantId, rfxSelectQuotationLine.getQuotationHeaderId(), (List) rfxQuotationLinesUpdate.stream().map(RfxQuotationLine::getQuotationLineId).collect(Collectors.toList()));
                } else {
                    rfxQuotationLineList = this.rfxQuotationLineRepository.quotationLine(tenantId, rfxSelectQuotationLine.getQuotationHeaderId(), submitLineIds);
                }
            } else {
                rfxQuotationLineList = this.rfxQuotationLineRepository.quotationLine(tenantId, rfxSelectQuotationLine.getQuotationHeaderId(), (List) null);
            }

            RfxHeader rfxHeader = this.rfxHeaderRepository.byQuotationHeaderIdVerifyRFQValidTime((RfxQuotationLine) rfxQuotationLineList.get(0));
            RfxQuotationLine.checkInvalidItemList(rfxQuotationLineList);
            this.checkLadderPrice(rfxQuotationLineList);
            RfxQuotationLine quotationLineFirst = (RfxQuotationLine) rfxQuotationLineList.get(0);
            Long currentQuotationRound = quotationLineFirst.getCurrentQuotationRound();
            RfxQuotationHeader rfxQuotationHeader = this.rfxQuotationHeaderService.findByPrimaryKey(quotationLineFirst.getQuotationHeaderId());
            this.iQuotationColumnDomainService.validQuotationDetailForSubmit(new QuotationDetailQueryDTO(rfxQuotationHeader));
            SourceTemplate sourceTemplate = this.sourceTemplateService.findByPrimaryKey(quotationLineFirst.getTemplateId());
            String supplierIp = this.inetAddressUtil.getLocalHostIp();
            rfxQuotationHeader.setSupplierCompanyIp(supplierIp);
            this.rfxQuotationHeaderRepository.updateQuotationHeaderStatus(rfxQuotationHeader);
            rfxHeader.setCurrentDateTime(date);
            rfxHeader.setQuotationHeaderId(rfxQuotationHeader.getQuotationHeaderId());
            rfxHeader.setRoundQuotationRule(sourceTemplate.getRoundQuotationRule());
            rfxHeader.setRoundQuotationRankFlag(sourceTemplate.getRoundQuotationRankFlag());
            rfxHeader.setRoundQuotationRankRule(sourceTemplate.getRoundQuotationRankRule());
            Assert.notNull(rfxHeader, "error.no_rfx_header_Id");
            List<RfxLineItem> rfxLineItems = null;
            if (sourceTemplate.getSourceCategory().equals("RFA")) {
                rfxLineItems = this.iRfxQuotationDomainService.supplierRFA(rfxQuotationLineList, rfxHeader, sourceTemplate);
            }

            this.iRfxQuotationDomainService.supplierQuotation(rfxQuotationLineList, rfxHeader, sourceTemplate);
            ((RfxQuotationLineService) this.self()).checkQuotationDetailAmtControl(rfxQuotationHeader, rfxSelectQuotationLine.getWeakCtrlConfirmFlag());
            rfxHeader.initRankRule(sourceTemplate);
            rfxHeader.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
            if (StringUtils.isNotBlank(sourceTemplate.getRoundQuotationRule()) && sourceTemplate.getRoundQuotationRule().contains("AUTO")) {
                RoundHeaderDate currentRoundHeaderDate = this.roundHeaderDateRepository.selectCurrentRoundDate(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX", rfxHeader.getRoundNumber(), date);
                if (currentQuotationRound != null && currentQuotationRound < currentRoundHeaderDate.getQuotationRound()) {
                    throw new CommonException("当前自动多轮轮次发生变化,请重新进入页面", new Object[0]);
                }

                rfxHeader.setCurrentQuotationRound(currentRoundHeaderDate.getQuotationRound());
            } else if (!"NONE".equals(sourceTemplate.getRoundQuotationRule())) {
                RoundHeader roundHeaderDb = (RoundHeader) this.roundHeaderRepository.selectOne(new RoundHeader(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX"));
                rfxHeader.setCurrentQuotationRound(roundHeaderDb.getQuotationRoundNumber());
            }

            if (!ShareConstants.SourceTemplate.CategoryType.RFQ.equals(rfxHeader.getSourceCategory())
                    && !RcwlShareConstants.CategoryType.RCBJ.equals(rfxHeader.getSourceCategory())
                    && !RcwlShareConstants.CategoryType.RCZB.equals(rfxHeader.getSourceCategory())
                    && !RcwlShareConstants.CategoryType.RCZW.equals(rfxHeader.getSourceCategory())) {
                this.iRfxQuotationRecordDomainService.generateQuotationRecords(rfxHeader, rfxQuotationLineList);
                this.rfxQuotationRecordService.sendMessageByUpdateQuotations(rfxHeader.getTenantId(), rfxQuotationHeader.getQuotationHeaderId(), sourceTemplate, rfxQuotationLineList, rfxLineItems);
            } else {
                this.iRfxQuotationRecordDomainService.generateQuotationRecords(rfxHeader, rfxQuotationLineList);
            }

            if (null != rfxHeader.getCurrentQuotationRound()) {
                this.roundHeaderDomainService.initSupplierRoundRankInfo(rfxQuotationLineList, rfxHeader);
            }

            List<List<RfxQuotationLine>> lists = ShareCommonUtil.splistList(rfxQuotationLineList, 1000);
            Iterator var15 = lists.iterator();

            while (var15.hasNext()) {
                List<RfxQuotationLine> list = (List) var15.next();
                this.eventSender.fireEvent("SSRC_QUOTATION_SUBMIT", "RFQ_QUOTATION_SUBMIT", BaseConstants.DEFAULT_TENANT_ID, "quotation submit", new QuotationSubmitDTO(list, rfxHeader));
            }
            // 当提交新的报价,实时刷新询价监控台
            rfxHeaderDomainService.refreshSupervisor(rfxQuotationLineList, rfxHeader);
            //生成多轮报价记录
            roundQuotationLineService.createByRfxQuotationLine(rfxQuotationLineList, rfxHeader);
            // 当询价单配置自动多轮并开启多轮时，设置报价排名
            rfxHeaderDomainService.updateSupplierRoundRank(rfxQuotationLineList, rfxHeader);
            // 2021-01-27 多轮报价优化
            roundHeaderDomainService.initSupplierRoundRankInfo(rfxQuotationLineList, rfxHeader);
            //消息发送
            sendMessageHandle.sendMessageForSubmit(rfxQuotationHeader, rfxHeader);
            RfxQuotationLine rfxQuotationLineParam = new RfxQuotationLine();
            rfxQuotationLineParam.setTenantId(tenantId);
            rfxQuotationLineParam.setQuotationHeaderId(quotationLineFirst.getQuotationHeaderId());
            rfxQuotationLineParam.setQuotationLineIds(rfxQuotationLineList.stream().map(RfxQuotationLine::getQuotationLineId).collect(Collectors.toList()));
            Page<RfxQuotationLine> rfxQuotationLines = rfxQuotationLineRepository.pageQuotationLine(new PageRequest(0, rfxQuotationLineList.size()), rfxQuotationLineParam);
            rfxQuotationLines.forEach(rfxQuotationLine -> rfxQuotationLine.setIsFlag(BaseConstants.Flag.YES.toString()));
            return null;
        }
    }
}
