package org.srm.source.cux.rfx.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.infra.mapper.RfxHeaderMapper;
import org.srm.web.annotation.Tenant;
import io.choerodon.mybatis.helper.ExtendMapper;

import java.util.List;

/**
 * @Author hand_ghq
 */
@Tenant(RcwlSourceConstant.TENANT_CODE)
public interface RcwlRfxSupplierQueryMapper  {

    /**
     * 根据条件查询询价信息
     * @param supplierCompanyId
     * @return
     */
    List<RcwlRfxSupplierQueryDTO> queryByCond(@Param("supplierCompanyId") Long supplierCompanyId);
}
