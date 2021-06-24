package org.srm.source.cux.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.api.dto.RcwlAbilityHeadDTO;
import org.srm.source.cux.api.dto.RcwlAbilityLineDTO;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;

import java.util.List;

public interface RcwlShortListSelectMapper {

    //查询selected为1的供应商
    List<RcwlSupplierHeader> selectSuppliers(@Param("tenantId") Long tenantId , @Param("shortListHeaderId") Long shortListHeaderId);

    //查询关联采购申请的物品行
    List<RcwlAbilityLineDTO> selectItems(@Param("tenantId") Long tenantId , @Param("shortListHeaderId") Long shortListHeaderId);

    //判断供应商是否在sslm_supply_ability中存在
    Long checkExistAbility(@Param("tenantId") Long tenantId , @Param("supplierCompanyId") Long supplierCompanyId);

    //判断供应商是否在sslm_supply_ability_line中存在
    Long checkExistAbilityLine(@Param("tenantId") Long tenantId , @Param("supplyAbilityId") Long supplyAbilityId,@Param("itemCategoryId") Long itemCategoryId);

    //插入供应商能力头
    RcwlAbilityHeadDTO insetAbilityHead(RcwlAbilityHeadDTO rcwlAbilityHeadDTO);

    //插入供应商能力行
    RcwlAbilityLineDTO insetAbilityLine(RcwlAbilityLineDTO rcwlAbilityLine);
}
