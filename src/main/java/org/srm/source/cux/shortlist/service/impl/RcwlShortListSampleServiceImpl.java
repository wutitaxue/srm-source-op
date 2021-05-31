package org.srm.source.cux.shortlist.service.impl;

import javassist.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.shortlist.api.dto.RcwlSampleInfoDTO;
import org.srm.source.cux.shortlist.api.dto.RcwlSampleSendReqDTO;
import org.srm.source.cux.shortlist.api.dto.RcwlShortListSampleDTO;
import org.srm.source.cux.shortlist.service.RcwlShortListSampleSendService;
import org.srm.source.cux.shortlist.service.RcwlShortListSampleService;
import org.srm.source.share.domain.vo.PrLineVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/31 15:41
 * @version:1.0
 */
@Service
public class RcwlShortListSampleServiceImpl implements RcwlShortListSampleService {

    @Autowired
    private RcwlShortListSampleSendService rcwlShortListSampleSendService;
    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    private static final Logger logger = LoggerFactory.getLogger(Loader.class);

    /**
     * 入围单批量创建送样单，并发布
     *
     * @param tenantId               租户id
     * @param rcwlShortListSampleDTO 入围单送样dto
     */

    @Override
    public void rcwlBanthCreateSample(Long tenantId, RcwlShortListSampleDTO rcwlShortListSampleDTO) {
        logger.info("-------------入围单批量创建送样开始-----------");
        RcwlSampleSendReqDTO rcwlSampleSendReqDTO = new RcwlSampleSendReqDTO();
        List<RcwlSampleInfoDTO> infoDtoList = new ArrayList<>();
        RcwlSampleInfoDTO rcwlSampleInfoDTO = new RcwlSampleInfoDTO();

        logger.info("---------------------查询采购申请开始：-------------------organizationId:{0},shortListId:{1}", tenantId, rcwlShortListSampleDTO.getShortListId());
        List<PrLineVO> prLineVOList = this.rcwlShortlistHeaderRepository.pageAssignList(rcwlShortListSampleDTO.getShortListId());
        //设置送样物料
        if (!ObjectUtils.isEmpty(prLineVOList)) {
            for (PrLineVO prLineVO : prLineVOList) {

            }

        }

        rcwlSampleSendReqDTO.setAttributeVarchar11(rcwlShortListSampleDTO.getShortListNum());
        rcwlShortListSampleSendService.releaseSubmit(tenantId, rcwlSampleSendReqDTO);

    }
}
