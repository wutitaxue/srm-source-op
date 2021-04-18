package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.domain.vo.PrLineVO;

import java.util.List;

/**
 * 入围单头表Mapper
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderMapper extends BaseMapper<RcwlShortlistHeader> {

    /**
     * 更新采购订单
     *
     * @param shortlistHeaderId 入围单ID
     * @param prLineId          采购订单行
     * @return 数量
     */
    int updatePrLineByShortlistHeaderId(@Param("shortlistHeaderId") Long shortlistHeaderId, @Param("prLineId") Long prLineId);

    /**
     * 根据当前入围单查询采购订单行是否存在
     *
     * @param prLineId          采购订单行
     * @param shortlistHeaderId 入围单ID
     * @return PrLineVO
     */
    PrLineVO selectPrLineByIdDontShortHeaderId(@Param("prLineId") Long prLineId, @Param("shortlistHeaderId") Long shortlistHeaderId);

    /**
     * 查询入围单头信息
     *
     * @param organizationId    租户ID
     * @param shortlistHeaderId 入围单ID
     * @return RcwlShortlistHeader
     */
    RcwlShortlistHeader selectShortlistHeaderById(@Param("organizationId") Long organizationId, @Param("shortlistHeaderId") Long shortlistHeaderId);


    /**
     * 根据入围单查询供应商信息
     *
     * @param organizationId    租户ID
     * @param shortlistHeaderId 入围单ID
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlSupplierHeader> selectSupplierByShortlistHeaderId(@Param("organizationId") Long organizationId, @Param("shortlistHeaderId") Long shortlistHeaderId);

    /**
     * 更新prLine 信息
     *
     * @param rcwlShortlistHeader 入围单信息
     */
    void updatePrLineByShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader);

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return User
     */
    User selectUserInfoById(Long userId);

    /**
     * 查询入围单头信息
     *
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlShortlistHeader>
     */
    Page<RcwlShortlistHeader> selectRcwlShortlistHeader(RcwlShortlistQueryDTO rcwlShortlistQueryDTO);
}
