package org.srm.source.cux.rfx.infra.mapper;

import org.springframework.stereotype.Component;
import org.srm.source.rfx.api.dto.RfxDTO;

import java.util.List;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/21 13:40
 * @version:1.0
 */
@Component
public interface RcwlRfxHeaderBaseMapper {
    /**
     * 供应商报价列表信息
     * @param rfxDTO
     * @return
     */
    List<RfxDTO> batchListRfx(RfxDTO rfxDTO);
}
