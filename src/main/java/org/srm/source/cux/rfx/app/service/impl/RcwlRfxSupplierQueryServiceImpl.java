package org.srm.source.cux.rfx.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;
import org.srm.source.cux.rfx.app.service.RcwlRfxSupplierQueryService;
import org.srm.source.cux.rfx.domain.repository.RcwlRfxHeaderRepository;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.source.rfx.api.dto.HeaderQueryDTO;
import org.srm.source.rfx.api.dto.RfxCheckItemDTO;
import org.srm.source.rfx.app.service.RfxQuotationLineService;
import org.srm.web.annotation.Tenant;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author hand_ghq
 * @Date 2021/3/24
 * @Version V1.0
 */
@Service
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxSupplierQueryServiceImpl implements RcwlRfxSupplierQueryService {

    @Autowired
    private RcwlRfxHeaderRepository rfxHeaderRepository;

    @Autowired
    private RfxQuotationLineService rfxQuotationLineService;

    /**
     * 根据条件查询询价信息
     * @param organizationId
     * @param supplierCompanyId
     * @return
     */
    @Override
    public List<RcwlRfxSupplierQueryDTO> queryRfxSupplierQueryDTO(Long organizationId, Long supplierCompanyId) {
        // 根据供应商公司id查询询价单信息
        List<RcwlRfxSupplierQueryDTO> rfxSupplierQueryDTOS = rfxHeaderRepository.queryByCond(supplierCompanyId);
        if (CollectionUtils.isNotEmpty(rfxSupplierQueryDTOS)) {
            for (RcwlRfxSupplierQueryDTO queryDTO : rfxSupplierQueryDTOS) {
                HeaderQueryDTO headerQueryDTO = new HeaderQueryDTO();
                headerQueryDTO.setTenantId(organizationId);
                headerQueryDTO.setRfxHeaderId(queryDTO.getRfxHeaderId());
                headerQueryDTO.setRfxLineSupplierId(queryDTO.getRfxLineSupplierId());
                Page<RfxCheckItemDTO> page = rfxQuotationLineService.pageQuotationDetail(headerQueryDTO, new PageRequest(0, 99));
                List<RfxCheckItemDTO> content = page.getContent();
                BigDecimal totalPrice = new BigDecimal(0);
                if (CollectionUtils.isNotEmpty(content)) {
                    for (RfxCheckItemDTO item : content) {
                        totalPrice = totalPrice.add(item.getTotalPrice());
                    }
                }
                queryDTO.setTotalPrice(totalPrice);
            }
        }
        return rfxSupplierQueryDTOS;
    }
}
