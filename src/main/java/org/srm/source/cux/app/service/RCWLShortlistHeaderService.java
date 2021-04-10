package org.srm.source.cux.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.cux.domain.entity.RCWLSample;
import org.srm.source.cux.domain.entity.RCWLShortlistHeader;
import org.srm.source.cux.domain.entity.RCWLSupplierHeader;

import java.util.List;

public interface RCWLShortlistHeaderService {
    /**
     * 租户查询入围单头
     *
     * @param rcwlShortlistHeader
     * @param pageRequest
     */
    Page<RCWLShortlistHeader> listShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader, PageRequest pageRequest);

    /**
     * 供应商查询入围单头
     *
     * @param rcwlShortlistHeader
     * @param pageRequest
     */
    Page<RCWLShortlistHeader> listShortlistHeaderSupplier(RCWLShortlistHeader rcwlShortlistHeader, PageRequest pageRequest);

    /**
     * 查询入围单头明细
     *
     * @param shortlistHeaderId
     */
    RCWLShortlistHeader selectOneShortlistHeader(Long shortlistHeaderId);

    /**
     * 查入围单下的供应商行
     *
     * @param pageRequest
     * @param shortlistHeaderId
     * @param rcwLSupplierHeader
     */
    Page<RCWLSupplierHeader> listShortlistSupplier(RCWLSupplierHeader rcwLSupplierHeader, PageRequest pageRequest, Long shortlistHeaderId);

    /**
     * 查询采购申请单
     *
     * @param prLine
     * @param pageRequest
     * @param shortlistHeaderId
     */
    Page<PrLineVO> listPrline(Long tenantId,PrLineVO prLine, PageRequest pageRequest, Long shortlistHeaderId);

    /**
     * 查询送样单
     *
     * @param rcwlSample
     * @param pageRequest
     * @param shortlistHeaderId
     */
    Page<RCWLSample> listSample(RCWLSample rcwlSample, PageRequest pageRequest, Long shortlistHeaderId);

    /**
     * 查询送样单明细
     *
     * @param sampleId
     */
    RCWLSample selectOneSample(Long sampleId);

    /**
     * 新建/维护入围单头
     *
     * @param rcwLShortlistHeader
     */
    RCWLShortlistHeader createOrUpdateShortlistHeader(RCWLShortlistHeader rcwLShortlistHeader);

    /**
     * 采购申请单转入围单
     *
     * @param prLineIds
     */
    RCWLShortlistHeader purchaseRequisitionToBeShortlisted(Long tenantId,List<Long> prLineIds);

    /**
     * 删除入围单
     *
     * @param shortlistHeaderId
     */
    void cancelShortlistHeader(Long shortlistHeaderId,List<PrLineVO> prLineList);

    /**
     * 提交入围单
     *
     * @param shortlistHeaderId
     */
    void submitShortlistHeader(Long shortlistHeaderId);

    /**
     * 发布入围单
     *
     * @param shortlistHeaderId
     */
    void releaseShortlistHeader(Long shortlistHeaderId);

    /**
     * 新建/维护送样单
     *
     * @param rcwlSample
     */
    void createOrUpdateSample(RCWLSample rcwlSample, Long companyId);

    /**
     * 批量创建送样单
     *
     * @param rcwlSamples
     */
    void batchInsertSample(List<RCWLSample> rcwlSamples, Long companyId);
}
