package org.srm.source.cux.rfx.infra.mapper;

import io.choerodon.mybatis.helper.ExtendMapper;
import org.springframework.stereotype.Component;
import org.srm.source.rfx.api.dto.HeaderQueryDTO;
import org.srm.source.rfx.api.dto.RfxDTO;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.infra.mapper.RfxHeaderMapper;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/21 13:40
 * @version:1.0
 */
@Component
@Tenant("SRM-RCWL")
public interface RcwlRfxHeaderBaseMapper extends RfxHeaderMapper, ExtendMapper<RfxHeader> {
    /**
     * 供应商报价列表信息
     * @param rfxDTO
     * @return
     */
    @Override
    List<RfxDTO> batchListRfx(RfxDTO rfxDTO);

    @Override
    RfxHeaderDTO selectOneRfxHeader(HeaderQueryDTO rfxHeaderParam);
}
