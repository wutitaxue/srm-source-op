package org.srm.source.cux.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.domain.vo.*;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.vo.RfxFullHeader;

import java.util.List;

public interface RcwlRfxHeaderBpmMapper {

    RcwlSendBpmData prepareDate(@Param("quotationHeaderId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);

    List<RcwlScoringTeamData> prepareScoringTeamData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);

    List<RcwlSupplierData> prepareSupplierData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);

    List<RcwlDetailData> prepareDetailData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);

    List<RcwlAttachmentData> prepareAttachmentData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);




}
