package org.srm.source.cux.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;

/**
 * 入围单供应商头信息资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierHeaderRepository extends BaseRepository<RcwlSupplierHeader> {

    /**
     * 查询入围单供应商信息
     * @param pageRequest 分页
     * @param rcwlSupplierHeader 入围单供应商头信息
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlSupplierHeader> pageAndSortRcwlSupplierHeader(PageRequest pageRequest, RcwlSupplierHeader rcwlSupplierHeader);
}
