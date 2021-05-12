package org.srm.source.cux.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;

/**
 * 入围单头表资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderRepository extends BaseRepository<RcwlShortlistHeader> {


    /**
     * 查询入围单列表
     *
     * @param pageRequest           分页
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlShortlistHeader>
     */
    Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO);

}
