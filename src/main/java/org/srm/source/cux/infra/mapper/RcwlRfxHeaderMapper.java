package org.srm.source.cux.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.cux.domain.entity.RcwlRfxHeaderAttachmentListDataForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;

import java.util.List;

public interface RcwlRfxHeaderMapper{
    List<String> getAttachmentList(String data);

    void updateRfxHeaderData(RcwlUpdateDataDTO rcwlUpdateDataDTO);

    void updateRfxHeader(Long id, String remark,Long tenantId);
}
