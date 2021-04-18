package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 入围单供应商头信息Mapper
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierHeaderMapper extends BaseMapper<RcwlSupplierHeader> {

    /**
     * 查询入围单供应商信息
     *
     * @param rcwlSupplierHeader 入围单供应商头信息
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlSupplierHeader> selectRcwlSupplierHeader(RcwlSupplierHeader rcwlSupplierHeader);
}

