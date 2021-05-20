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
import org.srm.source.rfx.api.dto.FieldPropertyDTO;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxMemberRepository;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.constant.Constants;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.share.api.dto.PreSourceHeaderDTO;
import org.srm.source.share.api.dto.SourceProjectDTO;
import org.srm.source.share.domain.entity.ProjectLineSection;
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

@ApiModel("询价单头表")
@VersionAudit
@ModifyAudit
@Table(
        name = "ssrc_rfx_header"
)
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
    public static final String FIELD_PUR_EMAIL = "purEmail";
    public static final String FIELD_PUR_PHONE = "purPhone";
    public static final String FIELD_ITEM_GENERATE_POLICY = "itemGeneratePolicy";
    public static final String FIELD_SCORE_PROCESS_FLAG = "scoreProcessFlag";
    public static final String FIELD_FINISHING_RATE = "finishingRate";
    public static final String FIELD_QUOTATION_CHANGE = "quotationChange";
    public static final String FIELD_ESTIMATED_START_TIME = "estimatedStartTime";
    public static final String FIELD_BUSINESS_WEIGHT = "businessWeight";
    public static final String FIELD_TECHNOLOGY_WEIGHT = "technologyWeight";
    public static final String FIELD_QUOTATION_ROUNDS = "quotationRounds";
    public static final String FIELD_MULTI_SECTION_FLAG = "multiSectionFlag";
    public static final String FIELD_SOURCE_PROJECT_ID = "sourceProjectId";
    public static final String FIELD_CHECK_FINISHED_DATE = "checkFinishedDate";
    public static final String FIELD_REDACT_FLAG = "redactFlag";
    public static final String FIELD_BARGAIN_REDACT_FLAG = "bargainRedactFlag";
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @NotNull(
            groups = {RfxHeader.Quotation.class}
    )
    @Encrypt
    private Long rfxHeaderId;
    @ApiModelProperty(
            value = "所属租户ID，hpfm_tenant.tenant_id",
            example = "9231"
    )
    @NotNull
    private Long tenantId;
    @ApiModelProperty(
            value = "询价单单号",
            example = "testNumber"
    )
    @NotBlank
    @Length(
            max = 30,
            message = "询价单单号最大长度30个字符"
    )
    private String rfxNum;
    @ApiModelProperty(
            value = "询价单状态SSRC.RFX_STATUS(NEW/新建|RELEASE_APPROVING/发布审批中|RELEASE_REJECTED/发布审批拒绝|NOT_START/未开始|IN_PREQUAL/资格预审中|PREQUAL_CUTOFF/资格预审截止|IN_QUOTATION/报价中|OPEN_BID_PENDING/待开标|PRETRIAL_PENDING/待初审|SCORING/评分中|CHECK_PENDING/待核价|CHECK_APPROVING/核价审批中|CHECK_REJECTED/核价审批拒绝|FINISHED/完成|PAUSED/暂停|CLOSED/关闭|ROUNDED/再次询价|IN_POSTQUAL/资格后审中|POSTQUAL_CUTOFF/资格后审截止)",
            example = "NEW"
    )
    @NotBlank
    @Length(
            max = 30,
            message = "询价单状态最大长度30个字符"
    )
    private String rfxStatus;
    @ApiModelProperty(
            value = "询价单标题",
            example = "test"
    )
    @NotBlank
    @Length(
            max = 80,
            message = "询价单标题最大长度80个字符"
    )
    private String rfxTitle;
    @ApiModelProperty("寻源模板ID")
    @NotNull
    @Encrypt
    private Long templateId;
    @ApiModelProperty("商务组权重")
    private BigDecimal businessWeight;
    @ApiModelProperty("技术组权重")
    private BigDecimal technologyWeight;
    @ApiModelProperty(
            value = "寻源类别SSRC.SOURCE_CATEGORY(RFQ/询价|RFA/竞价|BID/招投标)",
            example = "RFQ"
    )
    @NotBlank
    @Length(
            max = 30,
            message = "寻源类别最大长度30个字符"
    )
    @LovValue(
            lovCode = "SSRC.SOURCE_CATEGORY",
            meaningField = "sourceCategoryMeaning"
    )
    private String sourceCategory;
    @ApiModelProperty(
            value = "询价方式SSRC.SOURCE_METHOD(INVITE/邀请|OPEN/合作伙伴公开|ALL_OPEN/全平台公开)",
            example = "INVITE"
    )
    @NotBlank
    @Length(
            max = 30,
            message = "询价方式最大长度30个字符"
    )
    @LovValue(
            value = "SSRC.SOURCE_METHOD",
            meaningField = "sourceMethodMeaning"
    )
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
    @ApiModelProperty("采购方采购组织ID")
    private Long purOrganizationId;
    @ApiModelProperty("采购方企业ID")
    @NotNull
    @Encrypt
    private Long companyId;
    @ApiModelProperty("当前组别序号")
    private Integer currentSequenceNum;
    @ApiModelProperty("采购方企业名称")
    @NotBlank
    @Length(
            max = 360,
            message = "采购方企业名称最大长度360个字符"
    )
    private String companyName;
    @Transient
    private String companyNum;
    @ApiModelProperty(
            value = "竞价方向SSRC.SOURCE_AUCTION_DIRECTION(FORWARD/正向|REVERSE/反向)",
            example = "FORWARD"
    )
    @Length(
            max = 30,
            message = "竞价方向最大长度30个字符"
    )
    @LovValue(
            value = "SSRC.SOURCE_AUCTION_DIRECTION",
            meaningField = "auctionDirectionMeaning"
    )
    private String auctionDirection;
    private Long copyRfxHeaderId;
    @Transient
    private String auctionDirectionMeaning;
    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;
    @ApiModelProperty("含税标识")
    @NotNull
    private Integer taxIncludedFlag;
    @ApiModelProperty("税率ID")
    private Long taxId;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    @ApiModelProperty("币种")
    @NotBlank
    private String currencyCode;
    @ApiModelProperty("汇率")
    private Long exchangeRateId;
    @ApiModelProperty("汇率类型")
    private String exchangeRateType;
    @ApiModelProperty(
            value = "汇率日期",
            example = "2017-12-27 00:00:00"
    )
    private Date exchangeRateDate;
    @ApiModelProperty("汇率期间")
    private String exchangeRatePeriod;
    @ApiModelProperty("汇率")
    private BigDecimal exchangeRate;
    @ApiModelProperty("备注")
    private String rfxRemark;
    @ApiModelProperty("备注(内部)")
    private String internalRemark;
    @ApiModelProperty(
            value = "报价开始时间",
            example = "2017-12-29 00:00:00"
    )
    private Date quotationStartDate;
    @ApiModelProperty(
            value = "报价截止时间",
            example = "2017-12-30 23:59:59"
    )
    private Date quotationEndDate;
    @ApiModelProperty("密封报价标志")
    @NotNull
    @LovValue(
            lovCode = "HPFM.FLAG",
            defaultMeaning = "sealedQuotationFlagMeaning"
    )
    private Integer sealedQuotationFlag;
    @Transient
    private String sealedQuotationFlagMeaning;
    @ApiModelProperty(
            value = "公开规则SSRC.RFA_OPEN_RULE(HIDE_IDENTITY_HIDE_QUOTE/隐藏身份隐藏报价|HIDE_IDENTITY_OPEN_QUOTE/隐藏身份公开报价|OPEN_IDENTITY_HIDE_QUOTE/公开身份隐藏报价|OPEN_IDENTITY_OPEN_QUOTE/公开身份公开报价)",
            example = "HIDE_IDENTITY_HIDE_QUOTE"
    )
    private String openRule;
    @ApiModelProperty(
            value = "竞价排名SSRC.RFA_AUCTION_RANKING(OPEN_COUNT_OPEN_RANK显示参与者数目和当前排名|OPEN_COUNT_HIDE_RANK/显示参与者数目隐藏当前排名|HIDE_COUNT_OPEN_RANK/隐藏参与者数目显示当前排名|HIDE_COUNT_HIDE_RANK/隐藏参与者数目和当前排名)",
            example = "OPEN_COUNT_OPEN_RANK"
    )
    private String auctionRanking;
    @ApiModelProperty(
            value = "寻源类型SSRC.SOURCE_TYPE(常规|OEM|项目|外协|寄售)",
            example = "常规"
    )
    @NotBlank
    private String sourceType;
    @ApiModelProperty(
            value = "价格类型SSRC.SOURCE_PRICE_CATEGORY(STANDARD/标准|SAMPLE/样品)",
            example = "STANDARD"
    )
    @NotBlank
    private String priceCategory;
    @ApiModelProperty("付款方式ID")
    private Long paymentTypeId;
    @ApiModelProperty("付款条款")
    private Long paymentTermId;
    @ApiModelProperty("轮次")
    @NotNull
    private Long roundNumber;
    @ApiModelProperty("版本")
    @NotNull
    private Long versionNumber;
    @ApiModelProperty(
            value = "报价次序SSRC.QUOTATION_ORDER_TYPE(SEQUENCE/序列|STAGGER/交错|PARALLEL/并行)",
            example = "SEQUENCE"
    )
    private String quotationOrderType;
    @ApiModelProperty("竞价运行时间(RFA)")
    private BigDecimal quotationRunningDuration;
    @ApiModelProperty("报价时间间隔")
    private BigDecimal quotationInterval;
    @ApiModelProperty(
            value = "竞价规则SSRC.RFA_AUCTION_RULE(NONE/所有排名允许报相同价格|ALL/所有排名不允许报相同价格|TOP_THREE前三名不允许报相同价格)",
            example = "NONE"
    )
    private String auctionRule;
    @ApiModelProperty("是否启用自动延时")
    @NotNull
    private Integer autoDeferFlag;
    @ApiModelProperty("延时时长")
    private BigDecimal autoDeferDuration;
    @ApiModelProperty(
            value = "发布日期",
            example = "2017-12-31 00:00:00"
    )
    private Date releasedDate;
    @ApiModelProperty("发布人")
    private Long releasedBy;
    @ApiModelProperty(
            value = "终止时间",
            example = "2017-12-31 00:00:00"
    )
    private Date terminatedDate;
    @ApiModelProperty("终止人")
    private Long terminatedBy;
    @ApiModelProperty("终止原因")
    private String terminatedRemark;
    @ApiModelProperty(
            value = "审批日期",
            example = "2017-12-31 00:00:00"
    )
    private Date approvedDate;
    @ApiModelProperty("审批人")
    private Long approvedBy;
    @ApiModelProperty("审批备注")
    private String approvedRemark;
    @ApiModelProperty(
            value = "调整时间日期",
            example = "2017-12-31 00:00:00"
    )
    private Date timeAdjustedDate;
    @ApiModelProperty("调整时间人")
    private Long timeAdjustedBy;
    @ApiModelProperty("调整时间原因")
    private String timeAdjustedRemark;
    @ApiModelProperty("关闭标识")
    @NotNull
    private Integer closedFlag;
    @ApiModelProperty(
            value = "单据来源(MANUAL/手工创建|DEMAND_POOL/需求池|COPY/复制)",
            example = "MANUAL"
    )
    @NotBlank
    private String sourceFrom;
    @ApiModelProperty("发布即开始")
    private Integer startFlag;
    @ApiModelProperty("发布即开始：报价运行时间(RFQ)")
    private BigDecimal startQuotationRunningDuration;
    @ApiModelProperty("初审备注")
    private String pretrailRemark;
    @ApiModelProperty("核价备注")
    private String checkRemark;
    @ApiModelProperty("初审人")
    private Long pretrialUserId;
    @ApiModelProperty("初审状态")
    private String pretrialStatus;
    @ApiModelProperty("报价方式")
    @LovValue(
            value = "SSRC.QUOTATION_TYPE",
            meaningField = "quotationTypeMeaning"
    )
    private String quotationType;
    @Transient
    private String quotationTypeMeaning;
    @ApiModelProperty("退回至初审备注")
    private String backPretrialRemark;
    @ApiModelProperty("总成本")
    private BigDecimal totalCost;
    @ApiModelProperty("成本备注")
    private String costRemark;
    @ApiModelProperty("技术附件UUID")
    private String techAttachmentUuid;
    @ApiModelProperty("商务附件UUID")
    private String businessAttachmentUuid;
    @ApiModelProperty("预定标附件UUID")
    private String preAttachmentUuid;
    @ApiModelProperty("核价附件UUID")
    private String checkAttachmentUuid;
    @ApiModelProperty("报价范围")
    @LovValue(
            value = "SSRC.QUOTATION_SCOPE_CODE",
            meaningField = "quotationScopeMeaning"
    )
    private String quotationScope;
    @ApiModelProperty("需求部门ID")
    private Long unitId;
    @ApiModelProperty("创建人部门ID")
    private Long createdUnitId;
    @ApiModelProperty("创建人部门名称")
    private String createdUnitName;
    private Date latestQuotationEndDate;
    @ApiModelProperty("采购人Id")
    private Long purchaserId;
    @ApiModelProperty("可发起(INITIATE)/线上议价中(BARGAIN_ONLINE)/线下议价中(BARGAIN_OFFLINE)/关闭(CLOSE)")
    private String bargainStatus;
    @ApiModelProperty("议价截止时间")
    private Date bargainEndDate;
    @ApiModelProperty("议价附件")
    private String bargainAttachmentUuid;
    @ApiModelProperty("是否集采标识")
    private Integer centralPurchaseFlag;
    @ApiModelProperty("付款条件")
    private String paymentClause;
    @ApiModelProperty("是否允许多币种报价")
    private Integer multiCurrencyFlag;
    @ApiModelProperty("保证金")
    private BigDecimal bidBond;
    @ApiModelProperty("标书费")
    private BigDecimal bidFileExpense;
    @ApiModelProperty("初审附件")
    private String pretrialUuid;
    @ApiModelProperty("核价员ID")
    private Long checkedBy;
    @ApiModelProperty("最少报价供应商数")
    private Long minQuotedSupplier;
    @ApiModelProperty("报价不足通知发送标识")
    private Integer lackQuotedSendFlag;
    @ApiModelProperty("是否允许供应商修改付款条款&方式")
    private Integer paymentTermFlag;
    @ApiModelProperty("事业部(宝龙达个性化)")
    private String ouBusiness;
    @ApiModelProperty("库存分类(宝龙达个性化)")
    private String inventoryClassify;
    @ApiModelProperty("物料类型(宝龙达个性化)")
    private String itemType;
    @ApiModelProperty("维护完成率")
    private Long finishingRate;
    @Transient
    @ApiModelProperty("初审转交人")
    private Long deliverUserId;
    @Transient
    @ApiModelProperty("开标转交人")
    private Long openDeliverUserId;
    private Date handDownDate;
    @ApiModelProperty("价格有效期")
    private Date priceEffectiveDate;
    @ApiModelProperty("价格失效期")
    private Date priceExpiryDate;
    @ApiModelProperty("寻源事项说明必需标志")
    private Integer matterRequireFlag;
    @ApiModelProperty("寻源事项说明")
    private String matterDetail;
    @LovValue(
            value = "SSRC.RANK_RULE",
            meaningField = "rankRuleMeaning"
    )
    private String rankRule;
    private Date checkFinishedDate;
    private String purName;
    private String purEmail;
    private String purPhone;
    private Integer redactFlag;
    private Integer bargainRedactFlag;
    @ApiModelProperty("供应商升降价设置")
    private String quotationChange;
    private Integer scoreProcessFlag;
    @ApiModelProperty("预算总金额修改标志")
    private Integer budgetAmountFlag;
    @ApiModelProperty("物料生成策略(0/无需|1/允许创建|2/允许补充)")
    private String itemGeneratePolicy;
    @ApiModelProperty("(竞价)预计开始时间")
    private Date estimatedStartTime;
    @ApiModelProperty("评分方式")
    private String scoreWay;
    private Long quotationRounds;
    @ApiModelProperty("多标段标识")
    private Integer multiSectionFlag;
    @ApiModelProperty("来源立项id")
    @Encrypt
    private Long sourceProjectId;
    @Encrypt
    private Long projectLineSectionId;
    @ApiModelProperty("仅允许整单中标")
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
    @LovValue(
            lovCode = "HPFM.FLAG",
            defaultMeaning = "preQualificationFlagMeaning"
    )
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
    private Integer quotationFlag;
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
    @ApiModelProperty("能否修改报价截止时间标志：报价时间截止且未开标的密封报价单可以修改报价截止时间重新报价")
    @Transient
    private Integer quotationEndDateChangeFlag;
    @Transient
    @LovValue(
            lovCode = "HPFM.FLAG",
            defaultMeaning = "expertScoreFlagMeaning"
    )
    private Integer expertScoreFlag;
    @Transient
    private String expertScoreFlagMeaning;
    @Transient
    private String priceTypeCode;
    @Transient
    @ApiModelProperty("开启开标密码标识")
    private Integer passwordFlag;
    @Transient
    private int changeValue;
    @Transient
    private String changeDirection;
    @ApiModelProperty("发票类型")
    private String invoiceType;
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
    @Transient
    private String resultApproveType;
    @Transient
    @NotNull(
            groups = {RfxHeader.Quotation.class}
    )
    @Encrypt
    private Long supplierCompanyId;
    @Transient
    private String supplierCompanyName;
    @Transient
    private Long supplierCompanyTenantId;
    @Transient
    @NotBlank(
            groups = {RfxHeader.Abandon.class}
    )
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
    private Long supplierTenantId;
    @Transient
    private Integer fastBidding;
    @Transient
    @ApiModelProperty("议价阶段")
    private String bargainingStage;
    @Transient
    private List<RfxHeader> rfxHeaders;
    @Transient
    List<FieldPropertyDTO> fieldPropertyDTOList;

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
        rfxLineItemList.stream().filter((lineItem) -> {
            return null != lineItem.getCostPrice() && lineItem.getCostPrice().compareTo(new BigDecimal(0)) > 0;
        }).forEach((lineItem) -> {
            this.totalCost = this.totalCost.add(lineItem.getCostPrice().multiply(lineItem.getRfxQuantity()));
        });
        if ((new BigDecimal(0)).compareTo(this.totalCost) == 0) {
            this.totalCost = this.totalCost.setScale(2, RoundingMode.HALF_UP);
        }

    }

    public void validResume() {
        if (!"PAUSED".equals(this.rfxStatus)) {
            throw new CommonException("error.operate_status", new Object[0]);
        }
    }

    public void validClose() {
        String var1 = this.rfxStatus;
        byte var2 = -1;
        switch (var1.hashCode()) {
            case -771918317:
                if (var1.equals("CHECK_APPROVING")) {
                    var2 = 4;
                }
                break;
            case 77184:
                if (var1.equals("NEW")) {
                    var2 = 0;
                }
                break;
            case 108966002:
                if (var1.equals("FINISHED")) {
                    var2 = 2;
                }
                break;
            case 998689362:
                if (var1.equals("RELEASE_APPROVING")) {
                    var2 = 3;
                }
                break;
            case 1990776172:
                if (var1.equals("CLOSED")) {
                    var2 = 1;
                }
        }

        switch (var2) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                throw new CommonException("error.operate_status", new Object[0]);
            default:
        }
    }

    public void initClose(String terminatedRemark) {
        this.rfxStatus = "CLOSED";
        this.closedFlag = BaseConstants.Flag.YES;
        this.terminatedBy = DetailsHelper.getUserDetails().getUserId();
        this.terminatedDate = new Date();
        this.terminatedRemark = terminatedRemark;
    }

    public void validAuctionRule(SourceTemplate sourceTemplate) {
        if ("RFA".equals(this.sourceCategory)) {
            if (BaseConstants.Flag.YES.equals(this.getStartFlag()) && BaseConstants.Flag.YES.equals(sourceTemplate.getFastBidding())) {
                throw new CommonException("error.rfa_start_flag_disable", new Object[0]);
            }

            if ("NONE".equals(this.auctionDirection)) {
                throw new CommonException("error.rfa_auction_direction", new Object[0]);
            }

            if (this.quotationRunningDuration == null) {
                throw new CommonException("error.quotation_running_duration_is_null", new Object[0]);
            }

            if ("PARALLEL".equals(this.quotationOrderType)) {
                this.quotationInterval = null;
            } else if (this.quotationInterval == null) {
                throw new CommonException("error.quotation_interval_is_null", new Object[0]);
            }

            if (!"INVITE".equals(this.sourceMethod) && "WEIGHT_PRICE".equals(this.rankRule)) {
                throw new CommonException("error.weight_price_invite", new Object[0]);
            }

            if (BaseConstants.Flag.YES.equals(sourceTemplate.getFastBidding()) && this.estimatedStartTime == null) {
                throw new CommonException("error.estimated_start_time_is_null", new Object[0]);
            }
        }

    }

    public void initAuctionRule(SourceTemplate sourceTemplate) {
        if ("RFA".equals(sourceTemplate.getSourceCategory())) {
            this.openRule = sourceTemplate.getOpenRule();
            this.auctionRule = sourceTemplate.getAuctionRule();
            this.auctionRanking = sourceTemplate.getAuctionRanking();
            this.autoDeferFlag = sourceTemplate.getAutoDeferFlag();
            this.autoDeferDuration = sourceTemplate.getAutoDeferDuration();
            this.rankRule = sourceTemplate.getRankRule();
        }

    }

    public void initAdjustDate() {
        this.timeAdjustedBy = DetailsHelper.getUserDetails().getUserId();
        this.timeAdjustedDate = new Date();
    }

    public void validateOpener(SourceTemplate sourceTemplate, RfxMemberRepository rfxMemberRepository) {
        if (BaseConstants.Flag.YES.equals(this.sealedQuotationFlag) && BaseConstants.Flag.YES.equals(sourceTemplate.getOpenerFlag())) {
            List<RfxMember> rfxOpenerMembers = rfxMemberRepository.select(new RfxMember(this.tenantId, this.rfxHeaderId, "OPENED_BY", (Integer) null));
            if (CollectionUtils.isEmpty(rfxOpenerMembers)) {
                throw new CommonException("error.opener_not_null", new Object[0]);
            }
        }

    }

    public void adjustDate(RfxHeaderRepository rfxHeaderRepository, RfxHeader realRfxHeader) {
        Date now = new Date();
        if (realRfxHeader.getQuotationStartDate() != null && DateUtil.beforeStartDate(realRfxHeader.getQuotationStartDate(), now, "")) {
            if (DateUtil.beforeStartDate(now, this.quotationStartDate == null ? realRfxHeader.getQuotationStartDate() : this.quotationStartDate, "")) {
                throw new CommonException("error.start_time_earlier_then_current_time", new Object[0]);
            }

            if (Objects.nonNull(realRfxHeader.getQuotationEndDate())) {
                if (DateUtil.beforeStartDate(now, realRfxHeader.getQuotationEndDate(), "")) {
                    throw new CommonException("error.end_time_earlier_then_current_time", new Object[0]);
                }

                if (DateUtil.beforeStartDate(this.quotationStartDate == null ? realRfxHeader.getQuotationStartDate() : this.quotationStartDate, realRfxHeader.getQuotationEndDate(), "")) {
                    throw new CommonException("error.end_time_earlier_then_start_time", new Object[0]);
                }
            }

            if (realRfxHeader.getPrequalEndDate() != null && DateUtil.beforeStartDate(realRfxHeader.getPrequalEndDate(), this.quotationStartDate, (String) null)) {
                throw new CommonException("error.prequal_end_date_bigger_or_smaller", new Object[0]);
            }
        }

        String realStatus = realRfxHeader.getRfxStatus();
        if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(realRfxHeader.getSourceCategory())
                || RcwlShareConstants.CategoryType.RCBJ.equals(realRfxHeader.getSourceCategory())
                || RcwlShareConstants.CategoryType.RCZB.equals(realRfxHeader.getSourceCategory())
                || RcwlShareConstants.CategoryType.RCZW.equals(realRfxHeader.getSourceCategory()))) {
            if ("NOT_START".equals(realStatus) || "PENDING_PREQUAL".equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.quotationStartDate);
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                CustomizeHelper.ignore(() -> {
                    return rfxHeaderRepository.updateOptional(this, new String[]{"quotationStartDate", "timeAdjustedBy", "quotationEndDate", "timeAdjustedDate", "timeAdjustedRemark"});
                });
            }

            if ("IN_QUOTATION".equals(realStatus) || "LACK_QUOTED".equals(realStatus) || realRfxHeader.getQuotationEndDateChangeFlag() == BaseConstants.Flag.YES && "OPEN_BID_PENDING".equals(realStatus)) {
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                this.rfxStatus = "IN_QUOTATION";
                CustomizeHelper.ignore(() -> {
                    return rfxHeaderRepository.updateOptional(this, new String[]{"quotationEndDate", "timeAdjustedBy", "rfxStatus", "timeAdjustedDate", "timeAdjustedRemark"});
                });
            }

            if ("IN_PREQUAL".equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.getQuotationStartDate());
                realRfxHeader.setQuotationEndDate(this.getQuotationEndDate());
                CustomizeHelper.ignore(() -> {
                    return rfxHeaderRepository.updateOptional(this, new String[]{"quotationStartDate", "quotationEndDate", "timeAdjustedBy", "timeAdjustedDate", "timeAdjustedRemark"});
                });
            }
        }

        if ("RFA".equals(realRfxHeader.getSourceCategory())) {
            if ("NOT_START".equals(realStatus) || "PENDING_PREQUAL".equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.quotationStartDate);
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setQuotationInterval(this.quotationInterval);
                realRfxHeader.setQuotationRunningDuration(this.quotationRunningDuration);
                this.dealWithRfqQuotationEndDate();
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                CustomizeHelper.ignore(() -> {
                    return rfxHeaderRepository.updateOptional(this, new String[]{"quotationStartDate", "quotationRunningDuration", "quotationInterval", "timeAdjustedBy", "timeAdjustedDate", "quotationEndDate", "timeAdjustedRemark"});
                });
            }

            if ("IN_QUOTATION".equals(realStatus)) {
                throw new CommonException("error.operate_status", new Object[0]);
            }

            if ("IN_PREQUAL".equals(realStatus)) {
                realRfxHeader.setQuotationStartDate(this.getQuotationStartDate());
                this.dealWithRfqQuotationEndDate();
                realRfxHeader.setQuotationEndDate(this.quotationEndDate);
                realRfxHeader.setHandDownDate(this.quotationEndDate);
                CustomizeHelper.ignore(() -> {
                    return rfxHeaderRepository.updateOptional(this, new String[]{"quotationStartDate", "quotationRunningDuration", "quotationInterval", "timeAdjustedBy", "timeAdjustedDate", "quotationEndDate", "timeAdjustedRemark"});
                });
            }
        }

        this.latestQuotationEndDate = realRfxHeader.getQuotationEndDate();
        this.handDownDate = realRfxHeader.getQuotationEndDate();
        CustomizeHelper.ignore(() -> {
            return rfxHeaderRepository.updateOptional(this, new String[]{"latestQuotationEndDate", "handDownDate"});
        });
    }

    private void dealWithRfqQuotationEndDate() {
        if (Objects.nonNull(this.day)) {
            this.quotationEndDate = DateUtil.addDay(this.quotationStartDate, this.day.intValue());
        }

        if (Objects.nonNull(this.hour)) {
            this.quotationEndDate = DateUtil.addHourOrMin(this.quotationStartDate, this.hour.intValue(), 11);
        }

        if (Objects.nonNull(this.minute)) {
            this.quotationEndDate = DateUtil.addHourOrMin(this.quotationStartDate, this.minute.intValue(), 12);
        }

    }

    public void initPretrialUser(SourceTemplate sourceTemplate) {
        if (BaseConstants.Flag.YES.equals(sourceTemplate.getPretrialFlag())) {
            this.pretrialUserId = DetailsHelper.getUserDetails().getUserId();
            this.pretrialStatus = "NO_TRIAL";
        }

    }

    public void initPreQualificationFlag(SourceTemplate sourceTemplate) {
        this.preQualificationFlag = !"PRE".equals(sourceTemplate.getQualificationType()) && !"PRE_POST".equals(sourceTemplate.getQualificationType()) ? 0 : 1;
    }

    public void preRfxHeader(SourceTemplate sourceTemplate, PrLineVO prLineVO, Long organizationId) {
        this.templateId = sourceTemplate.getTemplateId();
        this.companyId = prLineVO.getCompanyId();
        this.companyName = prLineVO.getCompanyName();
        this.currencyCode = prLineVO.getCurrencyCode() == null ? "CNY" : prLineVO.getCurrencyCode();
        this.timeAdjustedBy = 1L;
        this.rfxStatus = "NEW";
        this.tenantId = organizationId;
        this.rfxTitle = " ";
        this.sourceMethod = sourceTemplate.getSourceMethod();
        this.sourceType = sourceTemplate.getSourceType();
        this.priceCategory = "STANDARD";
        this.sourceFrom = "DEMAND_POOL";
        this.sourceCategory = sourceTemplate.getSourceCategory();
        this.quotationType = sourceTemplate.getQuotationType();
        this.auctionDirection = sourceTemplate.getAuctionDirection();
        this.quotationScope = sourceTemplate.getQuotationScope();
        this.purchaserId = prLineVO.getPurchaseAgentId();
    }

    public void validQuotationType() {
        if ("OFFLINE".equals(this.quotationType) && "INVITE".equals(this.sourceMethod)) {
            throw new CommonException("error.offline_source_method", new Object[0]);
        }
    }

    public void initQuotationEndDateByItem(RfxHeaderRepository rfxHeaderRepository, List<RfxLineItem> rfxLineItems) {
        Optional<RfxLineItem> itemOptional = rfxLineItems.stream().max(Comparator.comparing(RfxLineItem::getRfxLineItemNum));
        itemOptional.ifPresent((rfxLineItem) -> {
            this.quotationEndDate = rfxLineItem.getQuotationEndDate();
            CustomizeHelper.ignore(() -> {
                return rfxHeaderRepository.updateOptional(this, new String[]{"quotationEndDate"});
            });
        });
    }

    public void pretrialSubmit() {
        this.rfxStatus = "CHECK_PENDING";
        this.pretrialStatus = "SUBMITED";
    }

    public void pretrialBack() {
        this.rfxStatus = "PRETRIAL_PENDING";
        this.pretrialStatus = "NO_TRIAL";
    }

    public void initRankRule(SourceTemplate sourceTemplate) {
        if (this.openRule != null) {
            sourceTemplate.setOpenRule(this.openRule);
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
        BigDecimal startQuotationRunningDuration = this.getStartQuotationRunningDuration();
        if (startQuotationRunningDuration != null && startQuotationRunningDuration.equals(BigDecimal.ZERO)) {
            this.startQuotationRunningDuration = null;
        }

        String categroyType = sourceTemplate.getSourceCategory();
        Integer quotationEndDateFlag = sourceTemplate.getQuotationEndDateFlag();
        Date now = new Date();
        if (this.startFlag != null && !BaseConstants.Flag.NO.equals(this.startFlag)) {
            this.quotationStartDate = null;
        } else if (BaseConstants.Flag.NO.equals(sourceTemplate.getFastBidding())) {
            Assert.notNull(this.quotationStartDate, "error.quotation_start_time_not_found");
            Assert.isTrue(this.quotationStartDate.compareTo(now) >= 0, "error.start_time_earlier_then_current_time");
            if (BaseConstants.Flag.YES.equals(quotationEndDateFlag) &&
                    (ShareConstants.SourceTemplate.CategoryType.RFQ.equals(categroyType)
                            || RcwlShareConstants.CategoryType.RCBJ.equals(categroyType)
                            || RcwlShareConstants.CategoryType.RCZB.equals(categroyType)
                            || RcwlShareConstants.CategoryType.RCZW.equals(categroyType))) {
                Assert.isTrue(this.startQuotationRunningDuration != null || this.quotationEndDate != null, "error.quotation_end_date_or_running_time_one_exist");
                Assert.isTrue(this.quotationEndDate == null || this.quotationEndDate.compareTo(now) >= 0, "error.end_time_earlier_then_current_time");
                Assert.isTrue(this.quotationEndDate == null || this.quotationEndDate.compareTo(this.quotationStartDate) > 0, "error.end_time_earlier_then_start_time");
            }
        }

        if (BaseConstants.Flag.NO.equals(quotationEndDateFlag)) {
            this.quotationEndDate = null;
            this.startQuotationRunningDuration = null;
        }

    }

    public void initAfterApproval() {
        this.sourceType = (String) Optional.ofNullable(this.sourceType).orElse("NORMAL");
        this.approvedBy = DetailsHelper.getUserDetails().getUserId();
        this.approvedDate = new Date();
        if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(this.sourceCategory)
                        || RcwlShareConstants.CategoryType.RCBJ.equals(this.sourceCategory)
                        || RcwlShareConstants.CategoryType.RCZB.equals(this.sourceCategory)
                        || RcwlShareConstants.CategoryType.RCZW.equals(this.sourceCategory))) {
            this.startQuotationRunningDuration = this.quotationEndDate == null ? null : new BigDecimal((this.quotationEndDate.getTime() - this.quotationStartDate.getTime()) / 60000L);
        }

        this.latestQuotationEndDate = this.quotationEndDate;
        this.handDownDate = this.quotationEndDate;
        if (!Objects.isNull(this.bargainRule) && !"NONE".equals(this.bargainRule)) {
            this.bargainStatus = "INITIATE";
        } else {
            this.bargainStatus = "CLOSE";
        }

    }

    public void initUnitInfo(List<PrLineVO> prLineVOList) {
        if (!CollectionUtils.isEmpty(prLineVOList)) {
            if (1 == ((Set) prLineVOList.stream().map(PrLineVO::getUnitId).collect(Collectors.toSet())).size()) {
                this.unitId = ((PrLineVO) prLineVOList.get(0)).getUnitId();
            }
        }
    }

    public void validationQuotationStartDate() {
        if (!Objects.isNull(this.quotationStartDate)) {
            if (this.quotationStartDate.before(super.getCreationDate())) {
                throw new CommonException("quotation.quotation_start_date.error", new Object[0]);
            }
        }
    }

    public void resetBargainStatus() {
        if (Objects.equals("PRE_EVALUATION_PENDING", this.rfxStatus)) {
            if (Objects.equals("BARGAINING_ONLINE", this.bargainStatus)) {
                this.bargainStatus = "BARGAIN_ONLINE";
            }

            if (Objects.equals("BARGAINING_OFFLINE", this.bargainStatus)) {
                this.bargainStatus = "BARGAIN_OFFLINE";
            }
        }

    }

    public void initSentMessagePara(Map<String, String> map, SimpleDateFormat format) {
        map.put("organizationId", String.valueOf(this.tenantId));
        map.put("companyName", this.companyName);
        map.put("companyId", String.valueOf(this.companyId));
        map.put("rfxTitle", this.rfxTitle);
        map.put("rfxNumber", this.rfxNum);
        map.put("rfxCheckSubmitTime", format.format(new Date()));
        map.put("sourceHeaderId", String.valueOf(this.rfxHeaderId));
        map.put("sourceType", "RFX");
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public List<PrChangeVO> initPrChangeVOS(List<RfxLineItem> rfxLineItems) {
        if (CollectionUtils.isEmpty(rfxLineItems)) {
            return null;
        } else {
            List<PrChangeVO> prChangeVOS = new ArrayList();
            rfxLineItems.forEach((rfxLineItem) -> {
                if (null != rfxLineItem.getPrHeaderId() && null != rfxLineItem.getPrLineId()) {
                    prChangeVOS.add(new PrChangeVO(rfxLineItem.getPrHeaderId(), rfxLineItem.getPrLineId(), "SOURCE_RFX", rfxLineItem.getRfxHeaderId(), rfxLineItem.getRfxLineItemId(), String.valueOf(rfxLineItem.getRfxLineItemNum()), this.rfxNum, (String) null, rfxLineItem.getRfxQuantity(), DetailsHelper.getUserDetails().getUserId()));
                }

            });
            return prChangeVOS;
        }
    }

    public void prequalData(Long quotationHeaderId, RfxLineSupplier rfxLineSupplier) {
        this.quotationHeaderId = quotationHeaderId;
        this.docCategory = "RFX_PREQUAL_DOC_CATEGORY";
        this.publishedDate = new Date();
        this.supplierCompanyId = rfxLineSupplier.getSupplierCompanyId();
        this.supplierName = rfxLineSupplier.getSupplierCompanyName();
        this.supplierTenantId = rfxLineSupplier.getSupplierTenantId();
    }

    public void initDataBeforeUpdate() {
        this.centralPurchaseFlag = this.centralPurchaseFlag == null ? BaseConstants.Flag.NO : this.centralPurchaseFlag;
        this.checkedBy = this.checkedBy == null ? (this.getCreatedBy() == null ? DetailsHelper.getUserDetails().getUserId() : this.getCreatedBy()) : this.checkedBy;
        this.lackQuotedSendFlag = this.lackQuotedSendFlag == null ? BaseConstants.Flag.NO : this.lackQuotedSendFlag;
        this.bidBond = this.bidBond == null ? new BigDecimal(0.0D) : this.bidBond;
        this.budgetAmountFlag = this.budgetAmountFlag == null ? BaseConstants.Flag.NO : this.budgetAmountFlag;
        this.multiSectionFlag = this.multiSectionFlag == null ? BaseConstants.Flag.NO : this.multiSectionFlag;
        this.redactFlag = this.redactFlag == null ? BaseConstants.Flag.NO : this.redactFlag;
    }

    public void initCustomizeField(CheckPriceHeaderDTO checkPriceHeaderDTO) {
        String priceEffectiveDate = checkPriceHeaderDTO.getPriceEffectiveDate();
        String priceExpiryDate = checkPriceHeaderDTO.getPriceExpiryDate();
        if (!StringUtils.isBlank(priceEffectiveDate) || !StringUtils.isBlank(priceExpiryDate)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.priceEffectiveDate = priceEffectiveDate == null ? null : simpleDateFormat.parse(priceEffectiveDate);
                this.priceExpiryDate = priceExpiryDate == null ? null : simpleDateFormat.parse(priceExpiryDate);
                if (priceEffectiveDate != null && priceExpiryDate != null) {
                    Assert.isTrue(this.priceExpiryDate.after(this.priceEffectiveDate), "error.effective_date_not_valid");
                }
            } catch (ParseException var5) {
            }

        }
    }

    public void initCalMergeRuleProperties(PreSourceHeaderDTO preSourceHeaderDTO) {
        this.purOrganizationId = preSourceHeaderDTO.getCalPurchaseOrgId();
        this.purchaserId = preSourceHeaderDTO.getCalPurchaseAgentId();
        this.currencyCode = preSourceHeaderDTO.getCalCurrencyCode();
        this.unitId = preSourceHeaderDTO.getCalUnitId();
    }

    public String[] buildConfigCenterParameters() {
        String companyId = this.companyId == null ? null : this.companyId.toString();
        String createdBy = this.getCreatedBy() == null ? null : this.getCreatedBy().toString();
        String createdUnitId = this.createdUnitId == null ? null : this.createdUnitId.toString();
        String purOrganizationId = this.purOrganizationId == null ? null : this.purOrganizationId.toString();
        String unitId = this.unitId == null ? null : this.unitId.toString();
        String templateId = this.templateId == null ? null : this.templateId.toString();
        return new String[]{companyId, createdBy, createdUnitId, purOrganizationId, unitId, templateId, this.sourceCategory};
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
        }

        if (StringUtils.isNotBlank(sourceProject.getPurAgent())) {
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

        this.sourceFrom = "PROJECT";
        this.sourceProjectId = sourceProject.getSourceProjectId();
        this.subjectMatterRule = sourceProject.getSubjectMatterRule();
        if (CollectionUtils.isNotEmpty(sourceProject.getProjectLineSections())) {
            this.projectLineSectionId = ((ProjectLineSection) sourceProject.getProjectLineSections().get(0)).getProjectLineSectionId();
        }

        if ("PACK".equals(this.subjectMatterRule)) {
            this.quotationScope = "ALL_QUOTATION";
            this.onlyAllowAllWinBids = BaseConstants.Flag.YES;
        }

    }

    public void closeBargain(RfxHeader tempHeader) {
        if ("BARGAINING_ONLINE".equals(this.bargainStatus) || "BARGAINING_OFFLINE".equals(this.bargainStatus)) {
            tempHeader.setBargainEndDate(tempHeader.getCheckFinishedDate());
            tempHeader.setBargainStatus("CLOSE");
        }

    }

    public void validationCheckPriceStatus() {
        if (!"IN_QUOTATION".equals(this.rfxStatus) && !"BARGAINING".equals(this.rfxStatus) && !"CHECK_PENDING".equals(this.rfxStatus) && !"CHECK_REJECTED".equals(this.rfxStatus)) {
            throw new CommonException("error.check_price_rfx_status", new Object[0]);
        }
    }

    public void validationVersion(RfxHeader rfxHeaderParam) {
        if (!this.getObjectVersionNumber().equals(rfxHeaderParam.getObjectVersionNumber()) || !this.roundNumber.equals(rfxHeaderParam.getRoundNumber())) {
            throw new CommonException("error.version_or_rotation", new Object[0]);
        }
    }

    public void validationQuotationEndDate() {
        if (!"ROUND_CHECKING".equals(this.getRoundHeaderStatus()) && !"ROUND_SCORING".equals(this.getRoundHeaderStatus()) && !"BARGAINING_ONLINE".equals(this.bargainStatus) && !"BARGAIN_ONLINE".equals(this.bargainStatus) && !"BARGAINING_OFFLINE".equals(this.bargainStatus)) {
            if (this.getQuotationEndDate() != null && (new Date()).compareTo(this.getQuotationEndDate()) > 0) {
                throw new CommonException("error.quotation_end_date", new Object[0]);
            }
        }
    }

    public void validationStatus() {
        if (!"IN_QUOTATION".equals(this.rfxStatus) && !"PAUSED".equals(this.rfxStatus) && !"NOT_START".equals(this.rfxStatus)) {
            throw new CommonException("error.current_status_quotation", new Object[0]);
        }
    }

    public void beforeReleaseCheck(RfxFullHeader rfxFullHeader, SourceTemplate sourceTemplate) {
    }

    public void sealedQuotationCheck() {
        if (!"ROUND_CHECKING".equals(this.getRoundHeaderStatus()) && !"ROUND_SCORING".equals(this.getRoundHeaderStatus()) && !"BARGAINING_ONLINE".equals(this.bargainStatus) && !"BARGAIN_ONLINE".equals(this.bargainStatus) && !"BARGAINING_OFFLINE".equals(this.bargainStatus)) {
            if (BaseConstants.Flag.YES.equals(this.getSealedQuotationFlag())) {
                throw new CommonException("quotation.sealed_quotation.error", new Object[0]);
            } else {
                this.validationQuotationEndDate();
            }
        }
    }

    public RfxHeader initRfxReleaseInfo(String resultApproveType) {
        this.releasedDate = new Date();
        this.releasedBy = DetailsHelper.getUserDetails().getUserId();
        if ("SELF".equals(resultApproveType)) {
            this.rfxStatus = "IN_QUOTATION";
        } else {
            this.rfxStatus = "RELEASE_APPROVING";
        }

        if ("SELF".equals(resultApproveType)) {
            this.approvedDate = this.releasedDate;
            this.approvedBy = DetailsHelper.getUserDetails().getUserId();
        }

        if ((ShareConstants.SourceTemplate.CategoryType.RFQ.equals(this.sourceCategory)
                || RcwlShareConstants.CategoryType.RCBJ.equals(this.sourceCategory)
                || RcwlShareConstants.CategoryType.RCZB.equals(this.sourceCategory)
                || RcwlShareConstants.CategoryType.RCZW.equals(this.sourceCategory))
                && Objects.nonNull(this.quotationStartDate) && Objects.nonNull(this.quotationEndDate)) {
            this.startQuotationRunningDuration = new BigDecimal((this.quotationEndDate.getTime() - this.quotationStartDate.getTime()) / 60000L);
        }

        return this;
    }

    public void checkVendorNumber(RfxFullHeader rfxFullHeader, SourceTemplate sourceTemplate) {
        if ("INVITE".equals(rfxFullHeader.getRfxHeader().getSourceMethod())) {
            long supplierSize = (long) rfxFullHeader.getRfxLineSupplierList().size();
            this.validateVendorNumber(supplierSize, sourceTemplate.getMinVendorNumber(), sourceTemplate.getMaxVendorQuantity());
        }

    }

    public void validateVendorNumber(Long vendorNumber, Long minVendorNumber, Long maxVendorNumber) {
        if (minVendorNumber != null && vendorNumber < minVendorNumber) {
            throw new CommonException("quotation.min_vendor_number.error", new Object[]{minVendorNumber});
        } else if (maxVendorNumber != null && vendorNumber > maxVendorNumber) {
            throw new CommonException("quotation.max_vendor_number.error", new Object[]{maxVendorNumber});
        }
    }

    public static void verifyRFXStatus(String rfqHeaderStatus) {
        if (StringUtils.isEmpty(rfqHeaderStatus)) {
            throw new CommonException("error.not_found", new Object[0]);
        } else if (!"IN_QUOTATION".equals(rfqHeaderStatus)) {
            throw new CommonException("inquiry.sheet.is.quoted.only.in.quotation", new Object[0]);
        }
    }

    public void clearAuctionRule() {
        this.openRule = null;
        this.auctionRule = null;
        this.auctionRanking = null;
        this.autoDeferFlag = 0;
        this.autoDeferDuration = null;
        this.quotationRunningDuration = null;
        this.quotationInterval = null;
    }

    public void cleanRfxQuotationDate() {
        this.quotationStartDate = null;
        this.quotationEndDate = null;
        this.quotationRunningDuration = null;
        this.startQuotationRunningDuration = null;
        this.timeAdjustedDate = null;
        this.handDownDate = null;
        this.latestQuotationEndDate = null;
    }

    public void initCurrentDate() {
        this.currentDateTime = new Date();
        if ("BARGAINING_ONLINE".equals(this.bargainStatus)) {
            if (Objects.nonNull(this.bargainEndDate)) {
                this.bargainClosedFlag = DateUtil.beforeNow(this.bargainEndDate, (String) null) ? BaseConstants.Flag.NO : BaseConstants.Flag.YES;
            } else {
                this.bargainClosedFlag = BaseConstants.Flag.YES;
            }
        } else {
            this.bargainClosedFlag = BaseConstants.Flag.YES;
        }

    }

    public void initScoringProgress() {
        if ("SYNC".equals(this.openBidOrder)) {
            this.scoringProgress = "BUSINESS_TECHNOLOGY";
        } else if ((!"BUSINESS_FIRST".equals(this.openBidOrder) || 1 != this.currentSequenceNum) && (!"TECH_FIRST".equals(this.openBidOrder) || 2 != this.currentSequenceNum)) {
            this.scoringProgress = "TECHNOLOGY";
        } else {
            this.scoringProgress = "BUSINESS";
        }

    }

    public boolean notDisabledDate(String timeField) {
        if (CollectionUtils.isEmpty(this.fieldPropertyDTOList)) {
            return false;
        } else {
            FieldPropertyDTO fieldPropertyDTO = (FieldPropertyDTO) this.fieldPropertyDTOList.stream().filter((item) -> {
                return timeField.equals(item.getName());
            }).findFirst().orElse(new FieldPropertyDTO());
            return BaseConstants.Flag.NO.equals(fieldPropertyDTO.getDisabled());
        }
    }

    public String getBargainingStage() {
        return this.bargainingStage;
    }

    public void setBargainingStage(String bargainingStage) {
        this.bargainingStage = bargainingStage;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getFastBidding() {
        return this.fastBidding;
    }

    public void setFastBidding(Integer fastBidding) {
        this.fastBidding = fastBidding;
    }

    public BigDecimal getBusinessWeight() {
        return this.businessWeight;
    }

    public void setBusinessWeight(BigDecimal businessWeight) {
        this.businessWeight = businessWeight;
    }

    public BigDecimal getTechnologyWeight() {
        return this.technologyWeight;
    }

    public void setTechnologyWeight(BigDecimal technologyWeight) {
        this.technologyWeight = technologyWeight;
    }

    public int getChangeValue() {
        return this.changeValue;
    }

    public void setChangeValue(int changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeDirection() {
        return this.changeDirection;
    }

    public void setChangeDirection(String changeDirection) {
        this.changeDirection = changeDirection;
    }

    public String getQuotationChange() {
        return this.quotationChange;
    }

    public void setQuotationChange(String quotationChange) {
        this.quotationChange = quotationChange;
    }

    public String getItemGeneratePolicy() {
        return this.itemGeneratePolicy;
    }

    public void setItemGeneratePolicy(String itemGeneratePolicy) {
        this.itemGeneratePolicy = itemGeneratePolicy;
    }

    public String getRankRule() {
        return this.rankRule;
    }

    public void setRankRule(String rankRule) {
        this.rankRule = rankRule;
    }

    public String getRankRuleMeaning() {
        return this.rankRuleMeaning;
    }

    public void setRankRuleMeaning(String rankRuleMeaning) {
        this.rankRuleMeaning = rankRuleMeaning;
    }

    public Date getCheckFinishedDate() {
        return this.checkFinishedDate;
    }

    public void setCheckFinishedDate(Date checkFinishedDate) {
        this.checkFinishedDate = checkFinishedDate;
    }

    public String getPurName() {
        return this.purName;
    }

    public void setPurName(String purName) {
        this.purName = purName;
    }

    public String getPurEmail() {
        return this.purEmail;
    }

    public void setPurEmail(String purEmail) {
        this.purEmail = purEmail;
    }

    public String getPurPhone() {
        return this.purPhone;
    }

    public void setPurPhone(String purPhone) {
        this.purPhone = purPhone;
    }

    public Integer getRedactFlag() {
        return this.redactFlag;
    }

    public void setRedactFlag(Integer redactFlag) {
        this.redactFlag = redactFlag;
    }

    public Integer getBargainRedactFlag() {
        return this.bargainRedactFlag;
    }

    public void setBargainRedactFlag(Integer bargainRedactFlag) {
        this.bargainRedactFlag = bargainRedactFlag;
    }

    public SourceTemplate getSourceTemplate() {
        return this.sourceTemplate;
    }

    public void setSourceTemplate(SourceTemplate sourceTemplate) {
        this.sourceTemplate = sourceTemplate;
    }

    public String getOuBusiness() {
        return this.ouBusiness;
    }

    public void setOuBusiness(String ouBusiness) {
        this.ouBusiness = ouBusiness;
    }

    public String getInventoryClassify() {
        return this.inventoryClassify;
    }

    public void setInventoryClassify(String inventoryClassify) {
        this.inventoryClassify = inventoryClassify;
    }

    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Date getPublishedDate() {
        return this.publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDocCategory() {
        return this.docCategory;
    }

    public void setDocCategory(String docCategory) {
        this.docCategory = docCategory;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getSupplierTenantId() {
        return this.supplierTenantId;
    }

    public void setSupplierTenantId(Long supplierTenantId) {
        this.supplierTenantId = supplierTenantId;
    }

    public String getPretrialUuid() {
        return this.pretrialUuid;
    }

    public void setPretrialUuid(String pretrialUuid) {
        this.pretrialUuid = pretrialUuid;
    }

    public Long getPurchaserId() {
        return this.purchaserId;
    }

    public void setPurchaserId(Long purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getQuotationScopeMeaning() {
        return this.quotationScopeMeaning;
    }

    public void setQuotationScopeMeaning(String quotationScopeMeaning) {
        this.quotationScopeMeaning = quotationScopeMeaning;
    }

    public Long getQuotationHeaderId() {
        return this.quotationHeaderId;
    }

    public void setQuotationHeaderId(Long quotationHeaderId) {
        this.quotationHeaderId = quotationHeaderId;
    }

    public Integer getMatchRestrictFlag() {
        return this.matchRestrictFlag;
    }

    public void setMatchRestrictFlag(Integer matchRestrictFlag) {
        this.matchRestrictFlag = matchRestrictFlag;
    }

    public String getRoundHeaderStatus() {
        return this.roundHeaderStatus;
    }

    public void setRoundHeaderStatus(String roundHeaderStatus) {
        this.roundHeaderStatus = roundHeaderStatus;
    }

    public String getRoundQuotationRule() {
        return this.roundQuotationRule;
    }

    public void setRoundQuotationRule(String roundQuotationRule) {
        this.roundQuotationRule = roundQuotationRule;
    }

    public Date getRoundQuotationEndDate() {
        return this.roundQuotationEndDate;
    }

    public void setRoundQuotationEndDate(Date roundQuotationEndDate) {
        this.roundQuotationEndDate = roundQuotationEndDate;
    }

    public Long getQuotationRoundNumber() {
        return this.quotationRoundNumber;
    }

    public void setQuotationRoundNumber(Long quotationRoundNumber) {
        this.quotationRoundNumber = quotationRoundNumber;
    }

    public Date getHandDownDate() {
        return this.handDownDate;
    }

    public void setHandDownDate(Date handDownDate) {
        this.handDownDate = handDownDate;
    }

    public String getExpertScoreType() {
        return this.expertScoreType;
    }

    public void setExpertScoreType(String expertScoreType) {
        this.expertScoreType = expertScoreType;
    }

    public String getAuctionDirectionMeaning() {
        return this.auctionDirectionMeaning;
    }

    public void setAuctionDirectionMeaning(String auctionDirectionMeaning) {
        this.auctionDirectionMeaning = auctionDirectionMeaning;
    }

    public Date getPriceEffectiveDate() {
        return this.priceEffectiveDate;
    }

    public void setPriceEffectiveDate(Date priceEffectiveDate) {
        this.priceEffectiveDate = priceEffectiveDate;
    }

    public Date getPriceExpiryDate() {
        return this.priceExpiryDate;
    }

    public void setPriceExpiryDate(Date priceExpiryDate) {
        this.priceExpiryDate = priceExpiryDate;
    }

    public String getSourceMethodMeaning() {
        return this.sourceMethodMeaning;
    }

    public void setSourceMethodMeaning(String sourceMethodMeaning) {
        this.sourceMethodMeaning = sourceMethodMeaning;
    }

    public String getSourceCategoryMeaning() {
        return this.sourceCategoryMeaning;
    }

    public void setSourceCategoryMeaning(String sourceCategoryMeaning) {
        this.sourceCategoryMeaning = sourceCategoryMeaning;
    }

    public Integer getPretrialFlag() {
        return this.pretrialFlag;
    }

    public void setPretrialFlag(Integer pretrialFlag) {
        this.pretrialFlag = pretrialFlag;
    }

    public String getPrequalStatus() {
        return this.prequalStatus;
    }

    public void setPrequalStatus(String prequalStatus) {
        this.prequalStatus = prequalStatus;
    }

    public Integer getOpenedFlag() {
        return this.openedFlag;
    }

    public void setOpenedFlag(Integer openedFlag) {
        this.openedFlag = openedFlag;
    }

    public Integer getOpenerFlag() {
        return this.openerFlag;
    }

    public void setOpenerFlag(Integer openerFlag) {
        this.openerFlag = openerFlag;
    }

    public String getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getPurOrganizationName() {
        return this.purOrganizationName;
    }

    public void setPurOrganizationName(String purOrganizationName) {
        this.purOrganizationName = purOrganizationName;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getAbandonRemark() {
        return this.abandonRemark;
    }

    public void setAbandonRemark(String abandonRemark) {
        this.abandonRemark = abandonRemark;
    }

    public Long getSupplierCompanyId() {
        return this.supplierCompanyId;
    }

    public void setSupplierCompanyId(Long supplierCompanyId) {
        this.supplierCompanyId = supplierCompanyId;
    }

    public String getSupplierCompanyName() {
        return this.supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public Long getSupplierCompanyTenantId() {
        return this.supplierCompanyTenantId;
    }

    public void setSupplierCompanyTenantId(Long supplierCompanyTenantId) {
        this.supplierCompanyTenantId = supplierCompanyTenantId;
    }

    public List<RoundHeaderDate> getRoundHeaderDates() {
        return this.roundHeaderDates;
    }

    public void setRoundHeaderDates(List<RoundHeaderDate> roundHeaderDates) {
        this.roundHeaderDates = roundHeaderDates;
    }

    public Long getSourceProjectId() {
        return this.sourceProjectId;
    }

    public void setSourceProjectId(Long sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public Long getProjectLineSectionId() {
        return this.projectLineSectionId;
    }

    public void setProjectLineSectionId(Long projectLineSectionId) {
        this.projectLineSectionId = projectLineSectionId;
    }

    public String getSubjectMatterRule() {
        return this.subjectMatterRule;
    }

    public void setSubjectMatterRule(String subjectMatterRule) {
        this.subjectMatterRule = subjectMatterRule;
    }

    public String getSectionCode() {
        return this.sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSourceProjectNum() {
        return this.sourceProjectNum;
    }

    public void setSourceProjectNum(String sourceProjectNum) {
        this.sourceProjectNum = sourceProjectNum;
    }

    public String getSourceProjectName() {
        return this.sourceProjectName;
    }

    public void setSourceProjectName(String sourceProjectName) {
        this.sourceProjectName = sourceProjectName;
    }

    public List<Long> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<Long> itemList) {
        this.itemList = itemList;
    }

    public String getResultApproveType() {
        return this.resultApproveType;
    }

    public void setResultApproveType(String resultApproveType) {
        this.resultApproveType = resultApproveType;
    }

    public Long getRfxHeaderId() {
        return this.rfxHeaderId;
    }

    public void setRfxHeaderId(Long rfxHeaderId) {
        this.rfxHeaderId = rfxHeaderId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getRfxNum() {
        return this.rfxNum;
    }

    public void setRfxNum(String rfxNum) {
        this.rfxNum = rfxNum;
    }

    public String getRfxStatus() {
        return this.rfxStatus;
    }

    public void setRfxStatus(String rfxStatus) {
        this.rfxStatus = rfxStatus;
    }

    public BigDecimal getStartQuotationRunningDuration() {
        return this.startQuotationRunningDuration;
    }

    public void setStartQuotationRunningDuration(BigDecimal startQuotationRunningDuration) {
        this.startQuotationRunningDuration = startQuotationRunningDuration;
    }

    public String getRfxTitle() {
        return this.rfxTitle;
    }

    public void setRfxTitle(String rfxTitle) {
        this.rfxTitle = rfxTitle;
    }

    public Long getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getSourceCategory() {
        return this.sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }

    public String getSourceMethod() {
        return this.sourceMethod;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    public Long getPurOrganizationId() {
        return this.purOrganizationId;
    }

    public void setPurOrganizationId(Long purOrganizationId) {
        this.purOrganizationId = purOrganizationId;
    }

    public Integer getCentralPurchaseFlag() {
        return this.centralPurchaseFlag;
    }

    public void setCentralPurchaseFlag(Integer centralPurchaseFlag) {
        this.centralPurchaseFlag = centralPurchaseFlag;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAuctionDirection() {
        return this.auctionDirection;
    }

    public void setAuctionDirection(String auctionDirection) {
        this.auctionDirection = auctionDirection;
    }

    public BigDecimal getBudgetAmount() {
        return this.budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Integer getTaxIncludedFlag() {
        return this.taxIncludedFlag;
    }

    public void setTaxIncludedFlag(Integer taxIncludedFlag) {
        this.taxIncludedFlag = taxIncludedFlag;
    }

    public Long getTaxId() {
        return this.taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getExchangeRateId() {
        return this.exchangeRateId;
    }

    public void setExchangeRateId(Long exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }

    public String getExchangeRateType() {
        return this.exchangeRateType;
    }

    public void setExchangeRateType(String exchangeRateType) {
        this.exchangeRateType = exchangeRateType;
    }

    public Date getExchangeRateDate() {
        return this.exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public String getQuotationType() {
        return this.quotationType;
    }

    public void setQuotationType(String quotationType) {
        this.quotationType = quotationType;
    }

    public String getQuotationTypeMeaning() {
        return this.quotationTypeMeaning;
    }

    public void setQuotationTypeMeaning(String quotationTypeMeaning) {
        this.quotationTypeMeaning = quotationTypeMeaning;
    }

    public BigDecimal getBidBond() {
        return this.bidBond;
    }

    public void setBidBond(BigDecimal bidBond) {
        this.bidBond = bidBond;
    }

    public BigDecimal getBidFileExpense() {
        return this.bidFileExpense;
    }

    public void setBidFileExpense(BigDecimal bidFileExpense) {
        this.bidFileExpense = bidFileExpense;
    }

    public String getExchangeRatePeriod() {
        return this.exchangeRatePeriod;
    }

    public void setExchangeRatePeriod(String exchangeRatePeriod) {
        this.exchangeRatePeriod = exchangeRatePeriod;
    }

    public BigDecimal getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getRfxRemark() {
        return this.rfxRemark;
    }

    public void setRfxRemark(String rfxRemark) {
        this.rfxRemark = rfxRemark;
    }

    public Date getQuotationStartDate() {
        return this.quotationStartDate;
    }

    public void setQuotationStartDate(Date quotationStartDate) {
        this.quotationStartDate = quotationStartDate;
    }

    public Date getLatestQuotationEndDate() {
        return this.latestQuotationEndDate;
    }

    public void setLatestQuotationEndDate(Date latestQuotationEndDate) {
        this.latestQuotationEndDate = latestQuotationEndDate;
    }

    public Date getQuotationEndDate() {
        return this.quotationEndDate;
    }

    public void setQuotationEndDate(Date quotationEndDate) {
        this.quotationEndDate = quotationEndDate;
    }

    public Integer getSealedQuotationFlag() {
        return this.sealedQuotationFlag;
    }

    public void setSealedQuotationFlag(Integer sealedQuotationFlag) {
        this.sealedQuotationFlag = sealedQuotationFlag;
    }

    public String getSealedQuotationFlagMeaning() {
        return this.sealedQuotationFlagMeaning;
    }

    public void setSealedQuotationFlagMeaning(String sealedQuotationFlagMeaning) {
        this.sealedQuotationFlagMeaning = sealedQuotationFlagMeaning;
    }

    public String getOpenRule() {
        return this.openRule;
    }

    public void setOpenRule(String openRule) {
        this.openRule = openRule;
    }

    public String getAuctionRanking() {
        return this.auctionRanking;
    }

    public void setAuctionRanking(String auctionRanking) {
        this.auctionRanking = auctionRanking;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getPriceCategory() {
        return this.priceCategory;
    }

    public void setPriceCategory(String priceCategory) {
        this.priceCategory = priceCategory;
    }

    public Long getPaymentTypeId() {
        return this.paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public Long getPaymentTermId() {
        return this.paymentTermId;
    }

    public void setPaymentTermId(Long paymentTermId) {
        this.paymentTermId = paymentTermId;
    }

    public Long getRoundNumber() {
        return this.roundNumber;
    }

    public void setRoundNumber(Long roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Long getVersionNumber() {
        return this.versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getQuotationOrderType() {
        return this.quotationOrderType;
    }

    public void setQuotationOrderType(String quotationOrderType) {
        this.quotationOrderType = quotationOrderType;
    }

    public String getQualificationType() {
        return this.qualificationType;
    }

    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
    }

    public BigDecimal getQuotationRunningDuration() {
        return this.quotationRunningDuration;
    }

    public void setQuotationRunningDuration(BigDecimal quotationRunningDuration) {
        this.quotationRunningDuration = quotationRunningDuration;
    }

    public BigDecimal getQuotationInterval() {
        return this.quotationInterval;
    }

    public void setQuotationInterval(BigDecimal quotationInterval) {
        this.quotationInterval = quotationInterval;
    }

    public String getAuctionRule() {
        return this.auctionRule;
    }

    public void setAuctionRule(String auctionRule) {
        this.auctionRule = auctionRule;
    }

    public Integer getAutoDeferFlag() {
        return this.autoDeferFlag;
    }

    public void setAutoDeferFlag(Integer autoDeferFlag) {
        this.autoDeferFlag = autoDeferFlag;
    }

    public BigDecimal getAutoDeferDuration() {
        return this.autoDeferDuration;
    }

    public void setAutoDeferDuration(BigDecimal autoDeferDuration) {
        this.autoDeferDuration = autoDeferDuration;
    }

    public Date getReleasedDate() {
        return this.releasedDate;
    }

    public void setReleasedDate(Date releasedDate) {
        this.releasedDate = releasedDate;
    }

    public Long getReleasedBy() {
        return this.releasedBy;
    }

    public void setReleasedBy(Long releasedBy) {
        this.releasedBy = releasedBy;
    }

    public Date getTerminatedDate() {
        return this.terminatedDate;
    }

    public void setTerminatedDate(Date terminatedDate) {
        this.terminatedDate = terminatedDate;
    }

    public Long getPrequalHeaderId() {
        return this.prequalHeaderId;
    }

    public void setPrequalHeaderId(Long prequalHeaderId) {
        this.prequalHeaderId = prequalHeaderId;
    }

    public Date getPrequalEndDate() {
        return this.prequalEndDate;
    }

    public void setPrequalEndDate(Date prequalEndDate) {
        this.prequalEndDate = prequalEndDate;
    }

    public Long getTerminatedBy() {
        return this.terminatedBy;
    }

    public void setTerminatedBy(Long terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public String getTerminatedRemark() {
        return this.terminatedRemark;
    }

    public void setTerminatedRemark(String terminatedRemark) {
        this.terminatedRemark = terminatedRemark;
    }

    public Date getApprovedDate() {
        return this.approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getStartFlag() {
        return this.startFlag;
    }

    public void setStartFlag(Integer startFlag) {
        this.startFlag = startFlag;
    }

    public Long getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedRemark() {
        return this.approvedRemark;
    }

    public void setApprovedRemark(String approvedRemark) {
        this.approvedRemark = approvedRemark;
    }

    public Date getTimeAdjustedDate() {
        return this.timeAdjustedDate;
    }

    public void setTimeAdjustedDate(Date timeAdjustedDate) {
        this.timeAdjustedDate = timeAdjustedDate;
    }

    public Long getTimeAdjustedBy() {
        return this.timeAdjustedBy;
    }

    public void setTimeAdjustedBy(Long timeAdjustedBy) {
        this.timeAdjustedBy = timeAdjustedBy;
    }

    public String getTimeAdjustedRemark() {
        return this.timeAdjustedRemark;
    }

    public void setTimeAdjustedRemark(String timeAdjustedRemark) {
        this.timeAdjustedRemark = timeAdjustedRemark;
    }

    public Integer getClosedFlag() {
        return this.closedFlag;
    }

    public void setClosedFlag(Integer closedFlag) {
        this.closedFlag = closedFlag;
    }

    public String getSourceFrom() {
        return this.sourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    public String getPretrailRemark() {
        return this.pretrailRemark;
    }

    public void setPretrailRemark(String pretrailRemark) {
        this.pretrailRemark = pretrailRemark;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getCostRemark() {
        return this.costRemark;
    }

    public void setCostRemark(String costRemark) {
        this.costRemark = costRemark;
    }

    public String getBackPretrialRemark() {
        return this.backPretrialRemark;
    }

    public void setBackPretrialRemark(String backPretrialRemark) {
        this.backPretrialRemark = backPretrialRemark;
    }

    public String getTechAttachmentUuid() {
        return this.techAttachmentUuid;
    }

    public void setTechAttachmentUuid(String techAttachmentUuid) {
        this.techAttachmentUuid = techAttachmentUuid;
    }

    public String getBusinessAttachmentUuid() {
        return this.businessAttachmentUuid;
    }

    public void setBusinessAttachmentUuid(String businessAttachmentUuid) {
        this.businessAttachmentUuid = businessAttachmentUuid;
    }

    public String getCheckAttachmentUuid() {
        return this.checkAttachmentUuid;
    }

    public void setCheckAttachmentUuid(String checkAttachmentUuid) {
        this.checkAttachmentUuid = checkAttachmentUuid;
    }

    public Long getPretrialUserId() {
        return this.pretrialUserId;
    }

    public void setPretrialUserId(Long pretrialUserId) {
        this.pretrialUserId = pretrialUserId;
    }

    public String getPretrialStatus() {
        return this.pretrialStatus;
    }

    public void setPretrialStatus(String pretrialStatus) {
        this.pretrialStatus = pretrialStatus;
    }

    public Long getDeliverUserId() {
        return this.deliverUserId;
    }

    public void setDeliverUserId(Long deliverUserId) {
        this.deliverUserId = deliverUserId;
    }

    public Long getOpenDeliverUserId() {
        return this.openDeliverUserId;
    }

    public void setOpenDeliverUserId(Long openDeliverUserId) {
        this.openDeliverUserId = openDeliverUserId;
    }

    public Integer getCurrentSequenceNum() {
        return this.currentSequenceNum;
    }

    public void setCurrentSequenceNum(Integer currentSequenceNum) {
        this.currentSequenceNum = currentSequenceNum;
    }

    public Long getUnitId() {
        return this.unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCreatedUnitId() {
        return this.createdUnitId;
    }

    public void setCreatedUnitId(Long createdUnitId) {
        this.createdUnitId = createdUnitId;
    }

    public String getCreatedUnitName() {
        return this.createdUnitName;
    }

    public void setCreatedUnitName(String createdUnitName) {
        this.createdUnitName = createdUnitName;
    }

    public Long getCurrentQuotationRound() {
        return this.currentQuotationRound;
    }

    public void setCurrentQuotationRound(Long currentQuotationRound) {
        this.currentQuotationRound = currentQuotationRound;
    }

    public String getCreatedByName() {
        return this.createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getExpertScoreFlag() {
        return this.expertScoreFlag;
    }

    public void setExpertScoreFlag(Integer expertScoreFlag) {
        this.expertScoreFlag = expertScoreFlag;
    }

    public String getPreQualificationFlagMeaning() {
        return this.preQualificationFlagMeaning;
    }

    public void setPreQualificationFlagMeaning(String preQualificationFlagMeaning) {
        this.preQualificationFlagMeaning = preQualificationFlagMeaning;
    }

    public String getExpertScoreFlagMeaning() {
        return this.expertScoreFlagMeaning;
    }

    public void setExpertScoreFlagMeaning(String expertScoreFlagMeaning) {
        this.expertScoreFlagMeaning = expertScoreFlagMeaning;
    }

    public void initAgainInquiryStataus() {
        Long var2 = this.versionNumber;
        Long var3 = this.versionNumber = this.versionNumber + 1L;
        this.setRfxStatus("ROUNDED");
        var2 = this.roundNumber;
        var3 = this.roundNumber = this.roundNumber + 1L;
        if (this.pretrialStatus != null) {
            this.pretrialStatus = "NO_TRIAL";
        }

        this.currentSequenceNum = 1;
    }

    public List<RfxLineItem> getRfxLineItemList() {
        return this.rfxLineItemList;
    }

    public String getPriceTypeCode() {
        return this.priceTypeCode;
    }

    public void setPriceTypeCode(String priceTypeCode) {
        this.priceTypeCode = priceTypeCode;
    }

    public void setRfxLineItemList(List<RfxLineItem> rfxLineItemList) {
        this.rfxLineItemList = rfxLineItemList;
    }

    public Integer getPreQualificationFlag() {
        return this.preQualificationFlag;
    }

    public void setPreQualificationFlag(Integer preQualificationFlag) {
        this.preQualificationFlag = preQualificationFlag;
    }

    public String getPreAttachmentUuid() {
        return this.preAttachmentUuid;
    }

    public void setPreAttachmentUuid(String preAttachmentUuid) {
        this.preAttachmentUuid = preAttachmentUuid;
    }

    public List<RfxQuotationHeader> getRfxQuotationHeaderList() {
        return this.rfxQuotationHeaderList;
    }

    public void setRfxQuotationHeaderList(List<RfxQuotationHeader> rfxQuotationHeaderList) {
        this.rfxQuotationHeaderList = rfxQuotationHeaderList;
    }

    public List<RfxQuotationLine> getRfxQuotationLineList() {
        return this.rfxQuotationLineList;
    }

    public void setRfxQuotationLineList(List<RfxQuotationLine> rfxQuotationLineList) {
        this.rfxQuotationLineList = rfxQuotationLineList;
    }

    public String getQuotationScope() {
        return this.quotationScope;
    }

    public void setQuotationScope(String quotationScope) {
        this.quotationScope = quotationScope;
    }

    public String getBargainStatus() {
        return this.bargainStatus;
    }

    public void setBargainStatus(String bargainStatus) {
        this.bargainStatus = bargainStatus;
    }

    public String getBargainRule() {
        return this.bargainRule;
    }

    public void setBargainRule(String bargainRule) {
        this.bargainRule = bargainRule;
    }

    public Integer getBargainOfflineFlag() {
        return this.bargainOfflineFlag;
    }

    public void setBargainOfflineFlag(Integer bargainOfflineFlag) {
        this.bargainOfflineFlag = bargainOfflineFlag;
    }

    public Date getCurrentDateTime() {
        return this.currentDateTime;
    }

    public void setCurrentDateTime(Date currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public Integer getBargainClosedFlag() {
        return this.bargainClosedFlag;
    }

    public void setBargainClosedFlag(Integer bargainClosedFlag) {
        this.bargainClosedFlag = bargainClosedFlag;
    }

    public Date getBargainEndDate() {
        return this.bargainEndDate;
    }

    public BigDecimal getDay() {
        return this.day;
    }

    public void setDay(BigDecimal day) {
        this.day = day;
    }

    public BigDecimal getHour() {
        return this.hour;
    }

    public void setHour(BigDecimal hour) {
        this.hour = hour;
    }

    public void setBargainEndDate(Date bargainEndDate) {
        this.bargainEndDate = bargainEndDate;
    }

    public String getOpenBidOrder() {
        return this.openBidOrder;
    }

    public void setOpenBidOrder(String openBidOrder) {
        this.openBidOrder = openBidOrder;
    }

    public String getScoringProgress() {
        return this.scoringProgress;
    }

    public void setScoringProgress(String scoringProgress) {
        this.scoringProgress = scoringProgress;
    }

    public BigDecimal getMinute() {
        return this.minute;
    }

    public void setMinute(BigDecimal minute) {
        this.minute = minute;
    }

    public String getBargainAttachmentUuid() {
        return this.bargainAttachmentUuid;
    }

    public void setBargainAttachmentUuid(String bargainAttachmentUuid) {
        this.bargainAttachmentUuid = bargainAttachmentUuid;
    }

    public Integer getMultiCurrencyFlag() {
        return this.multiCurrencyFlag;
    }

    public void setMultiCurrencyFlag(Integer multiCurrencyFlag) {
        this.multiCurrencyFlag = multiCurrencyFlag;
    }

    public Long getCheckedBy() {
        return this.checkedBy;
    }

    public void setCheckedBy(Long checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getCheckedByName() {
        return this.checkedByName;
    }

    public void setCheckedByName(String checkedByName) {
        this.checkedByName = checkedByName;
    }

    public Long getQuotationRounds() {
        return this.quotationRounds;
    }

    public void setQuotationRounds(Long quotationRounds) {
        this.quotationRounds = quotationRounds;
    }

    public Integer getMultiSectionFlag() {
        return this.multiSectionFlag;
    }

    public void setMultiSectionFlag(Integer multiSectionFlag) {
        this.multiSectionFlag = multiSectionFlag;
    }

    public Integer getRoundQuotationRankFlag() {
        return this.roundQuotationRankFlag;
    }

    public void setRoundQuotationRankFlag(Integer roundQuotationRankFlag) {
        this.roundQuotationRankFlag = roundQuotationRankFlag;
    }

    public String getRoundQuotationRankRule() {
        return this.roundQuotationRankRule;
    }

    public void setRoundQuotationRankRule(String roundQuotationRankRule) {
        this.roundQuotationRankRule = roundQuotationRankRule;
    }

    public void initFromProject(Long organizationId, SourceProjectDTO sourceProjectDTO, SourceTemplate sourceTemplate) {
        if (sourceProjectDTO != null && sourceTemplate != null) {
            this.tenantId = organizationId;
            this.templateId = sourceProjectDTO.getTemplateId();
            this.companyId = sourceProjectDTO.getCompanyId();
            this.companyName = sourceProjectDTO.getCompanyName();
            this.sourceMethod = sourceProjectDTO.getSourceMethod();
            this.budgetAmount = sourceProjectDTO.getBudgetAmount();
            this.setCurrencyCode("CNY");
            this.unitId = sourceProjectDTO.getUnitId();
            this.bidBond = sourceProjectDTO.getDepositAmount();
            this.paymentTypeId = sourceProjectDTO.getPaymentTypeId();
            this.paymentTermId = sourceProjectDTO.getPaymentTermId();
            this.timeAdjustedBy = 1L;
            this.rfxStatus = "NEW";
            this.sourceType = sourceTemplate.getSourceType();
            this.priceCategory = "STANDARD";
            this.sourceFrom = "PROJECT";
            this.sourceCategory = sourceTemplate.getSourceCategory();
            this.quotationType = sourceTemplate.getQuotationType();
            this.quotationScope = sourceTemplate.getQuotationScope();
            this.auctionDirection = sourceTemplate.getAuctionDirection();
            this.quotationScope = sourceTemplate.getQuotationScope();
            this.bidFileExpense = new BigDecimal("0");
            this.onlyAllowAllWinBids = sourceTemplate.getOnlyAllowAllWinBids();
            this.subjectMatterRule = sourceProjectDTO.getSubjectMatterRule();
            this.sourceProjectId = sourceProjectDTO.getSourceProjectId();
            if ("PACK".equals(this.subjectMatterRule)) {
                this.quotationScope = "ALL_QUOTATION";
                this.onlyAllowAllWinBids = BaseConstants.Flag.YES;
                if (CollectionUtils.isNotEmpty(sourceProjectDTO.getProjectLineSections())) {
                    this.projectLineSectionId = ((ProjectLineSection) sourceProjectDTO.getProjectLineSections().get(0)).getProjectLineSectionId();
                }
            }

        }
    }

    public Integer getQuotationFlag() {
        return this.quotationFlag;
    }

    public void setQuotationFlag(Integer quotationFlag) {
        this.quotationFlag = quotationFlag;
    }

    public BigDecimal getQuotationQuantity() {
        return this.quotationQuantity;
    }

    public void setQuotationQuantity(BigDecimal quotationQuantity) {
        this.quotationQuantity = quotationQuantity;
    }

    public String getInternalRemark() {
        return this.internalRemark;
    }

    public void setInternalRemark(String internalRemark) {
        this.internalRemark = internalRemark;
    }

    public Long getMinQuotedSupplier() {
        return this.minQuotedSupplier;
    }

    public void setMinQuotedSupplier(Long minQuotedSupplier) {
        this.minQuotedSupplier = minQuotedSupplier;
    }

    public Integer getLackQuotedSendFlag() {
        return this.lackQuotedSendFlag;
    }

    public void setLackQuotedSendFlag(Integer lackQuotedSendFlag) {
        this.lackQuotedSendFlag = lackQuotedSendFlag;
    }

    public String getCheckRemark() {
        return this.checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public Integer getSendMethodFlag() {
        return this.sendMethodFlag;
    }

    public void setSendMethodFlag(Integer sendMethodFlag) {
        this.sendMethodFlag = sendMethodFlag;
    }

    public Integer getPaymentTermFlag() {
        return this.paymentTermFlag;
    }

    public void setPaymentTermFlag(Integer paymentTermFlag) {
        this.paymentTermFlag = paymentTermFlag;
    }

    public Integer getMatterRequireFlag() {
        return this.matterRequireFlag;
    }

    public void setMatterRequireFlag(Integer matterRequireFlag) {
        this.matterRequireFlag = matterRequireFlag;
    }

    public String getMatterDetail() {
        return this.matterDetail;
    }

    public void setMatterDetail(String matterDetail) {
        this.matterDetail = matterDetail;
    }

    public Integer getBudgetAmountFlag() {
        return this.budgetAmountFlag;
    }

    public void setBudgetAmountFlag(Integer budgetAmountFlag) {
        this.budgetAmountFlag = budgetAmountFlag;
    }

    public String getPaymentClause() {
        return this.paymentClause;
    }

    public void setPaymentClause(String paymentClause) {
        this.paymentClause = paymentClause;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCreatedUnitCode() {
        return this.createdUnitCode;
    }

    public void setCreatedUnitCode(String createdUnitCode) {
        this.createdUnitCode = createdUnitCode;
    }

    public Long getCopyRfxHeaderId() {
        return this.copyRfxHeaderId;
    }

    public void setCopyRfxHeaderId(Long copyRfxHeaderId) {
        this.copyRfxHeaderId = copyRfxHeaderId;
    }

    public String getScoreRptFileUrl() {
        return this.scoreRptFileUrl;
    }

    public void setScoreRptFileUrl(String scoreRptFileUrl) {
        this.scoreRptFileUrl = scoreRptFileUrl;
    }

    public String getRfxByName() {
        return this.rfxByName;
    }

    public void setRfxByName(String rfxByName) {
        this.rfxByName = rfxByName;
    }

    public Integer getQuotationEndDateChangeFlag() {
        return this.quotationEndDateChangeFlag;
    }

    public void setQuotationEndDateChangeFlag(Integer quotationEndDateChangeFlag) {
        this.quotationEndDateChangeFlag = quotationEndDateChangeFlag;
    }

    public Integer getPasswordFlag() {
        return this.passwordFlag;
    }

    public void setPasswordFlag(Integer passwordFlag) {
        this.passwordFlag = passwordFlag;
    }

    public Integer getScoreProcessFlag() {
        return this.scoreProcessFlag;
    }

    public void setScoreProcessFlag(Integer scoreProcessFlag) {
        this.scoreProcessFlag = scoreProcessFlag;
    }

    public Long getFinishingRate() {
        return this.finishingRate;
    }

    public void setFinishingRate(Long finishingRate) {
        this.finishingRate = finishingRate;
    }

    public Date getEstimatedStartTime() {
        return this.estimatedStartTime;
    }

    public void setEstimatedStartTime(Date estimatedStartTime) {
        this.estimatedStartTime = estimatedStartTime;
    }

    public String getScoreWay() {
        return this.scoreWay;
    }

    public void setScoreWay(String scoreWay) {
        this.scoreWay = scoreWay;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public RfxLineItem getLine() {
        return this.line;
    }

    public void setLine(RfxLineItem line) {
        this.line = line;
    }

    public String getCompanyNum() {
        return this.companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getTemplateNum() {
        return this.templateNum;
    }

    public void setTemplateNum(String templateNum) {
        this.templateNum = templateNum;
    }

    public Integer getOnlyAllowAllWinBids() {
        return this.onlyAllowAllWinBids;
    }

    public void setOnlyAllowAllWinBids(Integer onlyAllowAllWinBids) {
        this.onlyAllowAllWinBids = onlyAllowAllWinBids;
    }

    public String getExpertSource() {
        return this.expertSource;
    }

    public void setExpertSource(String expertSource) {
        this.expertSource = expertSource;
    }

    public List<RfxHeader> getRfxHeaders() {
        return this.rfxHeaders;
    }

    public void setRfxHeaders(List<RfxHeader> rfxHeaders) {
        this.rfxHeaders = rfxHeaders;
    }

    public List<FieldPropertyDTO> getFieldPropertyDTOList() {
        return this.fieldPropertyDTOList;
    }

    public void setFieldPropertyDTOList(List<FieldPropertyDTO> fieldPropertyDTOList) {
        this.fieldPropertyDTOList = fieldPropertyDTOList;
    }

    public interface Abandon {
    }

    public interface Quotation {
    }
}