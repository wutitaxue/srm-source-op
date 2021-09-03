package org.srm.source.cux.rfx.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.srm.source.rfx.app.service.RfxBargainService;
import org.srm.source.rfx.app.service.impl.RfxBargainServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
import org.srm.web.annotation.Tenant;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Tenant("SRM-RCWL")
public class RcwlRfxBargainServiceImpl extends RfxBargainServiceImpl {

    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;

    @Override
    public RfxHeader startBargain(Long organizationId, Long rfxHeaderId, Date bargainEndDate, List<RfxQuotationLine> rfxQuotationLineLists) {
        ((RfxBargainService) this.self()).checkStartBargain(organizationId, rfxHeaderId, bargainEndDate);
        ((RfxBargainService) this.self()).chooseStartBargainApproveType(organizationId, rfxHeaderId, bargainEndDate, rfxQuotationLineLists);
        //回写头表
        //ssrc_rfx_quotation_header-attribute_varchar2    标识Y
        //ssrc_rfx_quotation_header-attribute_varchar3    原因
        RfxQuotationHeader rfxQuotationHeader = new RfxQuotationHeader();
        List<RfxQuotationLine> quotationLines = rfxQuotationLineLists.stream().filter(v -> 1 == v.getBargainSectionSelectedFlag() && 1 == v.getBargainSelectedFlag()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(quotationLines)){
            RfxQuotationLine rfxQuotationLine = quotationLines.get(0);
            rfxQuotationHeader.setQuotationHeaderId(rfxQuotationLine.getQuotationHeaderId());
            rfxQuotationHeader.setObjectVersionNumber(rfxQuotationHeaderRepository.selectOne(rfxQuotationHeader).getObjectVersionNumber());
            rfxQuotationHeader.setAttributeVarchar4(rfxQuotationLine.getAttributeVarchar2());
            rfxQuotationHeader.setAttributeVarchar3(rfxQuotationLine.getAttributeVarchar3());
            //更新头表字段
            rfxQuotationHeaderRepository.updateByPrimaryKeySelective(rfxQuotationHeader);
        }


        return (RfxHeader) this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
    }
}
