package org.srm.source.cux.shortlist.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.infra.mapper.RcwlSupplierHeaderMapper;
import org.srm.source.cux.shortlist.api.dto.RcwlSampleInfoDTO;
import org.srm.source.cux.shortlist.api.dto.RcwlSampleSendReqDTO;
import org.srm.source.cux.shortlist.api.dto.RcwlShortListSampleDTO;
import org.srm.source.cux.shortlist.api.dto.RcwlShortListSupplierDTO;
import org.srm.source.cux.shortlist.service.RcwlShortListSampleSendService;
import org.srm.source.cux.shortlist.service.RcwlShortListSampleService;
import org.srm.source.rfx.api.dto.CompanyDTO;
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

    @Autowired
    private RcwlSupplierHeaderMapper rcwlSupplierHeaderMapper;

    private static final Logger logger = LoggerFactory.getLogger(RcwlShortListSampleService.class);

    /**
     * 入围单批量创建送样单，并发布
     *
     * @param tenantId               租户id
     * @param rcwlShortListSampleDTO 入围单送样dto
     */

    @Override
    public void rcwlBanthCreateSample(Long tenantId, RcwlShortListSampleDTO rcwlShortListSampleDTO) {
        logger.info("-------------入围单批量创建送样开始-----------");
        List<RcwlSampleInfoDTO> infoDtoList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        logger.info("---------------------查询采购申请开始：-------------------organizationId:{0},shortListId:{1}", tenantId, rcwlShortListSampleDTO.getShortListId());
        List<PrLineVO> prLineVOList = this.rcwlShortlistHeaderRepository.pageAssignList(rcwlShortListSampleDTO.getShortListId());
        //设置送样物料
        if (!ObjectUtils.isEmpty(prLineVOList)) {
            for (PrLineVO prLineVO : prLineVOList) {
                RcwlSampleInfoDTO rcwlSampleInfoDTO = new RcwlSampleInfoDTO();
                rcwlSampleInfoDTO.setTenantId(tenantId);
                rcwlSampleInfoDTO.setItemCode(prLineVO.getItemCode());
                rcwlSampleInfoDTO.setItemName(prLineVO.getItemName());
                rcwlSampleInfoDTO.setLineNum(prLineVO.getDisplayLineNum());
                rcwlSampleInfoDTO.setUomCode(prLineVO.getUomCode());
                rcwlSampleInfoDTO.setUomName(prLineVO.getUomName());
                rcwlSampleInfoDTO.setCategoryId(prLineVO.getCategoryId());
                rcwlSampleInfoDTO.setCategoryNames(prLineVO.getCategoryName());
                rcwlSampleInfoDTO.setReqQuantity(prLineVO.getQuantity());
                rcwlSampleInfoDTO.setReqTime(prLineVO.getNeededDate());
                infoDtoList.add(rcwlSampleInfoDTO);
            }
        }
        if (!ObjectUtils.isEmpty(rcwlShortListSampleDTO.getRcwlShortListSupplierDTOList())) {
            for (RcwlShortListSupplierDTO rcwlShortListSupplierDTO : rcwlShortListSampleDTO.getRcwlShortListSupplierDTOList()
            ) {
                RcwlSampleSendReqDTO rcwlSampleSendReqDTO = new RcwlSampleSendReqDTO();
                rcwlSampleSendReqDTO.setInfoDtoList(infoDtoList);
                rcwlSampleSendReqDTO.setTenantId(tenantId);
                rcwlSampleSendReqDTO.setAttributeVarchar11(rcwlShortListSampleDTO.getShortListNum());
                rcwlSampleSendReqDTO.setCompanyId(rcwlShortListSampleDTO.getCompanyId());
                rcwlSampleSendReqDTO.setCompanyNum(rcwlShortListSampleDTO.getCompanyNum());
                rcwlSampleSendReqDTO.setCompanyName(rcwlShortListSampleDTO.getCompanyName());
                rcwlSampleSendReqDTO.setOuId(rcwlShortListSampleDTO.getOuId());
                rcwlSampleSendReqDTO.setOuCode(rcwlShortListSampleDTO.getOuCode());
                rcwlSampleSendReqDTO.setOuName(rcwlShortListSampleDTO.getOuName());
                rcwlSampleSendReqDTO.setInvOrganizationId(rcwlShortListSampleDTO.getInvOrganizationId());
                rcwlSampleSendReqDTO.setOrganizationCode(rcwlShortListSampleDTO.getOrganizationCode());
                rcwlSampleSendReqDTO.setOrganizationName(rcwlShortListSampleDTO.getOrganizationName());
                //供应商信息
                rcwlSampleSendReqDTO.setSupplierId(rcwlShortListSupplierDTO.getSupplierId());
                rcwlSampleSendReqDTO.setSupplierNum(rcwlShortListSupplierDTO.getSupplierNum());
                rcwlSampleSendReqDTO.setSupplierName(rcwlShortListSupplierDTO.getSupplierName());
                //供应商信息查询
                logger.info("-------------查询供应商信息-------");
                CompanyDTO companyDTO = rcwlSupplierHeaderMapper.selectCompanyById(rcwlShortListSupplierDTO.getSupplierId());
                rcwlSampleSendReqDTO.setSupplierTenantId(companyDTO.getTenantId());

                rcwlSampleSendReqDTO.setRecUserName(rcwlShortListSampleDTO.getRecUserName());
                rcwlSampleSendReqDTO.setRecUserPhone(rcwlShortListSampleDTO.getRecUserPhone());
                rcwlSampleSendReqDTO.setSampleSendAddress(rcwlShortListSampleDTO.getSampleSendAddress());
                rcwlSampleSendReqDTO.setReqUserId(DetailsHelper.getUserDetails().getUserId());
                rcwlSampleSendReqDTO.setReqUserName(DetailsHelper.getUserDetails().getRealName());
                rcwlSampleSendReqDTO.setReqUserPhone(rcwlShortListSampleDTO.getRecUserPhone());

                try {
                    logger.info("-----------送样创建开始---------：rcwlSampleSendReqDTO: {0}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rcwlSampleSendReqDTO));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                rcwlShortListSampleSendService.createReqWithLine(tenantId, rcwlSampleSendReqDTO);
                logger.info("---------------创建结束-------------");
            }

        } else {
            throw new CommonException("至少选择一家送样供应商!");
        }
    }
}
