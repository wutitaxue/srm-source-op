package org.srm.source.cux.api.controller.v1;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.api.dto.HeaderQueryDTO;
import org.srm.source.cux.api.dto.PlanHeaderExportDTO;
import org.srm.source.cux.app.service.RCWLPlanHeaderService;
import org.srm.source.cux.app.service.RCWLPrLineService;
import org.srm.source.cux.config.*;
import org.srm.source.cux.domain.entity.PlanHeader;
import org.srm.source.cux.domain.repository.RCWLPlanHeaderRepository;
import org.srm.source.cux.domain.vo.PlanHeaderExportVO;
import org.srm.source.cux.domain.vo.PlanHeaderVO;
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
    //@Permission(permissionPublic = true)
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
    //@Permission(permissionPublic = true)
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

   // @Permission(permissionPublic = true)
    @PostMapping("/header/attachment")
    public ResponseEntity saveAttachment(@PathVariable("organizationId") Long tenantId, @RequestBody PlanHeader planHeader) {
        RCWLPlanHeaderService.saveAttachment(tenantId, planHeader);
        return Results.success();
    }

    @ApiOperation(value = "批量取消采购计划")
    @Permission(level = ResourceLevel.ORGANIZATION)
//   @Permission(permissionPublic = true)

    @PostMapping("/cancel")
    public ResponseEntity<List<PlanHeader>> batchCancelPlanHeader(@ApiParam(value = "租户id", required = true) @PathVariable(value = "organizationId") Long organizationId,
                                                                  @ApiParam(value = "采购计划表list") @RequestBody List<PlanHeader> planHeaderList) {

        return Results.success(RCWLPlanHeaderService.batchCancelPlanHeader(organizationId, planHeaderList));
    }

    @ApiOperation(value = "创建保存采购计划")
    @Permission(level = ResourceLevel.ORGANIZATION)
    //@Permission(permissionPublic = true)
    @PostMapping("/save")
    public ResponseEntity batchCreateAndUpdate(@PathVariable Long organizationId,@RequestBody PlanHeader planHeaderParam) {
        planHeaderParam.setTenantId(organizationId);
        this.RCWLPlanHeaderService.createAndUpdate(planHeaderParam);
        return Results.success();
    }


}
