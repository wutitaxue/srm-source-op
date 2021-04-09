package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.cux.app.service.RCWLShortlistHeaderService;
import org.srm.source.cux.config.ShortlistSourceSwaggerApiConfig;
import org.srm.source.cux.domain.entity.RCWLSample;
import org.srm.source.cux.domain.entity.RCWLShortlistHeader;
import org.srm.source.cux.domain.entity.RCWLSupplierHeader;
import org.srm.source.cux.infra.constant.RCWLShortlistContants;
import org.srm.web.annotation.Tenant;

import javax.annotation.Resource;
import java.util.List;


@Api(ShortlistSourceSwaggerApiConfig.SOURCE_RFX_SHORTLIST)
@RestController("rcwlShortlistController.v1")
@RequestMapping({"/v1/{organizationId}/shortlist"})
@Tenant(RCWLShortlistContants.TENANT_NUMBER)
public class RCWLShortlistHeaderController {
    @Resource
    private RCWLShortlistHeaderService rcwlShortlistHeaderService;

    @ApiOperation(value = "租户批量查询入围单头-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/list")
    @FilterSupplier
    public ResponseEntity<Page<RCWLShortlistHeader>> listShortlistHeader(@PathVariable("organizationId") Long organizationId,
                                                                         @Encrypt RCWLShortlistHeader rcwlShortlistHeader, PageRequest pageRequest) {
        return Results.success(rcwlShortlistHeaderService.listShortlistHeader(rcwlShortlistHeader, pageRequest));
    }

    @ApiOperation(value = "供应商批量查询入围单头-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/supplier-list/list")
    @FilterSupplier
    public ResponseEntity<Page<RCWLShortlistHeader>> listShortlistHeaderSupplier(@PathVariable("organizationId") Long organizationId,
                                                                                 @Encrypt RCWLShortlistHeader rcwlShortlistHeader, PageRequest pageRequest) {
        return Results.success(rcwlShortlistHeaderService.listShortlistHeaderSupplier(rcwlShortlistHeader, pageRequest));
    }

    @ApiOperation(value = "明细入围单头表-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/{shortlistHeaderId}")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @FilterSupplier
    public ResponseEntity<RCWLShortlistHeader> detailShortlistHeader(@PathVariable Long organizationId,
                                                                     @Encrypt @PathVariable Long shortlistHeaderId) {
        return Results.success(rcwlShortlistHeaderService.selectOneShortlistHeader(shortlistHeaderId));
    }

    @ApiOperation(value = "查询入围单供应商列表-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/listSupplier/{shortlistHeaderId}")
    @FilterSupplier
    public ResponseEntity<Page<RCWLSupplierHeader>> listShortlistSupplier(@PathVariable("organizationId") Long organizationId,
                                                                          @Encrypt RCWLSupplierHeader rcwlSupplierHeader, PageRequest pageRequest, Long shortlistHeaderId) {
        return Results.success(rcwlShortlistHeaderService.listShortlistSupplier(rcwlSupplierHeader, pageRequest, shortlistHeaderId));
    }

    @ApiOperation(value = "查询入围单采购申请单-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/listPrline/{shortlistHeaderId}")
    @FilterSupplier
    public ResponseEntity<Page<PrLineVO>> listPrline(@PathVariable("organizationId") Long organizationId,
                                                     @Encrypt PrLineVO prline, PageRequest pageRequest, Long shortlistHeaderId) {
        return Results.success(rcwlShortlistHeaderService.listPrline(prline, pageRequest, shortlistHeaderId));
    }

    @ApiOperation(value = "批量查询送样单-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/listSample")
    @FilterSupplier
    public ResponseEntity<Page<RCWLSample>> listSample(@PathVariable("organizationId") Long organizationId,
                                                       @Encrypt RCWLSample rcwlSample, PageRequest pageRequest, Long shortlistHeaderId) {
        return Results.success(rcwlShortlistHeaderService.listSample(rcwlSample, pageRequest, shortlistHeaderId));
    }

    @ApiOperation(value = "明细送样表-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/scux-rcwl/sample/{sampleId}")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    @FilterSupplier
    public ResponseEntity<RCWLSample> selectOneSample(@PathVariable Long organizationId,
                                                      @Encrypt @PathVariable Long sampleId) {
        return Results.success(rcwlShortlistHeaderService.selectOneSample(sampleId));
    }

    @ApiOperation(value = "创建/修改入围单头表-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/create")
    @FilterSupplier
    public ResponseEntity<Integer> createOrUpdateShortlist(@PathVariable Long organizationId,
                                                           @Encrypt @RequestBody RCWLShortlistHeader rcwlShortlistHeader) {
        rcwlShortlistHeader.setTenantId(organizationId);
        rcwlShortlistHeaderService.createOrUpdateShortlistHeader(rcwlShortlistHeader);
        return Results.success();
    }

    @ApiOperation(value = "采购申请单转入围单-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/purchase-create")
    @FilterSupplier
    public ResponseEntity<RCWLShortlistHeader> purchaseRequisitionToBeShortlisted(@PathVariable Long organizationId,
                                                                      @Encrypt @RequestBody List<Long> prLineIds) {
        RCWLShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderService.purchaseRequisitionToBeShortlisted(prLineIds);
        return Results.success(rcwlShortlistHeader);
    }

    @ApiOperation(value = "删除入围单头-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/cancel/{shortlistHeaderId}")
    @FilterSupplier
    public ResponseEntity<Integer> cancelShortlistHeader(@PathVariable Long organizationId,
                                                         @Encrypt @RequestBody List<PrLineVO> prLineList, Long shortlistHeaderId) {
        rcwlShortlistHeaderService.cancelShortlistHeader(shortlistHeaderId, prLineList);
        return Results.success();
    }

    @ApiOperation(value = "提交入围单头-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/submit/{shortlistHeaderId}")
    @FilterSupplier
    public ResponseEntity<Integer> submitShortlistHeader(@PathVariable Long organizationId,
                                                         @PathVariable @Encrypt Long shortlistHeaderId) {
        rcwlShortlistHeaderService.submitShortlistHeader(shortlistHeaderId);
        return Results.success();
    }

    @ApiOperation(value = "发布入围单头-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/release/{shortlistHeaderId}")
    @FilterSupplier
    public ResponseEntity<Integer> releaseShortlistHeader(@PathVariable Long organizationId,
                                                          @PathVariable @Encrypt Long shortlistHeaderId) {
        rcwlShortlistHeaderService.releaseShortlistHeader(shortlistHeaderId);
        return Results.success();
    }

    @ApiOperation(value = "创建/修改送样表-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/sample/create")
    @FilterSupplier
    public ResponseEntity<Integer> createOrUpdateSample(@PathVariable Long organizationId,
                                                        @Encrypt @RequestBody RCWLSample rcwlSample, Long companyId) {
        rcwlSample.setTenantId(organizationId);
        rcwlShortlistHeaderService.createOrUpdateSample(rcwlSample, companyId);
        return Results.success();
    }

    @ApiOperation(value = "批量创建送样表-融创")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/scux-rcwl/sample/batch-create")
    @FilterSupplier
    public ResponseEntity<Integer> batchInsertSample(@PathVariable Long organizationId,
                                                     @Encrypt @RequestBody List<RCWLSample> rcwlSamples, Long companyId) {
        rcwlShortlistHeaderService.batchInsertSample(rcwlSamples, companyId);
        return Results.success();
    }
}
