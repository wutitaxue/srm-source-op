package org.srm.source.cux.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDataVO;
import org.srm.source.cux.domain.repository.RcwlClarifyRepository;
import org.srm.source.cux.infra.mapper.RcwlClarifyMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public void updateClarifyData(RcwlUpdateDataVO rcwlUpdateDTO) {
        rcwlClarifyMapper.updateClarifyData(rcwlUpdateDTO);
    }

    @Override
    public List<RcwlAttachmentListData> getAttachmentList(String id) {
        return rcwlClarifyMapper.getAttachmentList(id);
    }

    @Override
    public String getRoundNumber(Long id) {
        String data =  rcwlClarifyMapper.getRoundNumber(id);
        return data == null ? "":data;
    }

    @Override
    public Long getClarifyIdByClarifyNum(String clarifyNum) {
        Long l = rcwlClarifyMapper.getClarifyIdByClarifyNum(clarifyNum);
        return l == null ? 0l:l;
    }

    @Override
    public List<String> getTenantIdByclarifyNum(String clarifyNum) {
        List<String> l = rcwlClarifyMapper.getTenantIdByclarifyNum(clarifyNum);
        if(l == null || l.size()==0){
            l = new ArrayList<String>();
        }
        return l;
    }

    @Override
    public String getMeaningByLovCodeAndValue(String LovCode, String value) {
        return rcwlClarifyMapper.getMeaningByLovCodeAndValue(LovCode,value);
    }

    @Override
    public Long getSourceReleasedBy(Long sourceId) {
        return rcwlClarifyMapper.getSourceReleasedBy(sourceId);
    }
}
