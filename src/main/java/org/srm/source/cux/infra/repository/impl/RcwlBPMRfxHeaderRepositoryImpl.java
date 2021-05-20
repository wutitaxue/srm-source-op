package org.srm.source.cux.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataDTO;
import org.srm.source.cux.domain.repository.RcwlBPMRfxHeaderRepository;
import org.srm.source.cux.infra.mapper.RcwlBPMRfxHeaderMapper;
import org.srm.source.rfx.domain.entity.RfxHeader;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RcwlBPMRfxHeaderRepositoryImpl extends BaseRepositoryImpl<RfxHeader> implements RcwlBPMRfxHeaderRepository {

    @Resource
    RcwlBPMRfxHeaderMapper rcwlRfxHeaderMapper;

    @Override
    public List<String> getAttachmentList(String data) {
        List<String> list = new ArrayList<>();
        rcwlRfxHeaderMapper.getAttachmentList(data);
        return list;
    }

    @Override
    public void updateRfxHeaderData(RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO) {
        rcwlRfxHeaderMapper.updateRfxHeaderData(rcwlUpdateDataDTO);
    }

    @Override
    public void updateRfxHeader(Long id, String remark,Long tenantId) {
        rcwlRfxHeaderMapper.updateRfxHeader(id,remark,tenantId);
    }
}
