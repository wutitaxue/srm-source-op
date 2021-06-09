package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.CalibrationApprovalGetData;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;
import org.srm.source.rfx.app.service.RfxHeaderService;

@Api(
        tags = {"Rcwl Calibration Approval"}
)
@RestController("rcwlCalibrationApprovalController.v1")
@RequestMapping({"/v1/{organizationId}/rcwlCalibrationApprovalController"})
public class RcwlCalibrationApprovalController extends BaseController {

    @Autowired
    private RcwlCalibrationApprovalService rcwlCalibrationApprovalService;

    @ApiOperation("定标审批BPM模拟")
    @Permission(
            permissionPublic = true
//            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/release-bpm-simulate"})
    public ResponseCalibrationApprovalData connectCalibrationApprovalByBPM(@PathVariable String organizationId, @RequestBody CalibrationApprovalGetData calibrationApprovalData) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        responseData = rcwlCalibrationApprovalService.connectBPM(organizationId, calibrationApprovalData.getRfxHeaderId());
        return responseData;
    }

}
