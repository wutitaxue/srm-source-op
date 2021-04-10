package org.srm.source.cux.infra.mapper;

import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.cux.domain.entity.RCWLSample;
import org.srm.source.cux.domain.entity.RCWLShortlistHeader;
import org.srm.source.cux.domain.entity.RCWLSupplierHeader;

import java.util.List;

public interface RCWLShortlistHeaderMapper {
    /**
     * 租户查询入围单头
     *
     * @param rcwLShortlistHeader
     */
    List<RCWLShortlistHeader> listShortlistHeader(RCWLShortlistHeader rcwLShortlistHeader);

    /**
     * 供应商查询入围单头
     *
     * @param rcwLShortlistHeader
     */
    List<RCWLShortlistHeader> listShortlistHeaderSupplier(RCWLShortlistHeader rcwLShortlistHeader);

    /**
     * 查询入围单头明细
     *
     * @param shortlistHeaderId
     */
    RCWLShortlistHeader selectOneShortlistHeader(Long shortlistHeaderId);

    /**
     * 查询供应商
     *
     * @param rcwLSupplierHeader
     * @param shortlistHeaderId
     */
    List<RCWLSupplierHeader> listShortlistSupplier(RCWLSupplierHeader rcwLSupplierHeader, Long shortlistHeaderId);

    /**
     * 查询采购申请单
     *
     * @param prLine
     * @param shortlistHeaderId
     */
    List<PrLineVO> listPrline(Long tenantId,PrLineVO prLine, Long shortlistHeaderId);

    /**
     * 查询送样单
     *
     * @param rcwlSample
     * @param shortlistHeaderId
     */
    List<RCWLSample> listSample(RCWLSample rcwlSample, Long shortlistHeaderId);

    /**
     * 查询送样单明细
     *
     * @param sampleId
     */
    RCWLSample selectOneSample(Long sampleId);

    /**
     * 新建入围单
     *
     * @param rcwlShortlistHeader
     */
    void insertShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader);

    /**
     * 修改入围单
     *
     * @param rcwlShortlistHeader
     */
    void updateShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader);
    /**
     * 删除入围单
     *
     * @param shortlistHeaderId
     */
    void deleteShortlistHeader(Long shortlistHeaderId);

    /**
     * 采购申请单转入围单
     *
     * @param prLineId
     */
    PrLineVO selectOnePrline(Long tenantId,Long prLineId);

    void updatePrline(PrLineVO prLine);

    /**
     * 新建送样单
     *
     * @param rcwlSample
     */
    void insertSample(RCWLSample rcwlSample);

    /**
     * 修改送样单
     *
     * @param rcwlSample
     */
    void updateSample(RCWLSample rcwlSample);

}
