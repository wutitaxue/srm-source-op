package org.srm.source.cux.rfx.app.service;

import org.srm.source.rfx.api.dto.RfxHeaderDTO;

/**
 * @description:
 * @author: bin.zhang
 * @createDate: 2021/5/14 17:01
 */
public interface RcwlRfxHeaderAttachmentService {
    void saveCloseAttachment(RfxHeaderDTO rfxHeaderDTO, Long tenantId);
}
