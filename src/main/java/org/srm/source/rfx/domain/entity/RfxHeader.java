package org.srm.source.rfx.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.util.Assert;
import org.srm.boot.pr.domain.vo.PrChangeVO;
import org.srm.common.mybatis.domain.ExpandDomain;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.rfx.api.dto.CheckPriceHeaderDTO;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxMemberRepository;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.constant.Constants;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.share.api.dto.PreSourceHeaderDTO;
import org.srm.source.share.api.dto.SourceProjectDTO;
import org.srm.source.share.domain.entity.RoundHeaderDate;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.source.share.infra.utils.DateUtil;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 询价单头表
 *
 * @author xuan.zhang03@hand-china.com 2018-12-27 18:44:58
 */
@ApiModel("询价单头表")
@VersionAudit
@ModifyAudit
@Table(name = "ssrc_rfx_header")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfxHeader extends ExpandDomain {

    public static final String FIELD_RFX_HEADER_ID = "rfxHeaderId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RFX_NUM = "rfxNum";
    public static final String FIELD_RFX_STATUS = "rfxStatus";
    public static final String FIELD_RFX_TITLE = "rfxTitle";
    public static final String FIELD_TEMPLATE_ID = "templateId";
    public static final String FIELD_SOURCE_CATEGORY = "sourceCategory";
    public static final String FIELD_SOURCE_METHOD = "sourceMethod";
    public static final String FIELD_PUR_ORGANIZATION_ID = "purOrganizationId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPANY_NAME = "companyName";
    public static final String FIELD_AUCTION_DIRECTION = "auctionDirection";
    public static final String FIELD_BUDGET_AMOUNT = "budgetAmount";
    public static final String FIELD_TAX_INCLUDED_FLAG = "taxIncludedFlag";
    public static final String FIELD_TAX_ID = "taxId";
    public static final String FIELD_TAX_RATE = "taxRate";
    public static final String FIELD_CURRENCY_CODE = "currencyCode";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_EXCHANGE_RATE_ID = "exchangeRateId";
    public static final String FIELD_EXCHANGE_RATE_TYPE = "exchangeRateType";
    public static final String FIELD_EXCHANGE_RATE_DATE = "exchangeRateDate";
    public static final String FIELD_EXCHANGE_RATE_PERIOD = "exchangeRatePeriod";
    public static final String FIELD_EXCHANGE_RATE = "exchangeRate";
    public static final String FIELD_RFX_REMARK = "rfxRemark";
    public static final String FIELD_INTERNAL_REMARK = "internalRemark";
    public static final String FIELD_QUOTATION_START_DATE = "quotationStartDate";
    public static final String FIELD_QUOTATION_END_DATE = "quotationEndDate";
    public static final String FIELD_LATEST_QUOTATION_END_DATE = "latestQuotationEndDate";
    public static final String FIELD_SEALED_QUOTATION_FLAG = "sealedQuotationFlag";
    public static final String FIELD_OPEN_RULE = "openRule";
    public static final String FIELD_AUCTION_RANKING = "auctionRanking";
    public static final String FIELD_SOURCE_TYPE = "sourceType";
    public static final String FIELD_PRICE_CATEGORY = "priceCategory";
    public static final String FIELD_PAYMENT_TYPE_ID = "paymentTypeId";
    public static final String FIELD_PAYMENT_TERM_ID = "paymentTermId";
    public static final String FIELD_ROUND_NUMBER = "roundNumber";
    public static final String FIELD_VERSION_NUMBER = "versionNumber";
    public static final String FIELD_QUOTATION_ORDER_TYPE = "quotationOrderType";
    public static final String FIELD_QUOTATION_RUNNING_DURATION = "quotationRunningDuration";
    public static final String FIELD_QUOTATION_INTERVAL = "quotationInterval";
    public static final String FIELD_AUCTION_RULE = "auctionRule";
    public static final String FIELD_AUTO_DEFER_FLAG = "autoDeferFlag";
    public static final String FIELD_AUTO_DEFER_DURATION = "autoDeferDuration";
    public static final String FIELD_RELEASED_DATE = "releasedDate";
    public static final String FIELD_RELEASED_BY = "releasedBy";
    public static final String FIELD_TERMINATED_DATE = "terminatedDate";
    public static final String FIELD_TERMINATED_BY = "terminatedBy";
    public static final String FIELD_TERMINATED_REMARK = "terminatedRemark";
    public static final String FIELD_APPROVED_DATE = "approvedDate";
    public static final String FIELD_APPROVED_BY = "approvedBy";
    public static final String FIELD_APPROVED_REMARK = "approvedRemark";
    public static final String FIELD_TIME_ADJUSTED_DATE = "timeAdjustedDate";
    public static final String FIELD_TIME_ADJUSTED_BY = "timeAdjustedBy";
    public static final String FIELD_TIME_ADJUSTED_REMARK = "timeAdjustedRemark";
    public static final String FIELD_CLOSED_FLAG = "closedFlag";
    public static final String FIELD_SOURCE_FROM = "sourceFrom";
    public static final String FIELD_PRETRAIL_REMARK = "pretrailRemark";
    public static final String FIELD_CHECK_REMARK = "checkRemark";
    public static final String FIELD_TOTAL_COST = "totalCost";
    public static final String FIELD_COST_REMARK = "costRemark";
    public static final String FIELD_TECH_ATTACHMENT_UUID = "techAttachmentUuid";
    public static final String FIELD_BUSINESS_ATTACHMENT_UUID = "businessAttachmentUuid";
    public static final String FIELD_PRETRIAL_USER_ID = "pretrialUserId";
    public static final String FIELD_QUOTATION_TYPE = "quotationType";
    public static final String FIELD_BACK_PRETRIAL_REMARK = "backPretrialRemark";
    public static final String FIELD_PREQUAL_END_DATE = "prequalEndDate";
    public static final String FIELD_DELIVER_USER_ID = "deliverUserId";
    public static final String FIELD_PRETRIAL_STATUS = "pretrialStatus";
    public static final String FIELD_PRE_ATTACHMENT_UUID = "preAttachmentUuid";
    public static final String FIELD_HAND_DOWN_DATE = "handDownDate";
    public static final String FIELD_CHECK_ATTACHMENT_UUID = "checkAttachmentUuid";
    public static final String FIELD_QUOTATION_SCOPE = "quotationScope";
    public static final String FIELD_START_FLAG = "startFlag";
    public static final String FIELD_START_QUOTATION_RUNNING_DURATION = "startQuotationRunningDuration";
    public static final String FIELD_UNIT_ID = "unitId";
    public static final String FIELD_CREATED_UNIT_ID = "createdUnitId";
    public static final String FIELD_CREATED_UNIT_NAME = "createdUnitName";
    public static final String FIELD_BARGAIN_STATUS = "bargainStatus";
    public static final String FIELD_BARGAIN_END_DATE = "bargainEndDate";
    public static final String FIELD_PURCHASER_ID = "purchaserId";
    public static final String FIELD_BARGAIN_ATTACHMENT_UUID = "bargainAttachmentUuid";
    public static final String FIELD_MULTI_CURRENCY_FLAG = "multiCurrencyFlag";
    public static final String FIELD_PRETRIAL_UUID = "pretrialUuid";
    public static final String FIELD_MIN_QUOTED_SUPPLIER = "minQuotedSupplier";
    public static final String FIELD_LACK_QUOTED_SEND_FLAG = "lackQuotedSendFlag";
    public static final String FIELD_CENTRAL_PURCHASE_FLAG = "centralPurchaseFlag";
    public static final String FIELD_BID_BOND = "bidBond";
    public static final String FIELD_BID_FILE_EXPENSE = "bidFileExpense";
    public static final String FIELD_PRICE_EFFECTIVE_DATE = "priceEffectiveDate";
    public static final String FIELD_PRICE_EXPIRY_DATE = "priceExpiryDate";
    public static final String FIELD_PAYMENT_TERM_FLAG = "paymentTermFlag";
    public static final String FIELD_MATTER_REQUIRE_FLAG = "matterRequireFlag";
    public static final String FIELD_MATTER_DETAIL = "matterDetail";
    public static final String FIELD_BUDGET_AMOUNT_FLAG = "budgetAmountFlag";
    public static final String FIELD_PAYMENT_CLAUSE = "paymentClause";
    public static final String FIELD_RANK_RULE = "rankRule";
    public static final String FIELD_PUR_NAME = "purName";
    public static final String FIELD_PUR_EMAIL= "purEmail";
    public static final String FIELD_PUR_PHONE = "purPhone";
    public static final String FIELD_ITEM_GENERATE_POLICY = "itemGeneratePolicy";
    public static final String FIELD_SCORE_PROCESS_FLAG = "scoreProcessFlag";
    public static final String FIELD_FINISHING_RATE = "finishingRate";
    public static final String FIELD_QUOTATION_CHANGE = "quotationChange";
    public static final String FIELD_ESTIMATED_START_TIME = "estimatedStartTime";
    public static final String FIELD_BUSINESS_WEIGHT = "businessWeight";
    public static final String FIELD_TECHNOLOGY_WEIGHT = "technologyWeight";
    public static final String FIELD_QUOTATION_ROUNDS = "quotationRounds";
    public static final String FIELD_SOURCE_PROJECT_ID = "sourceProjectId";
    public static final String FIELD_CHECK_FINISHED_DATE = "checkFinishedDate";

    public RfxHeader() {
    }
    public RfxHeader(Integer quotationFlag) {
        this.quotationFlag = quotationFlag;
    }
    public RfxHeader(CheckPriceHeaderDTO checkPriceHeaderDTO) {
        this.setObjectVersionNumber(checkPriceHeaderDTO.getObjectVersionNumber());
        this.rfxHeaderId = checkPriceHeaderDTO.getRfxHeaderId();
        this.totalCost = checkPriceHeaderDTO.getTotalCost();
        this.costRemark = checkPriceHeaderDTO.getCostRemark();
        this.checkAttachmentUuid = checkPriceHeaderDTO.getCheckAttachmentUuid();
        this.checkRemark = checkPriceHeaderDTO.getCheckRemark();
    }

    public RfxHeader(Long tenantId, Long rfxHeaderId) {
        this.tenantId = tenantId;
        this.rfxHeaderId = rfxHeaderId;
    }

    public void initTotalCoast(List<RfxLineItem> rfxLineItemList) {
        this.totalCost = new BigDecimal(0);
        rfxLineItemList.stream()
                .filter(lineItem -> null != lineItem.getCostPrice() && lineItem.getCostPrice().compareTo(new BigDecimal(0)) > 0)
                .forEach(lineItem -> this.totalCost = this.totalCost.add(lineItem.getCostPrice().multiply(lineItem.getRfxQuantity())));
        if (new BigDecimal(0).compareTo(this.totalCost) == 0) {
            this.totalCost = this.totalCost.setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * 开启操作状态校验
     */
    public void validResume() {
        if (!SourceConstants.RfxStatus.PAUSED.equals(this.rfxStatus)) {
            throw new CommonException(SourceConstants.ErrorCode.ERROR_OPERATE_STATUS);
        }
    }

    /**
     * 关闭操作状态校验
     */
    public void validClose() {
        switch (this.rfxStatus) {
            case SourceConstants.RfxStatus.NEW:
            case SourceConstants.RfxStatus.CLOSED:
            case SourceConstants.RfxStatus.FINISHED:
            case SourceConstants.RfxStatus.RELEASE_APPROVING:
            case SourceConstants.RfxStatus.CHECK_APPROVING:
                throw new CommonException(SourceConstants.ErrorCode.ERROR_OPERATE_STATUS);
            default:
                return;
        }
    }

    /**
     * 关闭操作数据初始化
     */
    public void initClose(String terminatedRemark) {
        this.rfxStatus = SourceConstants.RfxStatus.CLOSED;
        this.closedFlag = BaseConstants.Flag.YES;
        this.terminatedBy = DetailsHelper.getUserDetails().getUserId();
        this.terminatedDate = new Date();
        this.terminatedRemark = terminatedRemark;
    }

    /**
     * 询价单校验竞价规则
     */
    public void validAuctionRule(SourceTemplate sourceTemplate) {
        if (ShareConstants.SourceTemplate.CategoryType.RFA.equals(this.sourceCategory)) {
            if (BaseConstants.Flag.YES.equals(this.getStartFlag()) && BaseConstants.Flag.YES.equals(sourceTemplate.getFastBidding())){
                throw new CommonException(SourceConstants.ErrorCode.ERROR_RFA_START_FLAG_DISABLE);
            }
            if (ShareConstants.SourceTemplate.AuctionDirection.NONE.equals(this.auctionDirection)) {
                throw new CommonException(SourceConstants.ErrorCode.ERROR_RFA_AUCTION_DIRECTION);
            }
            if (this.quotationRunningDuration == null) {
                throw new CommonException(SourceConstants.ErrorCode.ERROR_QUOTATION_RUNNING_DURATION_IS_NULL);
            }
            if (SourceConstants.QuotationOrderType.PARALLEL.equals(this.quotationOrderType)) {
                this.quotationInterval = null;
            } else {
                if (this.quotationInterval == null) {
                    throw new CommonException(SourceConstants.ErrorCode.ERROR_QUOTATION_INTERVAL_IS_NULL);
                }
            }
            if (!ShareConstants.SourceTemplate.SourceMethod.INVITE.equals(this.sourceMethod)
                    && ShareConstants.SourceTemplate.RankRule.WEIGHT_PRICE.equals(this.rankRule)) {
                throw new CommonException(ShareConstants.ErrorCode.ERROR_WEIGHT_PRICE_INVITE);
            }
            if (BaseConstants.Flag.YES.equals(sourceTemplate.getFastBidding()) && this.estimatedStartTime == null){
                //如果模板勾选了集中竞价，那么校验预计开始时间为必输
                throw new CommonException(SourceConstants.ErrorCode.ERROR_ESTIMATED_START_TIME_IS_NULL);
            }
        }
    }

    public void initAuctionRule(SourceTemplate sourceTemplate) {
        if (ShareConstants.SourceTemplate.CategoryType.RFA.equals(sourceTemplate.getSourceCategory())) {
            this.openRule = sourceTemplate.getOpenRule();
            this.auctionRule = sourceTemplate.getAuctionRule();
            this.auctionRanking = sourceTemplate.getAuctionRanking();
            this.autoDeferFlag = sourceTemplate.getAutoDeferFlag();
            this.autoDeferDuration = sourceTemplate.getAutoDeferDuration();
            this.rankRule = sourceTemplate.getRankRule();
        }
    }

    /**
     * 时间修改初始化
     */
    public void initAdjustDate() {
        this.timeAdjustedBy = DetailsHelper.getUserDetails().getUserId();
        this.timeAdjustedDate = new Date();
    }

    public void validateOpener(SourceTemplate sourceTemplate, RfxMemberRepository rfxMemberRepository) {
        if (BaseConstants.Flag.YES.equals(this.sealedQuotationFlag) && BaseConstants.Flag.YES.equals(sourceTemplate.getOpenerFlag())) {
            List<RfxMember> rfxOpenerMembers = rfxMemberRepository.select(new RfxMember(this.tenantId,this.rfxHeaderId, SourceConstants.RfxRole.OPENED_BY,null));
            if (CollectionUtils.isEmpty(rfxOpenerMembers)) {
                throw new CommonException(SourceConstants.ErrorCode.ERROR_OPENER_NOT_NULL);
            }
        }
    }

    /**
     * 询报价时间调整
     *
     * @param rfxHeaderRepository 询价单Repository
     * @param realRfxHeader       物料行号
     */
    public void adjustDate(RfxHeaderRepository rfxHeaderRepository, RfxHeader realRfxHeader) {
        Date now = new Date();
        //报价开始时间更改校验
        if (realRfxHeader.getQuotationStartDate() != null && DateUtil.beforeStartDate(realRfxHeader.getQuotationStartDate(), now, "")) {
            //如果报价开始时间是可选的，那么报价开始时间校验必须大于当前时间
            if (DateUtil.beforeStartDate(now, this.quotationStartDate == null ? realRfxHeader.getQuotationStartDate() : this.quotationStartDate, "")) {
                throw new CommonException(SourceConstants.ErrorCode.ERROR_START_TIME_EARLIER_THEN_CURRENT_TIME);
            }
            if (Objects.nonNull(realRfxHeader.getQuotationEndDate())) {
                if(DateUtil.beforeStartDate(now, realRfxHeader.getQuotationEndDate(), "")){
                    //如果有报价结束时间，那么报价开始时间校验必须大于当前时间
                    throw new CommonException(SourceConstants.ErrorCode.ERROR_END_TIME_EARLIER_THEN_CURRENT_TIME);
                }
                if(DateUtil.beforeStartDate(this.quotationStartDate == null ? realRfxHeader.getQuotationStartDate() : this.quotationStartDate, realRfxHeader.getQuotationEndDate(), "")) {
                    //如果有报价结束时间，那么更改报价开始时间的时候，报价开始时间还不能大于报价截止时间
                    throw new CommonException(SourceConstants.ErrorCode.ERROR_END_TIME_EARLIER_THEN_START_TIME);
                }
            }
            if (realRfxHeader.getPrequalEndDate() != null && DateUtil.beforeStartDate(realRfxHeader.getPrequalEndDate(), this.quotationStartDate, null)) {
                throw new CommonException(SourceConstants.ErrorCode.PREQUAL_END_TIME_BIGGER_OR_SMALLER);
            }
        }
        String realStatus = realRfxHeader.getRfxStatus();
        if (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(realRfxHeader.getSourceCategory())
                || RcwlShareConstants.CategoryType.RCBJ.equals(realRfxHeader.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZB.equals(realRfxHeader.getSourceCategory())
                ||RcwlShareConstants.CategoryType.RCZW.equals(realRfxHeader.getSourceCategory())) {
            if (SourceConstants.RfxStatus.NOT_START.equals(realStatus) || SourceConstants.RfxStatus.PENDING_PREQUAL.equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.quotationStartDate);
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this,
                        RfxHeader.FIELD_QUOTATION_START_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_BY,
                        RfxHeader.FIELD_QUOTATION_END_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_REMARK));
            }
            if (SourceConstants.RfxStatus.IN_QUOTATION.equals(realStatus) || SourceConstants.RfxStatus.LACK_QUOTED.equals(realStatus)
                    || (realRfxHeader.getQuotationEndDateChangeFlag()==BaseConstants.Flag.YES &&  SourceConstants.RfxStatus.OPEN_BID_PENDING.equals(realStatus))) {
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                this.rfxStatus = SourceConstants.RfxStatus.IN_QUOTATION;
                CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this,
                        RfxHeader.FIELD_QUOTATION_END_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_BY,
                        RfxHeader.FIELD_RFX_STATUS,
                        RfxHeader.FIELD_TIME_ADJUSTED_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_REMARK));
            }

            if (SourceConstants.RfxStatus.IN_PREQUAL.equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.getQuotationStartDate());
                CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this,
                        RfxHeader.FIELD_QUOTATION_START_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_BY,
                        RfxHeader.FIELD_TIME_ADJUSTED_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_REMARK
                ));

            }
        }
        if (ShareConstants.SourceTemplate.CategoryType.RFA.equals(realRfxHeader.getSourceCategory())) {
            if (SourceConstants.RfxStatus.NOT_START.equals(realStatus) || SourceConstants.RfxStatus.PENDING_PREQUAL.equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.quotationStartDate);
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setQuotationInterval(this.quotationInterval);
                realRfxHeader.setQuotationRunningDuration(this.quotationRunningDuration);
                dealWithRfqQuotationEndDate();
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this,
                        RfxHeader.FIELD_QUOTATION_START_DATE,
                        RfxHeader.FIELD_QUOTATION_RUNNING_DURATION,
                        RfxHeader.FIELD_QUOTATION_INTERVAL,
                        RfxHeader.FIELD_TIME_ADJUSTED_BY,
                        RfxHeader.FIELD_TIME_ADJUSTED_DATE,
                        RfxHeader.FIELD_QUOTATION_END_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_REMARK));
            }
            if (SourceConstants.RfxStatus.IN_QUOTATION.equals(realStatus)) {
                throw new CommonException(SourceConstants.ErrorCode.ERROR_OPERATE_STATUS);
            }
            if (SourceConstants.RfxStatus.IN_PREQUAL.equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.getQuotationStartDate());
                dealWithRfqQuotationEndDate();
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this,
                        RfxHeader.FIELD_QUOTATION_START_DATE,
                        RfxHeader.FIELD_QUOTATION_RUNNING_DURATION,
                        RfxHeader.FIELD_QUOTATION_INTERVAL,
                        RfxHeader.FIELD_TIME_ADJUSTED_BY,
                        RfxHeader.FIELD_TIME_ADJUSTED_DATE,
                        RfxHeader.FIELD_QUOTATION_END_DATE,
                        RfxHeader.FIELD_TIME_ADJUSTED_REMARK
                ));
            }
        }
        this.latestQuotationEndDate = realRfxHeader.getQuotationEndDate();
        this.handDownDate = realRfxHeader.getQuotationEndDate();
        CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this, RfxHeader.FIELD_LATEST_QUOTATION_END_DATE, RfxHeader.FIELD_HAND_DOWN_DATE));
    }

    /**
     * 竞价的时候对报价截止时间处理
     */
    private void dealWithRfqQuotationEndDate() {
        if (Objects.nonNull(this.day)) {
            this.quotationEndDate = DateUtil.addDay(this.quotationStartDate, day.intValue());
        }
        if (Objects.nonNull(this.hour)){
            this.quotationEndDate = DateUtil.addHourOrMin(this.quotationStartDate,hour.intValue(), Calendar.HOUR_OF_DAY);
        }
        if (Objects.nonNull(this.minute)){
            this.quotationEndDate = DateUtil.addHourOrMin(this.quotationStartDate,minute.intValue(),Calendar.MINUTE);
        }
    }

    public void initPretrialUser(SourceTemplate sourceTemplate) {
        if (BaseConstants.Flag.YES.equals(sourceTemplate.getPretrialFlag())) {
            this.pretrialUserId = DetailsHelper.getUserDetails().getUserId();
            this.pretrialStatus = SourceConstants.PretrialStatus.NO_TRIAL;
        }
    }

    /**
     * 初始化资格预审标志
     *
     * @param sourceTemplate
     */
    public void initPreQualificationFlag(SourceTemplate sourceTemplate) {
        this.preQualificationFlag = (BidConstants.BidHeader.QualificationType.PRE.equals(sourceTemplate.getQualificationType())
                || BidConstants.BidHeader.QualificationType.PRE_POST.equals(sourceTemplate.getQualificationType())) ? BaseConstants.Digital.ONE : BaseConstants.Digital.ZERO;
    }

    /**
     * 准备询价单头初始值
     *
     * @param sourceTemplate 寻源模板
     * @param prLineVO       参数
     * @param organizationId 租户id
     * @return RfxHeader
     */
    public void preRfxHeader(SourceTemplate sourceTemplate, PrLineVO prLineVO, Long organizationId) {
        this.templateId = sourceTemplate.getTemplateId();
        this.companyId = prLineVO.getCompanyId();
        this.companyName = prLineVO.getCompanyName();
        this.currencyCode = prLineVO.getCurrencyCode() == null? SourceConstants.RfxConstants.CURRENCY_CODE_DEFAULT : prLineVO.getCurrencyCode();
        this.timeAdjustedBy = 1L;
        this.rfxStatus = SourceConstants.RfxStatus.NEW;
        this.tenantId = organizationId;
        this.rfxTitle = BaseConstants.Symbol.SPACE;
        this.sourceMethod = sourceTemplate.getSourceMethod();
        this.sourceType = sourceTemplate.getSourceType();
        this.priceCategory = ShareConstants.SourceTemplate.SourcePriceCategory.STANDARD;
        this.sourceFrom = SourceConstants.SourceFrom.DEMAND_POOL;
        this.sourceCategory = sourceTemplate.getSourceCategory();
        this.quotationType = sourceTemplate.getQuotationType();
        this.auctionDirection = sourceTemplate.getAuctionDirection();
        this.quotationScope = sourceTemplate.getQuotationScope();
        this.purchaserId=prLineVO.getPurchaseAgentId();
    }

    public void validQuotationType() {
        if (SourceConstants.EntryMethod.OFFLINE.equals(this.quotationType) && SourceConstants.RfxType.INVITE.equals(this.sourceMethod)) {
            throw new CommonException(SourceConstants.ErrorCode.ERROR_OFFLINE_SOURCE_METHOD);
        }
    }

    public void initQuotationEndDateByItem(RfxHeaderRepository rfxHeaderRepository, List<RfxLineItem> rfxLineItems) {
        Optional<RfxLineItem> itemOptional = rfxLineItems.stream().max(Comparator.comparing(RfxLineItem::getRfxLineItemNum));
        itemOptional.ifPresent(rfxLineItem -> {
            this.quotationEndDate = rfxLineItem.getQuotationEndDate();
            CustomizeHelper.ignore(() -> rfxHeaderRepository.updateOptional(this, RfxHeader.FIELD_QUOTATION_END_DATE));
        });
    }

    public void pretrialSubmit() {
        this.rfxStatus = SourceConstants.RfxStatus.CHECK_PENDING;
        this.pretrialStatus = SourceConstants.PretrialStatus.SUBMITED;
    }

    public void pretrialBack() {
        this.rfxStatus = SourceConstants.RfxStatus.PRETRIAL_PENDING;
        this.pretrialStatus = SourceConstants.PretrialStatus.NO_TRIAL;
    }

    /**
     * 初始化动态排名配置
     *
     * @param sourceTemplate 寻源模板
     */
    public void initRankRule(SourceTemplate sourceTemplate) {
        if (this.openRule != null) {
            sourceTemplate.setOpenRule(openRule);
        }
        if (this.auctionDirection != null) {
            sourceTemplate.setAuctionDirection(this.auctionDirection);
        }
        if (this.autoDeferFlag != null) {
            sourceTemplate.setAutoDeferFlag(this.autoDeferFlag);
        }
        if (this.quotationOrderType != null) {
            sourceTemplate.setQuotationOrderType(this.quotationOrderType);
        }
        sourceTemplate.setRoundNumber(this.roundNumber);
        sourceTemplate.setRfxHeaderId(this.rfxHeaderId);
        sourceTemplate.setRankRule(this.rankRule);
        sourceTemplate.setRfxNum(this.rfxNum);
    }

    public void validQuotationTime(SourceTemplate sourceTemplate) {
        //判断如果报价运行时间为0则改成null
        BigDecimal startQuotationRunningDuration = this.getStartQuotationRunningDuration();
        if (startQuotationRunningDuration !=null && startQuotationRunningDuration.equals(BigDecimal.ZERO)){
            this.startQuotationRunningDuration = null;
        }
        //寻源类别
        String categroyType = sourceTemplate.getSourceCategory();
        // 是否设置报价截至时间标识
        Integer quotationEndDateFlag = sourceTemplate.getQuotationEndDateFlag();
        Date now = new Date();
        if (this.startFlag == null || BaseConstants.Flag.NO.equals(startFlag)) {
            if (BaseConstants.Flag.NO.equals(sourceTemplate.getFastBidding())){
                Assert.notNull(this.quotationStartDate, SourceConstants.ErrorCode.ERROR_QUOTATION_START_TIME_NOT_FOUND);
                Assert.isTrue(this.quotationStartDate.compareTo(now) >= 0, SourceConstants.ErrorCode.ERROR_START_TIME_EARLIER_THEN_CURRENT_TIME);
                if (BaseConstants.Flag.YES.equals(quotationEndDateFlag)
                        && (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(categroyType)
                        ||RcwlShareConstants.CategoryType.RCBJ.equals(categroyType)
                        ||RcwlShareConstants.CategoryType.RCZB.equals(categroyType)
                        ||RcwlShareConstants.CategoryType.RCZW.equals(categroyType))) {
                    //报价运行时间和报价截至时间不能同时为空
                    Assert.isTrue(this.startQuotationRunningDuration != null || this.quotationEndDate != null, SourceConstants.ErrorCode.ERROR_QUOTATION_END_DATE_OR_RUNNING_TIME_ONE_EXIST);
                    Assert.isTrue(this.quotationEndDate == null || this.quotationEndDate.compareTo(now) >= 0, SourceConstants.ErrorCode.ERROR_END_TIME_EARLIER_THEN_CURRENT_TIME);
                    Assert.isTrue(this.quotationEndDate == null || this.quotationEndDate.compareTo(quotationStartDate) > 0, SourceConstants.ErrorCode.ERROR_END_TIME_EARLIER_THEN_START_TIME);
                }
            }
        } else {
            this.quotationStartDate = null;
        }
        if (BaseConstants.Flag.NO.equals(quotationEndDateFlag)) {
            this.quotationEndDate = null;
            this.startQuotationRunningDuration = null;
        }
    }

    /**
     * 询价单审批通过后的初始化
     */
    public void initAfterApproval() {
        this.sourceType = Optional.ofNullable(this.sourceType).orElse(SourceConstants.SourceType.NORMAL);
        this.approvedBy = DetailsHelper.getUserDetails().getUserId();
        this.approvedDate = new Date();
        if (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(this.sourceCategory)
                ||RcwlShareConstants.CategoryType.RCBJ.equals(this.sourceCategory)
                ||RcwlShareConstants.CategoryType.RCZB.equals(this.sourceCategory)
                ||RcwlShareConstants.CategoryType.RCZW.equals(this.sourceCategory)) {
            this.startQuotationRunningDuration = this.quotationEndDate == null ? null : new BigDecimal((this.quotationEndDate.getTime() - this.quotationStartDate.getTime()) / 60000);
        }
        this.latestQuotationEndDate = quotationEndDate;
        //默认下发时间为报价截至时间
        this.handDownDate = quotationEndDate;
        //设置议价状态
        if (Objects.isNull(this.bargainRule) || SourceConstants.BargainRule.NONE.equals(this.bargainRule)) {
            this.bargainStatus = SourceConstants.BargainStatus.CLOSE;
        } else {
            this.bargainStatus = SourceConstants.BargainStatus.INITIATE;
        }
    }

    /**
     * 初始化需求部门
     *
     * @param prLineVOList
     */
    public void initUnitInfo(List<PrLineVO> prLineVOList) {
        if (CollectionUtils.isEmpty(prLineVOList)) {
            return;
        }
        if (BaseConstants.Digital.ONE != prLineVOList.stream().map(PrLineVO::getUnitId).collect(Collectors.toSet()).size()) {
            return;
        }
        this.unitId = prLineVOList.get(BaseConstants.Digital.ZERO).getUnitId();
    }

    public void validationQuotationStartDate() {
        if (Objects.isNull(this.quotationStartDate)) {
            return;
        }
        if (this.quotationStartDate.before(super.getCreationDate())) {
            throw new CommonException(SourceConstants.ErrorCode.ERROR_QUOTATION_START_DATE);
        }
    }

    /**
     * 确认中标候选人时复原议价状态
     */
    public void resetBargainStatus() {
        if (Objects.equals(SourceConstants.RfxStatus.PRE_EVALUATION_PENDING, this.rfxStatus)) {
            if (Objects.equals(SourceConstants.BargainStatus.BARGAINING_ONLINE, this.bargainStatus)) {
                this.bargainStatus = SourceConstants.BargainStatus.BARGAIN_ONLINE;
            }
            if (Objects.equals(SourceConstants.BargainStatus.BARGAINING_OFFLINE, this.bargainStatus)) {
                this.bargainStatus = SourceConstants.BargainStatus.BARGAIN_OFFLINE;
            }
        }
    }

    public void initSentMessagePara(Map<String, String> map, SimpleDateFormat format) {
        map.put(Constants.MessageConstants.ORGANIZATION_ID, String.valueOf(this.tenantId));
        map.put(Constants.MessageConstants.COMPANY_NAME, this.companyName);
        map.put(Constants.MessageConstants.COMPANY_ID, String.valueOf(this.companyId));
        map.put(Constants.MessageConstants.RFX_TITLE, this.rfxTitle);
        map.put(Constants.MessageConstants.RFX_NUMBER, this.rfxNum);
        map.put(Constants.MessageConstants.RFX_CHECK_SUBMIT_TIME, format.format(new Date()));
        map.put(Constants.MessageConstants.SOURCE_HEADER_ID, String.valueOf(this.rfxHeaderId));
        map.put(Constants.MessageConstants.SOURCE_TYPE, ShareConstants.SourceCategory.RFX);
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public List<PrChangeVO> initPrChangeVOS(List<RfxLineItem> rfxLineItems) {
        if (CollectionUtils.isEmpty(rfxLineItems)){
            return null;
        }
        List<PrChangeVO> prChangeVOS = new ArrayList<>();
        rfxLineItems.forEach(rfxLineItem -> {
            if (null != rfxLineItem.getPrHeaderId() && null != rfxLineItem.getPrLineId()){
                prChangeVOS.add(new PrChangeVO(rfxLineItem.getPrHeaderId(), rfxLineItem.getPrLineId(), ShareConstants.PrBillType.SOURCE_RFX, rfxLineItem.getRfxHeaderId(), rfxLineItem.getRfxLineItemId(),String.valueOf(rfxLineItem.getRfxLineItemNum()),this.rfxNum, null, rfxLineItem.getRfxQuantity(),DetailsHelper.getUserDetails().getUserId()));
            }
        });
        return prChangeVOS;
    }

    public void prequalData(Long quotationHeaderId, RfxLineSupplier rfxLineSupplier) {
        this.quotationHeaderId=quotationHeaderId;
        this.docCategory=ShareConstants.SourceEventCode.RFX_PREQUAL_DOC_CATEGORY;
        this.publishedDate=new Date();
        this.supplierCompanyId=rfxLineSupplier.getSupplierCompanyId();
        this.supplierName=rfxLineSupplier.getSupplierCompanyName();
        this.supplierTenantId=rfxLineSupplier.getSupplierTenantId();
    }

    public void initDataBeforeUpdate() {
        this.centralPurchaseFlag = (this.centralPurchaseFlag == null? BaseConstants.Flag.NO :this.centralPurchaseFlag);
        this.checkedBy = (this.checkedBy == null? (this.getCreatedBy() == null? DetailsHelper.getUserDetails().getUserId() : this.getCreatedBy()) :this.checkedBy);
        this.lackQuotedSendFlag = (this.lackQuotedSendFlag == null? BaseConstants.Flag.NO :this.lackQuotedSendFlag);
        this.bidBond = (this.bidBond == null? new BigDecimal(0.00) : this.bidBond);
        this.budgetAmountFlag = (this.budgetAmountFlag == null? BaseConstants.Flag.NO :this.budgetAmountFlag);
    }

    public void initCustomizeField(CheckPriceHeaderDTO checkPriceHeaderDTO){
        String priceEffectiveDate = checkPriceHeaderDTO.getPriceEffectiveDate();
        String priceExpiryDate = checkPriceHeaderDTO.getPriceExpiryDate();
        if (StringUtils.isBlank(priceEffectiveDate) && StringUtils.isBlank(priceExpiryDate)) return;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConstants.Pattern.DATE);
            this.priceEffectiveDate = priceEffectiveDate == null? null : simpleDateFormat.parse(priceEffectiveDate);
            this.priceExpiryDate = priceExpiryDate == null? null : simpleDateFormat.parse(priceExpiryDate);
            if (priceEffectiveDate != null && priceExpiryDate!= null) {
                Assert.isTrue(this.priceExpiryDate.after(this.priceEffectiveDate), SourceConstants.ErrorCode.ERROR_EFFECTIVE_DATE_NOT_VALID);
            }
        } catch (ParseException p) {

        }
    }

    public void initCalMergeRuleProperties(PreSourceHeaderDTO preSourceHeaderDTO) {
        this.purOrganizationId = preSourceHeaderDTO.getCalPurchaseOrgId();
        this.purchaserId = preSourceHeaderDTO.getCalPurchaseAgentId();
        this.currencyCode = preSourceHeaderDTO.getCalCurrencyCode();
        this.unitId = preSourceHeaderDTO.getCalUnitId();
    }

    public String[] buildConfigCenterParameters() {
        //公司、创建人、创建人部门、采购组织、需求部门、寻源模板、寻源类别
        String companyId = this.companyId == null? null : this.companyId.toString();
        String createdBy = this.getCreatedBy() == null? null : this.getCreatedBy().toString();
        String createdUnitId = this.createdUnitId == null? null : this.createdUnitId.toString();
        String purOrganizationId = this.purOrganizationId == null? null : this.purOrganizationId.toString();
        String unitId = this.unitId == null? null : this.unitId.toString();
        String templateId = this.templateId == null? null : this.templateId.toString();
        return new String[]{companyId, createdBy, createdUnitId, purOrganizationId, unitId, templateId, sourceCategory};
    }

    public void referenceSourceProject(SourceProjectDTO sourceProject) {
        this.rfxTitle = null;
        if (StringUtils.isNotBlank(sourceProject.getSourceMethod())) {
            this.sourceMethod = sourceProject.getSourceMethod();
        }
        if (StringUtils.isNotBlank(sourceProject.getSourceCategory())) {
            this.sourceCategory = sourceProject.getSourceCategory();
        }
        if (StringUtils.isNotBlank(sourceProject.getCompanyName())) {
            this.companyName = sourceProject.getCompanyName();
        }
        if (StringUtils.isNotBlank(sourceProject.getContactMail())) {
            this.purEmail = sourceProject.getContactMail();
        }
        if (StringUtils.isNotBlank(sourceProject.getContactMobilephone())) {
            this.purPhone = sourceProject.getContactMobilephone();
        }if (StringUtils.isNotBlank(sourceProject.getPurAgent())) {
            this.purName = sourceProject.getPurAgent();
        }
        if (sourceProject.getCompanyId() != null) {
            this.companyId = sourceProject.getCompanyId();
        }
        if (sourceProject.getBudgetAmount() != null) {
            this.budgetAmount = sourceProject.getBudgetAmount();
        }
        if (sourceProject.getDepositAmount() != null) {
            this.bidBond = sourceProject.getDepositAmount();
        }
        if (sourceProject.getPaymentTypeId() != null) {
            this.paymentTypeId = sourceProject.getPaymentTypeId();
        }
        if (sourceProject.getPaymentTermId() != null) {
            this.paymentTermId = sourceProject.getPaymentTermId();
        }
        if (sourceProject.getUnitId() != null) {
            this.unitId = sourceProject.getUnitId();
        }
        this.sourceFrom = SourceConstants.SourceType.PROJECT;
        this.sourceProjectId = sourceProject.getSourceProjectId();
        this.subjectMatterRule = sourceProject.getSubjectMatterRule();
        if (CollectionUtils.isNotEmpty(sourceProject.getProjectLineSections())) {
            this.projectLineSectionId = sourceProject.getProjectLineSections().get(0).getProjectLineSectionId();
        }
        if (ShareConstants.SubjectMatterRule.PACK.equals(this.subjectMatterRule)) {
            this.quotationScope = SourceConstants.QuotationScope.ALL_QUOTATION;
            this.onlyAllowAllWinBids = BaseConstants.Flag.YES;
        }

    }

    public void closeBargain(RfxHeader tempHeader) {
        if (SourceConstants.BargainStatus.BARGAINING_ONLINE.equals(this.bargainStatus)
                || SourceConstants.BargainStatus.BARGAINING_OFFLINE.equals(this.bargainStatus)) {
            tempHeader.setBargainEndDate(tempHeader.getCheckFinishedDate());
            tempHeader.setBargainStatus(SourceConstants.BargainStatus.CLOSE);
        }
    }

    public interface Quotation {
    }

    public interface Abandon {
    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @NotNull(groups = Quotation.class)
    @Encrypt
    private Long rfxHeaderId;

    @ApiModelProperty(value = "所属租户ID，hpfm_tenant.tenant_id", example = "9231")
    @NotNull
    private Long tenantId;

    @ApiModelProperty(value = "询价单单号", example = "testNumber")
    @NotBlank
    @Length(max = 30, message = "询价单单号最大长度30个字符")
    private String rfxNum;

    @ApiModelProperty(value = "询价单状态SSRC.RFX_STATUS(NEW/新建|RELEASE_APPROVING/发布审批中|RELEASE_REJECTED/发布审批拒绝|NOT_START/未开始|IN_PREQUAL/资格预审中|PREQUAL_CUTOFF/资格预审截止|IN_QUOTATION/报价中|OPEN_BID_PENDING/待开标|PRETRIAL_PENDING/待初审|SCORING/评分中|CHECK_PENDING/待核价|CHECK_APPROVING/核价审批中|CHECK_REJECTED/核价审批拒绝|FINISHED/完成|PAUSED/暂停|CLOSED/关闭|ROUNDED/再次询价|IN_POSTQUAL/资格后审中|POSTQUAL_CUTOFF/资格后审截止)", example = "NEW")
    @NotBlank
    @Length(max = 30, message = "询价单状态最大长度30个字符")
    private String rfxStatus;

    @ApiModelProperty(value = "询价单标题", example = "test")
    @NotBlank
    @Length(max = 80, message = "询价单标题最大长度80个字符")
    private String rfxTitle;

    @ApiModelProperty(value = "寻源模板ID")
    @NotNull
    @Encrypt
    private Long templateId;

    @ApiModelProperty(value = "商务组权重")
    private BigDecimal businessWeight;
    @ApiModelProperty(value = "技术组权重")
    private BigDecimal technologyWeight;

    @ApiModelProperty(value = "寻源类别SSRC.SOURCE_CATEGORY(RFQ/询价|RFA/竞价|BID/招投标)", example = "RFQ")
    @NotBlank
    @Length(max = 30, message = "寻源类别最大长度30个字符")
    @LovValue(lovCode = "SSRC.SOURCE_CATEGORY", meaningField = "sourceCategoryMeaning")
    private String sourceCategory;

    @ApiModelProperty(value = "询价方式SSRC.SOURCE_METHOD(INVITE/邀请|OPEN/合作伙伴公开|ALL_OPEN/全平台公开)", example = "INVITE")
    @NotBlank
    @Length(max = 30, message = "询价方式最大长度30个字符")
    @LovValue(value = "SSRC.SOURCE_METHOD", meaningField = "sourceMethodMeaning")
    private String sourceMethod;
    @Transient
    private String sourceMethodMeaning;
    @Transient
    private String roundHeaderStatus;
    @Transient
    private String roundQuotationRule;
    @Transient
    private Date roundQuotationEndDate;
    @Transient
    private Long quotationRoundNumber;

    @ApiModelProperty(value = "采购方采购组织ID")
    private Long purOrganizationId;

    @ApiModelProperty(value = "采购方企业ID")
    @NotNull
    @Encrypt
    private Long companyId;
    @ApiModelProperty(value = "当前组别序号")
    private Integer currentSequenceNum;

    @ApiModelProperty(value = "采购方企业名称")
    @NotBlank
    @Length(max = 360, message = "采购方企业名称最大长度360个字符")
    private String companyName;

    @Transient
    private String companyNum;

    @ApiModelProperty(value = "竞价方向SSRC.SOURCE_AUCTION_DIRECTION(FORWARD/正向|REVERSE/反向)", example = "FORWARD")
    @Length(max = 30, message = "竞价方向最大长度30个字符")
    @LovValue(value = "SSRC.SOURCE_AUCTION_DIRECTION", meaningField = "auctionDirectionMeaning")
    private String auctionDirection;

    private Long copyRfxHeaderId;

    @Transient
    private String auctionDirectionMeaning;

    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "含税标识")
    @NotNull
    private Integer taxIncludedFlag;

    @ApiModelProperty(value = "税率ID")
    private Long taxId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "币种")
    @NotBlank
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    private Long exchangeRateId;

    @ApiModelProperty(value = "汇率类型")
    private String exchangeRateType;

    @ApiModelProperty(value = "汇率日期", example = "2017-12-27 00:00:00")
    private Date exchangeRateDate;

    @ApiModelProperty(value = "汇率期间")
    private String exchangeRatePeriod;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "备注")
    private String rfxRemark;

    @ApiModelProperty(value = "备注(内部)")
    private String internalRemark;

    @ApiModelProperty(value = "报价开始时间", example = "2017-12-29 00:00:00")
    private Date quotationStartDate;

    @ApiModelProperty(value = "报价截止时间", example = "2017-12-30 23:59:59")
    private Date quotationEndDate;

    @ApiModelProperty(value = "密封报价标志")
    @NotNull
    @LovValue(lovCode = "HPFM.FLAG", defaultMeaning = "sealedQuotationFlagMeaning")
    private Integer sealedQuotationFlag;
    @Transient
    private String sealedQuotationFlagMeaning;

    @ApiModelProperty(value = "公开规则SSRC.RFA_OPEN_RULE(HIDE_IDENTITY_HIDE_QUOTE/隐藏身份隐藏报价|HIDE_IDENTITY_OPEN_QUOTE/隐藏身份公开报价|OPEN_IDENTITY_HIDE_QUOTE/公开身份隐藏报价|OPEN_IDENTITY_OPEN_QUOTE/公开身份公开报价)", example = "HIDE_IDENTITY_HIDE_QUOTE")
    private String openRule;

    @ApiModelProperty(value = "竞价排名SSRC.RFA_AUCTION_RANKING(OPEN_COUNT_OPEN_RANK显示参与者数目和当前排名|OPEN_COUNT_HIDE_RANK/显示参与者数目隐藏当前排名|HIDE_COUNT_OPEN_RANK/隐藏参与者数目显示当前排名|HIDE_COUNT_HIDE_RANK/隐藏参与者数目和当前排名)", example = "OPEN_COUNT_OPEN_RANK")
    private String auctionRanking;

    @ApiModelProperty(value = "寻源类型SSRC.SOURCE_TYPE(常规|OEM|项目|外协|寄售)", example = "常规")
    @NotBlank
    private String sourceType;

    @ApiModelProperty(value = "价格类型SSRC.SOURCE_PRICE_CATEGORY(STANDARD/标准|SAMPLE/样品)", example = "STANDARD")
    @NotBlank
    private String priceCategory;

    @ApiModelProperty(value = "付款方式ID")
    private Long paymentTypeId;

    @ApiModelProperty(value = "付款条款")
    private Long paymentTermId;

    @ApiModelProperty(value = "轮次")
    @NotNull
    private Long roundNumber;

    @ApiModelProperty(value = "版本")
    @NotNull
    private Long versionNumber;

    @ApiModelProperty(value = "报价次序SSRC.QUOTATION_ORDER_TYPE(SEQUENCE/序列|STAGGER/交错|PARALLEL/并行)", example = "SEQUENCE")
    private String quotationOrderType;

    @ApiModelProperty(value = "竞价运行时间(RFA)")
    private BigDecimal quotationRunningDuration;


    @ApiModelProperty(value = "报价时间间隔")
    private BigDecimal quotationInterval;

    @ApiModelProperty(value = "竞价规则SSRC.RFA_AUCTION_RULE(NONE/所有排名允许报相同价格|ALL/所有排名不允许报相同价格|TOP_THREE前三名不允许报相同价格)", example = "NONE")
    private String auctionRule;

    @ApiModelProperty(value = "是否启用自动延时")
    @NotNull
    private Integer autoDeferFlag;

    @ApiModelProperty(value = "延时时长")
    private BigDecimal autoDeferDuration;

    @ApiModelProperty(value = "发布日期", example = "2017-12-31 00:00:00")
    private Date releasedDate;

    @ApiModelProperty(value = "发布人")
    private Long releasedBy;

    @ApiModelProperty(value = "终止时间", example = "2017-12-31 00:00:00")
    private Date terminatedDate;

    @ApiModelProperty(value = "终止人")
    private Long terminatedBy;

    @ApiModelProperty(value = "终止原因")
    private String terminatedRemark;

    @ApiModelProperty(value = "审批日期", example = "2017-12-31 00:00:00")
    private Date approvedDate;

    @ApiModelProperty(value = "审批人")
    private Long approvedBy;

    @ApiModelProperty(value = "审批备注")
    private String approvedRemark;

    @ApiModelProperty(value = "调整时间日期", example = "2017-12-31 00:00:00")
    private Date timeAdjustedDate;

    @ApiModelProperty(value = "调整时间人")
    private Long timeAdjustedBy;

    @ApiModelProperty(value = "调整时间原因")
    private String timeAdjustedRemark;

    @ApiModelProperty(value = "关闭标识")
    @NotNull
    private Integer closedFlag;

    @ApiModelProperty(value = "单据来源(MANUAL/手工创建|DEMAND_POOL/需求池|COPY/复制)", example = "MANUAL")
    @NotBlank
    private String sourceFrom;

    @ApiModelProperty(value = "发布即开始")
    private Integer startFlag;

    @ApiModelProperty(value = "发布即开始：报价运行时间(RFQ)")
    private BigDecimal startQuotationRunningDuration;


    @ApiModelProperty(value = "初审备注")
    private String pretrailRemark;

    @ApiModelProperty(value = "核价备注")
    private String checkRemark;

    @ApiModelProperty(value = "初审人")
    private Long pretrialUserId;

    @ApiModelProperty(value = "初审状态")
    private String pretrialStatus;

    @ApiModelProperty(value = "报价方式")
    @LovValue(value = "SSRC.QUOTATION_TYPE", meaningField = "quotationTypeMeaning")
    private String quotationType;
    @Transient
    private String quotationTypeMeaning;

    @ApiModelProperty(value = "退回至初审备注")
    private String backPretrialRemark;

    @ApiModelProperty(value = "总成本")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "成本备注")
    private String costRemark;

    @ApiModelProperty(value = "技术附件UUID")
    private String techAttachmentUuid;

    @ApiModelProperty(value = "商务附件UUID")
    private String businessAttachmentUuid;

    @ApiModelProperty(value = "预定标附件UUID")
    private String preAttachmentUuid;

    @ApiModelProperty(value = "核价附件UUID")
    private String checkAttachmentUuid;

    @ApiModelProperty(value = "报价范围")
    @LovValue(value = "SSRC.QUOTATION_SCOPE_CODE", meaningField = "quotationScopeMeaning")
    private String quotationScope;

    @ApiModelProperty(value = "需求部门ID")
    private Long unitId;

    @ApiModelProperty(value = "创建人部门ID")
    private Long createdUnitId;

    @ApiModelProperty(value = "创建人部门名称")
    private String createdUnitName;

    private Date latestQuotationEndDate;
    @ApiModelProperty(value = "采购人Id")
    private Long purchaserId;

    @ApiModelProperty(value = "可发起(INITIATE)/线上议价中(BARGAIN_ONLINE)/线下议价中(BARGAIN_OFFLINE)/关闭(CLOSE)")
    private String bargainStatus;

    @ApiModelProperty(value = "议价截止时间")
    private Date bargainEndDate;

    @ApiModelProperty(value = "议价附件")
    private String bargainAttachmentUuid;
    @ApiModelProperty(value = "是否集采标识")
    private Integer centralPurchaseFlag;

    @ApiModelProperty(value = "付款条件")
    private String paymentClause;

    @ApiModelProperty(value = "是否允许多币种报价")
    private Integer multiCurrencyFlag;
    @ApiModelProperty(value = "保证金")
    private BigDecimal bidBond;
    @ApiModelProperty(value = "标书费")
    private BigDecimal bidFileExpense;

    @ApiModelProperty(value = "初审附件")
    private String pretrialUuid;

    @ApiModelProperty(value = "核价员ID")
    private Long checkedBy;

    @ApiModelProperty(value = "最少报价供应商数")
    private Long minQuotedSupplier;

    @ApiModelProperty(value = "报价不足通知发送标识")
    private Integer lackQuotedSendFlag;

    @ApiModelProperty(value = "是否允许供应商修改付款条款&方式")
    private Integer paymentTermFlag;

    @ApiModelProperty(value = "事业部(宝龙达个性化)")
    private String ouBusiness;
    @ApiModelProperty(value = "库存分类(宝龙达个性化)")
    private String inventoryClassify;
    @ApiModelProperty(value = "物料类型(宝龙达个性化)")
    private String itemType;
    @ApiModelProperty(value = "维护完成率")
    private Long finishingRate;

    @Transient
    @ApiModelProperty(value = "初审转交人")
    private Long deliverUserId;
    @Transient
    @ApiModelProperty(value = "开标转交人")
    private Long openDeliverUserId;
    private Date handDownDate;
    @ApiModelProperty(value = "价格有效期")
    private Date priceEffectiveDate;
    @ApiModelProperty(value = "价格失效期")
    private Date priceExpiryDate;

    @ApiModelProperty(value = "寻源事项说明必需标志")
    private Integer matterRequireFlag;
    @ApiModelProperty(value = "寻源事项说明")
    private String matterDetail;
    @LovValue(value = "SSRC.RANK_RULE", meaningField = "rankRuleMeaning")
    private String rankRule;
    private Date checkFinishedDate;
    private String purName;
    private String purEmail;
    private String purPhone;
    @ApiModelProperty(value = "供应商升降价设置")
    private String quotationChange;
    private Integer scoreProcessFlag;//评分计算标志

    @ApiModelProperty(value = "预算总金额修改标志")
    private Integer budgetAmountFlag;
    @ApiModelProperty(value = "物料生成策略(0/无需|1/允许创建|2/允许补充)")
    private String itemGeneratePolicy;

    @ApiModelProperty(value = "(竞价)预计开始时间")
    private Date estimatedStartTime;
    @ApiModelProperty(value = "评分方式")
    private String scoreWay;
    private Long quotationRounds;
    @ApiModelProperty(value = "来源立项id")
    @Encrypt
    private Long sourceProjectId;
    @Encrypt
    private Long projectLineSectionId;
    @ApiModelProperty(value = "仅允许整单中标")
    private Integer onlyAllowAllWinBids;
    @Transient
    private String subjectMatterRule;

    @Transient
    private Integer roundQuotationRankFlag;
    @Transient
    private Long currentQuotationRound;
    @Transient
    private String roundQuotationRankRule;

    @Transient
    private String rankRuleMeaning;
    @Transient
    private String quotationScopeMeaning;
    @Transient
    @LovValue(lovCode = "HPFM.FLAG", defaultMeaning = "preQualificationFlagMeaning")
    private Integer preQualificationFlag;
    @Transient
    private String preQualificationFlagMeaning;
    @Transient
    private String expertScoreType;
    @Transient
    private List<RfxLineItem> rfxLineItemList;
    @Transient
    private List<RoundHeaderDate> roundHeaderDates;
    @Transient
    private List<RfxQuotationHeader> rfxQuotationHeaderList;
    @Transient
    private List<RfxQuotationLine> rfxQuotationLineList;
    @Transient
    private Integer matchRestrictFlag;
    @Transient
    private String bargainRule;
    @Transient
    private Integer bargainOfflineFlag;
    @Transient
    private Date currentDateTime;
    @Transient
    private BigDecimal totalPrice;
    @Transient
    private Integer bargainClosedFlag;
    @Transient
    private String scoringProgress;
    @Transient
    private String openBidOrder;
    @Transient
    private BigDecimal day;
    @Transient
    private BigDecimal hour;
    @Transient
    private BigDecimal minute;
    @Transient
    private Integer quotationFlag ;
    @Transient
    private String docCategory;
    @Transient
    private Date publishedDate;
    @Transient
    private String checkedByName;
    @Transient
    private String rfxByName;
    @Transient
    private Integer sendMethodFlag;
    @Transient
    private SourceTemplate sourceTemplate;
    @Transient
    private String scoreRptFileUrl;
    @Transient
    private String unitCode;
    @Transient
    private String unitName;
    @Transient
    private String createdUnitCode;
    @Transient
    private String createdByName;
    @ApiModelProperty(value = "能否修改报价截止时间标志：报价时间截止且未开标的密封报价单可以修改报价截止时间重新报价")
    @Transient
    private Integer quotationEndDateChangeFlag;
    @Transient
    @LovValue(lovCode = "HPFM.FLAG", defaultMeaning = "expertScoreFlagMeaning")
    private Integer expertScoreFlag;
    @Transient
    private String expertScoreFlagMeaning;
    @Transient
    private String priceTypeCode;
    @Transient
    @ApiModelProperty(value = "开启开标密码标识")
    private Integer passwordFlag;
    @Transient
    private int changeValue;
    @Transient
    private String changeDirection;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;
    //
    @Transient
    private RfxLineItem line;
    @Transient
    private String sectionCode;
    @Transient
    private String sectionName;
    @Transient
    private String sourceProjectNum;
    @Transient
    private String sourceProjectName;
    @Transient
    private String expertSource;
    @Transient
    private List<Long> itemList;
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 校验核价状态
     */
    public void validationCheckPriceStatus() {
        if (!SourceConstants.RfxStatus.IN_QUOTATION.equals(this.rfxStatus) &&
                !SourceConstants.RfxStatus.BARGAINING.equals(this.rfxStatus) &&
                !SourceConstants.RfxStatus.CHECK_PENDING.equals(this.rfxStatus) &&
                !SourceConstants.RfxStatus.CHECK_REJECTED.equals(this.rfxStatus)) {
            throw new CommonException(SourceConstants.ErrorCode.CHECK_PRICE_RFX_STATUS_ERROR);
        }
    }

    /**
     * 版本轮次校验
     */
    public void validationVersion(RfxHeader rfxHeaderParam) {
        if (!this.getObjectVersionNumber().equals(rfxHeaderParam.getObjectVersionNumber()) ||
                !this.roundNumber.equals(rfxHeaderParam.getRoundNumber())) {
            throw new CommonException(SourceConstants.ErrorCode.VERSION_OR_ROTATION_ERROR);
        }

    }

    /**
     * 校验询价单截止时间
     */
    public void validationQuotationEndDate() {
        if (ShareConstants.RoundHeaderStatus.ROUNDCHECKING.equals(this.getRoundHeaderStatus()) ||
                ShareConstants.RoundHeaderStatus.ROUNDSCORING.equals(this.getRoundHeaderStatus()) ||
                SourceConstants.BargainStatus.BARGAINING_ONLINE.equals(this.bargainStatus) ||
                SourceConstants.BargainStatus.BARGAIN_ONLINE.equals(this.bargainStatus)||
                SourceConstants.BargainStatus.BARGAINING_OFFLINE.equals(this.bargainStatus)) {
            return;
        }
        if (this.getQuotationEndDate() != null && new Date().compareTo(this.getQuotationEndDate()) > 0) {
            throw new CommonException(SourceConstants.ErrorCode.QUOTATION_END_ERROR);
        }
    }

    /**
     * 当询价头状态为暂停和报价中才可以参与或放弃
     */
    public void validationStatus() {
        if (!SourceConstants.RfxStatus.IN_QUOTATION.equals(this.rfxStatus) &&
                !SourceConstants.RfxStatus.PAUSED.equals(this.rfxStatus) && !SourceConstants.RfxStatus.NOT_START.equals(this.rfxStatus)) {
            throw new CommonException(SourceConstants.ErrorCode.CURRENT_STATUS_QUOTATION_ERROR);
        }
    }

    /**
     * 发布时状态校验，新建和发布审批拒绝状态才能发布
     *
     * @param rfxFullHeader
     * @param sourceTemplate 寻源模板
     */
    public void beforeReleaseCheck(RfxFullHeader rfxFullHeader, SourceTemplate sourceTemplate) {
//		RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
//		switch (rfxHeader.getRfxStatus()) {
//			case SourceConstants.RfxStatus.NEW:
//				break;
//			case SourceConstants.RfxStatus.RELEASE_APPROVING:
//				break;
//			case SourceConstants.RfxStatus.ROUNDED:
//				break;
//			case SourceConstants.RfxStatus.RELEASE_REJECTED:
//				break;
//			default:
//				throw new CommonException(SourceConstants.ErrorCode.ERROR_RELEASE_STATUS);
//		}
//		if (CollectionUtils.isEmpty(rfxFullHeader.getRfxLineItemList())) {
//			throw new CommonException(SourceConstants.ErrorCode.ERROR_ITEM_LINE_LEAST_ONE);
//		}
//		if (!rfxHeader.getRfxHeaderId().equals(rfxFullHeader.getRfxLineItemList().get(0).getRfxHeaderId())) {
//			throw new CommonException(SourceConstants.ErrorCode.ERROR_ITEM_LINE_LEAST_ONE);
//		}
//		this.checkVendorNumber(rfxFullHeader, sourceTemplate);
    }

    /**
     * 密封报价校验
     */
    public void sealedQuotationCheck() {
        if (ShareConstants.RoundHeaderStatus.ROUNDCHECKING.equals(this.getRoundHeaderStatus()) ||
                ShareConstants.RoundHeaderStatus.ROUNDSCORING.equals(this.getRoundHeaderStatus()) ||
                SourceConstants.BargainStatus.BARGAINING_ONLINE.equals(this.bargainStatus) ||
                SourceConstants.BargainStatus.BARGAIN_ONLINE.equals(this.bargainStatus)||
                SourceConstants.BargainStatus.BARGAINING_OFFLINE.equals(this.bargainStatus)) {
            return;
        }
        if (BaseConstants.Flag.YES.equals(this.getSealedQuotationFlag())) {
            throw new CommonException(SourceConstants.ErrorCode.ERROR_SEALED_QUOTATION);
        }
        this.validationQuotationEndDate();
    }

    /**
     * 发布设置报价中、发布人、发布日期
     *
     * @param resultApproveType
     * @return 询价单信息
     */
    public RfxHeader initRfxReleaseInfo(String resultApproveType) {
        this.releasedDate = new Date();
        this.releasedBy = DetailsHelper.getUserDetails().getUserId();
        if (ShareConstants.SourceTemplate.ReleaseApproveType.SELF.equals(resultApproveType)) {
            this.rfxStatus = SourceConstants.RfxStatus.IN_QUOTATION;
        } else {
            this.rfxStatus = SourceConstants.RfxStatus.RELEASE_APPROVING;
        }
        if (ShareConstants.SourceTemplate.ReleaseApproveType.SELF.equals(resultApproveType)) {
            this.approvedDate = this.releasedDate;
            this.approvedBy = DetailsHelper.getUserDetails().getUserId();
        }
        if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(this.sourceCategory)
                ||RcwlShareConstants.CategoryType.RCBJ.equals(this.sourceCategory)
                ||RcwlShareConstants.CategoryType.RCZB.equals(this.sourceCategory)
                ||RcwlShareConstants.CategoryType.RCZW.equals(this.sourceCategory))
                && Objects.nonNull(this.quotationStartDate) && Objects.nonNull(this.quotationEndDate)) {
            this.startQuotationRunningDuration = new BigDecimal((this.quotationEndDate.getTime() - this.quotationStartDate.getTime()) / 60000);
        }
        return this;
    }

    /**
     * 校验供应商数量范围
     *
     * @param rfxFullHeader
     * @param sourceTemplate 寻源模板
     */
    public void checkVendorNumber(RfxFullHeader rfxFullHeader, SourceTemplate sourceTemplate) {
        if (SourceConstants.RfxType.INVITE.equals(rfxFullHeader.getRfxHeader().getSourceMethod())) {
            long supplierSize = rfxFullHeader.getRfxLineSupplierList().size();
            this.validateVendorNumber(supplierSize, sourceTemplate.getMinVendorNumber(), sourceTemplate.getMaxVendorQuantity());
        }
    }

    /**
     * 校验供应商范围
     *
     * @param vendorNumber
     * @param minVendorNumber
     * @param maxVendorNumber
     */
    public void validateVendorNumber(Long vendorNumber, Long minVendorNumber, Long maxVendorNumber) {
        if (minVendorNumber != null && vendorNumber < minVendorNumber) {
            throw new CommonException(SourceConstants.ErrorCode.ERROR_QUOTATION_MIN_VENDOR_NUMBER, minVendorNumber);
        }
        if (maxVendorNumber != null && vendorNumber > maxVendorNumber) {
            throw new CommonException(SourceConstants.ErrorCode.ERROR_QUOTATION_MAX_VENDOR_NUMBER, maxVendorNumber);
        }
    }

    /**
     * 校验询价单状态
     * 实际展示状态为报价中的询价单才能报价，如果不为报价中就报错提示
     *
     * @param rfqHeaderStatus
     */
    public static void verifyRFXStatus(String rfqHeaderStatus) {

        if (StringUtils.isEmpty(rfqHeaderStatus)) {
            throw new CommonException(BaseConstants.ErrorCode.NOT_FOUND);
        }

        if (!SourceConstants.RfxStatus.IN_QUOTATION.equals(rfqHeaderStatus)) {
            throw new CommonException(SourceConstants.ErrorCode.INQUIRY_SHEET_IS_QUOTED_ONLY_IN_QUOTATION);
        }

    }

    /**
     * 清空竞价信息
     */
    public void clearAuctionRule() {
        this.openRule = null;
        this.auctionRule = null;
        this.auctionRanking = null;
        this.autoDeferFlag = BaseConstants.Digital.ZERO;
        this.autoDeferDuration = null;
        this.quotationRunningDuration = null;
        this.quotationInterval = null;
    }

    /**
     * 询价单再次询价，清空报价时间
     */
    public void cleanRfxQuotationDate() {
        this.quotationStartDate = null;
        this.quotationEndDate = null;
        this.quotationRunningDuration = null;
        this.startQuotationRunningDuration = null;
        this.timeAdjustedDate = null;
        this.handDownDate = null;
        this.latestQuotationEndDate = null;
    }

    /**
     * 初始化时间
     */
    public void initCurrentDate() {
        this.currentDateTime = new Date();
        if (SourceConstants.BargainStatus.BARGAINING_ONLINE.equals(this.bargainStatus)) {
            if (Objects.nonNull(this.bargainEndDate)) {
                this.bargainClosedFlag = DateUtil.beforeNow(this.bargainEndDate, null) ? BaseConstants.Flag.NO : BaseConstants.Flag.YES;
            } else {
                this.bargainClosedFlag = BaseConstants.Flag.YES;
            }
        } else {
            this.bargainClosedFlag = BaseConstants.Flag.YES;
        }
    }

    public void initScoringProgress() {
        if (SourceConstants.OpenBidOrder.SYNC.equals(this.openBidOrder)) {
            this.scoringProgress = ShareConstants.Expert.ExpertCategory.BUSINESS_TECHNOLOGY;
        } else if ((SourceConstants.OpenBidOrder.BUSINESS_FIRST.equals(this.openBidOrder)
                && BaseConstants.Digital.ONE == this.currentSequenceNum)
                || (SourceConstants.OpenBidOrder.TECH_FIRST.equals(this.openBidOrder)
                && BaseConstants.Digital.TWO == this.currentSequenceNum)) {
            this.scoringProgress = ShareConstants.Expert.ExpertCategory.BUSINESS;
        } else {
            this.scoringProgress = ShareConstants.Expert.ExpertCategory.TECHNOLOGY;
        }
    }

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    @NotNull(groups = Quotation.class)
    @Encrypt
    private Long supplierCompanyId;

    @Transient
    private String supplierCompanyName;

    @Transient
    private Long supplierCompanyTenantId;

    @Transient
    @NotBlank(groups = Abandon.class)
    private String abandonRemark;

    @Transient
    private String purOrganizationName;

    @Transient
    private String templateName;

    @Transient
    private String templateNum;

    @Transient
    private String processStatus;

    @Transient
    private Integer openerFlag;

    @Transient
    private Integer openedFlag;

    @Transient
    private String sourceCategoryMeaning;
    @Transient
    private Long prequalHeaderId;
    @Transient
    private Date prequalEndDate;
    @Transient
    private String qualificationType;
    @Transient
    private String prequalStatus;
    @Transient
    private Integer pretrialFlag;
    @Transient
    @Encrypt
    private Long quotationHeaderId;
    @Transient
    private String serverName;
    @Transient
    private BigDecimal quotationQuantity;
    @Transient
    private String supplierName;
    @Transient
    private Long supplierTenantId ;
    @Transient
    private Integer fastBidding;
    @Transient
    @ApiModelProperty(value = "议价阶段")
    private String bargainingStage;
    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public String getBargainingStage() {
        return bargainingStage;
    }

    public void setBargainingStage(String bargainingStage) {
        this.bargainingStage = bargainingStage;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getFastBidding() {
        return fastBidding;
    }

    public void setFastBidding(Integer fastBidding) {
        this.fastBidding = fastBidding;
    }


    public BigDecimal getBusinessWeight() {
        return businessWeight;
    }

    public void setBusinessWeight(BigDecimal businessWeight) {
        this.businessWeight = businessWeight;
    }

    public BigDecimal getTechnologyWeight() {
        return technologyWeight;
    }

    public void setTechnologyWeight(BigDecimal technologyWeight) {
        this.technologyWeight = technologyWeight;
    }

    public int getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(int changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeDirection() {
        return changeDirection;
    }

    public void setChangeDirection(String changeDirection) {
        this.changeDirection = changeDirection;
    }

    public String getQuotationChange() {
        return quotationChange;
    }

    public void setQuotationChange(String quotationChange) {
        this.quotationChange = quotationChange;
    }

    public String getItemGeneratePolicy() {
        return itemGeneratePolicy;
    }

    public void setItemGeneratePolicy(String itemGeneratePolicy) {
        this.itemGeneratePolicy = itemGeneratePolicy;
    }

    public String getRankRule() {
        return rankRule;
    }

    public void setRankRule(String rankRule) {
        this.rankRule = rankRule;
    }

    public String getRankRuleMeaning() {
        return rankRuleMeaning;
    }

    public void setRankRuleMeaning(String rankRuleMeaning) {
        this.rankRuleMeaning = rankRuleMeaning;
    }

    public Date getCheckFinishedDate() {
        return checkFinishedDate;
    }

    public void setCheckFinishedDate(Date checkFinishedDate) {
        this.checkFinishedDate = checkFinishedDate;
    }

    public String getPurName() {
        return purName;
    }

    public void setPurName(String purName) {
        this.purName = purName;
    }

    public String getPurEmail() {
        return purEmail;
    }

    public void setPurEmail(String purEmail) {
        this.purEmail = purEmail;
    }

    public String getPurPhone() {
        return purPhone;
    }

    public void setPurPhone(String purPhone) {
        this.purPhone = purPhone;
    }

    public SourceTemplate getSourceTemplate() {
        return sourceTemplate;
    }

    public void setSourceTemplate(SourceTemplate sourceTemplate) {
        this.sourceTemplate = sourceTemplate;
    }

    public String getOuBusiness() {
        return ouBusiness;
    }

    public void setOuBusiness(String ouBusiness) {
        this.ouBusiness = ouBusiness;
    }

    public String getInventoryClassify() {
        return inventoryClassify;
    }

    public void setInventoryClassify(String inventoryClassify) {
        this.inventoryClassify = inventoryClassify;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(String docCategory) {
        this.docCategory = docCategory;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getSupplierTenantId() {
        return supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public String getPretrialUuid() {
        return pretrialUuid;
    }

    public void setPretrialUuid(String pretrialUuid) {
        this.pretrialUuid = pretrialUuid;
    }

    public Long getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(Long purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getServerName() {
        return serverName;
    }

    public String getQuotationScopeMeaning() {
        return quotationScopeMeaning;
    }

    public void setQuotationScopeMeaning(String quotationScopeMeaning) {
        this.quotationScopeMeaning = quotationScopeMeaning;
    }

    public Long getQuotationHeaderId() {
        return quotationHeaderId;
    }

    public void setQuotationHeaderId(Long quotationHeaderId) {
        this.quotationHeaderId = quotationHeaderId;
    }

    public Integer getMatchRestrictFlag() {
        return matchRestrictFlag;
    }

    public void setMatchRestrictFlag(Integer matchRestrictFlag) {
        this.matchRestrictFlag = matchRestrictFlag;
    }

    public String getRoundHeaderStatus() {
        return roundHeaderStatus;
    }

    public void setRoundHeaderStatus(String roundHeaderStatus) {
        this.roundHeaderStatus = roundHeaderStatus;
    }

    public String getRoundQuotationRule() {
        return roundQuotationRule;
    }

    public void setRoundQuotationRule(String roundQuotationRule) {
        this.roundQuotationRule = roundQuotationRule;
    }

    public Date getRoundQuotationEndDate() {
        return roundQuotationEndDate;
    }

    public void setRoundQuotationEndDate(Date roundQuotationEndDate) {
        this.roundQuotationEndDate = roundQuotationEndDate;
    }

    public Long getQuotationRoundNumber() {
        return quotationRoundNumber;
    }

    public void setQuotationRoundNumber(Long quotationRoundNumber) {
        this.quotationRoundNumber = quotationRoundNumber;
    }

    public Date getHandDownDate() {
        return handDownDate;
    }

    public void setHandDownDate(Date handDownDate) {
        this.handDownDate = handDownDate;
    }

    public String getExpertScoreType() {
        return expertScoreType;
    }

    public void setExpertScoreType(String expertScoreType) {
        this.expertScoreType = expertScoreType;
    }

    public String getAuctionDirectionMeaning() {
        return auctionDirectionMeaning;
    }

    public void setAuctionDirectionMeaning(String auctionDirectionMeaning) {
        this.auctionDirectionMeaning = auctionDirectionMeaning;
    }

    public Date getPriceEffectiveDate() {
        return priceEffectiveDate;
    }

    public void setPriceEffectiveDate(Date priceEffectiveDate) {
        this.priceEffectiveDate = priceEffectiveDate;
    }

    public Date getPriceExpiryDate() {
        return priceExpiryDate;
    }

    public void setPriceExpiryDate(Date priceExpiryDate) {
        this.priceExpiryDate = priceExpiryDate;
    }

    public String getSourceMethodMeaning() {
        return sourceMethodMeaning;
    }

    public void setSourceMethodMeaning(String sourceMethodMeaning) {
        this.sourceMethodMeaning = sourceMethodMeaning;
    }

    public String getSourceCategoryMeaning() {
        return sourceCategoryMeaning;
    }

    public void setSourceCategoryMeaning(String sourceCategoryMeaning) {
        this.sourceCategoryMeaning = sourceCategoryMeaning;
    }

    public Integer getPretrialFlag() {
        return pretrialFlag;
    }

    public void setPretrialFlag(Integer pretrialFlag) {
        this.pretrialFlag = pretrialFlag;
    }

    public String getPrequalStatus() {
        return prequalStatus;
    }

    public void setPrequalStatus(String prequalStatus) {
        this.prequalStatus = prequalStatus;
    }

    public Integer getOpenedFlag() {
        return openedFlag;
    }

    public void setOpenedFlag(Integer openedFlag) {
        this.openedFlag = openedFlag;
    }

    public Integer getOpenerFlag() {
        return openerFlag;
    }

    public void setOpenerFlag(Integer openerFlag) {
        this.openerFlag = openerFlag;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getPurOrganizationName() {
        return purOrganizationName;
    }

    public void setPurOrganizationName(String purOrganizationName) {
        this.purOrganizationName = purOrganizationName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getAbandonRemark() {
        return abandonRemark;
    }

    public void setAbandonRemark(String abandonRemark) {
        this.abandonRemark = abandonRemark;
    }

    public Long getSupplierCompanyId() {
        return supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public Long getSupplierCompanyTenantId() {
        return supplierCompanyTenantId;
    }

    public void setSupplierCompanyTenantId(Long supplierCompanyTenantId) {
        this.supplierCompanyTenantId = supplierCompanyTenantId;
    }

    public List<RoundHeaderDate> getRoundHeaderDates() {
        return roundHeaderDates;
    }

    public void setRoundHeaderDates(List<RoundHeaderDate> roundHeaderDates) {
        this.roundHeaderDates = roundHeaderDates;
    }

    public Long getSourceProjectId() {
        return sourceProjectId;
    }

    public void setSourceProjectId(Long sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public Long getProjectLineSectionId() {
        return projectLineSectionId;
    }

    public void setProjectLineSectionId(Long projectLineSectionId) {
        this.projectLineSectionId = projectLineSectionId;
    }

    public String getSubjectMatterRule() {
        return subjectMatterRule;
    }

    public void setSubjectMatterRule(String subjectMatterRule) {
        this.subjectMatterRule = subjectMatterRule;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSourceProjectNum() {
        return sourceProjectNum;
    }

    public void setSourceProjectNum(String sourceProjectNum) {
        this.sourceProjectNum = sourceProjectNum;
    }

    public String getSourceProjectName() {
        return sourceProjectName;
    }

    public void setSourceProjectName(String sourceProjectName) {
        this.sourceProjectName = sourceProjectName;
    }

    public List<Long> getItemList() {
        return itemList;
    }

    public void setItemList(List<Long> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getRfxHeaderId() {
        return rfxHeaderId;
    }

    public void setRfxHeaderId(Long rfxHeaderId) {
        this.rfxHeaderId = rfxHeaderId;
    }

    /**
     * @return 所属租户ID，hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 询价单单号
     */
    public String getRfxNum() {
        return rfxNum;
    }

    public void setRfxNum(String rfxNum) {
        this.rfxNum = rfxNum;
    }

    /**
     * @return 询价单状态SourceConstants.RfxStatus(NEW / 新建 | RELEASE_APPROVING / 发布审批中 | RELEASE_REJECTED / 发布审批拒绝 | NOT_START / 未开始 | IN_PREQUAL / 资格预审中 | PREQUAL_CUTOFF / 资格预审截止 | IN_QUOTATION / 报价中 | OPEN_BID_PENDING / 待开标 | PRETRIAL_PENDING / 待初审 | SCORING / 评分中 | CHECK_PENDING / 待核价 | CHECK_APPROVING / 核价审批中 | CHECK_REJECTED / 核价审批拒绝 | FINISHED / 完成 | PAUSED / 暂停 | CLOSED / 关闭 | ROUNDED / 再次询价 | IN_POSTQUAL / 资格后审中 | POSTQUAL_CUTOFF / 资格后审截止)
     */
    public String getRfxStatus() {
        return rfxStatus;
    }

    public void setRfxStatus(String rfxStatus) {
        this.rfxStatus = rfxStatus;
    }

    public BigDecimal getStartQuotationRunningDuration() {
        return startQuotationRunningDuration;
    }

    public void setStartQuotationRunningDuration(BigDecimal startQuotationRunningDuration) {
        this.startQuotationRunningDuration = startQuotationRunningDuration;
    }

    /**
     * @return 询价单标题
     */
    public String getRfxTitle() {
        return rfxTitle;
    }

    public void setRfxTitle(String rfxTitle) {
        this.rfxTitle = rfxTitle;
    }

    /**
     * @return 寻源模板ID
     */
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    /**
     * @return 寻源类别SSRC.SOURCE_CATEGORY(RFQ / 询价 | RFA / 竞价 | BID / 招投标)
     */
    public String getSourceCategory() {
        return sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }

    /**
     * @return 询价方式SSRC.SOURCE_METHOD(INVITE / 邀请 | OPEN / 合作伙伴公开 | ALL_OPEN / 全平台公开)
     */
    public String getSourceMethod() {
        return sourceMethod;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    /**
     * @return 采购方采购组织ID
     */
    public Long getPurOrganizationId() {
        return purOrganizationId;
    }

    public void setPurOrganizationId(Long purOrganizationId) {
        this.purOrganizationId = purOrganizationId;
    }

    public Integer getCentralPurchaseFlag() {
        return centralPurchaseFlag;
    }

    public void setCentralPurchaseFlag(Integer centralPurchaseFlag) {
        this.centralPurchaseFlag = centralPurchaseFlag;
    }

    /**
     * @return 采购方企业ID
     */
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return 采购方企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return 竞价方向SSRC.SOURCE_AUCTION_DIRECTION(FORWARD / 正向 | REVERSE / 反向)
     */
    public String getAuctionDirection() {
        return auctionDirection;
    }

    public void setAuctionDirection(String auctionDirection) {
        this.auctionDirection = auctionDirection;
    }

    /**
     * @return 预算金额
     */
    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    /**
     * @return 含税标识
     */
    public Integer getTaxIncludedFlag() {
        return taxIncludedFlag;
    }

    public void setTaxIncludedFlag(Integer taxIncludedFlag) {
        this.taxIncludedFlag = taxIncludedFlag;
    }

    /**
     * @return 税率ID
     */
    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    /**
     * @return 税率
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return 币种
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return 汇率
     */
    public Long getExchangeRateId() {
        return exchangeRateId;
    }

    public void setExchangeRateId(Long exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }

    /**
     * @return 汇率类型
     */
    public String getExchangeRateType() {
        return exchangeRateType;
    }

    public void setExchangeRateType(String exchangeRateType) {
        this.exchangeRateType = exchangeRateType;
    }

    /**
     * @return 汇率日期
     */
    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getQuotationType() {
        return quotationType;
    }

    public void setQuotationType(String quotationType) {
        this.quotationType = quotationType;
    }

    public String getQuotationTypeMeaning() {
        return quotationTypeMeaning;
    }

    public void setQuotationTypeMeaning(String quotationTypeMeaning) {
        this.quotationTypeMeaning = quotationTypeMeaning;
    }

    public BigDecimal getBidBond() {
        return bidBond;
    }

    public void setBidBond(BigDecimal bidBond) {
        this.bidBond = bidBond;
    }

    public BigDecimal getBidFileExpense() {
        return bidFileExpense;
    }

    public void setBidFileExpense(BigDecimal bidFileExpense) {
        this.bidFileExpense = bidFileExpense;
    }

    /**
     * @return 汇率期间
     */
    public String getExchangeRatePeriod() {
        return exchangeRatePeriod;
    }

    public void setExchangeRatePeriod(String exchangeRatePeriod) {
        this.exchangeRatePeriod = exchangeRatePeriod;
    }

    /**
     * @return 汇率
     */
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * @return 备注
     */
    public String getRfxRemark() {
        return rfxRemark;
    }

    public void setRfxRemark(String rfxRemark) {
        this.rfxRemark = rfxRemark;
    }

    /**
     * @return 报价开始时间
     */
    public Date getQuotationStartDate() {
        return quotationStartDate;
    }

    public void setQuotationStartDate(Date quotationStartDate) {
        this.quotationStartDate = quotationStartDate;
    }

    public Date getLatestQuotationEndDate() {
        return latestQuotationEndDate;
    }

    public void setLatestQuotationEndDate(Date latestQuotationEndDate) {
        this.latestQuotationEndDate = latestQuotationEndDate;
    }

    /**
     * @return 报价截止时间
     */
    public Date getQuotationEndDate() {
        return quotationEndDate;
    }

    public void setQuotationEndDate(Date quotationEndDate) {
        this.quotationEndDate = quotationEndDate;
    }

    /**
     * @return 密封报价标志
     */
    public Integer getSealedQuotationFlag() {
        return sealedQuotationFlag;
    }

    public void setSealedQuotationFlag(Integer sealedQuotationFlag) {
        this.sealedQuotationFlag = sealedQuotationFlag;
    }

    public String getSealedQuotationFlagMeaning() {
        return sealedQuotationFlagMeaning;
    }

    public void setSealedQuotationFlagMeaning(String sealedQuotationFlagMeaning) {
        this.sealedQuotationFlagMeaning = sealedQuotationFlagMeaning;
    }

    /**
     * @return 公开规则SSRC.RFA_OPEN_RULE(HIDE_IDENTITY_HIDE_QUOTE / 隐藏身份隐藏报价 | HIDE_IDENTITY_OPEN_QUOTE / 隐藏身份公开报价 | OPEN_IDENTITY_HIDE_QUOTE / 公开身份隐藏报价 | OPEN_IDENTITY_OPEN_QUOTE / 公开身份公开报价)
     */
    public String getOpenRule() {
        return openRule;
    }

    public void setOpenRule(String openRule) {
        this.openRule = openRule;
    }

    /**
     * @return 竞价排名SSRC.RFA_AUCTION_RANKING(OPEN_COUNT_OPEN_RANK显示参与者数目和当前排名 | OPEN_COUNT_HIDE_RANK / 显示参与者数目隐藏当前排名 | HIDE_COUNT_OPEN_RANK / 隐藏参与者数目显示当前排名 | HIDE_COUNT_HIDE_RANK / 隐藏参与者数目和当前排名)
     */
    public String getAuctionRanking() {
        return auctionRanking;
    }

    public void setAuctionRanking(String auctionRanking) {
        this.auctionRanking = auctionRanking;
    }

    /**
     * @return 寻源类型SSRC.SOURCE_TYPE(常规 | OEM | 项目 | 外协 | 寄售)
     */
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * @return 价格类型SSRC.SOURCE_PRICE_CATEGORY(STANDARD / 标准 | SAMPLE / 样品)
     */
    public String getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(String priceCategory) {
        this.priceCategory = priceCategory;
    }

    /**
     * @return 付款方式ID
     */
    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    /**
     * @return 付款条款
     */
    public Long getPaymentTermId() {
        return paymentTermId;
    }

    public void setPaymentTermId(Long paymentTermId) {
        this.paymentTermId = paymentTermId;
    }

    /**
     * @return 轮次
     */
    public Long getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Long roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * @return 版本
     */
    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * @return 报价次序SSRC.QUOTATION_ORDER_TYPE(SEQUENCE / 序列 | STAGGER / 交错 | PARALLEL / 并行)
     */
    public String getQuotationOrderType() {
        return quotationOrderType;
    }

    public void setQuotationOrderType(String quotationOrderType) {
        this.quotationOrderType = quotationOrderType;
    }

    public String getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
    }

    /**
     * @return 报价运行时间
     */
    public BigDecimal getQuotationRunningDuration() {
        return quotationRunningDuration;
    }

    public void setQuotationRunningDuration(BigDecimal quotationRunningDuration) {
        this.quotationRunningDuration = quotationRunningDuration;
    }

    /**
     * @return 报价时间间隔
     */
    public BigDecimal getQuotationInterval() {
        return quotationInterval;
    }

    public void setQuotationInterval(BigDecimal quotationInterval) {
        this.quotationInterval = quotationInterval;
    }

    /**
     * @return 竞价规则SSRC.RFA_AUCTION_RULE(NONE / 所有排名允许报相同价格 | ALL / 所有排名不允许报相同价格 | TOP_THREE前三名不允许报相同价格)
     */
    public String getAuctionRule() {
        return auctionRule;
    }

    public void setAuctionRule(String auctionRule) {
        this.auctionRule = auctionRule;
    }

    /**
     * @return 是否启用自动延时
     */
    public Integer getAutoDeferFlag() {
        return autoDeferFlag;
    }

    public void setAutoDeferFlag(Integer autoDeferFlag) {
        this.autoDeferFlag = autoDeferFlag;
    }

    /**
     * @return 延时时长
     */
    public BigDecimal getAutoDeferDuration() {
        return autoDeferDuration;
    }

    public void setAutoDeferDuration(BigDecimal autoDeferDuration) {
        this.autoDeferDuration = autoDeferDuration;
    }

    /**
     * @return 发布日期
     */
    public Date getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    /**
     * @return 发布人
     */
    public Long getReleasedBy() {
        return releasedBy;
    }

    public void setReleasedBy(Long releasedBy) {
        this.releasedBy = releasedBy;
    }

    /**
     * @return 终止时间
     */
    public Date getTerminatedDate() {
        return terminatedDate;
    }

    public void setTerminatedDate(Date terminatedDate) {
        this.terminatedDate = terminatedDate;
    }

    public Long getPrequalHeaderId() {
        return prequalHeaderId;
    }

    public void setPrequalHeaderId(Long prequalHeaderId) {
        this.prequalHeaderId = prequalHeaderId;
    }

    public Date getPrequalEndDate() {
        return prequalEndDate;
    }

    public void setPrequalEndDate(Date prequalEndDate) {
        this.prequalEndDate = prequalEndDate;
    }

    /**
     * @return 终止人
     */
    public Long getTerminatedBy() {
        return terminatedBy;
    }

    public void setTerminatedBy(Long terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    /**
     * @return 终止原因
     */
    public String getTerminatedRemark() {
        return terminatedRemark;
    }

    public void setTerminatedRemark(String terminatedRemark) {
        this.terminatedRemark = terminatedRemark;
    }

    /**
     * @return 审批日期
     */
    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }


    public Integer getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(Integer startFlag) {
        this.startFlag = startFlag;
    }

    /**
     * @return 审批人
     */
    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    /**
     * @return 审批备注
     */
    public String getApprovedRemark() {
        return approvedRemark;
    }

    public void setApprovedRemark(String approvedRemark) {
        this.approvedRemark = approvedRemark;
    }

    /**
     * @return 调整时间日期
     */
    public Date getTimeAdjustedDate() {
        return timeAdjustedDate;
    }

    public void setTimeAdjustedDate(Date timeAdjustedDate) {
        this.timeAdjustedDate = timeAdjustedDate;
    }

    /**
     * @return 调整时间人
     */
    public Long getTimeAdjustedBy() {
        return timeAdjustedBy;
    }

    public void setTimeAdjustedBy(Long timeAdjustedBy) {
        this.timeAdjustedBy = timeAdjustedBy;
    }

    /**
     * @return 调整时间原因
     */
    public String getTimeAdjustedRemark() {
        return timeAdjustedRemark;
    }

    public void setTimeAdjustedRemark(String timeAdjustedRemark) {
        this.timeAdjustedRemark = timeAdjustedRemark;
    }

    /**
     * @return 关闭标识
     */
    public Integer getClosedFlag() {
        return closedFlag;
    }

    public void setClosedFlag(Integer closedFlag) {
        this.closedFlag = closedFlag;
    }

    /**
     * @return 单据来源(MANUAL / 手工创建 | DEMAND_POOL / 需求池 | COPY / 复制)
     */
    public String getSourceFrom() {
        return sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    /**
     * @return 初审备注
     */
    public String getPretrailRemark() {
        return pretrailRemark;
    }

    public void setPretrailRemark(String pretrailRemark) {
        this.pretrailRemark = pretrailRemark;
    }

    /**
     * @return 总成本
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return 成本备注
     */
    public String getCostRemark() {
        return costRemark;
    }

    public void setCostRemark(String costRemark) {
        this.costRemark = costRemark;
    }


    /**
     * @return 退回至初审备注
     */
    public String getBackPretrialRemark() {
        return backPretrialRemark;
    }

    public void setBackPretrialRemark(String backPretrialRemark) {
        this.backPretrialRemark = backPretrialRemark;
    }

    /**
     * @return 技术附件UUID
     */
    public String getTechAttachmentUuid() {
        return techAttachmentUuid;
    }

    public void setTechAttachmentUuid(String techAttachmentUuid) {
        this.techAttachmentUuid = techAttachmentUuid;
    }

    /**
     * @return 商务附件UUID
     */
    public String getBusinessAttachmentUuid() {
        return businessAttachmentUuid;
    }

    public void setBusinessAttachmentUuid(String businessAttachmentUuid) {
        this.businessAttachmentUuid = businessAttachmentUuid;
    }

    /**
     * @return 核价附件UUID
     */
    public String getCheckAttachmentUuid() {
        return checkAttachmentUuid;
    }

    public void setCheckAttachmentUuid(String checkAttachmentUuid) {
        this.checkAttachmentUuid = checkAttachmentUuid;
    }

    /**
     * 初审人
     *
     * @return
     */
    public Long getPretrialUserId() {
        return pretrialUserId;
    }

    public void setPretrialUserId(Long pretrialUserId) {
        this.pretrialUserId = pretrialUserId;
    }

    /**
     * 初审状态
     *
     * @return
     */
    public String getPretrialStatus() {
        return pretrialStatus;
    }

    public void setPretrialStatus(String pretrialStatus) {
        this.pretrialStatus = pretrialStatus;
    }

    /**
     * 初审转交人
     *
     * @return
     */
    public Long getDeliverUserId() {
        return deliverUserId;
    }

    public void setDeliverUserId(Long deliverUserId) {
        this.deliverUserId = deliverUserId;
    }

    public Long getOpenDeliverUserId() {
        return openDeliverUserId;
    }

    public void setOpenDeliverUserId(Long openDeliverUserId) {
        this.openDeliverUserId = openDeliverUserId;
    }

    public Integer getCurrentSequenceNum() {
        return currentSequenceNum;
    }

    public void setCurrentSequenceNum(Integer currentSequenceNum) {
        this.currentSequenceNum = currentSequenceNum;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCreatedUnitId() {
        return createdUnitId;
    }

    public void setCreatedUnitId(Long createdUnitId) {
        this.createdUnitId = createdUnitId;
    }

    public String getCreatedUnitName() {
        return createdUnitName;
    }

    public void setCreatedUnitName(String createdUnitName) {
        this.createdUnitName = createdUnitName;
    }

    public Long getCurrentQuotationRound() {
        return currentQuotationRound;
    }

    public void setCurrentQuotationRound(Long currentQuotationRound) {
        this.currentQuotationRound = currentQuotationRound;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getExpertScoreFlag() {
        return expertScoreFlag;
    }

    public void setExpertScoreFlag(Integer expertScoreFlag) {
        this.expertScoreFlag = expertScoreFlag;
    }

    public String getPreQualificationFlagMeaning() {
        return preQualificationFlagMeaning;
    }

    public void setPreQualificationFlagMeaning(String preQualificationFlagMeaning) {
        this.preQualificationFlagMeaning = preQualificationFlagMeaning;
    }

    public String getExpertScoreFlagMeaning() {
        return expertScoreFlagMeaning;
    }

    public void setExpertScoreFlagMeaning(String expertScoreFlagMeaning) {
        this.expertScoreFlagMeaning = expertScoreFlagMeaning;
    }

    /***
     * 再次询价,初始化询价单头数据
     */
    public void initAgainInquiryStataus() {
        this.versionNumber++;
        this.setRfxStatus(SourceConstants.RfxStatus.ROUNDED);
        this.roundNumber++;
        // 如果有初审，则重置初审状态
        if (this.pretrialStatus != null) {
            this.pretrialStatus = SourceConstants.PretrialStatus.NO_TRIAL;
        }
        //重置当前评分序列
        this.currentSequenceNum = BaseConstants.Digital.ONE;
    }

    public List<RfxLineItem> getRfxLineItemList() {
        return rfxLineItemList;
    }


    public String getPriceTypeCode() {
        return priceTypeCode;
    }

    public void setPriceTypeCode(String priceTypeCode) {
        this.priceTypeCode = priceTypeCode;
    }

    public void setRfxLineItemList(List<RfxLineItem> rfxLineItemList) {
        this.rfxLineItemList = rfxLineItemList;
    }

    public Integer getPreQualificationFlag() {
        return preQualificationFlag;
    }

    public void setPreQualificationFlag(Integer preQualificationFlag) {
        this.preQualificationFlag = preQualificationFlag;
    }

    public String getPreAttachmentUuid() {
        return preAttachmentUuid;
    }

    public void setPreAttachmentUuid(String preAttachmentUuid) {
        this.preAttachmentUuid = preAttachmentUuid;
    }

    public List<RfxQuotationHeader> getRfxQuotationHeaderList() {
        return rfxQuotationHeaderList;
    }

    public void setRfxQuotationHeaderList(List<RfxQuotationHeader> rfxQuotationHeaderList) {
        this.rfxQuotationHeaderList = rfxQuotationHeaderList;
    }

    public List<RfxQuotationLine> getRfxQuotationLineList() {
        return rfxQuotationLineList;
    }

    public void setRfxQuotationLineList(List<RfxQuotationLine> rfxQuotationLineList) {
        this.rfxQuotationLineList = rfxQuotationLineList;
    }

    public String getQuotationScope() {
        return quotationScope;
    }

    public void setQuotationScope(String quotationScope) {
        this.quotationScope = quotationScope;
    }

    public String getBargainStatus() {
        return bargainStatus;
    }

    public void setBargainStatus(String bargainStatus) {
        this.bargainStatus = bargainStatus;
    }

    public String getBargainRule() {
        return bargainRule;
    }

    public void setBargainRule(String bargainRule) {
        this.bargainRule = bargainRule;
    }

    public Integer getBargainOfflineFlag() {
        return bargainOfflineFlag;
    }

    public void setBargainOfflineFlag(Integer bargainOfflineFlag) {
        this.bargainOfflineFlag = bargainOfflineFlag;
    }

    public Date getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(Date currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public Integer getBargainClosedFlag() {
        return bargainClosedFlag;
    }

    public void setBargainClosedFlag(Integer bargainClosedFlag) {
        this.bargainClosedFlag = bargainClosedFlag;
    }

    public Date getBargainEndDate() {
        return bargainEndDate;
    }
    public BigDecimal getDay() {
        return day;
    }

    public void setDay(BigDecimal day) {
        this.day = day;
    }

    public BigDecimal getHour() {
        return hour;
    }

    public void setHour(BigDecimal hour) {
        this.hour = hour;
    }

    public void setBargainEndDate(Date bargainEndDate) {
        this.bargainEndDate = bargainEndDate;
    }

    public String getOpenBidOrder() {
        return openBidOrder;
    }

    public void setOpenBidOrder(String openBidOrder) {
        this.openBidOrder = openBidOrder;
    }

    public String getScoringProgress() {
        return scoringProgress;
    }

    public void setScoringProgress(String scoringProgress) {
        this.scoringProgress = scoringProgress;
    }
    public BigDecimal getMinute() {
        return minute;
    }

    public void setMinute(BigDecimal minute) {
        this.minute = minute;
    }

    public String getBargainAttachmentUuid() {
        return bargainAttachmentUuid;
    }

    public void setBargainAttachmentUuid(String bargainAttachmentUuid) {
        this.bargainAttachmentUuid = bargainAttachmentUuid;
    }

    public Integer getMultiCurrencyFlag() {
        return multiCurrencyFlag;
    }

    public void setMultiCurrencyFlag(Integer multiCurrencyFlag) {
        this.multiCurrencyFlag = multiCurrencyFlag;
    }

    public Long getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(Long checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getCheckedByName() {
        return checkedByName;
    }

    public void setCheckedByName(String checkedByName) {
        this.checkedByName = checkedByName;
    }

    public Long getQuotationRounds() {
        return quotationRounds;
    }

    public void setQuotationRounds(Long quotationRounds) {
        this.quotationRounds = quotationRounds;
    }

    public Integer getRoundQuotationRankFlag() {
        return roundQuotationRankFlag;
    }

    public void setRoundQuotationRankFlag(Integer roundQuotationRankFlag) {
        this.roundQuotationRankFlag = roundQuotationRankFlag;
    }

    public String getRoundQuotationRankRule() {
        return roundQuotationRankRule;
    }

    public void setRoundQuotationRankRule(String roundQuotationRankRule) {
        this.roundQuotationRankRule = roundQuotationRankRule;
    }

    /**
     *  立项转询价初始化
     */
    public void initFromProject(Long organizationId, SourceProjectDTO sourceProjectDTO, SourceTemplate sourceTemplate){
        if(sourceProjectDTO == null || sourceTemplate == null){
            return;
        }
        this.tenantId = organizationId;
        // 模板id
        this.templateId = sourceProjectDTO.getTemplateId();
        // 项目名称
//		this.rfxTitle = sourceProjectDTO.getSourceProjectName();
        // 公司
        this.companyId = sourceProjectDTO.getCompanyId();
        this.companyName = sourceProjectDTO.getCompanyName();
        // 寻源方式
        this.sourceMethod = sourceProjectDTO.getSourceMethod();
        // 预算总额
        this.budgetAmount = sourceProjectDTO.getBudgetAmount();
        //设置币种默认值
        this.setCurrencyCode(SourceConstants.RfxConstants.CURRENCY_CODE_DEFAULT);
        // 需求部门
        this.unitId = sourceProjectDTO.getUnitId();
        // 保证金
        this.bidBond = sourceProjectDTO.getDepositAmount();
        // 付款方式
        this.paymentTypeId = sourceProjectDTO.getPaymentTypeId();
        // 付款条款
        this.paymentTermId = sourceProjectDTO.getPaymentTermId();
        this.timeAdjustedBy = 1L;
        this.rfxStatus = SourceConstants.RfxStatus.NEW;
        this.sourceType = sourceTemplate.getSourceType();
        this.priceCategory = ShareConstants.SourceTemplate.SourcePriceCategory.STANDARD;
        this.sourceFrom = SourceConstants.SourceFrom.PROJECT;
        this.sourceCategory = sourceTemplate.getSourceCategory();
        this.quotationType = sourceTemplate.getQuotationType();
        this.quotationScope = sourceTemplate.getQuotationScope();
        this.auctionDirection = sourceTemplate.getAuctionDirection();
        this.quotationScope = sourceTemplate.getQuotationScope();
        this.bidFileExpense = new BigDecimal("0");
        this.onlyAllowAllWinBids = sourceTemplate.getOnlyAllowAllWinBids();
        this.subjectMatterRule = sourceProjectDTO.getSubjectMatterRule();
        this.sourceProjectId = sourceProjectDTO.getSourceProjectId();
        if (ShareConstants.SubjectMatterRule.PACK.equals(this.subjectMatterRule)) {
            this.quotationScope = SourceConstants.QuotationScope.ALL_QUOTATION;
            this.onlyAllowAllWinBids = BaseConstants.Flag.YES;
            if (CollectionUtils.isNotEmpty(sourceProjectDTO.getProjectLineSections())) {
                this.projectLineSectionId = sourceProjectDTO.getProjectLineSections().get(0).getProjectLineSectionId();
            }
        }
    }


    public Integer getQuotationFlag() {
        return quotationFlag;
    }

    public void setQuotationFlag(Integer quotationFlag) {
        this.quotationFlag = quotationFlag;
    }

    public BigDecimal getQuotationQuantity() {
        return quotationQuantity;
    }

    public void setQuotationQuantity(BigDecimal quotationQuantity) {
        this.quotationQuantity = quotationQuantity;
    }
    public String getInternalRemark() {
        return internalRemark;
    }

    public void setInternalRemark(String internalRemark) {
        this.internalRemark = internalRemark;
    }

    public Long getMinQuotedSupplier() {
        return minQuotedSupplier;
    }

    public void setMinQuotedSupplier(Long minQuotedSupplier) {
        this.minQuotedSupplier = minQuotedSupplier;
    }

    public Integer getLackQuotedSendFlag() {
        return lackQuotedSendFlag;
    }

    public void setLackQuotedSendFlag(Integer lackQuotedSendFlag) {
        this.lackQuotedSendFlag = lackQuotedSendFlag;
    }
    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public Integer getSendMethodFlag() {
        return sendMethodFlag;
    }

    public void setSendMethodFlag(Integer sendMethodFlag) {
        this.sendMethodFlag = sendMethodFlag;
    }

    public Integer getPaymentTermFlag() {
        return paymentTermFlag;
    }

    public void setPaymentTermFlag(Integer paymentTermFlag) {
        this.paymentTermFlag = paymentTermFlag;
    }

    public Integer getMatterRequireFlag() {
        return matterRequireFlag;
    }

    public void setMatterRequireFlag(Integer matterRequireFlag) {
        this.matterRequireFlag = matterRequireFlag;
    }

    public String getMatterDetail() {
        return matterDetail;
    }

    public void setMatterDetail(String matterDetail) {
        this.matterDetail = matterDetail;
    }

    public Integer getBudgetAmountFlag() {
        return budgetAmountFlag;
    }

    public void setBudgetAmountFlag(Integer budgetAmountFlag) {
        this.budgetAmountFlag = budgetAmountFlag;
    }

    public String getPaymentClause() {
        return paymentClause;
    }

    public void setPaymentClause(String paymentClause) {
        this.paymentClause = paymentClause;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCreatedUnitCode() {
        return createdUnitCode;
    }

    public void setCreatedUnitCode(String createdUnitCode) {
        this.createdUnitCode = createdUnitCode;
    }

    public Long getCopyRfxHeaderId() { return copyRfxHeaderId; }

    public void setCopyRfxHeaderId(Long copyRfxHeaderId) { this.copyRfxHeaderId = copyRfxHeaderId; }

    public String getScoreRptFileUrl() {
        return scoreRptFileUrl;
    }

    public void setScoreRptFileUrl(String scoreRptFileUrl) {
        this.scoreRptFileUrl = scoreRptFileUrl;
    }

    public String getRfxByName() { return rfxByName; }

    public void setRfxByName(String rfxByName) { this.rfxByName = rfxByName; }

    public Integer getQuotationEndDateChangeFlag() {
        return quotationEndDateChangeFlag;
    }

    public void setQuotationEndDateChangeFlag(Integer quotationEndDateChangeFlag) {
        this.quotationEndDateChangeFlag = quotationEndDateChangeFlag;
    }

    public Integer getPasswordFlag() { return passwordFlag; }

    public void setPasswordFlag(Integer passwordFlag) { this.passwordFlag = passwordFlag; }

    public Integer getScoreProcessFlag() {
        return scoreProcessFlag;
    }

    public void setScoreProcessFlag(Integer scoreProcessFlag) {
        this.scoreProcessFlag = scoreProcessFlag;
    }

    public Long getFinishingRate() {
        return finishingRate;
    }

    public void setFinishingRate(Long finishingRate) {
        this.finishingRate = finishingRate;
    }

    public Date getEstimatedStartTime() {
        return estimatedStartTime;
    }

    public void setEstimatedStartTime(Date estimatedStartTime) {
        this.estimatedStartTime = estimatedStartTime;
    }

    public String getScoreWay() { return scoreWay; }

    public void setScoreWay(String scoreWay) { this.scoreWay = scoreWay; }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public RfxLineItem getLine() {
        return line;
    }

    public void setLine(RfxLineItem line) {
        this.line = line;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getTemplateNum() {
        return templateNum;
    }

    public void setTemplateNum(String templateNum) {
        this.templateNum = templateNum;
    }

    public Integer getOnlyAllowAllWinBids() {
        return onlyAllowAllWinBids;
    }

    public void setOnlyAllowAllWinBids(Integer onlyAllowAllWinBids) {
        this.onlyAllowAllWinBids = onlyAllowAllWinBids;
    }

    public String getExpertSource() { return expertSource; }

    public void setExpertSource(String expertSource) { this.expertSource = expertSource; }
}