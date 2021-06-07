package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataVO;

import java.util.List;

public interface RcwlClarifyRepository extends BaseRepository<RcwlClarifyForBPM> {

    String getSourceNumAndNameAndClarifyNumberById(Long id);

    String getCountOfAlikeSourceId(Long id,Long referFlag);

    void updateClarifyData(RcwlUpdateDataVO rcwlUpdateDTO);

    List<RcwlAttachmentListData> getAttachmentList(String id);

    String getRoundNumber(Long id);

    Long getClarifyIdByClarifyNum(String clarifyNum);

    List<String> getTenantIdByclarifyNum(String clarifyNum);

    String getMeaningByLovCodeAndValue(String LovCode,String value);

    Long getSourceReleasedBy(Long sourceId);
}
