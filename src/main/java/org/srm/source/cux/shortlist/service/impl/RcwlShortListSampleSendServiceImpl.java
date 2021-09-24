package org.srm.source.cux.shortlist.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.srm.source.cux.shortlist.api.dto.RcwlSampleSendReqDTO;
import org.srm.source.cux.shortlist.service.RcwlShortListSampleSendService;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/28 15:15
 * @version:1.0
 */
@Service
public class RcwlShortListSampleSendServiceImpl implements RcwlShortListSampleSendService {
    private static final Logger logger = LoggerFactory.getLogger(Loader.class);

    /**
     * @param organizationId
     * @param rcwlSampleSendReqDTO
     */
    @Override
    public void createReqWithLine(Long organizationId, RcwlSampleSendReqDTO rcwlSampleSendReqDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("feign调用失败：" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rcwlSampleSendReqDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布提交
     *
     * @param tenantId
     * @param rcwlSampleSendReqDTO
     */
    @Override
    public void releaseSubmit(Long tenantId, RcwlSampleSendReqDTO rcwlSampleSendReqDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info("feign调用失败：" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rcwlSampleSendReqDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
