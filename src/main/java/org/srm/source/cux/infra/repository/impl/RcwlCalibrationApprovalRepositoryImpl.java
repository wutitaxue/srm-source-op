package org.srm.source.cux.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataDTO;
import org.srm.source.cux.domain.repository.RcwlCalibrationApprovalRepository;
import org.srm.source.cux.infra.mapper.RcwlCalibrationApprovalMapper;
import org.srm.source.rfx.domain.entity.RfxHeader;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RcwlCalibrationApprovalRepositoryImpl extends BaseRepositoryImpl<RfxHeader> implements RcwlCalibrationApprovalRepository {

    @Resource
    private RcwlCalibrationApprovalMapper rcwlCalibrationApprovalMapper;

    @Override
    public List<String> getAttachmentList(String data) {
        List<String> list = new ArrayList<>();
        list = rcwlCalibrationApprovalMapper.getAttachmentList(data);
        return list;
    }

    @Override
    public String getWinningSupplyNum(Long rfxHeaderId) {
        return rcwlCalibrationApprovalMapper.getWinningSupplyNum(rfxHeaderId);
    }

    @Override
    public String getquotationRoundNum(Long rfxHeaderId) {
        return rcwlCalibrationApprovalMapper.getquotationRoundNum(rfxHeaderId);
    }

    @Override
    public List<String> getDbdbjgList(Long rfxHeaderId) {
        List<String> l = new ArrayList<>();
        l = rcwlCalibrationApprovalMapper.getDbdbjgList(rfxHeaderId);
        return l;
    }

    @Override
    public Integer getAppendRemark(String id) {
        return rcwlCalibrationApprovalMapper.getAppendRemark(id);
    }

    @Override
    public String getRemark(String id) {
        String data = rcwlCalibrationApprovalMapper.getRemark(id);
        return data == null ? "":data ;
    }

    @Override
    public void updateClarifyData(RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateDataDTO) {
        rcwlCalibrationApprovalMapper.updateClarifyData(rcwlUpdateDataDTO);
    }

    @Override
    public Long getRfxHeaderIdByRfxNum(String rfxNum) {
        Long data = rcwlCalibrationApprovalMapper.getRfxHeaderIdByRfxNum(rfxNum);
        return data == null ? 0l:data ;
    }

    @Override
    public String getQuotationAmount(String s) {
        String data = rcwlCalibrationApprovalMapper.getQuotationAmount(s);
        return data == null ? "":data ;
    }
}
