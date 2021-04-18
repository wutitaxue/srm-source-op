package org.srm.source.cux.app.service;

import org.srm.source.cux.domain.entity.RcwlShortlistHeader;

/**
 * 入围单头表应用服务
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderService {

    /**
     * 创建入围单信息
     * @param rcwlShortlistHeader 入围单
     */
    void createShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader);
}
