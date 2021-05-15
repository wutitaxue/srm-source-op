package org.srm.source.cux.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.repository.RcwlClarifyRepository;
import org.srm.source.cux.infra.mapper.RcwlClarifyMapper;
import org.srm.source.share.domain.entity.Clarify;
import javax.annotation.Resource;
import java.util.List;

@Component
public class RcwlClarifyRepositoryImpl extends BaseRepositoryImpl<RcwlClarifyForBPM> implements RcwlClarifyRepository {

    @Resource
    RcwlClarifyMapper rcwlClarifyMapper;

    @Override
    public String getSourceNumAndNameAndClarifyNumberById(Long id) {
        String data =  rcwlClarifyMapper.getSourceNumAndNameAndClarifyNumberById(id);
        return data == null ? "":data;
    }

    @Override
    public String getCountOfAlikeSourceId(Long id,Long referFlag) {
        String data =  rcwlClarifyMapper.getCountOfAlikeSourceId(id,referFlag);
        return data == null ? "":data;
    }

    @Override
    public void updateClarifyData(RcwlUpdateDataDTO rcwlUpdateDTO) {
        rcwlClarifyMapper.updateClarifyData(rcwlUpdateDTO);
    }

    @Override
    public List<String> getAttachmentList(String id) {
        return rcwlClarifyMapper.getAttachmentList(id);
    }
}
