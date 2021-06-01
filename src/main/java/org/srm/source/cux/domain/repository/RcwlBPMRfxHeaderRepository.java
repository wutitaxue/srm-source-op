package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
import org.srm.source.rfx.domain.entity.RfxHeader;

import java.util.List;

public interface RcwlBPMRfxHeaderRepository extends BaseRepository<RfxHeader> {

    List<RcwlAttachmentListData> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id , String remark,Long tenantId);

    Long getRfxHeaderIdByRfxNum(String rfxNum);

    String getRealNameById(Long tenantId);
}
