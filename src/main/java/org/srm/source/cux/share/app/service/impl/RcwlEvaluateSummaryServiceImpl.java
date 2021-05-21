package org.srm.source.cux.share.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.choerodon.core.exception.CommonException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants.Flag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.srm.source.cux.share.infra.constant.SourceBaseConstant;
import org.srm.source.rfx.app.service.RfxQuotationHeaderService;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.share.api.dto.EvaluateSummaryDTO;
import org.srm.source.share.api.dto.EvaluateSupplierDTO;
import org.srm.source.share.api.dto.EvaluateSupplierDTO.EvaluateIndicate;
import org.srm.source.share.app.service.impl.EvaluateSummaryServiceImpl;
import org.srm.source.share.domain.entity.EvaluateSummary;
import org.srm.source.share.domain.repository.EvaluateScoreLineRepository;
import org.srm.source.share.domain.service.IEvaluateDomainService;
import org.srm.web.annotation.Tenant;

/**
 * @author kaibo.li
 * @date 2021-05-21 15:51
 */
@Service
@Tenant(SourceBaseConstant.TENANT_NUM)
public class RcwlEvaluateSummaryServiceImpl extends EvaluateSummaryServiceImpl {
    @Autowired
    FileClient fileClient;
    @Autowired
    IEvaluateDomainService evaluateDomainServiceImpl;
    @Autowired
    EvaluateScoreLineRepository evaluateScoreLineRepository;
    @Autowired
    SendMessageHandle rfxSendMessageHandle;
    @Autowired
    RfxQuotationHeaderService rfxQuotationHeaderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluateSummaryServiceImpl.class);

    public RcwlEvaluateSummaryServiceImpl() {
        super();
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public List preProcessing(List<EvaluateSummary> summaryList) {
        LOGGER.info("24769 RCWL preProcessing ==============================");
        if (CollectionUtils.isEmpty((Collection)summaryList)) {
            return (List)summaryList;
        } else {

//            List<Long> invalidQuotationHeaderIdList = (List)((List)).stream().filter((item) -> {
//                return Flag.YES.equals(item.getInvalidFlag());
//            }).map(EvaluateSummary::getQuotationHeaderId).collect(Collectors.toList());

            List<Long> invalidQuotationHeaderIdList = summaryList
                    .stream()
                    .filter(item -> Flag.YES.equals(item.getInvalidFlag()))
                    .map(EvaluateSummary::getQuotationHeaderId)
                    .collect(Collectors.toList());

            EvaluateSummary firstSummary = (EvaluateSummary)((List)summaryList).get(0);
            String sourceFrom = firstSummary.getSourceFrom();
            Long sourceHeaderId = firstSummary.getSourceHeaderId();
            Long organizationId = firstSummary.getTenantId();
            if (CollectionUtils.isEmpty(invalidQuotationHeaderIdList)) {
                return (List)summaryList;
            } else if (!StringUtils.isEmpty(sourceFrom) && sourceHeaderId != null && organizationId != null) {
                // this.evaluateScoreLineService._autoEvaluateScore(new AutoScoreDTO(organizationId, sourceFrom, sourceHeaderId, invalidQuotationHeaderIdList));
                Map supplierEvaluateInfoMap = null;
                String subjectMatterRule;
                if ("BID".equals(sourceFrom) && Objects.isNull(firstSummary.getBidLineItemIds())) {
                    Set<Long> collect = (Set)summaryList.stream().map(EvaluateSummary::getSectionId).collect(Collectors.toSet());

                    subjectMatterRule = (String)collect.stream().map(String::valueOf).collect(Collectors.joining(","));
                    supplierEvaluateInfoMap = this.supplierEvaluateInfo(new EvaluateSummaryDTO(organizationId, sourceHeaderId, sourceFrom, subjectMatterRule));
                } else {
                    supplierEvaluateInfoMap = this.supplierEvaluateInfo(new EvaluateSummaryDTO(organizationId, sourceHeaderId, sourceFrom, firstSummary.getBidLineItemIds()));
                }

                JSONObject jsonObject = new JSONObject(supplierEvaluateInfoMap);
                subjectMatterRule = jsonObject.getString("subjectMatterRule");
                if ("NONE".equals(subjectMatterRule)) {
                    List<EvaluateSupplierDTO> evaluateSupplierList = jsonObject.getJSONArray(subjectMatterRule).toJavaList(EvaluateSupplierDTO.class);
                    if (CollectionUtils.isEmpty(evaluateSupplierList)) {
                        throw new CommonException("查询结果有误", new Object[0]);
                    }

                    summaryList = new ArrayList();
                    Iterator var11 = evaluateSupplierList.iterator();

                    while(var11.hasNext()) {
                        EvaluateSupplierDTO evaluateSupplier = (EvaluateSupplierDTO)var11.next();
                        EvaluateSummary evaluateSummary = new EvaluateSummary();
                        evaluateSummary.setEvaluateSummaryId(evaluateSupplier.getEvaluateSummaryId());
                        evaluateSummary.setInvalidFlag(invalidQuotationHeaderIdList.contains(evaluateSupplier.getQuotationHeaderId()) ? 1 : evaluateSupplier.getInvalidFlag());
                        evaluateSummary.setTenantId(organizationId);
                        evaluateSummary.setSourceHeaderId(evaluateSupplier.getSourceHeaderId());
                        evaluateSummary.setBusinessScore(evaluateSupplier.getBusinessScoreTotal());
                        evaluateSummary.setTechnologyScore(evaluateSupplier.getTechnologyScoreTotal());
                        evaluateSummary.setScore(evaluateSupplier.getScoreTotal());
                        evaluateSummary.setBusinessWeight(evaluateSupplier.getBusinessWeight());
                        evaluateSummary.setTechnologyWeight(evaluateSupplier.getTechnologyWeight());
                        evaluateSummary.setSourceFrom(sourceFrom);
                        evaluateSummary.setQuotationHeaderId(evaluateSupplier.getQuotationHeaderId());
                        summaryList.add(evaluateSummary);
                    }
                }

                if ("PACK".equals(subjectMatterRule)) {
                    List<EvaluateSupplierDTO> evaluateSupplierList = new ArrayList();
                    List list = jsonObject.getJSONArray(subjectMatterRule);
                    Iterator var19 = list.iterator();

                    while(var19.hasNext()) {
                        Object o = var19.next();
                        Map map = (Map)o;
                        List<EvaluateSupplierDTO> evaluateSupplierDTOS = (List)map.get("supplier");
                        evaluateSupplierList.addAll(evaluateSupplierDTOS);
                    }

                    if (evaluateSupplierList.get(0).getEvaluateSummaryId() == null) {
                        summaryList = this.processPACKFirst(summaryList, organizationId, sourceHeaderId, sourceFrom, evaluateSupplierList);
                    } else {
                        this.processPACK(summaryList, evaluateSupplierList);
                    }
                }

                return summaryList;
            } else {
                throw new CommonException("error.param", new Object[0]);
            }
        }
    }

    private List<EvaluateSummary> processPACKFirst(List<EvaluateSummary> origin, Long organizationId, Long sourceHeaderId, String sourceFrom, List<EvaluateSupplierDTO> evaluateSupplierList) {
        List<Long> invalidQuotationHeaderIdList = (List)origin.stream().filter((item) -> {
            return Flag.YES.equals(item.getInvalidFlag());
        }).map(EvaluateSummary::getQuotationHeaderId).collect(Collectors.toList());
        List<EvaluateSummary> evaluateSummaries = new ArrayList();
        Iterator var8 = evaluateSupplierList.iterator();

        while(var8.hasNext()) {
            EvaluateSupplierDTO evaluateSupplierDTO = (EvaluateSupplierDTO)var8.next();
            EvaluateSummary evaluateSummary = new EvaluateSummary();
            evaluateSummary.setInvalidFlag(invalidQuotationHeaderIdList.contains(evaluateSupplierDTO.getQuotationHeaderId()) ? 1 : evaluateSupplierDTO.getInvalidFlag());
            evaluateSummary.setSectionFlag(1);
            evaluateSummary.setSectionId(evaluateSupplierDTO.getIndicateList().get(0).getBidLineItemId());
            evaluateSummary.setTenantId(organizationId);
            evaluateSummary.setSourceHeaderId(sourceHeaderId);
            evaluateSummary.setBusinessScore(evaluateSupplierDTO.getBusinessScoreTotal());
            evaluateSummary.setTechnologyScore(evaluateSupplierDTO.getTechnologyScoreTotal());
            evaluateSummary.setScore(evaluateSupplierDTO.getScoreTotal());
            evaluateSummary.setBusinessWeight(evaluateSupplierDTO.getBusinessWeight());
            evaluateSummary.setTechnologyWeight(evaluateSupplierDTO.getTechnologyWeight());
            evaluateSummary.setSourceFrom(sourceFrom);
            evaluateSummary.setQuotationHeaderId(evaluateSupplierDTO.getQuotationHeaderId());
            evaluateSummaries.add(evaluateSummary);
        }

        return evaluateSummaries;
    }
}
