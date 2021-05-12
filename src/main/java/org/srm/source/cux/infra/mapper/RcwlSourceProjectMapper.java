package org.srm.source.cux.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.source.share.domain.entity.ProjectLineItem;
import org.srm.source.share.domain.entity.ProjectLineSupplier;

import java.util.List;

public interface RcwlSourceProjectMapper {

    //通过入围单号查找物料行信息
    List<ProjectLineItem> selectItemByShortlistNum(@Param("tenantId") Long tenantId,@Param("shortlistNum") String shortlistNum);

    //通过入围单号查找供应商行信息
    List<ProjectLineSupplier> selectSupplierByShortlistNum(@Param("tenantId") Long tenantId,@Param("shortlistNum") String shortlistNum);

}
