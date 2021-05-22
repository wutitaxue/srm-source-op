package org.srm.source.cux.share.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.source.bid.domain.entity.BidHeader;
import org.srm.source.bid.domain.repository.BidHeaderRepository;
import org.srm.source.cux.rfx.domain.repository.IRcwlRfxQuotationLineRepository;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.app.service.impl.EvaluateScoreLineServiceImpl;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.SourceTemplateRepository;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kaibo.li
 * @date 2021-05-21 17:52
 */
@Service
@Tenant("SRM-RCWL")
public class RcwlEvaluateScoreLineServiceImpl extends EvaluateScoreLineServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluateScoreLineServiceImpl.class);

    /**
     * 原始的
     */
    @Autowired
    private BidHeaderRepository bidHeaderRepository;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private CommonQueryRepository commonQueryRepository;
    @Autowired
    private SourceTemplateRepository sourceTemplateRepository;

    /**
     * 新写的
     */
    @Autowired
    private IRcwlRfxQuotationLineRepository rcwlRfxQuotationLineRepository;

    public RcwlEvaluateScoreLineServiceImpl() {
        super();
    }

    @Override
    public void _autoEvaluateScore(AutoScoreDTO autoScoreDTO) {
        LOGGER.debug("24769---RCWL _autoEvaluateScore auto evaluate score started");

        AUTO_FLAG.set(true);
        String sourceFrom = autoScoreDTO.getSourceFrom();
        Long sourceHeaderId = autoScoreDTO.getSourceHeaderId();
        Long tenantId = autoScoreDTO.getTenantId();
        List<Long> invalidQuotationHeaderIdList = autoScoreDTO.getInvalidQuotationHeaderIdList();
        String subjectMatterRule = "NONE";
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("auto evaluate score started, params => [sourceFrom : {},sourceHeaderId:{},tenantId:{}]", sourceFrom, sourceHeaderId, tenantId);
        }

        Map<String, String> parameter = new HashMap(1);
        String priceTypeCode = "";
        Map<Long, BigDecimal> quotationLineMaps;
        SourceTemplate sourceTemplate;
        if ("RFX".equals(sourceFrom)) {
            RfxHeader rfxHeader = this.rfxHeaderRepository.selectByPrimaryKey(sourceHeaderId);
            parameter.put("company", rfxHeader.getCompanyId().toString());
            parameter.put("sourceCategory", rfxHeader.getSourceCategory());
            sourceTemplate = this.sourceTemplateRepository.selectByPrimaryKey(rfxHeader.getTemplateId());
            parameter.put("sourceTemplate", sourceTemplate.getTemplateNum());
            priceTypeCode = CnfHelper.select(tenantId, "SITE.SSRC.QUOTATION_SET", String.class).invokeWithParameter(parameter);
            // 获取报价头id，与报价行价格
            quotationLineMaps = this.self().getRfxQuotationLineMaps(autoScoreDTO, priceTypeCode);
        } else {
            BidHeader bidHeader = this.bidHeaderRepository.selectByPrimaryKey(sourceHeaderId);
            parameter.put("company", bidHeader.getCompanyId().toString());
            parameter.put("sourceCategory", bidHeader.getSourceCategory());
            sourceTemplate = this.sourceTemplateRepository.selectByPrimaryKey(bidHeader.getTemplateId());
            parameter.put("sourceTemplate", sourceTemplate.getTemplateNum());
            priceTypeCode = CnfHelper.select(tenantId, "SITE.SSRC.QUOTATION_SET", String.class).invokeWithParameter(parameter);
            subjectMatterRule = bidHeader.getSubjectMatterRule();
            quotationLineMaps = this.getBidQuotationLineMaps(autoScoreDTO, priceTypeCode);
        }

        Map<Long, BigDecimal> validQuotationLineMaps = (Map)quotationLineMaps.entrySet().stream().filter((map) -> {
            return CollectionUtils.isEmpty(invalidQuotationHeaderIdList) || !invalidQuotationHeaderIdList.contains(map.getKey());
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        if (!CollectionUtils.isEmpty(validQuotationLineMaps.values())) {
            this.simulateExpertDoScore(autoScoreDTO, subjectMatterRule, quotationLineMaps, priceTypeCode);
        }
    }

    @Override
    public Map<Long, BigDecimal> getRfxQuotationLineMaps(AutoScoreDTO autoScoreDTO, String priceTypeCode) {
        LOGGER.debug("24769---RCWL getRfxQuotationLineMaps");
        Long sourceHeaderId = autoScoreDTO.getSourceHeaderId();
        Long tenantId = autoScoreDTO.getTenantId();
        RfxHeader rfxHeader = this.rfxHeaderRepository.selectByPrimaryKey(sourceHeaderId);
        if (Objects.isNull(rfxHeader)) {
            throw new CommonException("rfx header not exists!");
        } else {
            // RCWL 获取有效报价行数据
            List<RfxQuotationLine> rfxQuotationLines = this.rcwlRfxQuotationLineRepository.querySumQuotationByRfxHeaderId(sourceHeaderId);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("rfx quotation lines : {}", rfxQuotationLines);
            }

            if (CollectionUtils.isEmpty(rfxQuotationLines)) {
                throw new CommonException("rfx quotation line not exists!");
            } else {
                Map<Long, BigDecimal> quotationLineMaps = (Map)rfxQuotationLines.stream().collect(Collectors.toMap(RfxQuotationLine::getQuotationHeaderId, (quotationLine) -> {
                    // 获取租户编码
                    String tenantNum = (String)this.commonQueryRepository.selectTenantExists(tenantId).get(0);
                    // ssrc_rfx_header 头表 attribute_varchar16 值为 Y 含税金额, N为不含税金额。
                    return "Y".equals(rfxHeader.getAttributeVarchar16()) && "SRM-RCWL".equals(tenantNum) ? quotationLine.getTotalAmount() : quotationLine.getNetAmount();
                }));
                return quotationLineMaps;
            }
        }
    }
}
