package org.srm.source.cux.rfx.domain.repository;

import org.srm.source.rfx.domain.entity.RfxQuotationLine;

import java.util.List;

/**
 * @author kaibo.li
 * @date 2021-05-19 16:15
 */
public interface IRcwlRfxQuotationLineRepository {

    List<RfxQuotationLine> querySumQuotationByRfxHeaderId(Long rfxHeaderId);
}
