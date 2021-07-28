package org.srm.source.cux.shortrfx.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import javassist.Loader;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.shortrfx.app.service.RcwlShortListToRfxService;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxLineSupplierService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;
import org.srm.source.rfx.domain.repository.RfxLineSupplierRepository;
import org.srm.source.share.api.dto.PrLineDTO;
import org.srm.source.share.api.dto.PreFullSourceHeaderDTO;
import org.srm.source.share.api.dto.PreSourceHeaderDTO;
import org.srm.source.share.app.service.PrLineService;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.web.annotation.Tenant;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/12 16:53
 * @version:1.0
 */
@Service
@Tenant("SRM-RCWL")
public class RcwlShortListToRfxServiceImpl implements RcwlShortListToRfxService {
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private PrLineService prLineService;
    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;
    @Autowired
    private RfxLineSupplierService lineSupplierService;

    @Autowired
    private RfxLineSupplierRepository rfxLineSupplierRepository;
    private static final Logger logger = LoggerFactory.getLogger(Loader.class);


    /**
     * 入围单转询价
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public PreSourceHeaderDTO rcwlShortListToRfx(Long organizationId, Long shortlistHeaderId, Long templateId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PrLineDTO prLineDTO = new PrLineDTO();
        prLineDTO.setNewPriceLibSearch(1);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setSize(100);
//        Set<Long> ids = this.rcwlShortlistHeaderRepository.queryPrLine(shortlistHeaderId);
//        prLineDTO.setPrLineIds(ids);
//        logger.info("ids:" + ids.toString());

        PreFullSourceHeaderDTO preFullSourceHeaderDTO = new PreFullSourceHeaderDTO();
        //logger.info("prLineDTO：" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(prLineDTO));
        logger.info("---------------------查询采购申请开始：-------------------organizationId：" + organizationId);
        List<PrLineVO> prLineVOList = this.rcwlShortlistHeaderRepository.pageAssignList(shortlistHeaderId);
        RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectShortlistHeaderById(organizationId,shortlistHeaderId);
        logger.info("------------prLineVOList:" + prLineVOList.size());
        preFullSourceHeaderDTO.setSourceFrom("RW");
        preFullSourceHeaderDTO.setTemplateId(templateId);
        preFullSourceHeaderDTO.setPrLineList(prLineVOList);
        //logger.debug("preFullSourceHeaderDTO:[{}]" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(preFullSourceHeaderDTO));

        //入围单供应商查询
        List<RcwlSupplierHeader> list = rcwlShortlistHeaderRepository.rcwlSelectToRfxSuppier(organizationId, shortlistHeaderId);
        //创建询价单

        PreSourceHeaderDTO preSourceHeaderDTO = this.rfxHeaderService.createRfxHeaderFromPurchase(organizationId, preFullSourceHeaderDTO);
        logger.info("----------------创建询价单头结束-----------");
        //创建询价单供应商
        List<RfxLineSupplier> rfxLineSupplierList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (RcwlSupplierHeader supplier : list
            ) {
                //logger.info("供应商信息：" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(supplier));
                RfxLineSupplier rfxLineSupplier = new RfxLineSupplier();
                //供应商复制
                rfxLineSupplier.initSupContact(supplier.getCompanyName());
//                rfxLineSupplier.setRfxLineSupplierId(supplier.getSupplierId());
                rfxLineSupplier.setSupplierCompanyId(supplier.getSupplierId());
                rfxLineSupplier.setCompanyId(supplier.getCompanyId());
                rfxLineSupplier.setCompanyNum(supplier.getCompanyNum());
                rfxLineSupplier.setSupplierCompanyNum(supplier.getSupplierNum());
                rfxLineSupplier.setRfxHeaderId(preSourceHeaderDTO.getRfxHeader().getRfxHeaderId());
                rfxLineSupplier.setTenantId(organizationId);
                rfxLineSupplier.setSupplierCompanyName(supplier.getSupplierName());
                rfxLineSupplier.setFeedbackStatus("NEW");

                rfxLineSupplier.setContactMobilephone(supplier.getPhone());
                rfxLineSupplier.setContactTelephone(supplier.getPhone());
                rfxLineSupplier.setContactName(supplier.getContacts());
                rfxLineSupplier.setReadFlag(0);
                rfxLineSupplier.setAppendFlag(0);

                rfxLineSupplier.setSupplierTenantId(supplier.getSupplierTenantId());
                rfxLineSupplier.setSupplierContactId(supplier.getSupplierContactId());
                rfxLineSupplier.setContactMail(supplier.getContactMail());

                rfxLineSupplierList.add(rfxLineSupplier);
            }
        }
        logger.info("---------创建供应商开始------");
        if (CollectionUtils.isNotEmpty(rfxLineSupplierList)) {
//            rfxLineSupplierRepository.batchInsert(rfxLineSupplierList);
            lineSupplierService.createOrUpdateLineSupplier(organizationId, preSourceHeaderDTO.getRfxHeader().getRfxHeaderId(), rfxLineSupplierList);
        }
        logger.info("----------创建供应商结束--------");
        rcwlShortlistHeaderRepository.updateRfxSourceMethod("INVITE",preSourceHeaderDTO.getRfxHeader().getRfxHeaderId(),rcwlShortlistHeader.getShortlistNum());

        RfxHeader rfxHeader = preSourceHeaderDTO.getRfxHeader();
        rfxHeader.setSourceMethod("INVITE");
        preSourceHeaderDTO.setRfxHeader(rfxHeader);
        return preSourceHeaderDTO;
    }
}
