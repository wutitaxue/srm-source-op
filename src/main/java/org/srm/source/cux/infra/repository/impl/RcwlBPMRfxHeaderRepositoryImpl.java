package org.srm.source.cux.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.entity.ClarifyToReleaseDTO;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
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
    public List<RcwlAttachmentListData> getAttachmentList(String data) {
        List<RcwlAttachmentListData> list = new ArrayList<RcwlAttachmentListData>();
        list = rcwlRfxHeaderMapper.getAttachmentList(data);
        return list;
    }

    @Override
    public void updateRfxHeaderData(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO) {
        rcwlRfxHeaderMapper.updateRfxHeaderData(rcwlUpdateDataDTO);
    }

    @Override
    public void updateRfxHeader(Long id, String remark,Long tenantId) {
        rcwlRfxHeaderMapper.updateRfxHeader(id,remark,tenantId);
    }

    @Override
    public Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId) {
        return rcwlRfxHeaderMapper.getRfxHeaderIdByRfxNum(rfxNum,tenantId);
    }

    @Override
    public String getRealNameById(Long tenantId) {
        return  rcwlRfxHeaderMapper.getRealNameById(tenantId);
    }

    @Override
    public ClarifyToReleaseDTO getClarifyToReleaseDTO(Long clarifyId) {
        return rcwlRfxHeaderMapper.getClarifyToReleaseDTO(clarifyId);
    }

    @Override
    public List<Long> getIssueLineIdListByClarifyId(Long clarifyId) {
        List<Long> list = new ArrayList<Long>();
        list = rcwlRfxHeaderMapper.getIssueLineIdListByClarifyId(clarifyId);
        return list;
    }

    @Override
    public void updateSubmitBy(long l, Long rfxHeaderIds) {
        rcwlRfxHeaderMapper.updateSubmitBy(l,rfxHeaderIds);
    }

    @Override
    public void updateTerminatedBy(Long rfxHeaderIds) {
        rcwlRfxHeaderMapper.updateTerminatedBy(rfxHeaderIds);
    }

    @Override
    public void updateCheckedBy(Long rfxHeaderId) {
        rcwlRfxHeaderMapper.updateCheckedBy(rfxHeaderId);
    }
}
