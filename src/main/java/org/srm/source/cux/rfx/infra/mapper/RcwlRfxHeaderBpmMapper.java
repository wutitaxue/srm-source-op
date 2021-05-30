package org.srm.source.cux.rfx.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.rfx.domain.vo.*;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.share.api.dto.EvaluateIndicDTO;
import org.srm.source.share.domain.entity.EvaluateIndic;

import java.util.List;

public interface RcwlRfxHeaderBpmMapper {

    List<EvaluateIndic> rcwlQueryEvaluateIndicate(@Param("evaluateIndicDTO") EvaluateIndicDTO evaluateIndicDTO);

    RcwlSendBpmData prepareDate(@Param("organizationId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);

    List<RcwlScoringTeamData> prepareScoringTeamData(@Param("organizationId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);

    List<RcwlSupplierData> prepareSupplierData(@Param("organizationId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);

    List<RcwlDetailData> prepareDetailData(@Param("organizationId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);

    List<RcwlAttachmentData> prepareAttachmentData(@Param("organizationId")Long organizationId,@Param("rfxHeader") RfxHeader rfxHeader);




}
