package org.srm.source.cux.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlDBGetDataFromDatabase;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataVO;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;

import java.util.List;

public interface RcwlCalibrationApprovalRepository extends BaseRepository<RfxHeader> {


    List<RcwlAttachmentListData> getAttachmentList(String data);

    String getWinningSupplyNum(Long rfxHeaderId);

    String getquotationRoundNum(Long rfxHeaderId);

    List<RcwlDBGetDataFromDatabase> getDbdbjgList(Long rfxHeaderId);

    Integer getAppendRemark(String id);

    String getRemark(String id);

    void updateClarifyData(RcwlUpdateCalibrationApprovalDataVO rcwlUpdateDataDTO);

    Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId);

    String getQuotationAmount(String s);

    List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId);

    List<Long> getRfxLineItemIdByRfxHeaderId(Long rfxHeaderId);

    List<RfxQuotationLine> getQuotationLineListByQuotationHeaderID(Long id);

    RfxQuotationLine getRfxQuotationLineDataByQuotationHeaderIDs(String id);

    Long getRoundNumber(Long rfxHeaderId, Long tenantId);
}
