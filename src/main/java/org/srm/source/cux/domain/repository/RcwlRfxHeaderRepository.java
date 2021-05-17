package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;

import java.util.List;

public interface RcwlRfxHeaderRepository extends BaseRepository<RfxHeader> {

    List<String> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id , String remark,Long tenantId);
}
