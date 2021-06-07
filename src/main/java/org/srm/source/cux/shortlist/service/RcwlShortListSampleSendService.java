package org.srm.source.cux.shortlist.service;

import io.swagger.annotations.ApiParam;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.srm.source.cux.shortlist.api.dto.RcwlSampleSendReqDTO;
import org.srm.source.cux.shortlist.service.impl.RcwlShortListSampleSendServiceImpl;

/**
 * @author lu.cheng01@hand-china.com
 * @description 入围单feign调用sslm送样
 * @date 2021/5/28 14:48
 * @version:1.0
 */

@FeignClient(value = "srm-supplier-lifecycle", fallback = RcwlShortListSampleSendServiceImpl.class, path = "/v1/")
public interface RcwlShortListSampleSendService {

    /**
     * 创建头行
     * @param organizationId
     * @param rcwlSampleSendReqDTO
     */
    @PostMapping({"{organizationId}/sample-send-reqs/createReqWithLine"})
    void createReqWithLine(@PathVariable("organizationId") Long organizationId, @Encrypt @RequestBody RcwlSampleSendReqDTO rcwlSampleSendReqDTO);

    /**
     * 发布提交
     * @param tenantId
     * @param rcwlSampleSendReqDTO
     */
    @PostMapping({"{organizationId}/sample-send-reqs/release/submit"})
    void releaseSubmit(@ApiParam(value = "租户id",required = true) @PathVariable("organizationId") Long tenantId, @Encrypt @RequestBody RcwlSampleSendReqDTO rcwlSampleSendReqDTO);

}
