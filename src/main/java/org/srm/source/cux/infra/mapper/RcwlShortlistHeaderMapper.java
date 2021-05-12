package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;

/**
 * 入围单头表Mapper
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderMapper extends BaseMapper<RcwlShortlistHeader> {

    /**
     * 查询入围单头信息
     *
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlShortlistHeader>
     */
    Page<RcwlShortlistHeader> selectRcwlShortlistHeader(RcwlShortlistQueryDTO rcwlShortlistQueryDTO);


}
