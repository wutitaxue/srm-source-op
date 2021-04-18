package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.rfx.api.dto.CompanyDTO;

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
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlShortlistHeader> selectRcwlSupplierHeader(RcwlShortlistQueryDTO rcwlShortlistQueryDTO);

    /**
     * 查询公司信息
     *
     * @param companyId 公司ID
     * @return CompanyDTO
     */
    CompanyDTO selectCompanyById(@Param("companyId") Long companyId);

    /**
     * 查询明细
     *
     * @param supplierHeaderId 入围单供应商ID
     * @return RcwlSupplierHeader
     */
    RcwlSupplierHeader selectRcwlSupplierHeaderById(@Param("supplierHeaderId") Long supplierHeaderId);
}

