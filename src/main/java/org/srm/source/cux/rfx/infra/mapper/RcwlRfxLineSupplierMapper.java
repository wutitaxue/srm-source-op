package org.srm.source.cux.rfx.infra.mapper;

import org.springframework.stereotype.Component;
import org.srm.source.rfx.api.dto.RfxLineSupplierDTO;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;

import java.util.List;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/21 17:05
 * @version:1.0
 */
@Component
public interface RcwlRfxLineSupplierMapper {
    List<RfxLineSupplierDTO> listRfxCheckSuppliers(RfxLineSupplier lineSupplier);
}
