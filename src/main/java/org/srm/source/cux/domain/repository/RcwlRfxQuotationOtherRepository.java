package org.srm.source.cux.domain.repository;

import org.srm.source.rfx.domain.entity.RfxQuotationHeader;

import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description 询报价其他操作累
 * @date 2021/5/26 14:52
 * @version:1.0
 */
public interface RcwlRfxQuotationOtherRepository {
     void updateSuppierFlag(List<RfxQuotationHeader> rfxQuotationHeaderList);
}
