package org.srm.source.cux.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;

/**
 * 入围供应商单附件资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierAttachmentRepository extends BaseRepository<RcwlSupplierAttachment> {

    /**
     * 查询供应商附件信息条数
     *
     * @param rcwlSupplierAttachment 查询条件
     * @return Long
     */
    Long rcwlSupplierAttachmentCount(RcwlSupplierAttachment rcwlSupplierAttachment);

    /**
     * 查询供应商附件信息
     *
     * @param pageRequest            分页
     * @param rcwlSupplierAttachment 查询条件
     * @return Page<RcwlSupplierAttachment>
     */
    Page<RcwlSupplierAttachment> pageAndSortByRcwlSupplierAttachment(PageRequest pageRequest, RcwlSupplierAttachment rcwlSupplierAttachment);

    /**
     * 根据入围单附件模本删除供应商对应附件信息
     *
     * @param rcwlShortlistAttachmentId 模本ID
     * @return int
     */
    int deleteByShortlistAttachmentId(Long rcwlShortlistAttachmentId);
}
