//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.srm.source.cux.rfx.domain.repository;


import java.util.List;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;

/**
 * @author hand_ghq
 */
public interface RcwlRfxHeaderRepository extends RfxHeaderRepository {

    /**
     * 查询询价单信息
     * @param supplierCompanyId
     * @return
     */
    List<RcwlRfxSupplierQueryDTO> queryByCond(Long supplierCompanyId);

}
