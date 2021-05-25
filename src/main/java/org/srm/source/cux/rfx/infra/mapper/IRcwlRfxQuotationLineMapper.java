package org.srm.source.cux.rfx.infra.mapper;

import org.srm.source.rfx.domain.entity.RfxQuotationLine;

import java.util.List;

/**
 * @author kaibo.li
 * @date 2021-05-19 16:20
 */
public interface IRcwlRfxQuotationLineMapper {
    List<RfxQuotationLine> querySumQuotationByRfxHeaderId(Long rfxHeaderId, Long tenantId);
}
