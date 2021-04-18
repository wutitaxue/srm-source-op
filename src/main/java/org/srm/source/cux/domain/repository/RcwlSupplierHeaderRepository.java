package org.srm.source.cux.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;

import java.util.List;

/**
 * 入围单供应商头信息资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierHeaderRepository extends BaseRepository<RcwlSupplierHeader> {

    /**
     * 查询入围单供应商信息
     *
     * @param pageRequest           分页
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlShortlistHeader> pageAndSortRcwlSupplierHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO);

    /**
     * 删除入围单供应商信息
     *
     * @param rcwlSupplierHeaders 入围单供应商头信息
     */
    void batchDeleteBySupplierHeader(List<RcwlSupplierHeader> rcwlSupplierHeaders);

    /**
     * 入围单供应商明细
     *
     * @param supplierId 入围单供应商ID
     * @return RcwlSupplierHeader
     */
    RcwlSupplierHeader detailRcwlSupplierHeader(Long supplierId);

    /**
     * 创建更新入围单供应商
     * @param rcwlSupplierHeader 入围单供应商
     * @return RcwlSupplierHeader
     */
    RcwlSupplierHeader createAndUpdateSupplierHeader(RcwlSupplierHeader rcwlSupplierHeader);
}
