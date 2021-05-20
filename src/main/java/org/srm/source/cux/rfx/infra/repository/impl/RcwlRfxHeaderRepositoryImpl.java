//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.srm.source.cux.rfx.infra.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;
import org.srm.source.cux.rfx.domain.repository.RcwlRfxHeaderRepository;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.cux.rfx.infra.mapper.RcwlRfxSupplierQueryMapper;
import org.srm.source.rfx.infra.repository.impl.RfxHeaderRepositoryImpl;
import org.srm.web.annotation.Tenant;

/**
 * @author hand_ghq
 */
@Component
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxHeaderRepositoryImpl extends RfxHeaderRepositoryImpl implements RcwlRfxHeaderRepository {

    @Autowired
    private RcwlRfxSupplierQueryMapper supplierQueryMapper;


    @Override
    public List<RcwlRfxSupplierQueryDTO> queryByCond(Long supplierCompanyId) {
        return supplierQueryMapper.queryByCond(supplierCompanyId);
    }
}
