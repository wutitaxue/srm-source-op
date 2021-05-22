package org.srm.source.cux.domain.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.api.controller.v1.dto.StaticTextDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.vo.SupplierVO;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.domain.vo.PrLineVO;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 入围单头表资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderRepository extends BaseRepository<RcwlShortlistHeader> {


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
     * 根据入围单查询供应商信息
     *
     * @param pageRequest       分页
     * @param organizationId    租户ID
     * @param shortlistHeaderId 入围单ID
     * @return Page<RcwlSupplierHeader>
     */
    Page<RcwlSupplierHeader> selectSupplierByShortlistHeaderId(PageRequest pageRequest, Long organizationId, Long shortlistHeaderId);


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
     * @param pageRequest           分页
     * @param rcwlShortlistQueryDTO 查询条件
     * @return Page<RcwlShortlistHeader>
     */
    Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO);


    /**
     * 转询价的供应商ß
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlSupplierHeader> rcwlSelectToRfxSuppier(Long organizationId, Long shortlistHeaderId);

    /**
     * 恢复prLine的值
     *
     * @param rcwlShortlistHeader 入围单
     */
    void updatePrLineByShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader);

    /**
     * @param shortlistHeaderId 入围单ID
     * @param prLineIds         prLineID
     */
    void updatePrLineByShortlistHeaderId(Long shortlistHeaderId, List<Long> prLineIds);

    /**
     * 根据编码获取静态文本内容
     *
     * @param organizationId 租户
     * @param textCode       编码
     * @return StaticTextDTO
     */
    StaticTextDTO selectStaticTextValueByCode(Long organizationId, String textCode);

    /**
     * 审批
     *
     * @param rcwlShortlistHeaders 入围单
     * @param status               状态
     * @return List<RcwlShortlistHeader>
     */
    List<RcwlShortlistHeader> approve(List<RcwlShortlistHeader> rcwlShortlistHeaders, String status);

    /**
     * 发布
     *
     * @param rcwlShortlistHeader 入围单
     * @return RcwlShortlistHeader
     */
    RcwlShortlistHeader published(RcwlShortlistHeader rcwlShortlistHeader);

    /**
     * 提交
     *
     * @param rcwlShortlistHeader 入围单
     * @return RcwlShortlistHeader
     */
    RcwlShortlistHeader submit(RcwlShortlistHeader rcwlShortlistHeader) throws IOException;

    /**
     * 获取当前供应商信息
     *
     * @return SupplierVO
     */
    SupplierVO currentSupplierInfo();


    Set<Long> queryPrLine(Long shortlistHeaderId);

    List<PrLineVO> pageAssignList(Long shortlistHeaderId);

    /**
     * 更新寻源方式
     *
     * @param sourceMethod
     * @param rfxHeaderId
     */
    void updateRfxSourceMethod(String sourceMethod, Long rfxHeaderId, String shotListNum);

    /**
     * 通过code查询ID
     *
     * @param organizationId
     * @param shotListNum
     * @return
     */
    Long rcwlSelectShortListHeaderIdByCode(Long organizationId, String shotListNum);
}
