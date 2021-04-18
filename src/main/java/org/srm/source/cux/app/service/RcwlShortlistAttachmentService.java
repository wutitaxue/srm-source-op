package org.srm.source.cux.app.service;

import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;

import java.util.List;

/**
 * 入围单附件模版应用服务
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistAttachmentService {

    /**
     * 批量保存
     * @param rcwlShortlistAttachments 入围单附件信息
     * @return List<RcwlShortlistAttachment>
     */
    List<RcwlShortlistAttachment> createShortlistAttachment(List<RcwlShortlistAttachment> rcwlShortlistAttachments);

    /**
     * 删除附件
     *
     * @param rcwlShortlistAttachments 附件
     */
    void deleteRcwlShortlistAttachment(List<RcwlShortlistAttachment> rcwlShortlistAttachments);
}
