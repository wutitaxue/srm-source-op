package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListFilesDto;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListPrDTO;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListSuppierDTO;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.vo.CompanyContactVO;
import org.srm.source.cux.domain.vo.SupplierVO;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.domain.vo.PrLineVO;

import java.util.List;
import java.util.Set;

/**
 * 入围单头表Mapper
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
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

    /**
     * 根据编码获取静态文本内容
     *
     * @param organizationId 租户
     * @param textCode       编码
     * @return String
     */
    String selectStaticTextValueByCode(@Param("organizationId") Long organizationId, @Param("textCode") String textCode);

    /**
     * 获取当前用户信息
     *
     * @param userId 用户ID
     * @return SupplierVO
     */
    SupplierVO currentSupplierInfo(Long userId);

    /**
     * 获取公司联系人
     *
     * @param companyId 公司ID
     * @return List<CompanyContactVO>
     */
    List<CompanyContactVO> selectContactsByCompanyId(@Param("companyId") Long companyId);

    /**
     * 供应商个数查询ß
     *
     * @param shortlistHeaderId
     * @return
     */
    Long supplierCount(Long shortlistHeaderId);

    /**
     * 转询价的供应商
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlSupplierHeader> rcwlSelectToRfxSuppier(@Param("organizationId") Long organizationId, @Param("shortlistHeaderId") Long shortlistHeaderId);

    Set<Long> queryPrLine(Long shortlistHeaderId);

    List<PrLineVO> pageAssignList(Long shortlistHeaderId);

    void updateRfxSourceMethod(@Param("sourceMethod") String sourceMethod, @Param("rfxHeaderId") Long rfxHeaderId, @Param("shotListNum") String shotListNum);

    /**
     * bpm查询入围供应商
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlBpmShortListSuppierDTO> rcwlSelectBpmSuppier(@Param("organizationId") Long organizationId, @Param("shortlistHeaderId") Long shortlistHeaderId);

    /**
     * 查询pr
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlBpmShortListPrDTO> rcwlSelectBpmPr(@Param("organizationId") Long organizationId, @Param("shortlistHeaderId") Long shortlistHeaderId);

    /**
     * 查询附件
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlBpmShortListFilesDto> rcwlSelectBpmFile(@Param("organizationId") Long organizationId, @Param("shortlistHeaderId")Long shortlistHeaderId);

    /**
     * 通过code查id
     * @param organizationId
     * @param shotListNum
     * @return
     */
    Long rcwlSelectShortListHeaderIdByCode(@Param("organizationId") Long organizationId, @Param("shotListNum") String shotListNum);
}
