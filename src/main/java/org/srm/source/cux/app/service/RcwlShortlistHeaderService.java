package org.srm.source.cux.app.service;

import org.srm.source.cux.domain.entity.RcwlShortlistHeader;

import java.util.List;

/**
 * 入围单头表应用服务
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistHeaderService {

    /**
     * 创建入围单信息
     *
     * @param rcwlShortlistHeader 入围单
     */
    void createShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader);

    /**
     * 删除入围单
     *
     * @param rcwlShortlistHeaders 入围单
     */
    void deleteRcwlShortlistHeaderByIds(List<RcwlShortlistHeader> rcwlShortlistHeaders);

    /**
     * 提交成功
     * @param tenantId
     * @param ShorListNum
     */
    void rcwlSubmitBpmSuccessed(Long tenantId, String ShorListNum);

    /**
     * 审批拒绝
     * @param tenantId
     * @param ShorListNum
     */
    void rcwlSubmitBpmReject(Long tenantId, String ShorListNum);

    /**
     * 审批通过
     * @param tenantId
     * @param ShorListNum
     */
    void rcwlSubmitBpmApproved(Long tenantId, String ShorListNum);

    void RcwlBpmUpateInstance(Long tenantId, String shorListNum, String attributeVarchar8, String attributeVarchar9);
}
