package org.srm.source.cux.rfx.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.rfx.infra.mapper.RcwlRfxLineItemMapper;
import org.srm.source.rfx.api.dto.ItemDTO;
import org.srm.source.rfx.infra.mapper.RfxLineItemMapper;
import org.srm.source.rfx.infra.repository.impl.RfxLineItemRepositoryImpl;
import org.srm.web.annotation.Tenant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Tenant("SRM-RCWL")
public class RcwlRfxLineItemRepositoryImpl extends RfxLineItemRepositoryImpl {

    @Autowired
    private RfxLineItemMapper itemMapper;
    @Autowired
    private RcwlRfxLineItemMapper rcwlRfxLineItemMapper;

    @Override
    public Page<ItemDTO> pagePartnerItemDim(PageRequest pageRequest, ItemDTO itemDTO) {
        Page<ItemDTO> itemPage = PageHelper.doPageAndSort(pageRequest, () -> {
            return this.rcwlRfxLineItemMapper.listPartnerItemDim(itemDTO);
        });
        List<ItemDTO> itemList = itemPage.getContent();
        this.transItemCategoryInItem(itemList, itemDTO.getTenantId());
        return itemPage;
    }
    private List<ItemDTO> transItemCategoryInItem(List<ItemDTO> itemList, Long tenant_id) {
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(itemList)) {
            List<Long> itemIdList = (List)itemList.stream().map(ItemDTO::getItemId).collect(Collectors.toList());
            List<ItemDTO> itemCategoryList = this.itemMapper.queryItemCategoryByItemIds(tenant_id, itemIdList);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(itemCategoryList)) {
                Map<Long, List<ItemDTO>> itemCategoryListMap = (Map)itemCategoryList.stream().collect(Collectors.groupingBy(ItemDTO::getItemId));
                itemList.forEach((item) -> {
                    List<ItemDTO> items = (List)itemCategoryListMap.get(item.getItemId());
                    if (!org.apache.commons.collections.CollectionUtils.isEmpty(items)) {
                        ItemDTO itemCategory;
                        if (items.size() > 1) {
                            itemCategory = (ItemDTO)items.stream().filter((e) -> {
                                return BaseConstants.Flag.YES.equals(e.getDefaultFlag());
                            }).findFirst().orElse((ItemDTO)null);
                        } else {
                            itemCategory = (ItemDTO)items.get(0);
                        }

                        if (itemCategory != null) {
                            item.setCategoryId(itemCategory.getCategoryId());
                            item.setCategoryCode(itemCategory.getCategoryCode());
                            item.setCategoryName(itemCategory.getCategoryName());
                        }
                    }
                });
            }
        }

        return itemList;
    }
}
