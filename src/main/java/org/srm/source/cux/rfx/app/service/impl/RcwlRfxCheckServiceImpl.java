package org.srm.source.cux.rfx.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.helper.LanguageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.priceLib.infra.util.LovUtil;
import org.srm.source.rfx.api.dto.SupplierItemDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxQuotationHeaderService;
import org.srm.source.rfx.app.service.impl.RfxCheckServiceImpl;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.rfx.infra.util.LambdaUtils;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.web.annotation.Tenant;

import java.text.Collator;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@Service
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxCheckServiceImpl extends RfxCheckServiceImpl {
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private RfxQuotationHeaderService rfxQuotationHeaderService;
    @Autowired
    private LovUtil lovUtil;

    public static final String SUPPLIER_COMPANY_NAME = "supplierCompanyName";

    public static final String ITEM_NAME = "itemName";

    @Override
    public Page<SupplierItemDTO> listBargainProcessTable(SupplierItemDTO supplierItemDTO, PageRequest pageRequest) {
        Page<SupplierItemDTO> itemDTOPage = rfxHeaderService.listBargainProcessTable(supplierItemDTO, pageRequest);
        if (CollectionUtils.isEmpty(itemDTOPage)){
            return new Page<>();
        }
        String benchmarkPriceType = rfxQuotationHeaderService.selectBenchmarkPriceType(supplierItemDTO.getTenantId(),itemDTOPage.get(0).getRfxHeaderId());
        //根据业务规则定义中的基准价类型来取值
        LovValueDTO lovValueDTO = lovUtil.queryLovValue(supplierItemDTO.getTenantId(),"SSRC.COMMON_MULTILINGUAL",benchmarkPriceType);
        itemDTOPage.get(0).setBenchmarkPriceTypeMeaning(lovValueDTO.getMeaning());
        itemDTOPage.get(0).setBenchmarkPriceType(benchmarkPriceType);
        if(SourceConstants.BenchmarkPriceType.NET_PRICE.equals(benchmarkPriceType)){
            itemDTOPage.getContent().forEach(item -> item.setQuotationPrice(item.getValidNetPrice()));
        }
        Collator collator;
        if (ShareConstants.Language.ZH.equals(LanguageHelper.language())) {
            collator = Collator.getInstance(Locale.CHINA);
        } else {
            collator = Collator.getInstance(Locale.US);
        }
        if (CollectionUtils.isNotEmpty(itemDTOPage.getContent())) {
            itemDTOPage.getContent().forEach(SupplierItemDTO::dealWithQuotationName);
            LambdaUtils.forEach(1, itemDTOPage.getContent(), (i, p) -> p.setNum(i));
            if (StringUtils.isBlank(supplierItemDTO.getOrderType())) {
                return itemDTOPage;
            }
            if (SUPPLIER_COMPANY_NAME.equals(supplierItemDTO.getOrderType())) {
                if (BaseConstants.Digital.ONE == Optional.ofNullable(supplierItemDTO.getOrderFlag()).orElse(BaseConstants.Digital.ZERO)) {
                    //降序
                    Collections.sort(itemDTOPage.getContent(), (e1, e2) -> {
                        return collator.compare(e2.getSupplierCompanyName(), e1.getSupplierCompanyName());
                    });
                    return itemDTOPage;
                }
                //升序
                Collections.sort(itemDTOPage.getContent(), (e1, e2) -> {
                    return collator.compare(e1.getSupplierCompanyName(), e2.getSupplierCompanyName());
                });
            } else if (ITEM_NAME.equals(supplierItemDTO.getOrderType())) {
                if (BaseConstants.Digital.ONE == Optional.ofNullable(supplierItemDTO.getOrderFlag()).orElse(BaseConstants.Digital.ZERO)) {
                    //降序
                    Collections.sort(itemDTOPage.getContent(), (e1, e2) -> {
                        return collator.compare(e2.getItemName(), e1.getItemName());
                    });
                    return itemDTOPage;
                }
                //升序
                Collections.sort(itemDTOPage.getContent(), (e1, e2) -> {
                    return collator.compare(e1.getItemName(), e2.getItemName());
                });
            }
        }
        return itemDTOPage;
    }
}
