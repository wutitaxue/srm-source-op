package org.srm.source.cux.shortrfx.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.srm.source.share.api.dto.PreSourceHeaderDTO;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/12 16:48
 * @version:1.0
 */
public interface RcwlShortListToRfxService {
    /**
     * 入围单转询价
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    PreSourceHeaderDTO rcwlShortListToRfx(Long organizationId,Long shortlistHeaderId,Long templateId) throws JsonProcessingException;
}
