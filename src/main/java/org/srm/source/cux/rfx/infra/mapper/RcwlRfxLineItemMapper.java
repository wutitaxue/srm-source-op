package org.srm.source.cux.rfx.infra.mapper;

import org.srm.source.rfx.api.dto.ItemDTO;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.infra.mapper.RfxLineItemMapper;
import org.srm.web.annotation.Tenant;


import java.util.List;


public interface RcwlRfxLineItemMapper {

    List<ItemDTO> listPartnerItemDim(ItemDTO itemDTO);

}
