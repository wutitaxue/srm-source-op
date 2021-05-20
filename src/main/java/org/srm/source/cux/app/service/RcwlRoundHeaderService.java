package org.srm.source.cux.app.service;

import org.srm.source.rfx.domain.entity.RfxQuotationHeader;

import java.util.Date;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/17 14:22
 * @version:1.0
 */
public interface RcwlRoundHeaderService {

    void startQuotation(Long tenantId, Long sourceHeaderId, Date roundQuotationEndDate, String startingReason, List<RfxQuotationHeader> rfxQuotationHeaderList);

}
