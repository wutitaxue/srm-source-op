package org.srm.source.cux.share.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.source.bid.domain.entity.BidHeader;
import org.srm.source.bid.domain.repository.BidHeaderRepository;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.cux.rfx.domain.repository.IRcwlRfxQuotationLineRepository;
import org.srm.source.cux.rfx.domain.strategy.RcwlAutoScoreStrategyService;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.cux.share.infra.constant.Constant;
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
import org.srm.source.share.domain.entity.EvaluateSummary;
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
@Tenant(Constant.TENANT_NUM)
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
//    @Autowired
//    private AutoScoreStrategyService autoScoreStrategyService;
    /**
     * 新写的
     */
    @Autowired
    private IRcwlRfxQuotationLineRepository rcwlRfxQuotationLineRepository;
    @Autowired
    private RcwlAutoScoreStrategyService rcwlAutoScoreStrategyService;

    public RcwlEvaluateScoreLineServiceImpl() {
    }

    @Override
    public void _autoEvaluateScore(AutoScoreDTO autoScoreDTO) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("24769---RCWL _autoEvaluateScore auto evaluate score started");
        }

        AUTO_FLAG.set(true);
        String sourceFrom = autoScoreDTO.getSourceFrom();
        Long sourceHeaderId = autoScoreDTO.getSourceHeaderId();
        Long tenantId = autoScoreDTO.getTenantId();
        List<Long> invalidQuotationHeaderIdList = autoScoreDTO.getInvalidQuotationHeaderIdList();
        String subjectMatterRule = "NONE";
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("auto evaluate score started, params => [sourceFrom : {},sourceHeaderId:{},tenantId:{}]", sourceFrom, sourceHeaderId, tenantId);
        }
        LOGGER.info("invalidQuotationHeaderIdList : {}",invalidQuotationHeaderIdList);
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
            // 获取报价头id，与报价行价格；
            try {
                IRcwlEvaluateScoreLineService self = (IRcwlEvaluateScoreLineService) AopContext.currentProxy();
                LOGGER.info("24769 RCWL self: {}",self);
                System.out.println(self);
                quotationLineMaps = self.getRfxQuotationLineMaps(autoScoreDTO, priceTypeCode);
            } catch (Exception e) {
                LOGGER.info("24769 RCWL self Exception:");
                e.printStackTrace();
                quotationLineMaps = this.self().getRfxQuotationLineMaps(autoScoreDTO, priceTypeCode);
            }
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

        // 筛选出有效的报价行(评分确认及汇总的时候，要重新评分，被勾选无效的供应商要过滤掉)
        Map<Long,BigDecimal> validQuotationLineMaps = quotationLineMaps
                .entrySet()
                .stream()
                .filter(map -> CollectionUtils.isEmpty(invalidQuotationHeaderIdList) || !invalidQuotationHeaderIdList.contains(map.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
        if(CollectionUtils.isEmpty(validQuotationLineMaps.values())){
            // 在评分确认及汇总的时候，如果所有供应商都被勾选无效了，那就不需要重新评分了
            return ;
        }
        simulateExpertDoScore(autoScoreDTO,subjectMatterRule,quotationLineMaps,priceTypeCode);
    }

    @Override
    public void simulateExpertDoScore(AutoScoreDTO autoScoreDTO, String subjectMatterRule, Map<Long, BigDecimal> quotationLineMaps, String priceTypeCode) {
        Long tenantId = autoScoreDTO.getTenantId();
        Long sourceHeaderId = autoScoreDTO.getSourceHeaderId();
        String sourceFrom = autoScoreDTO.getSourceFrom();
        // 先查专家
        List<EvaluateExpert> evaluateExperts = this.evaluateExpertRepository.queryEvaluateExpertAll(tenantId, sourceHeaderId, sourceFrom, null, null);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("evaluate experts : {}", evaluateExperts);
        }

        // 循环专家查询寻源单下的评分(要素维度)
        for (EvaluateExpert evaluateExpert : evaluateExperts) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("evaluateExpert : {}", evaluateExpert.getEvaluateExpertId());
            }
            if ("TECHNOLOGY".equals(evaluateExpert.getTeam())){
                // 技术没有自动评分，直接跳过
                continue;
            }
            EvaluateScoreQueryDTO evaluateScoreQueryDTO = new EvaluateScoreQueryDTO(sourceHeaderId, tenantId, evaluateExpert.getExpertId(), evaluateExpert.getExpertUserId(), sourceFrom);
            evaluateScoreQueryDTO.setEvaluateExpertId(evaluateExpert.getEvaluateExpertId());
            evaluateScoreQueryDTO.setExpertSequenceNum(evaluateExpert.getSequenceNum());
            evaluateScoreQueryDTO.setSubjectMatterRule(subjectMatterRule);
            EvaluateExpertFullDTO evaluateExpertFullDTO = evaluateScoreService.selectAllEvaluateScore(evaluateScoreQueryDTO);
            evaluateExpertFullDTO.setExpertUserId(evaluateExpert.getExpertUserId());
            evaluateExpertFullDTO.setEvaluateExpertId(evaluateExpert.getEvaluateExpertId());
            if (BidConstants.BidHeader.SubjectMatterRule.NONE.equals(subjectMatterRule)){
                // 拿到一级要素
                List<EvaluateScoreLineDTO> evaluateScoreLineDTOS = evaluateExpertFullDTO.getEvaluateScoreLineDTOS();

                // 循环要素,如果是自动计算的类型则填上分数
                for (EvaluateScoreLineDTO evaluateScoreLineDTO : evaluateScoreLineDTOS) {
                    if (!"AUTO".equals(evaluateScoreLineDTO.getCalculateType())) {
                        continue;
                    }
                    // 如果是自动评分的要素，自动计算评分
                    // 拿到要素评分细则
                    EvaluateIndicDetail evaluateIndicDetail = evaluateScoreLineDTO.getEvaluateIndicDetail();
                    if (Objects.isNull(evaluateIndicDetail)) {
                        continue;
                    }
                    if(!"PRICE".equals(evaluateScoreLineDTO.getScoreType())){
                        throw new CommonException("only support price!");
                    }
                    // RCWL 计算基准价
                    BigDecimal benchmarkPrice = this.rcwlAutoScoreStrategyService.calcBenchmarkPrice(evaluateIndicDetail.getBenchmarkPriceMethod(), priceTypeCode, autoScoreDTO, evaluateIndicDetail);
//                    BigDecimal benchmarkPrice1 = this.autoScoreStrategyService.calcBenchmarkPrice(evaluateIndicDetail.getBenchmarkPriceMethod(), priceTypeCode, autoScoreDTO, evaluateIndicDetail);
                    LOGGER.info("24769  benchmarkPrice RCWL : {} ", benchmarkPrice);
                    //每一个供应商
                    List<EvaluateScoreDTO> evaluateScoreDTOS = evaluateScoreLineDTO.getEvaluateScoreDTOS();
                    if (CollectionUtils.isEmpty(evaluateScoreDTOS)) {
                        continue;
                    }
                    for (EvaluateScoreDTO evaluateScoreDTO : evaluateScoreDTOS) {
                        evaluateIndicDetail.setMaxScore(evaluateScoreDTO.getMaxScore());
                        // RCWL 计算评分
                        BigDecimal score = this.rcwlAutoScoreStrategyService.calcScore(evaluateIndicDetail.getFormula(), quotationLineMaps.get(evaluateScoreDTO.getQuotationHeaderId()), evaluateScoreLineDTO, evaluateIndicDetail, benchmarkPrice);
//                        BigDecimal score1 = this.autoScoreStrategyService.calcScore(evaluateIndicDetail.getFormula(), quotationLineMaps.get(evaluateScoreDTO.getQuotationHeaderId()), evaluateScoreLineDTO, evaluateIndicDetail, benchmarkPrice);
//                        LOGGER.info("24769  scoreRcwl : {} , score : {}", score,score1);
                        LOGGER.info("24769  scoreRcwl : {} ", score);
                        //取最大分数
                        List<BigDecimal> scores = new ArrayList<>();
                        scores.add(evaluateScoreLineDTO.getMaxScore().compareTo(score)>0?score:evaluateScoreLineDTO.getMaxScore());
                        scores.add(evaluateScoreLineDTO.getMinScore());
                        score = Collections.max(scores);
                        evaluateScoreDTO.setIndicScore(score);
                    }
                }
                // 保存
                evaluateExpertFullDTO.setCurrentSequenceNum(evaluateExpert.getSequenceNum());
                this.self().saveOrUpdateAllEvaluateScoreNew(evaluateExpertFullDTO,sourceFrom,sourceHeaderId,tenantId);
            }
            if (BidConstants.BidHeader.SubjectMatterRule.PACK.equals(subjectMatterRule)) {
                // 执行填分操作
                bidAutoScorePACK(autoScoreDTO, priceTypeCode, evaluateExpert, evaluateExpertFullDTO);
                EvaluateExpertFullDTO evaluateExpertFullDTONew = rebuildEvaluateExpertFullDTO(evaluateExpertFullDTO);
                // 调用保存
                this.saveOrUpdateAllEvaluateScore(evaluateExpertFullDTONew,autoScoreDTO.getSourceFrom(),autoScoreDTO.getSourceHeaderId(),autoScoreDTO.getTenantId());

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
        // 有效报价头表id
        List<Long> invalidQuotationHeaderIdList = autoScoreDTO.getInvalidQuotationHeaderIdList();
        RfxHeader rfxHeader = this.rfxHeaderRepository.selectByPrimaryKey(sourceHeaderId);
        if (Objects.isNull(rfxHeader)) {
            throw new CommonException("rfx header not exists!");
        } else {
            // RCWL 获取有效报价行数据
            List<RfxQuotationLine> rfxQuotationLines = this.rcwlRfxQuotationLineRepository.querySumQuotationByRfxHeaderId(sourceHeaderId,tenantId);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("rfx quotation lines : {}", rfxQuotationLines);
            }

            if (CollectionUtils.isEmpty(rfxQuotationLines)) {
                throw new CommonException("rfx quotation line not exists!");
            } else {
                // key供应商 value供应商投标总价,（含税/不含税）之和，过滤勾选无效报价供应商
                Map<Long, BigDecimal> quotationLineMaps = rfxQuotationLines
                        .stream()
                        .filter(item -> CollectionUtils.isEmpty(invalidQuotationHeaderIdList) || !invalidQuotationHeaderIdList.contains(item.getQuotationHeaderId()))
                        .collect(Collectors.toMap(RfxQuotationLine::getQuotationHeaderId, (quotationLine) -> {
                            // 获取租户编码
                            String tenantNum = this.commonQueryRepository.selectTenantExists(tenantId).get(0);
                            // ssrc_rfx_header 头表 attribute_varchar16 值为 Y 含税金额, N为不含税金额。
                            return "Y".equals(rfxHeader.getAttributeVarchar16()) && "SRM-RCWL".equals(tenantNum) ? quotationLine.getTotalAmount() : quotationLine.getNetAmount();
                        }));
                return quotationLineMaps;
            }
        }
    }

    @Override
    public List<Long> getInvalidQuotationHeaderIdList(RfxHeader rfxHeader) {
        // 获取专家评分汇总信息
        List<EvaluateSummary> evaluateSummary = this.rcwlRfxQuotationLineRepository.queryEvaluateSummary(new EvaluateSummary(rfxHeader.getTenantId(),rfxHeader.getRfxHeaderId(),"RFX",rfxHeader.getRoundNumber()));

        // 获取无效报价头 ID
        return evaluateSummary
                .stream()
                .filter(item -> BaseConstants.Flag.YES.equals(item.getInvalidFlag()))
                .map(EvaluateSummary::getQuotationHeaderId)
                .collect(Collectors.toList());
    }
}
