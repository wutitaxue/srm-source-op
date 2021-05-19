package org.srm.source.cux.rfx.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.srm.source.cux.rfx.app.service.RcwlRfxHeaderAttachmentService;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;

/**
 * @description:
 * @author: bin.zhang
 * @createDate: 2021/5/14 17:03
 */
@Component
public class RcwlRfxHeaderAttachmentServiceImpl implements RcwlRfxHeaderAttachmentService {
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Override
    public void saveCloseAttachment(RfxHeaderDTO rfxHeaderDTO, Long tenantId) {
        if(!StringUtils.isEmpty(rfxHeaderDTO.getAttributeVarchar20())){
            RfxHeader rfxHeader = rfxHeaderRepository.selectByPrimaryKey(rfxHeaderDTO.getRfxHeaderId());
            rfxHeader.setAttributeVarchar20(rfxHeaderDTO.getAttributeVarchar20());
            this.rfxHeaderRepository.updateOptional(rfxHeader, new String[]{"attributeVarchar20"});
        }
    }
}
