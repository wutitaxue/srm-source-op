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
import org.srm.source.cux.rfx.domain.strategy.RcwlAutoScoreStrategyService;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.api.dto.EvaluateExpertFullDTO;
import org.srm.source.share.api.dto.EvaluateScoreDTO;
import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.api.dto.EvaluateScoreQueryDTO;
import org.srm.source.share.api.dto.EvaluateSectionDTO;
import org.srm.source.share.app.service.EvaluateScoreService;
import org.srm.source.share.app.service.impl.EvaluateScoreLineServiceImpl;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
import org.srm.source.share.domain.repository.SourceTemplateRepository;
import org.srm.source.share.domain.strategy.AutoScoreStrategyService;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
public class RcwlEvaluateScoreLineServiceImpl extends EvaluateScoreLineServiceImpl implements IRcwlEvaluateScoreLineService {

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
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private EvaluateScoreService evaluateScoreService;
    @Autowired
    private AutoScoreStrategyService autoScoreStrategyService;
    /**
     * 新写的
     */
    @Autowired
    private IRcwlRfxQuotationLineRepository rcwlRfxQuotationLineRepository;
    @Autowired
    private RcwlAutoScoreStrategyService rcwlAutoScoreStrategyService;

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
    public void simulateExpertDoScore(AutoScoreDTO autoScoreDTO, String subjectMatterRule, Map<Long, BigDecimal> quotationLineMaps, String priceTypeCode) {
        Long tenantId = autoScoreDTO.getTenantId();
        Long sourceHeaderId = autoScoreDTO.getSourceHeaderId();
        String sourceFrom = autoScoreDTO.getSourceFrom();
        List<EvaluateExpert> evaluateExperts = this.evaluateExpertRepository.queryEvaluateExpertAll(tenantId, sourceHeaderId, sourceFrom, (String)null, (String)null);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("evaluate experts : {}", evaluateExperts);
        }

        Iterator var9 = evaluateExperts.iterator();

        while(true) {
            EvaluateExpert evaluateExpert;
            do {
                if (!var9.hasNext()) {
                    return;
                }

                evaluateExpert = (EvaluateExpert)var9.next();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("evaluateExpert : {}", evaluateExpert.getEvaluateExpertId());
                }
            } while("TECHNOLOGY".equals(evaluateExpert.getTeam()));

            EvaluateScoreQueryDTO evaluateScoreQueryDTO = new EvaluateScoreQueryDTO(sourceHeaderId, tenantId, evaluateExpert.getExpertId(), evaluateExpert.getExpertUserId(), sourceFrom);
            evaluateScoreQueryDTO.setEvaluateExpertId(evaluateExpert.getEvaluateExpertId());
            evaluateScoreQueryDTO.setExpertSequenceNum(evaluateExpert.getSequenceNum());
            evaluateScoreQueryDTO.setSubjectMatterRule(subjectMatterRule);
            EvaluateExpertFullDTO evaluateExpertFullDTO = this.evaluateScoreService.selectAllEvaluateScore(evaluateScoreQueryDTO);
            evaluateExpertFullDTO.setExpertUserId(evaluateExpert.getExpertUserId());
            evaluateExpertFullDTO.setEvaluateExpertId(evaluateExpert.getEvaluateExpertId());
            if ("NONE".equals(subjectMatterRule)) {
                List<EvaluateScoreLineDTO> evaluateScoreLineDTOS = evaluateExpertFullDTO.getEvaluateScoreLineDTOS();
                Iterator var14 = evaluateScoreLineDTOS.iterator();

                label72:
                while(true) {
                    EvaluateScoreLineDTO evaluateScoreLineDTO;
                    EvaluateIndicDetail evaluateIndicDetail;
                    BigDecimal benchmarkPrice;
                    List evaluateScoreDTOS;
                    do {
                        do {
                            do {
                                if (!var14.hasNext()) {
                                    evaluateExpertFullDTO.setCurrentSequenceNum(evaluateExpert.getSequenceNum());
                                    this.self().saveOrUpdateAllEvaluateScoreNew(evaluateExpertFullDTO, sourceFrom, sourceHeaderId, tenantId);
                                    break label72;
                                }

                                evaluateScoreLineDTO = (EvaluateScoreLineDTO)var14.next();
                            } while(!"AUTO".equals(evaluateScoreLineDTO.getCalculateType()));

                            evaluateIndicDetail = evaluateScoreLineDTO.getEvaluateIndicDetail();
                        } while(Objects.isNull(evaluateIndicDetail));

                        if (!"PRICE".equals(evaluateScoreLineDTO.getScoreType())) {
                            throw new CommonException("only support price!", new Object[0]);
                        }

                        benchmarkPrice = this.autoScoreStrategyService.calcBenchmarkPrice(evaluateIndicDetail.getBenchmarkPriceMethod(), priceTypeCode, autoScoreDTO, evaluateIndicDetail);
                        evaluateScoreDTOS = evaluateScoreLineDTO.getEvaluateScoreDTOS();
                    } while(CollectionUtils.isEmpty(evaluateScoreDTOS));

                    Iterator var19 = evaluateScoreDTOS.iterator();

                    while(var19.hasNext()) {
                        EvaluateScoreDTO evaluateScoreDTO = (EvaluateScoreDTO)var19.next();
                        evaluateIndicDetail.setMaxScore(evaluateScoreDTO.getMaxScore());
                        // RCWL 计算评分
                        BigDecimal score = this.rcwlAutoScoreStrategyService.calcScore(evaluateIndicDetail.getFormula(), (BigDecimal)quotationLineMaps.get(evaluateScoreDTO.getQuotationHeaderId()), evaluateScoreLineDTO, evaluateIndicDetail, benchmarkPrice);
                        List<BigDecimal> scores = new ArrayList();
                        scores.add(evaluateScoreLineDTO.getMaxScore().compareTo(score) > 0 ? score : evaluateScoreLineDTO.getMaxScore());
                        scores.add(evaluateScoreLineDTO.getMinScore());
                        score = (BigDecimal) Collections.max(scores);
                        evaluateScoreDTO.setIndicScore(score);
                    }
                }
            }

            if ("PACK".equals(subjectMatterRule)) {
                this.bidAutoScorePACK(autoScoreDTO, priceTypeCode, evaluateExpert, evaluateExpertFullDTO);
                EvaluateExpertFullDTO evaluateExpertFullDTONew = this.rebuildEvaluateExpertFullDTO(evaluateExpertFullDTO);
                this.saveOrUpdateAllEvaluateScore(evaluateExpertFullDTONew, autoScoreDTO.getSourceFrom(), autoScoreDTO.getSourceHeaderId(), autoScoreDTO.getTenantId());
            }
        }
    }

    private EvaluateExpertFullDTO rebuildEvaluateExpertFullDTO(EvaluateExpertFullDTO evaluateExpertFullDTO) {
        EvaluateExpertFullDTO fullDTO = new EvaluateExpertFullDTO();
        List<EvaluateScoreLineDTO> evaluateScoreLineDTOS = new ArrayList();
        Iterator var4 = evaluateExpertFullDTO.getEvaluateSectionDTOS().iterator();

        while(var4.hasNext()) {
            EvaluateSectionDTO evaluateSectionDTO = (EvaluateSectionDTO)var4.next();
            evaluateScoreLineDTOS.addAll(evaluateSectionDTO.getEvaluateScoreLineDTOS());
        }

        fullDTO.setEvaluateScoreLineDTOS(evaluateScoreLineDTOS);
        fullDTO.setSectionFlag(1);
        fullDTO.setEvaluateExpertId(evaluateExpertFullDTO.getEvaluateExpertId());
        fullDTO.setExpertUserId(evaluateExpertFullDTO.getExpertUserId());
        return fullDTO;
    }

    @Override
    public Map<Long, BigDecimal> getRfxQuotationLineMaps(AutoScoreDTO autoScoreDTO, String priceTypeCode) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("24769---RCWL getRfxQuotationLineMaps");
        }
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
