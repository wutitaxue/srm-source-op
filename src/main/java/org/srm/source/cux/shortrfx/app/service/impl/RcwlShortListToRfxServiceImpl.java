package org.srm.source.cux.shortrfx.app.service.impl;

import cfca.com.itextpdf.text.log.Logger;
import cfca.com.itextpdf.text.log.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import javassist.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.shortrfx.app.service.RcwlShortListToRfxService;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxLineSupplierService;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;
import org.srm.source.share.api.dto.PrLineDTO;
import org.srm.source.share.api.dto.PreFullSourceHeaderDTO;
import org.srm.source.share.api.dto.PreSourceHeaderDTO;
import org.srm.source.share.app.service.PrLineService;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.web.annotation.Tenant;

import java.util.ArrayList;
import java.util.List;

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
    private static final Logger logger = LoggerFactory.getLogger(Loader.class);


    /**
     * 入围单转询价
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    @Override
    public PreSourceHeaderDTO rcwlShortListToRfx(Long organizationId, Long shortlistHeaderId, Long templateId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PreSourceHeaderDTO preSourceHeaderDTO = new PreSourceHeaderDTO();
        PrLineDTO prLineDTO = new PrLineDTO();
        prLineDTO.setNewPriceLibSearch(1);
        prLineDTO.setAttributeBigint1(shortlistHeaderId);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setSize(100);


        PreFullSourceHeaderDTO preFullSourceHeaderDTO = new PreFullSourceHeaderDTO();
        logger.info("prLineDTO");
        Page<PrLineVO> prLineVOS = this.prLineService.listPurchase(pageRequest, prLineDTO, organizationId);
        List<PrLineVO> prLineVOList = prLineVOS.getContent();
        preFullSourceHeaderDTO.setSourceFrom("RW");
        preFullSourceHeaderDTO.setTemplateId(templateId);
        preFullSourceHeaderDTO.setPrLineList(prLineVOList);
        logger.debug("preFullSourceHeaderDTO:[{}]");

        //入围单供应商查询
        List<RcwlSupplierHeader> list = rcwlShortlistHeaderRepository.rcwlSelectToRfxSuppier(organizationId, shortlistHeaderId);
        //创建询价单
        preSourceHeaderDTO = this.rfxHeaderService.createRfxHeaderFromPurchase(organizationId, preFullSourceHeaderDTO);
        //创建询价单供应商
        List<RfxLineSupplier> rfxLineSupplierList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            for (RcwlSupplierHeader supplier : list
            ) {
                RfxLineSupplier rfxLineSupplier = new RfxLineSupplier();
                //供应商复制
                rfxLineSupplier.initSupContact(supplier.getCompanyName());
                rfxLineSupplier.setRfxLineSupplierId(supplier.getSupplierId());
                rfxLineSupplier.setCompanyId(supplier.getCompanyId());
                rfxLineSupplier.setCompanyNum(supplier.getCompanyNum());
                rfxLineSupplier.setSupplierCompanyNum(supplier.getSupplierNum());
                rfxLineSupplier.setTenantId(organizationId);

                rfxLineSupplierList.add(rfxLineSupplier);
                lineSupplierService.createOrUpdateLineSupplier(organizationId, preSourceHeaderDTO.getRfxHeader().getRfxHeaderId(), rfxLineSupplierList);
            }
        }
        lineSupplierService.createOrUpdateLineSupplier(organizationId, preSourceHeaderDTO.getRfxHeader().getRfxHeaderId(), rfxLineSupplierList);


        return preSourceHeaderDTO;
    }
}
