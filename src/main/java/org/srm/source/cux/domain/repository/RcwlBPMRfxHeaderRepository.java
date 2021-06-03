package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.ClarifyToReleaseDTO;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
import org.srm.source.rfx.domain.entity.RfxHeader;

import java.util.List;

public interface RcwlBPMRfxHeaderRepository extends BaseRepository<RfxHeader> {

    List<RcwlAttachmentListData> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id , String remark,Long tenantId);

    Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId);

    String getRealNameById(Long tenantId);

    ClarifyToReleaseDTO getClarifyToReleaseDTO(Long clarifyId);

    List<Long> getIssueLineIdListByClarifyId(Long clarifyId);

    void updateSubmitBy(long l, Long rfxHeaderIds);

    void updateTerminatedBy(Long rfxHeaderIds);

    void updateCheckedBy(Long rfxHeaderId);
}
