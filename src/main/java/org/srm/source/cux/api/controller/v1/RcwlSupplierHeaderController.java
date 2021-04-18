package org.srm.source.cux.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlSupplierHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 入围单供应商头信息 管理 API
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@RestController("rcwlSupplierHeaderController.v1")
@RequestMapping("/v1/{organizationId}/rcwl-supplier-headers")
public class RcwlSupplierHeaderController extends BaseController {

    @Autowired
    private RcwlSupplierHeaderRepository rcwlSupplierHeaderRepository;

    @ApiOperation(value = "入围单供应商头信息列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RcwlShortlistHeader>> list(RcwlShortlistQueryDTO rcwlShortlistQueryDTO, @ApiIgnore @SortDefault(value = RcwlSupplierHeader.FIELD_SUPPLIER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RcwlShortlistHeader> list = rcwlSupplierHeaderRepository.pageAndSortRcwlSupplierHeader(pageRequest, rcwlShortlistQueryDTO);
        return Results.success(list);
    }

    @ApiOperation(value = "入围单供应商头信息明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{supplierHeaderId}")
    public ResponseEntity<RcwlSupplierHeader> detail(@PathVariable Long supplierHeaderId) {
        RcwlSupplierHeader rcwlSupplierHeader = rcwlSupplierHeaderRepository.detailRcwlSupplierHeader(supplierHeaderId);
        return Results.success(rcwlSupplierHeader);
    }

    @ApiOperation(value = "创建入围单供应商头信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RcwlSupplierHeader> create(@RequestBody RcwlSupplierHeader rcwlSupplierHeader) {
        validObject(rcwlSupplierHeader);
        rcwlSupplierHeaderRepository.createAndUpdateSupplierHeader(rcwlSupplierHeader);
        return Results.success(rcwlSupplierHeader);
    }

    @ApiOperation(value = "删除入围单供应商头信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<RcwlSupplierHeader> rcwlSupplierHeaders) {
        //SecurityTokenHelper.validToken(rcwlSupplierHeader);
        rcwlSupplierHeaderRepository.batchDeleteBySupplierHeader(rcwlSupplierHeaders);
        return Results.success();
    }

}
