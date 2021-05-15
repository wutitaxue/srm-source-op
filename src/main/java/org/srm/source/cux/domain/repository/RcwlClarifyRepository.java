package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.share.domain.entity.Clarify;

import java.util.List;

public interface RcwlClarifyRepository extends BaseRepository<RcwlClarifyForBPM> {

    String getSourceNumAndNameAndClarifyNumberById(Long id);

    String getCountOfAlikeSourceId(Long id,Long referFlag);

    void updateClarifyData(RcwlUpdateDataDTO rcwlUpdateDTO);

    List<String> getAttachmentList(String id);
}
