package org.srm.source.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.api.dto.HeaderQueryDTO;
import org.srm.source.api.dto.PlanHeaderExportDTO;
import org.srm.source.app.service.RCWLPlanHeaderService;
import org.srm.source.app.service.RCWLPrLineService;
import org.srm.source.config.SsrcSwaggerApiConfig;
import org.srm.source.domain.entity.PlanHeader;
import org.srm.source.domain.repository.RCWLPlanHeaderRepository;
import org.srm.source.domain.vo.PlanHeaderExportVO;
import org.srm.source.domain.vo.PlanHeaderVO;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采购计划表 管理 API
 *
 * @author bin.zhang06@hand-china.com 2021-03-15 12:30:00
 */
@Api(tags = SsrcSwaggerApiConfig.SPRM_PLAN)
@RestController("planHeaderController.v1")
@RequestMapping("/v1/{organizationId}/plan-headers")
public class RCWLPlanHeaderController extends BaseController {

    @Autowired
    private RCWLPlanHeaderRepository RCWLPlanHeaderRepository;
    @Autowired
    private RCWLPlanHeaderService RCWLPlanHeaderService;
    @Autowired
    private RCWLPrLineService prLineService;
    @ApiOperation(value = "采购计划查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<Page<PlanHeaderVO>> listRfxHeader(@PathVariable Long organizationId,
                                                            HeaderQueryDTO planHeaderParam,
                                                            @ApiIgnore @SortDefault(value = PlanHeader.FIELD_PLAN_ID,
                                                                     direction = Sort.Direction.DESC) PageRequest pageRequest) {

        planHeaderParam.setTenantId(organizationId);
        return Results.success(RCWLPlanHeaderService.listPlanHeader(pageRequest, organizationId, planHeaderParam));
    }


    @ApiOperation("采购计划头导出")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/all/export"})
    @ExcelExport(
            value = PlanHeaderExportVO.class

    )
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<List<PlanHeaderExportVO>> exportPrHeaders(@PathVariable("organizationId") Long organizationId, PlanHeaderExportDTO planHeaderExportDTO, ExportParam exportParam, HttpServletResponse response,@ApiIgnore @SortDefault(value = PlanHeader.FIELD_CREATION_DATE, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        planHeaderExportDTO.setTenantId(organizationId);
        Page<PlanHeaderExportVO> planHeaderExportVOS = RCWLPlanHeaderRepository.exportPlanHeaders(pageRequest, planHeaderExportDTO);
        return Results.success(planHeaderExportVOS);
    }

    @ApiOperation(value = "采购计划附件保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/header/attachment")
    public ResponseEntity saveAttachment(@PathVariable("organizationId") Long tenantId, @RequestBody PlanHeader planHeader) {
        RCWLPlanHeaderService.saveAttachment(tenantId, planHeader);
        return Results.success();
    }

    @ApiOperation(value = "批量取消采购计划")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cancel")
    public ResponseEntity<List<PlanHeader>> batchCancelPlanHeader(@ApiParam(value = "租户id", required = true) @PathVariable(value = "organizationId") Long organizationId,
                                                                  @ApiParam(value = "采购计划表list") @RequestBody List<PlanHeader> planHeaderList) {

        return Results.success(RCWLPlanHeaderService.batchCancelPlanHeader(organizationId, planHeaderList));
    }

    @ApiOperation(value = "创建保存采购计划")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save")
    public ResponseEntity batchCreateAndUpdate(@PathVariable Long organizationId,@RequestBody PlanHeader planHeaderParam) {
        planHeaderParam.setTenantId(organizationId);
        this.RCWLPlanHeaderService.createAndUpdate(planHeaderParam);
        return Results.success();
    }

    @ApiOperation(value = "采购计划表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<PlanHeader>> list(PlanHeader planHeader, @ApiIgnore @SortDefault(value = PlanHeader.FIELD_PLAN_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<PlanHeader> list = RCWLPlanHeaderRepository.pageAndSort(pageRequest, planHeader);
        return Results.success(list);
    }

    @ApiOperation(value = "采购计划表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{planId}")
    public ResponseEntity<PlanHeader> detail(@PathVariable Long planId) {
        PlanHeader planHeader = RCWLPlanHeaderRepository.selectByPrimaryKey(planId);
        return Results.success(planHeader);
    }

    @ApiOperation(value = "创建采购计划表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<PlanHeader> create(@RequestBody PlanHeader planHeader) {
        validObject(planHeader);
        RCWLPlanHeaderRepository.insertSelective(planHeader);
        return Results.success(planHeader);
    }

    @ApiOperation(value = "修改采购计划表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<PlanHeader> update(@RequestBody PlanHeader planHeader) {
        SecurityTokenHelper.validToken(planHeader);
        RCWLPlanHeaderRepository.updateByPrimaryKeySelective(planHeader);
        return Results.success(planHeader);
    }

    @ApiOperation(value = "删除采购计划表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody PlanHeader planHeader) {
        SecurityTokenHelper.validToken(planHeader);
        RCWLPlanHeaderRepository.deleteByPrimaryKey(planHeader);
        return Results.success();
    }

}
