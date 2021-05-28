package org.srm.source.cux.rfx.infra.repository.impl;

import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.domain.repository.IRcwlRfxQuotationLineRepository;
import org.srm.source.cux.rfx.infra.mapper.IRcwlRfxQuotationLineMapper;
import org.srm.source.cux.share.infra.constant.Constant;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.app.service.impl.EvaluateScoreLineServiceImpl;
import org.srm.source.share.domain.entity.EvaluateSummary;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kaibo.li
 * @date 2021-05-19 16:17
 */
@Component
@Tenant(Constant.TENANT_NUM)
public class RcwlRfxQuotationLineRepositoryImpl implements IRcwlRfxQuotationLineRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluateScoreLineServiceImpl.class);

    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private CommonQueryRepository commonQueryRepository;

    @Autowired
    private IRcwlRfxQuotationLineMapper rcwlRfxQuotationLineMapper;

    @Override
    public List<RfxQuotationLine> querySumQuotationByRfxHeaderId(Long rfxHeaderId, Long tenantId) {
        return this.rcwlRfxQuotationLineMapper.querySumQuotationByRfxHeaderId(rfxHeaderId, tenantId);
    }

    @Override
    public List<EvaluateSummary> queryEvaluateSummary(EvaluateSummary evaluateSummary) {
        return this.rcwlRfxQuotationLineMapper.queryEvaluateSummary(evaluateSummary);
    }

    @Override
    public Map<Long, BigDecimal> getRfxQuotationLineMaps(AutoScoreDTO autoScoreDTO, String priceTypeCode) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("24769 ---RCWL getRfxQuotationLineMaps RcwlRfxQuotationLineRepositoryImpl");
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
            List<RfxQuotationLine> rfxQuotationLines = this.querySumQuotationByRfxHeaderId(sourceHeaderId,tenantId);
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
}
