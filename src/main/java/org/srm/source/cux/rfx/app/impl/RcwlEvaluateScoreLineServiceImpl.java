package org.srm.source.cux.rfx.app.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.AopProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.rfx.app.IRcwlEvaluateScoreLineService;
import org.srm.source.cux.rfx.domain.repository.IRcwlRfxQuotationLineRepository;
import org.srm.source.cux.rfx.domain.strategy.RcwlAutoScoreStrategyService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.api.dto.EvaluateExpertFullDTO;
import org.srm.source.share.api.dto.EvaluateScoreDTO;
import org.srm.source.share.api.dto.EvaluateScoreLineDTO;
import org.srm.source.share.api.dto.EvaluateScoreQueryDTO;
import org.srm.source.share.app.service.EvaluateScoreLineService;
import org.srm.source.share.app.service.EvaluateScoreService;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.domain.entity.EvaluateIndicDetail;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
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
 * @date 2021-05-18 20:13
 */
@Service
@Tenant("SRM-RCWL")
public class RcwlEvaluateScoreLineServiceImpl implements IRcwlEvaluateScoreLineService, AopProxy<IRcwlEvaluateScoreLineService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlEvaluateScoreLineServiceImpl.class);

    /**
     * 原始的
     */
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private EvaluateScoreService evaluateScoreService;
    @Autowired
    private CommonQueryRepository commonQueryRepository;

    /**
     * 新写的
     */
    @Autowired
    private IRcwlEvaluateScoreLineService evaluateScoreLineService;
    @Autowired
    private RcwlAutoScoreStrategyService autoScoreStrategyService;
    @Autowired
    private IRcwlRfxQuotationLineRepository rfxQuotationLineRepository;


    public static ThreadLocal<Boolean> AUTO_FLAG = new ThreadLocal<Boolean>() {
        @Override
        public Boolean initialValue() {
            return false;
        }
    };

    public RcwlEvaluateScoreLineServiceImpl() {
    }

    @Override
    public void autoEvaluateScore(AutoScoreDTO autoScoreDTO) {
        this._autoEvaluateScore(autoScoreDTO);
    }

    public void _autoEvaluateScore(AutoScoreDTO autoScoreDTO) {
        LOGGER.debug("auto process start");

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
        Map<Long, BigDecimal> quotationLineMaps = new HashMap<>();
        SourceTemplate sourceTemplate;
        if ("RFX".equals(sourceFrom)) {
            RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(sourceHeaderId);
            quotationLineMaps = this.evaluateScoreLineService.getRfxQuotationLineMaps(autoScoreDTO, priceTypeCode);
        }

        Map<Long, BigDecimal> validQuotationLineMaps = (Map)quotationLineMaps.entrySet().stream().filter((map) -> {
            return CollectionUtils.isEmpty(invalidQuotationHeaderIdList) || !invalidQuotationHeaderIdList.contains(map.getKey());
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        if (!CollectionUtils.isEmpty(validQuotationLineMaps.values())) {
            this.simulateExpertDoScore(autoScoreDTO, subjectMatterRule, quotationLineMaps, priceTypeCode);
        }
    }

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
                                    ((EvaluateScoreLineService)this.self()).saveOrUpdateAllEvaluateScoreNew(evaluateExpertFullDTO, sourceFrom, sourceHeaderId, tenantId);
                                    break label72;
                                }

                                evaluateScoreLineDTO = (EvaluateScoreLineDTO)var14.next();
                            } while(!"AUTO".equals(evaluateScoreLineDTO.getCalculateType()));

                            evaluateIndicDetail = evaluateScoreLineDTO.getEvaluateIndicDetail();
                        } while(Objects.isNull(evaluateIndicDetail));

                        if (!"PRICE".equals(evaluateScoreLineDTO.getScoreType())) {
                            throw new CommonException("only support price!");
                        }

                        benchmarkPrice = this.autoScoreStrategyService.calcBenchmarkPrice(evaluateIndicDetail.getBenchmarkPriceMethod(), priceTypeCode, autoScoreDTO, evaluateIndicDetail);
                        evaluateScoreDTOS = evaluateScoreLineDTO.getEvaluateScoreDTOS();
                    } while(CollectionUtils.isEmpty(evaluateScoreDTOS));

                    Iterator var19 = evaluateScoreDTOS.iterator();

                    while(var19.hasNext()) {
                        EvaluateScoreDTO evaluateScoreDTO = (EvaluateScoreDTO)var19.next();
                        evaluateIndicDetail.setMaxScore(evaluateScoreDTO.getMaxScore());
                        BigDecimal score = this.autoScoreStrategyService.calcScore(evaluateIndicDetail.getFormula(), (BigDecimal)quotationLineMaps.get(evaluateScoreDTO.getQuotationHeaderId()), evaluateScoreLineDTO, evaluateIndicDetail, benchmarkPrice);
                        List<BigDecimal> scores = new ArrayList();
                        scores.add(evaluateScoreLineDTO.getMaxScore().compareTo(score) > 0 ? score : evaluateScoreLineDTO.getMaxScore());
                        scores.add(evaluateScoreLineDTO.getMinScore());
                        score = (BigDecimal) Collections.max(scores);
                        evaluateScoreDTO.setIndicScore(score);
                    }
                }
            }
        }
    }

    @Override
    public Map<Long, BigDecimal> getRfxQuotationLineMaps(AutoScoreDTO autoScoreDTO, String priceTypeCode) {
        Long sourceHeaderId = autoScoreDTO.getSourceHeaderId();
        Long tenantId = autoScoreDTO.getTenantId();
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(sourceHeaderId);
        if (Objects.isNull(rfxHeader)) {
            throw new CommonException("rfx header not exists!");
        } else {
            // 获取有效报价行数据
            List<RfxQuotationLine> rfxQuotationLines = this.rfxQuotationLineRepository.querySumQuotationByRfxHeaderId(sourceHeaderId);
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
