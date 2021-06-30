package org.srm.source.cux.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlDBGetDataFromDatabase;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataVO;
import org.srm.source.cux.domain.repository.RcwlCalibrationApprovalRepository;
import org.srm.source.cux.infra.mapper.RcwlCalibrationApprovalMapper;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RcwlCalibrationApprovalRepositoryImpl extends BaseRepositoryImpl<RfxHeader> implements RcwlCalibrationApprovalRepository {

    @Resource
    private RcwlCalibrationApprovalMapper rcwlCalibrationApprovalMapper;

    @Override
    public List<RcwlAttachmentListData> getAttachmentList(String data) {
        List<RcwlAttachmentListData> list = new ArrayList<>();
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
    public List<RcwlDBGetDataFromDatabase> getDbdbjgList(Long rfxHeaderId) {
        List<RcwlDBGetDataFromDatabase> l = rcwlCalibrationApprovalMapper.getDbdbjgList(rfxHeaderId);
        return l == null ? new ArrayList<RcwlDBGetDataFromDatabase>() : l ;
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
    public void updateClarifyData(RcwlUpdateCalibrationApprovalDataVO rcwlUpdateDataDTO) {
        rcwlCalibrationApprovalMapper.updateClarifyData(rcwlUpdateDataDTO);
    }

    @Override
    public Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId) {
        Long data = rcwlCalibrationApprovalMapper.getRfxHeaderIdByRfxNum(rfxNum,tenantId);
        return data == null ? 0l:data ;
    }

    @Override
    public String getQuotationAmount(String s) {
        String data = rcwlCalibrationApprovalMapper.getQuotationAmount(s);
        return data == null ? "":data ;
    }

    @Override
    public List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId) {
        List<String> qQuotationHeaderIDs = rcwlCalibrationApprovalMapper.getQuotationHeaderIDByRfxHeaderId(rfxHeaderId,tenantId);
        return qQuotationHeaderIDs == null ? new ArrayList<String>():qQuotationHeaderIDs;
    }

    @Override
    public List<Long> getRfxLineItemIdByRfxHeaderId(Long rfxHeaderId) {
        List<Long> l = rcwlCalibrationApprovalMapper.getRfxLineItemIdByRfxHeaderId(rfxHeaderId);
        return l == null ? new ArrayList<Long>() : l;
    }

    @Override
    public List<RfxQuotationLine> getQuotationLineListByQuotationHeaderID(Long id) {
        List<RfxQuotationLine> l = rcwlCalibrationApprovalMapper.getQuotationLineListByQuotationHeaderID(id);
        return l == null ? new ArrayList<RfxQuotationLine>() : l;
    }

    @Override
    public RfxQuotationLine getRfxQuotationLineDataByQuotationHeaderIDs(String id) {
        RfxQuotationLine s =  rcwlCalibrationApprovalMapper.getRfxQuotationLineDataByQuotationHeaderIDs(id);
        return s == null ? new RfxQuotationLine() : s;
    }

    @Override
    public Long getRoundNumber(Long rfxHeaderId, Long tenantId) {
        Long data = rcwlCalibrationApprovalMapper.getRoundNumber(rfxHeaderId,tenantId);
        return data == null ? 0l:data ;
    }

    @Override
    public String getBIDPRICE(String quotationHeaderId) {
        return  rcwlCalibrationApprovalMapper.getBIDPRICE(quotationHeaderId);
    }
}
