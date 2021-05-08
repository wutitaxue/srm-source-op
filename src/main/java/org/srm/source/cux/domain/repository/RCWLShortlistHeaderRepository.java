package org.srm.source.cux.domain.repository;

import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.cux.domain.entity.RCWLSample;
import org.srm.source.cux.domain.entity.RCWLShortlistHeader;
import org.srm.source.cux.domain.entity.RCWLSupplierHeader;

import java.util.List;

public interface RCWLShortlistHeaderRepository {
    /**
     * 租户查询入围单头
     *
     * @param rcwlShortlistHeader
     */
    List<RCWLShortlistHeader> listShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader);

    /**
     * 供应商查询入围单头
     *
     * @param rcwlShortlistHeader
     */
    List<RCWLShortlistHeader> listShortlistHeaderSupplier(RCWLShortlistHeader rcwlShortlistHeader);

    /**
     * 查询入围单头明细
     *
     * @param shortlistHeaderId
     */
    RCWLShortlistHeader selectOneShortlistHeader(Long shortlistHeaderId);

    /**
     * 查询入围单头
     *
     * @param Num
     */
    Long selectOneShortlistHeaderByNum(String Num);
    /**
     * 查询入围单供应商
     *
     * @param rcwlSupplierHeader
     * @param shortlistHeaderId
     */
    List<RCWLSupplierHeader> listShortlistSupplier(RCWLSupplierHeader rcwlSupplierHeader, Long shortlistHeaderId);

    /**
     * 供应商个数查询ß
     *
     * @param shortlistHeaderId
     * @return
     */
    Long supplierCount(Long shortlistHeaderId);
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


    /**
     * 更新采购申请单行入围单编码
     *
     * @param prLine
     */
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
