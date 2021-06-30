package org.srm.source.cux.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataVO;

import java.util.List;

public interface RcwlClarifyMapper extends BaseMapper<RcwlClarifyForBPM> {

    String getSourceNumAndNameAndClarifyNumberById(Long id);

    String getCountOfAlikeSourceId(Long id,Long referFlag);

    void updateClarifyData(RcwlUpdateDataVO rcwlUpdateDTO);

    List<RcwlAttachmentListData> getAttachmentList(String id);

    String getRoundNumber(Long id);

    Long getClarifyIdByClarifyNum(String clarifyNum);

    List<String> getTenantIdByclarifyNum(String clarifyNum);

    String getMeaningByLovCodeAndValue(String lovCode, String value);

    Long getSourceReleasedBy(Long sourceId);
}
