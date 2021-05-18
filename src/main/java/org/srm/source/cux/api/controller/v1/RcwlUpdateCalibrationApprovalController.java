package org.srm.source.cux.api.controller.v1;

import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataDTO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;

@Api(
        tags = {"Rcwl Update Calibration Approval"}
)
@RestController("rcwlUpdateCalibrationApprovalController.v1")
@RequestMapping({"/v1/rcwlUpdateCalibrationApprovalController"})
public class RcwlUpdateCalibrationApprovalController {
    @Autowired
    private RcwlCalibrationApprovalService rcwlCalibrationApprovalService;

    @ApiOperation("更新定标字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-clarify"})
    public ResponseCalibrationApprovalData updateClarifyData(@RequestBody RcwlUpdateCalibrationApprovalDTO rcwlUpdateDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        RcwlUpdateCalibrationApprovalDataDTO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateCalibrationApprovalDataDTO();
        if(rcwlUpdateDataDTO.getRfxNum() == null || "".equals(rcwlUpdateDataDTO.getRfxNum())){
            responseData.setCode("201");
            responseData.setMessage("单据编号获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getAttribute_varchar4() == null || "".equals(rcwlUpdateDataDTO.getAttribute_varchar4()))&&
                (rcwlUpdateDataDTO.getAttribute_varchar5() == null || "".equals(rcwlUpdateDataDTO.getAttribute_varchar5()))){
            responseData.setCode("201");
            responseData.setMessage("所需更新数据至少有一个值！");
            return responseData;
        }
        responseData = rcwlCalibrationApprovalService.updateClarifyData(rcwlUpdateDataDTO);
        return responseData;
    }
}
