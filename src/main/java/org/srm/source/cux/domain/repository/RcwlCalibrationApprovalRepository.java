package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;

import java.util.List;

public interface RcwlCalibrationApprovalRepository extends BaseRepository<RfxHeader> {


    List<String> getAttachmentList(String data);

    String getWinningSupplyNum(Long rfxHeaderId);

    String getquotationRoundNum(Long rfxHeaderId);

    List<String> getDbdbjgList(Long rfxHeaderId);

    Integer getAppendRemark(String id);

    String getRemark(String id);

    void updateClarifyData(RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateDataDTO);

    Long getRfxHeaderIdByRfxNum(String rfxNum);

    String getQuotationAmount(String s);

    List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId);
}
