package org.srm.source.cux.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.domain.vo.*;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.share.api.dto.EvaluateIndicDTO;
import org.srm.source.share.domain.entity.EvaluateIndic;

import java.util.List;

public interface RcwlRfxHeaderBpmMapper {

    List<EvaluateIndic> rcwlQueryEvaluateIndicate(EvaluateIndicDTO evaluateIndicDTO);

    RcwlSendBpmData prepareDate(@Param("quotationHeaderId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);

    List<RcwlScoringTeamData> prepareScoringTeamData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);

    List<RcwlSupplierData> prepareSupplierData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);

    List<RcwlDetailData> prepareDetailData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);

    List<RcwlAttachmentData> prepareAttachmentData(@Param("quotationHeaderId")Long organizationId,@Param("quotationHeaderId") RfxHeader rfxHeader);




}
