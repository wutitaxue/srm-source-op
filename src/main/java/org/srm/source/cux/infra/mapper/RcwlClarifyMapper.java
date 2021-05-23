package org.srm.source.cux.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.share.domain.entity.Clarify;

import java.util.List;

public interface RcwlClarifyMapper extends BaseMapper<RcwlClarifyForBPM> {

    String getSourceNumAndNameAndClarifyNumberById(Long id);

    String getCountOfAlikeSourceId(Long id,Long referFlag);

    void updateClarifyData(RcwlUpdateDataDTO rcwlUpdateDTO);

    List<String> getAttachmentList(String id);

    String getRoundNumber(Long id);

    Long getClarifyIdByClarifyNum(String clarifyNum);

    List<String> getTenantIdByclarifyNum(String clarifyNum);
}
