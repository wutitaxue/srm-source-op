package org.srm.source.cux.app.service;

import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;

import java.util.List;

/**
 * 入围供应商单附件应用服务
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierAttachmentService {

    /**
     * 更新供应商附件信息
     * @param rcwlSupplierAttachments 附件信息
     * @return List<RcwlSupplierAttachment>
     */
    List<RcwlSupplierAttachment> createAndUpdate(List<RcwlSupplierAttachment> rcwlSupplierAttachments);
}
