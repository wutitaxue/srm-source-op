package org.srm.source.cux.rfx.app.service;

import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;

import java.util.List;

/**
 * @Author hand_ghq
 */
public interface RcwlRfxSupplierQueryService {

    /**
     * 根据条件查询询价信息
     * @param organizationId
     * @param supplierCompanyId
     * @return
     */
    List<RcwlRfxSupplierQueryDTO> queryRfxSupplierQueryDTO(Long organizationId, Long supplierCompanyId);

}
