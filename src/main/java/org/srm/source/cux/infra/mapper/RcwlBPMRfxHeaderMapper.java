package org.srm.source.cux.infra.mapper;

import org.srm.source.cux.domain.entity.ClarifyToReleaseDTO;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;

import java.util.List;

public interface RcwlBPMRfxHeaderMapper {
    List<RcwlAttachmentListData> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id, String remark,Long tenantId);

    Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId);

    String getRealNameById(Long tenantId);

    ClarifyToReleaseDTO getClarifyToReleaseDTO(Long clarifyId);

    List<Long> getIssueLineIdListByClarifyId(Long clarifyId);

    void updateSubmitBy(long l, Long rfxHeaderIds);

    void updateTerminatedBy(Long rfxHeaderIds);
}
