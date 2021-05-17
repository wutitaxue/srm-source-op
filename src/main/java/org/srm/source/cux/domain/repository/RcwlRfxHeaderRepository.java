package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlRfxHeaderAttachmentListDataForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.infra.mapper.RcwlRfxHeaderMapper;
import org.srm.source.rfx.domain.entity.RfxHeader;

import javax.annotation.Resource;
import java.util.List;

public interface RcwlRfxHeaderRepository extends BaseRepository<RfxHeader> {

    List<String> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateDataDTO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id , String remark,Long tenantId);
}
