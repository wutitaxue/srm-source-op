package org.srm.source.cux.infra.mapper;

import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataDTO;

import java.util.List;

public interface RcwlBPMRfxHeaderMapper {
    List<String> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id, String remark,Long tenantId);

    Long getRfxHeaderIdByRfxNum(String rfxNum);
}