package org.srm.source.cux.shortlist.service;

import org.srm.source.cux.shortlist.api.dto.RcwlShortListSampleDTO;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/31 15:40
 * @version:1.0
 */
public interface RcwlShortListSampleService {
    /**
     * 入围单批量创建送样单，并发布
     *
     * @param tenantId               租户id
     * @param rcwlShortListSampleDTO 入围单送样dto
     */
    void rcwlBanthCreateSample(Long tenantId, RcwlShortListSampleDTO rcwlShortListSampleDTO);
}
