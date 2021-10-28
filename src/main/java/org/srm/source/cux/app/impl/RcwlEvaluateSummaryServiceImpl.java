package org.srm.source.cux.app.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.starter.keyencrypt.core.EncryptContext;
import org.hzero.starter.keyencrypt.core.IEncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.source.bid.infra.constant.BidConstants;
import org.srm.source.share.api.dto.EvaluateSummaryDTO;
import org.srm.source.share.api.dto.EvaluateSupplierDTO;
import org.srm.source.share.app.service.impl.EvaluateSummaryServiceImpl;
import org.srm.source.share.domain.entity.EvaluateScore;
import org.srm.source.share.domain.entity.EvaluateScoreLine;
import org.srm.source.share.domain.entity.EvaluateScoreLineDtl;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.source.share.infra.mapper.EvaluateSummaryMapper;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.*;

import static org.srm.source.cux.share.infra.constant.Constant.TENANT_NUM;

/**
 * @Author: longjunquan 21420
 * @Date: 2021/10/28 19:22
 * @Description:
 */
@Service
@Tenant(TENANT_NUM)
public class RcwlEvaluateSummaryServiceImpl extends EvaluateSummaryServiceImpl {
    @Autowired
    private EvaluateSummaryMapper evaluateSummaryMapper;
    @Autowired
    private IEncryptionService iEncryptionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlEvaluateSummaryServiceImpl.class);


    @Override
    public List<EvaluateSupplierDTO> getSupplierList(EvaluateSummaryDTO evaluateSummaryDTO , Map<String , Map<String , List>> scoreDetailMap , SourceTemplate sourceTemplate , String bidLineItemId){
        LOGGER.info("getSupplierList============{}==",evaluateSummaryDTO);
        //查询出供应商,每个供应商下面的所有评分(一个评分针对多个评分要素), 每个评分下面的评分详情(针对每个评分要素的打分情况) ,查出数据是树结构(供应商->评分->评分详情)
        List<EvaluateSummaryDTO> supplierListDB ;
        if(ShareConstants.SourceCategory.BID.equals(evaluateSummaryDTO.getSourceFrom())){
            supplierListDB = evaluateSummaryMapper.supplierEvaluateInfoBID(evaluateSummaryDTO);
        }else{
            supplierListDB = evaluateSummaryMapper.supplierEvaluateInfoRFX(evaluateSummaryDTO);
        }
        List<EvaluateSupplierDTO> supplierList = new ArrayList<>();
        supplierListDB.forEach(supplier ->{
            String quotationHeaderId = supplier.getQuotationHeaderId().toString();
            String quotationHeaderIdEncrypt = quotationHeaderId;
            if (EncryptContext.isEncrypt()){
                //由于ID作为Map主键返回，导致无法通过框架加密，故主动加密处理
                quotationHeaderIdEncrypt = iEncryptionService.encrypt(String.valueOf(quotationHeaderId),"") ;
            }
            scoreDetailMap.put(quotationHeaderIdEncrypt , new HashMap<>() );
            BigDecimal businessWeight = null ;
            BigDecimal technologyWeight = null ;
            //这个for循环获取得每个要素的详情
            for(EvaluateScore score : supplier.getEvaluateScoreList()){
                for(EvaluateScoreLine scoreLineDB : score.getEvaluateScoreLineList()){
                    //考虑到区分商务技术的时候,indicateId+team才能确定唯一
                    //由于添加手动维护评分要素的需求，导致indicateId未必有值，所以改成indicateTitle
                    String indicateIdAndTeam = scoreLineDB.getIndicateTitle()+"_"+scoreLineDB.getTeam();
                    if(scoreDetailMap.get(quotationHeaderIdEncrypt).get(indicateIdAndTeam) == null){
                        scoreDetailMap.get(quotationHeaderIdEncrypt).put(indicateIdAndTeam , new ArrayList<EvaluateSupplierDTO.EvaluateScoreDetail>());
                    }
                    //scoreDetail : 当鼠标悬浮的时候,显示每个要素的专家的评分详情
                    EvaluateSupplierDTO.EvaluateScoreDetail scoreDetail = new EvaluateSupplierDTO.EvaluateScoreDetail();
                    scoreDetail.setDetailEnabledFlag(scoreLineDB.getDetailEnabledFlag());
                    scoreDetail.setSubAccount(score.getSubAccount());
                    scoreDetail.setExpertName(score.getExpertName());
                    scoreDetail.setExpertId(score.getExpertId());
                    scoreDetail.setIndicateTitle(scoreLineDB.getIndicateTitle());
                    scoreDetail.setIndicateName(scoreLineDB.getIndicateName());
                    scoreDetail.setExpertSuggestion(score.getExpertSuggestion());
                    scoreDetail.setSuggestInvalidFlag(score.getSuggestInvalidFlag());
                    scoreDetail.setRemark( scoreLineDB.getRemark());
                    scoreDetail.setEvaluateScoreDetailFullList(new ArrayList<>());
                    scoreDetail.setIndicateType(scoreLineDB.getIndicateType());
                    scoreDetail.setTeam(scoreLineDB.getTeam());
                    scoreDetail.setWeight(scoreLineDB.getWeight());
                    scoreDetail.setEvaluateScoreId(score.getEvaluateScoreId());
                    scoreDetail.setIndicateId(scoreLineDB.getEvaluateIndicId());
                    //二级评分要素改造
                    if(Integer.valueOf(0).equals(scoreLineDB.getDetailEnabledFlag())){
                        //如果要素没有启用评分要素细项，逻辑跟之前保持一至，直接在ssrc_evaluate_score_line表中打的分
                        scoreDetail.setIndicScore(scoreLineDB.getIndicScore());
                        scoreDetail.setPassStatus(scoreLineDB.getPassStatus());
                    }else{
                        //如果启用了二级评分要素细项，所有的分数就需要从二级要素中去计算
                        List<EvaluateScoreLineDtl> evaluateScoreLineDtlList = scoreLineDB.getEvaluateScoreLineDtlList();
                        if(ShareConstants.SourceCategory.RFX.equals(evaluateSummaryDTO.getSourceFrom()) &&CollectionUtils.isEmpty(evaluateScoreLineDtlList)){
                            throw new CommonException(ShareConstants.ErrorCode.ERROR_NOT_EXIST_TOW_LEVEL_INDICATE , scoreLineDB.getIndicateName());
                        }
                        evaluateScoreLineDtlList.forEach(evaluateScoreLineDtl -> {
                            EvaluateSupplierDTO.EvaluateScoreDetailFull evaluateScoreDetailFull = new EvaluateSupplierDTO.EvaluateScoreDetailFull();
                            evaluateScoreDetailFull.setTwoIndicateName(evaluateScoreLineDtl.getTwoIndicateName());
                            evaluateScoreDetailFull.setTwoMinScore(evaluateScoreLineDtl.getTwoMinScore());
                            evaluateScoreDetailFull.setTwoMaxScore(evaluateScoreLineDtl.getTwoMaxScore());
                            evaluateScoreDetailFull.setTwoWeight(evaluateScoreLineDtl.getTwoWeight());
                            evaluateScoreDetailFull.setTwoScore(evaluateScoreLineDtl.getTwoIndicateScore());
                            evaluateScoreDetailFull.setEvaluateDetailId(evaluateScoreLineDtl.getEvaluateDetailId());
                            evaluateScoreDetailFull.setEvaluateIndicateId(scoreLineDB.getEvaluateIndicId());
                            scoreDetail.getEvaluateScoreDetailFullList().add(evaluateScoreDetailFull);
                        });
                    }

                    if(ShareConstants.Expert.ExpertCategory.BUSINESS.equals(scoreLineDB.getTeam())){
                        Assert.notNull(scoreLineDB.getBusinessWeight()  , ShareConstants.ErrorCode.BUSINESS_WEIGHT_NOT_NULL);
                        if(businessWeight == null){
                            businessWeight = scoreLineDB.getBusinessWeight();
                        }else if(!businessWeight.equals(scoreLineDB.getBusinessWeight())){
                            throw new CommonException(ShareConstants.ErrorCode.SAME_TEAM_SAME_WEIGHT);
                        }
                    }else if(ShareConstants.Expert.ExpertCategory.TECHNOLOGY.equals(scoreLineDB.getTeam())){
                        Assert.notNull(scoreLineDB.getTechnologyWeight()  ,  ShareConstants.ErrorCode.TECHNOLOGY_WEIGHT_NOT_NULL);
                        if(technologyWeight == null){
                            technologyWeight = scoreLineDB.getTechnologyWeight();
                        }else if(!technologyWeight.equals(scoreLineDB.getTechnologyWeight())){
                            throw new CommonException(ShareConstants.ErrorCode.SAME_TEAM_SAME_WEIGHT);
                        }
                    }

                    scoreDetailMap.get(quotationHeaderIdEncrypt).get(indicateIdAndTeam).add(scoreDetail);
                }
            }

            EvaluateSupplierDTO supplierDTO = getSupplier(scoreDetailMap.get(quotationHeaderIdEncrypt) ,sourceTemplate ,  bidLineItemId , quotationHeaderId ,businessWeight ,  technologyWeight );
            supplierDTO.setQuotationHeaderId( supplier.getQuotationHeaderId());
            supplierDTO.setInvalidFlag(supplier.getInvalidFlag());
            supplierDTO.setEvaluateSummaryId(supplier.getEvaluateSummaryId());
            supplierDTO.setSupplierCompanyId(supplier.getSupplierCompanyId());
            supplierDTO.setSupplierCompanyName(supplier.getSupplierCompanyName());
            supplierDTO.setTechnologyWeight(technologyWeight);
            supplierDTO.setBusinessWeight(businessWeight);
            supplierDTO.setSourceFrom(evaluateSummaryDTO.getSourceFrom());
            supplierDTO.setSourceHeaderId(evaluateSummaryDTO.getSourceHeaderId());
            supplierDTO.setSupplierCompanyIp(supplier.getSupplierCompanyIp());
            // 如果开标步制是先商务后技术 , 则标书规则一定是区分商务/技术
            if(ShareConstants.SourceTemplate.OpenBidOrder.BUSINESS_FIRST.equals(sourceTemplate.getOpenBidOrder())){
                //判断当前是商务汇总还是技术汇总
                if(BidConstants.BidHeader.CurrentSequenceNum.FIRST.equals(supplier.getSequenceNum())){
                    supplierDTO.setValidBusinessAttachmentUuid(supplier.getValidBusinessAttachmentUuid());
                }else{
                    supplierDTO.setValidTechAttachmentUuid(supplier.getValidTechAttachmentUuid());
                }
            }else if(ShareConstants.SourceTemplate.OpenBidOrder.TECH_FIRST.equals(sourceTemplate.getOpenBidOrder())){
                //modified by 21420 融创二开，商务确认时需要看见全部的附件
               // if(BidConstants.BidHeader.CurrentSequenceNum.FIRST.equals(supplier.getSequenceNum())){
                    supplierDTO.setValidTechAttachmentUuid(supplier.getValidTechAttachmentUuid());
               // }else{
                    supplierDTO.setValidBusinessAttachmentUuid(supplier.getValidBusinessAttachmentUuid());
              //  }
            }else{
                supplierDTO.setValidTechAttachmentUuid(supplier.getValidTechAttachmentUuid());
                supplierDTO.setValidBusinessAttachmentUuid(supplier.getValidBusinessAttachmentUuid());
            }
            supplierList.add(supplierDTO);
        });

        return supplierList;
    }
}
