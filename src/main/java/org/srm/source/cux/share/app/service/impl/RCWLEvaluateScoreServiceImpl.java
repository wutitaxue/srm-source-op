package org.srm.source.cux.share.app.service.impl;

import io.choerodon.core.exception.CommonException;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.srm.source.bid.app.service.BidLineItemService;
import org.srm.source.bid.app.service.common.BidSendMessageHandle;
import org.srm.source.bid.domain.entity.BidHeader;
import org.srm.source.bid.domain.repository.BidHeaderRepository;
import org.srm.source.bid.domain.service.IBidActionDomainService;
import org.srm.source.cux.share.infra.constant.SourceBaseConstant;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.service.IRfxActionDomainService;
import org.srm.source.share.api.dto.*;
import org.srm.source.share.app.service.EvaluateExpertService;
import org.srm.source.share.app.service.EvaluateScoreLineService;
import org.srm.source.share.app.service.impl.EvaluateScoreServiceImpl;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.domain.entity.EvaluateScore;
import org.srm.source.share.domain.entity.EvaluateScoreLine;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
import org.srm.source.share.domain.repository.EvaluateScoreLineRepository;
import org.srm.source.share.domain.repository.EvaluateScoreRepository;
import org.srm.source.share.infra.utils.ShareEventUtil;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/19 10:54
 * @version:1.0
 */
@Service
@Tenant(SourceBaseConstant.TENANT_NUM)
public class RCWLEvaluateScoreServiceImpl extends EvaluateScoreServiceImpl {
    @Autowired
    private EvaluateScoreLineService evaluateScoreLineService;
    @Autowired
    private BidLineItemService bidLineItemService;
    @Autowired
    private EvaluateScoreRepository evaluateScoreRepository;
    @Autowired
    private BidHeaderRepository bidHeaderRepository;
    @Autowired
    private IBidActionDomainService bidActionDomainService;
    @Autowired
    private IRfxActionDomainService rfxActionDomainService;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private EvaluateExpertService evaluateExpertService;
    @Autowired
    private BidSendMessageHandle bidSendMessageHandle;
    @Autowired
    private SendMessageHandle rfxSendMessageHandle;
    @Autowired
    private ShareEventUtil shareEventUtil;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private EvaluateScoreLineRepository evaluateScoreLineRepository;

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void submit(Long tenantId, EvaluateScoreQueryDTO evaluateScoreQueryDTO) {
        evaluateScoreQueryDTO.setTenantId(tenantId);
        EvaluateExpertFullDTO evaluateExpertFullDTO;
        if (evaluateScoreQueryDTO.getElementFlag().equals(BaseConstants.Flag.YES)) {
            this.checkBeforeSumbit(evaluateScoreQueryDTO);
            evaluateExpertFullDTO = new EvaluateExpertFullDTO(evaluateScoreQueryDTO.getEvaluateScoreLineDTOS(), evaluateScoreQueryDTO.getExpertUserId(), evaluateScoreQueryDTO.getSectionFlag());
            this.evaluateScoreLineService.saveOrUpdateAllEvaluateScore(evaluateExpertFullDTO, evaluateScoreQueryDTO.getSourceFrom(), evaluateScoreQueryDTO.getSourceHeaderId(), tenantId);
        } else {
            evaluateExpertFullDTO = this.selectAllEvaluateScore(evaluateScoreQueryDTO);
            evaluateScoreQueryDTO.setEvaluateScoreLineDTOS(evaluateExpertFullDTO.getEvaluateScoreLineDTOS());
            this.checkBeforeSumbit(evaluateScoreQueryDTO);
        }

        if ("PACK".equals(evaluateScoreQueryDTO.getSubjectMatterRule())) {
            List<EvaluateSectionDTO> evaluateSectionDTOS = this.bidLineItemService.selectSectionsByBidHeaderIdAndTenantId(tenantId, evaluateScoreQueryDTO.getSourceHeaderId(), (Long) null);
            Assert.notEmpty(evaluateSectionDTOS, "error.not_null");
            evaluateSectionDTOS.forEach((evaluateSectionDTO) -> {
                evaluateScoreQueryDTO.setBidLineItemId(evaluateSectionDTO.getBidLineItemId());
                List<EvaluateScoreDTO> evaluateScoreDTOS = this.selectEvaluateScoresBySectionId(evaluateScoreQueryDTO);
                this.updateEvaluateStatus(evaluateScoreDTOS, tenantId);
            });
        } else {
            List<EvaluateScoreDTO> evaluateSectionDTOS = this.selectEvaluateSuppliersBySourceHeaderIdAndTenantId(evaluateScoreQueryDTO);
            this.updateEvaluateStatus(evaluateSectionDTOS, tenantId);
        }

        Integer currentSequenceNum = null;
        BidHeader header = new BidHeader();
        RfxHeader rfxHeader = new RfxHeader();
        if ("BID".equals(evaluateScoreQueryDTO.getSourceFrom())) {
            header = (BidHeader) this.bidHeaderRepository.selectByPrimaryKey(evaluateScoreQueryDTO.getSourceHeaderId());
            Assert.notNull(header, "error.data_not_exists");
            currentSequenceNum = header.getCurrentSequenceNum();
            evaluateScoreQueryDTO.setSourceHeaderId(header.getBidHeaderId());
            this.bidActionDomainService.insertActionRecord(header, "SUBMIT_SCORE", (String) null);
            this.updateSourceHeaderStateAndSentMessage(evaluateScoreQueryDTO, header, rfxHeader);
        }

        if ("RFX".equals(evaluateScoreQueryDTO.getSourceFrom())) {
            rfxHeader = (RfxHeader) this.rfxHeaderRepository.selectByPrimaryKey(evaluateScoreQueryDTO.getSourceHeaderId());
            Assert.notNull(rfxHeader, "error.data_not_exists");
            currentSequenceNum = rfxHeader.getCurrentSequenceNum();
            evaluateScoreQueryDTO.setSourceHeaderId(rfxHeader.getRfxHeaderId());
            this.rfxActionDomainService.insertAction(rfxHeader, "SUBMIT_SCORE", (String) null);
            this.updateSourceHeaderStateAndSentMessage(evaluateScoreQueryDTO, header, rfxHeader);
        }

        EvaluateExpert evaluateExpert = new EvaluateExpert(tenantId, evaluateScoreQueryDTO.getExpertUserId(), evaluateScoreQueryDTO.getSourceHeaderId(), currentSequenceNum, "SCORED", evaluateScoreQueryDTO.getExpertAttachmentUuid(), evaluateScoreQueryDTO.getSourceFrom());
        this.evaluateExpertService.updateEvaluateExpert(evaluateExpert);
    }


    private void checkBeforeSumbit(EvaluateScoreQueryDTO evaluateScoreQueryDTO) {
        List<EvaluateScoreLineDTO> evaluateScoreLineDTOS = evaluateScoreQueryDTO.getEvaluateScoreLineDTOS();
        if (!CollectionUtils.isEmpty(evaluateScoreLineDTOS)) {
            Set<String> errorSuppliers = new HashSet();
            evaluateScoreLineDTOS.forEach((evaluateScoreLineDTO) -> {
                List<EvaluateScoreDTO> evaluateScoreDTOList = evaluateScoreLineDTO.getEvaluateScoreDTOS();
                if (CollectionUtils.isEmpty(evaluateScoreDTOList)) {
                    throw new CommonException("error.param", new Object[0]);
                } else if (Integer.valueOf(0).equals(evaluateScoreLineDTO.getDetailEnabledFlag())) {
                    Iterator var3 = evaluateScoreDTOList.iterator();

                    while (true) {
                        EvaluateScoreDTO evaluateScoreDTOx;
                        do {
                            if (!var3.hasNext()) {
                                return;
                            }

                            evaluateScoreDTOx = (EvaluateScoreDTO) var3.next();
                        }
                        while ((!"SCORE".equals(evaluateScoreLineDTO.getIndicateType()) || evaluateScoreDTOx.getIndicScore() != null) && (!"PASS".equals(evaluateScoreLineDTO.getIndicateType()) || !StringUtils.isEmpty(evaluateScoreDTOx.getPassStatus())));

                        errorSuppliers.add(evaluateScoreDTOx.getSupplierCompanyName());
                    }
                } else {
                    List<EvaluateScoreLineDtlDTO> evaluateScoreLineDtlDTOList = evaluateScoreLineDTO.getEvaluateScoreLineDetailS();
                    if (CollectionUtils.isEmpty(evaluateScoreLineDtlDTOList)) {
                        throw new CommonException("error.param", new Object[0]);
                    } else {
                        Iterator var10 = evaluateScoreLineDtlDTOList.iterator();

                        while (var10.hasNext()) {
                            EvaluateScoreLineDtlDTO evaluateScoreLineDtlDTO = (EvaluateScoreLineDtlDTO) var10.next();
                            List<EvaluateScoreDTO> evaluateScoreDTOS = evaluateScoreLineDtlDTO.getEvaluateScoreDTOS();
                            Iterator var7 = evaluateScoreDTOS.iterator();

                            while (var7.hasNext()) {
                                EvaluateScoreDTO evaluateScoreDTO = (EvaluateScoreDTO) var7.next();
                                if (evaluateScoreDTO.getIndicScore() == null) {
                                    errorSuppliers.add(evaluateScoreDTO.getSupplierCompanyName());
                                }
                            }
                        }

                    }
                }
            });
            if (!CollectionUtils.isEmpty(errorSuppliers)) {
                String errorMsg = (String) errorSuppliers.stream().map((e) -> {
                    return "【{MSG}】".replace("{MSG}", e);
                }).collect(Collectors.joining());
                throw new CommonException("error.supplier_not_scored", new Object[]{errorMsg});
            }
        }
    }


    private void updateEvaluateStatus(List<EvaluateScoreDTO> evaluateScores, Long tenantId) {
        Assert.notEmpty(evaluateScores, "error.not_null");
        List<Long> evaluateScoreIds = new ArrayList();
        List<EvaluateScore> scores = new ArrayList();
        evaluateScores.forEach((evaluateScoreDTO) -> {
            List<EvaluateTeamDTO> evaluateTeamDTOList = evaluateScoreDTO.getEvaluateTeamDTOList();
            evaluateTeamDTOList.forEach((evaluateTeamDTO) -> {
                Assert.notNull(evaluateTeamDTO.getEvaluateScoreId(), "error.incomplete_data_filling");
                evaluateScoreIds.add(evaluateTeamDTO.getEvaluateScoreId());
                EvaluateScore evaluateScore = new EvaluateScore(evaluateTeamDTO.getEvaluateScoreId(), "SCORED", evaluateTeamDTO.getObjectVersionNumber());
                scores.add(evaluateScore);
            });
        });
        this.checkEvaluateScoreIndic(evaluateScoreIds, tenantId);
        this.evaluateScoreRepository.batchUpdateByPrimaryKeySelective(scores);
    }


    private void checkEvaluateScoreIndic(List<Long> evaluateScoreIds, Long tenantId) {
        List<EvaluateScoreLine> evaluateIndics = this.evaluateScoreLineService.selectEvaluateScoreIndicsByEvaluateScoreIds(evaluateScoreIds, tenantId);
        evaluateIndics.forEach((evaluateScoreLine) -> {
            if ("SCORE".equals(evaluateScoreLine.getIndicateType())) {
                Assert.notNull(evaluateScoreLine.getIndicScore(), "error.incomplete_data_filling");
            }

            if ("PASS".equals(evaluateScoreLine.getIndicateType())) {
                Assert.notNull(evaluateScoreLine.getPassStatus(), "error.incomplete_data_filling");
            }

        });
    }


    private void updateSourceHeaderStateAndSentMessage(EvaluateScoreQueryDTO evaluateScoreQueryDTO, BidHeader header, RfxHeader rfxHeader) {
        int count = this.evaluateExpertService.selectExpertScoreNumByExpertIdAndSourceHeaderId(evaluateScoreQueryDTO);
        if (count == 0) {

            //评分负责人不评分，给各个评分人的均值
            this.evaluateLeaderScore(header, rfxHeader);

            if (header.getBidHeaderId() != null) {
                header.setBidStatus(SourceBaseConstant.RfxStatus.BID_EVALUATION_PENDING);
                this.bidHeaderRepository.updateByPrimaryKeySelective(header);
                this.bidSendMessageHandle.sendMessageForTeamLeader(header);
                this.shareEventUtil.eventSend(SourceBaseConstant.RfxCategory.SSRC_EVALUATE, SourceBaseConstant.EventCode.SSRC_EVALUATE_PENDING, SourceBaseConstant.RfxAction.SUMMARY_PENDING, header);
            }

            if (rfxHeader.getRfxHeaderId() != null) {
                rfxHeader.setRfxStatus(SourceBaseConstant.RfxStatus.RFX_EVALUATION_PENDING);
                this.rfxHeaderRepository.updateByPrimaryKeySelective(rfxHeader);
                this.rfxSendMessageHandle.sendMessageForTeamLeader(rfxHeader);
                this.shareEventUtil.eventSend(SourceBaseConstant.RfxCategory.SSRC_EVALUATE, SourceBaseConstant.EventCode.SSRC_EVALUATE_PENDING, SourceBaseConstant.RfxAction.SUMMARY_PENDING, rfxHeader);
            }

        }
    }

    /**
     * 给评分负责人均值
     *
     * @param
     * @param header
     * @param rfxHeader
     */
    private void evaluateLeaderScore( BidHeader header, RfxHeader rfxHeader) {
        Long sourceHeaderId = rfxHeader.getRfxHeaderId();
        String sourceFrom = SourceBaseConstant.SourceFrom.RFX;
        Integer currentSequenceNum = rfxHeader.getCurrentSequenceNum();
        if (null != header.getBidHeaderId()) {
            sourceHeaderId = header.getBidHeaderId();
            sourceFrom = SourceBaseConstant.SourceFrom.BID;
            currentSequenceNum = header.getCurrentSequenceNum();
        }
        //查出分数
        List<EvaluateScore> evaluateScores = this.evaluateScoreRepository.selectByCondition(Condition.builder(EvaluateScore.class)
                .andWhere(Sqls.custom().andEqualTo(EvaluateScore.FIELD_SOURCE_FROM, sourceFrom)
                        .andEqualTo(EvaluateScore.FIELD_SOURCE_HEADER_ID, sourceHeaderId)
                        .andEqualTo(EvaluateExpert.FIELD_SEQUENCE_NUM, currentSequenceNum)).build());
        //要素得分
        List<Long> scoreIds = evaluateScores.stream().map(v -> v.getEvaluateScoreId()).collect(Collectors.toList());
        List<EvaluateScoreLine> evaluateScoreLines = this.evaluateScoreLineRepository.selectByCondition(Condition.builder(EvaluateScoreLine.class)
                .andWhere(Sqls.custom().andIn(EvaluateScoreLine.FIELD_EVALUATE_SCORE_ID, scoreIds)).build());

        //查出评分人
        List<EvaluateExpert> evaluateExperts = this.evaluateExpertRepository.selectByCondition(Condition.builder(EvaluateExpert.class).andWhere(Sqls.custom().andEqualTo(EvaluateExpert.FIELD_SOURCE_HEADER_ID, sourceHeaderId)
                .andEqualTo(EvaluateExpert.FIELD_SOURCE_FROM, sourceFrom).andEqualTo(EvaluateExpert.FIELD_SEQUENCE_NUM, currentSequenceNum)).build());
        //过滤出评分负责人
        List<EvaluateExpert> evaluateLeaderExperts = evaluateExperts.stream().filter(e -> BaseConstants.Flag.YES.equals(e.getEvaluateLeaderFlag())).collect(Collectors.toList());
        Long evaluateExpertId = evaluateLeaderExperts.get(0).getEvaluateExpertId();
        //取出评分负责人的数据
        List<EvaluateScore> evaluateLeaderCollect = evaluateScores.stream().filter(e -> evaluateExpertId.equals(e.getEvaluateExpertId())).collect(Collectors.toList());
        Set<Long> evaluateLeaderScoreIds = evaluateLeaderCollect.stream().map(v -> v.getEvaluateScoreId()).collect(Collectors.toSet());
        List<EvaluateScoreLine> evaluateLeaderScoreLine = evaluateScoreLines.stream().filter(e -> evaluateLeaderScoreIds.contains(e.getEvaluateScoreId())).collect(Collectors.toList());
        Map<Long, List<EvaluateScore>> leader = evaluateLeaderCollect.stream().collect(Collectors.groupingBy(EvaluateScore::getQuotationHeaderId));
        Map<Long, List<Long>> leaderIds = this.quotationAndScoreIds(leader);
        //取出评分其他人的数据
        List<EvaluateScore> evaluateCollect = evaluateScores.stream().filter(e -> !evaluateExpertId.equals(e.getEvaluateExpertId())).collect(Collectors.toList());
        Set<Long> evaluateScoreIds = evaluateCollect.stream().map(v -> v.getEvaluateScoreId()).collect(Collectors.toSet());
        List<EvaluateScoreLine> evaluateScoreLine = evaluateScoreLines.stream().filter(e -> evaluateScoreIds.contains(e.getEvaluateScoreId())).collect(Collectors.toList());
        //分数对应报价单
        Map<Long, List<EvaluateScore>> longListMap = evaluateCollect.stream().collect(Collectors.groupingBy(EvaluateScore::getQuotationHeaderId));
        Map<Long, List<Long>> quotationAndScoreIds = this.quotationAndScoreIds(longListMap);
        //要素分平均  通过要素分类计算
        Map<Long, List<EvaluateScoreLine>> lineScoreMap = evaluateScoreLine.stream().collect(Collectors.groupingBy(EvaluateScoreLine::getEvaluateIndicId));
        Map<Long, List<EvaluateScoreLine>> scoreLines = new HashMap<>();
        for (Long quoId : quotationAndScoreIds.keySet()) {
            List<Long> longs = quotationAndScoreIds.get(quoId);
            List<EvaluateScoreLine> lines = new ArrayList<>();
            for (Long indicId : lineScoreMap.keySet()) {
                EvaluateScoreLine evgScoreLine = new EvaluateScoreLine();
                List<EvaluateScoreLine> lineList = lineScoreMap.get(indicId);
                BigDecimal reduce = lineList.stream().filter(e -> longs.contains(e.getEvaluateScoreId())).map(v -> ObjectUtils.isEmpty(v.getIndicScore()) ? BigDecimal.ZERO : v.getIndicScore()).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal divide = reduce.divide(BigDecimal.valueOf(longs.size()), 2, BigDecimal.ROUND_HALF_UP);
                evgScoreLine.setIndicScore(divide);
                evgScoreLine.setEvaluateIndicId(indicId);
                lines.add(evgScoreLine);
            }
            scoreLines.put(quoId, lines);
        }
        //要素评分
        evaluateLeaderScoreLine.forEach(line -> {
            for (Long quoID : scoreLines.keySet()) {
                Long scoreId = leaderIds.get(quoID).get(0);
                EvaluateScoreLine scoreLine = scoreLines.get(quoID).stream().filter(e -> e.getEvaluateIndicId().equals(line.getEvaluateIndicId())).collect(Collectors.toList()).get(0);
                if(scoreId.equals(line.getEvaluateScoreId())&&line.getEvaluateIndicId().equals(scoreLine.getEvaluateIndicId())){
                    line.setIndicScore(scoreLine.getIndicScore());
                }
            }
        });
        //总分平均
        Map<Long, BigDecimal> evgScore = new HashMap<>();
        for (Long key : longListMap.keySet()) {
            BigDecimal reduce = longListMap.get(key).stream().map(v -> ObjectUtils.isEmpty(v.getSumIndicScore()) ? BigDecimal.ZERO : v.getSumIndicScore()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal divide = reduce.divide(BigDecimal.valueOf(evaluateExperts.size() - evaluateLeaderExperts.size()), 2, BigDecimal.ROUND_HALF_UP);
            evgScore.put(key, divide);
        }

        //赋值
        evaluateLeaderCollect.forEach(e -> {
            Long id = evgScore.keySet().stream().filter(s -> s.equals(e.getQuotationHeaderId())).collect(Collectors.toList()).get(0);
            e.setSumIndicScore(evgScore.get(id));
            e.setExpertSuggestion(SourceBaseConstant.Text.EXPERT_SUGGESTION);
            e.setScoreStatus(SourceBaseConstant.ScoredStatus.SCORED);
        });
        //评分保存
        this.evaluateScoreLineRepository.batchUpdateByPrimaryKeySelective(evaluateLeaderScoreLine);
        this.evaluateScoreRepository.batchUpdateByPrimaryKeySelective(evaluateLeaderCollect);
        //评分人状态保存
        evaluateLeaderExperts.forEach(e -> {
            e.setScoredStatus(SourceBaseConstant.ScoredStatus.SCORED);
        });
        this.evaluateExpertRepository.batchUpdateByPrimaryKeySelective(evaluateLeaderExperts);
    }

    private Map<Long, List<Long>> quotationAndScoreIds(Map<Long, List<EvaluateScore>> scoreMap) {
        Map<Long, List<Long>> quotationAndScoreIds = new HashMap<>();
        for (Long quotationId : scoreMap.keySet()) {
            List<Long> scoreIdList = new ArrayList<>();
            scoreMap.get(quotationId).forEach(e -> {
                scoreIdList.add(e.getEvaluateScoreId());
            });
            quotationAndScoreIds.put(quotationId, scoreIdList);
        }
        return quotationAndScoreIds;
    }
}
