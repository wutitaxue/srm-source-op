package org.srm.source.cux.rfx.infra.mapper;

import io.lettuce.core.dynamic.annotation.Param;

public interface RcwlOpenCheckMapper {

    Long selectOpenCheck(@Param("tenantId") Long tenantId,@Param("rfxHeaderId") Long rfxHeaderId);

}
