package org.srm.source.cux.bid.infra.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.srm.source.cux.bid.api.dto.RfxHeaderInfoDTO;
import org.srm.source.rfx.api.dto.OperationUnitDTO;

/**
 * description
 *
 * @author xiubing.wang@hand-china.com 2021/09/27 18:15
 */
@Component
public interface RcwlIBidHeaderMapper {

    /**
     * 获取开标信息
     * @param tenMinuteBefore tenMinuteBefore
     */
    List<RfxHeaderInfoDTO> selectRfxInfo(Date tenMinuteBefore);

    /**
     * 获取开标员
     * @param rfxHeaderId rfxHeaderId
     * @param tenantId tenantId
     */
    List<RfxHeaderInfoDTO> selectRfxOpeners(@Param("rfxHeaderId") Long rfxHeaderId, @Param("tenantId") Long tenantId);

    /**
     * 更新发送标识
     * @param rfxHeaderId rfxHeaderId
     * @param tenantId tenantId
     */
    void updateSendFlag(@Param("tenantId") Long tenantId, @Param("rfxHeaderId") Long rfxHeaderId);

}
