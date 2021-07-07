package org.srm.source.cux.rfx.infra.mapper;

import io.choerodon.mybatis.helper.ExtendMapper;
import org.springframework.stereotype.Component;
import org.srm.source.rfx.api.dto.HeaderQueryDTO;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.infra.mapper.RfxHeaderMapper;
import org.srm.web.annotation.Tenant;

/**
 * @description:
 * @author: bin.zhang
 * @createDate: 2021/7/6 18:51
 */
@Component
@Tenant("SRM-RCWL")
public interface RcwlRfxHeaderMapper extends RfxHeaderMapper, ExtendMapper<RfxHeader> {
    @Override
    RfxHeaderDTO selectOneRfxHeader(HeaderQueryDTO rfxHeaderParam);
}