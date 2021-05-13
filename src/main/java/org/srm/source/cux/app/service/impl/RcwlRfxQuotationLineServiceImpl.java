package org.srm.source.cux.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
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
import org.srm.source.rfx.app.service.RfxQuotationRecordService;
import org.srm.source.rfx.app.service.impl.RfxQuotationLineServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationLineRepository;
import org.srm.source.rfx.domain.service.IRfxQuotationDomainService;
import org.srm.source.rfx.domain.service.IRfxQuotationRecordDomainService;
import org.srm.source.rfx.infra.constant.SourceConstants;
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



    @Override
    public Page<RfxQuotationLine> quotationSubmit(Long tenantId, RfxSelectQuotationLineDTO rfxSelectQuotationLine) {
        Date date = new Date();
        if (CollectionUtils.isNotEmpty(rfxSelectQuotationLine.getRfxQuotationLines())) {
            rfxSelectQuotationLine.getRfxQuotationLines().forEach(rfxQuotationLine
                    -> rfxQuotationLine.setAbandonedFlag(
                    rfxQuotationLine.getAbandonedFlag() == null ? BaseConstants.Digital.ZERO : rfxQuotationLine.getAbandonedFlag()));
        }
        List<RfxQuotationLine> rfxQuotationLinesUpdate = rfxSelectQuotationLine.getRfxQuotationLines();
        List<Long> submitLineIds = rfxSelectQuotationLine.getSelectedRowKeys();
        List<RfxQuotationLine> rfxQuotationLineList;
        if (CollectionUtils.isEmpty(rfxQuotationLinesUpdate) && CollectionUtils.isEmpty(submitLineIds)) {
            return null;
        }
        // 提交前保存付款方式和付款条款
        if (!Objects.isNull(rfxSelectQuotationLine.getRfxQuotationHeader())) {
            rfxSelectQuotationLine.getRfxQuotationHeader().setTenantId(tenantId);
            // 供应商报价单头更新---付款方式和付款条款
            rfxQuotationHeaderRepository.updateOptional(rfxSelectQuotationLine.getRfxQuotationHeader(),
                    RfxQuotationHeader.PAYMENT_TYPE_ID,
                    RfxQuotationHeader.PAYMENT_TERM_ID);
        }
        if (CollectionUtils.isNotEmpty(rfxQuotationLinesUpdate)) {
            //更新保存数据
            iRfxQuotationDomainService.supplierQuotationSave(tenantId, rfxQuotationLinesUpdate);
        }
        if (BaseConstants.Flag.NO.equals(rfxSelectQuotationLine.getAllSubmitFlag())) {
            if (CollectionUtils.isEmpty(submitLineIds)) {
                //提交ID为空，提交数据为保存数据
                rfxQuotationLineList = rfxQuotationLineRepository.quotationLine(tenantId, rfxSelectQuotationLine.getQuotationHeaderId(), rfxQuotationLinesUpdate.stream().map(RfxQuotationLine::getQuotationLineId).collect(Collectors.toList()));
            } else {
                //查出提交数据
                rfxQuotationLineList = rfxQuotationLineRepository.quotationLine(tenantId, rfxSelectQuotationLine.getQuotationHeaderId(), submitLineIds);
            }
        } else {
            //查出所有报价行数据
            rfxQuotationLineList = rfxQuotationLineRepository.quotationLine(tenantId, rfxSelectQuotationLine.getQuotationHeaderId(), null);
        }
        RfxHeader rfxHeader = rfxHeaderRepository.byQuotationHeaderIdVerifyRFQValidTime(rfxQuotationLineList.get(BaseConstants.Digital.ZERO));
        //检查勾选提交,但未填写单价行
        RfxQuotationLine.checkInvalidItemList(rfxQuotationLineList);
        //校验勾选提交，阶梯价格单价未填写
        this.checkLadderPrice(rfxQuotationLineList);
        RfxQuotationLine quotationLineFirst = rfxQuotationLineList.get(0);
        Long currentQuotationRound = quotationLineFirst.getCurrentQuotationRound();
        RfxQuotationHeader rfxQuotationHeader = rfxQuotationHeaderService.findByPrimaryKey(quotationLineFirst.getQuotationHeaderId());
        //校验报价明细行的数据
        iQuotationColumnDomainService.validQuotationDetailForSubmit(new QuotationDetailQueryDTO(rfxQuotationHeader));
        SourceTemplate sourceTemplate = sourceTemplateService.findByPrimaryKey(quotationLineFirst.getTemplateId());
        String supplierIp = inetAddressUtil.getLocalHostIp();
        rfxQuotationHeader.setSupplierCompanyIp(supplierIp);
        //更新报价单头状态为已报价
        rfxQuotationHeaderRepository.updateQuotationHeaderStatus(rfxQuotationHeader);
//        RfxHeader rfxHeader = rfxHeaderRepository.selectByPrimaryKey(rfxQuotationHeader.getRfxHeaderId());
        rfxHeader.setCurrentDateTime(date);
        rfxHeader.setQuotationHeaderId(rfxQuotationHeader.getQuotationHeaderId());
        rfxHeader.setRoundQuotationRule(sourceTemplate.getRoundQuotationRule());
        rfxHeader.setRoundQuotationRankFlag(sourceTemplate.getRoundQuotationRankFlag());
        rfxHeader.setRoundQuotationRankRule(sourceTemplate.getRoundQuotationRankRule());
        Assert.notNull(rfxHeader, SourceConstants.ErrorCode.NO_RFX_HEADER_ID);
//        rfxQuotationDomainService.disposeAbandoned(rfxQuotationLineList, tenantId);
        List<RfxLineItem> rfxLineItems = null;
        // 处理竞价延时和规则
        if (sourceTemplate.getSourceCategory().equals(ShareConstants.SourceTemplate.CategoryType.RFA)) {
            rfxLineItems = iRfxQuotationDomainService.supplierRFA(rfxQuotationLineList, rfxHeader, sourceTemplate);
        }
        // 处理报价行
        iRfxQuotationDomainService.supplierQuotation(rfxQuotationLineList, rfxHeader, sourceTemplate);
        // 放弃时清空阶梯报价信息
//        ladderQuotationDomainService.processAbandonedLadderQuotation(rfxQuotationLineList);
        //报价明细总价管控校验
        this.self().checkQuotationDetailAmtControl(rfxQuotationHeader,rfxSelectQuotationLine.getWeakCtrlConfirmFlag());
        rfxHeader.initRankRule(sourceTemplate);

        rfxHeader.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
        if (StringUtils.isNotBlank(sourceTemplate.getRoundQuotationRule()) && sourceTemplate.getRoundQuotationRule().contains(ShareConstants.RoundQuotationRule.AUTO)) {
            RoundHeaderDate currentRoundHeaderDate = roundHeaderDateRepository.selectCurrentRoundDate(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), ShareConstants.SourceTemplate.CategoryType.RFX, rfxHeader.getRoundNumber(),date);
            if (currentQuotationRound != null && currentQuotationRound < currentRoundHeaderDate.getQuotationRound()) {
                throw new CommonException("当前自动多轮轮次发生变化,请重新进入页面");
            }
            rfxHeader.setCurrentQuotationRound(currentRoundHeaderDate.getQuotationRound());
        } else {
            if (!ShareConstants.RoundQuotationRule.NONE.equals(sourceTemplate.getRoundQuotationRule())) {
                RoundHeader roundHeaderDb = roundHeaderRepository.selectOne(new RoundHeader(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), ShareConstants.SourceCategory.RFX));
                rfxHeader.setCurrentQuotationRound(roundHeaderDb.getQuotationRoundNumber());
            }
        }
        // 竞价图表实时刷新
        if (!ShareConstants.SourceTemplate.CategoryType.RFQ.equals(rfxHeader.getSourceCategory())
                &&!RcwlShareConstants.CategoryType.RCBJ.equals(rfxHeader.getSourceCategory())
                &&!RcwlShareConstants.CategoryType.RCZB.equals(rfxHeader.getSourceCategory())
                &&!RcwlShareConstants.CategoryType.RCZW.equals(rfxHeader.getSourceCategory())) {
            iRfxQuotationRecordDomainService.generateQuotationRecords(rfxHeader, rfxQuotationLineList);
            rfxQuotationRecordService.sendMessageByUpdateQuotations(rfxHeader.getTenantId(), rfxQuotationHeader.getQuotationHeaderId(), sourceTemplate, rfxQuotationLineList, rfxLineItems);
        }

        // 发送事件，
        if (null != rfxHeader.getCurrentQuotationRound()) {
            roundHeaderDomainService.initSupplierRoundRankInfo(rfxQuotationLineList, rfxHeader);
        }
        List<List<RfxQuotationLine>> lists = ShareCommonUtil.splistList(rfxQuotationLineList, 1000);
        for (List<RfxQuotationLine> list : lists) {
//            // 处理rank
//            eventSender.fireEvent(SourceConstants.Event.SSRC_QUOTATION_SUBMIT, SourceConstants.Event.EventCode.QUOTATION_SUBMIT_RANK, BaseConstants.DEFAULT_TENANT_ID, "quotation submit", new QuotationSubmitDTO(list,rfxHeader));
            // 处理多轮报价和报价记录
            eventSender.fireEvent(SourceConstants.Event.SSRC_QUOTATION_SUBMIT, SourceConstants.Event.EventCode.RFQ_QUOTATION_SUBMIT, BaseConstants.DEFAULT_TENANT_ID, "quotation submit", new QuotationSubmitDTO(list,rfxHeader));
        }
        return null;
    }
}
