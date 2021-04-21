package org.srm.source.cux.rfx.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.rfx.api.controller.config.TransactionSwaggerApiConfig;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;
import org.srm.source.cux.rfx.app.service.RcwlRfxSupplierQueryService;
import org.srm.source.cux.rfx.infra.constant.RcwlSourceConstant;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * @Author hand_ghq
 * @Date 2021/3/24
 * @Version V1.0
 */
@Api(TransactionSwaggerApiConfig.RFX_SUPPLIER_QUERY)
@RestController("rcwlRfxSupplierQueryController.v1")
@RequestMapping({"/v1/{organizationId}/rfx/supplier"})
@Tenant(RcwlSourceConstant.TENANT_CODE)
public class RcwlRfxSupplierQueryController extends BaseController {

    @Autowired
    private RcwlRfxSupplierQueryService rfxSupplierQueryService;

    @ApiOperation("供应商360查询：询价信息")
    @GetMapping("/query")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    public ResponseEntity<List<RcwlRfxSupplierQueryDTO>> queryRfxSupplierQueryDTO(@PathVariable("organizationId") Long organizationId,
                                                                                  @RequestParam("supplierCompanyId") Long supplierCompanyId){
        return Results.success(rfxSupplierQueryService.queryRfxSupplierQueryDTO(organizationId, supplierCompanyId));
    }
}
