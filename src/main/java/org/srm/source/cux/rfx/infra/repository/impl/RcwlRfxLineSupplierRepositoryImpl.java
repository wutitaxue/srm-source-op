package org.srm.source.cux.rfx.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.cux.rfx.infra.mapper.RcwlRfxLineSupplierMapper;
import org.srm.source.rfx.api.dto.RfxLineSupplierDTO;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;
import org.srm.source.rfx.infra.repository.impl.RfxLineSupplierRepositoryImpl;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/21 17:02
 * @version:1.0
 */
@Component
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxLineSupplierRepositoryImpl extends RfxLineSupplierRepositoryImpl {
    @Autowired
    private RcwlRfxLineSupplierMapper rcwlRfxLineSupplierMapper;
    /**
     * 多轮报价查询供应商信息
     * @param lineSupplier
     * @return
     */
    @Override
    public List<RfxLineSupplierDTO> listAllRfxCheckSuppliers(RfxLineSupplier lineSupplier) {
        return this.rcwlRfxLineSupplierMapper.listRfxCheckSuppliers(lineSupplier);
    }

}
