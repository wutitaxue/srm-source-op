package org.srm.source.cux.infra.mapper;

import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataDTO;

import java.util.List;

public interface RcwlCalibrationApprovalMapper {
    List<String> getAttachmentList(String data);

    String getWinningSupplyNum(Long rfxHeaderId);

    String getquotationRoundNum(Long rfxHeaderId);

    List<String> getDbdbjgList(Long rfxHeaderId);

    Integer getAppendRemark(String id);

    String getRemark(String id);

    void updateClarifyData(RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateDataDTO);
}
