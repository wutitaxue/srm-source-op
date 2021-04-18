package org.srm.source.cux.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.domain.vo.PrLineVO;

import java.util.List;

/**
 * 入围单头表资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderRepository extends BaseRepository<RcwlShortlistHeader> {

    /**
     * 更新prLine 入围单信息
     *
     * @param shortlistHeaderId 入围单id
     * @param prLineIds         采购订单行
     */
    void updatePrLineByShortlistHeaderId(Long shortlistHeaderId, List<Long> prLineIds);

    /**
     * 根据当前入围单查询采购订单行是否存在
     *
     * @param prLineId          采购订单行
     * @param shortlistHeaderId 入围单ID
     * @return prLineVO
     */
    PrLineVO selectPrLineByIdDontShortHeaderId(Long prLineId, Long shortlistHeaderId);


    /**
     * 查询入围单头信息
     *
     * @param organizationId    租户ID
     * @param shortlistHeaderId 入围单ID
     * @return RcwlShortlistHeader
     */
    RcwlShortlistHeader selectShortlistHeaderById(Long organizationId, Long shortlistHeaderId);

    /**
     * 根据入围单查询采购订单行信息
     *
     * @param pageRequest       分页
     * @param organizationId    租户ID
     * @param shortlistHeaderId 入围单ID
     * @return List<PrLineVO>
     */
    Page<PrLineVO> selectPrLineByShortlistHeaderId(PageRequest pageRequest, Long organizationId, Long shortlistHeaderId);

    /**
     * 根据入围单查询供应商信息
     *
     * @param pageRequest       分页
     * @param organizationId    租户ID
     * @param shortlistHeaderId 入围单ID
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlSupplierHeader> selectSupplierByShortlistHeaderId(PageRequest pageRequest, Long organizationId, Long shortlistHeaderId);

    /**
     * 删除入围单信息
     *
     * @param rcwlShortlistHeaders 入围单
     */
    void deleteRcwlShortlistHeaderByIds(List<RcwlShortlistHeader> rcwlShortlistHeaders);

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return User
     */
    User selectUserInfoById(Long userId);

    /**
     * 查询入围单列表
     *
     * @param pageRequest         分页
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlShortlistHeader>
     */
    Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO);
}
