package org.srm.source.cux.api.controller.v1;


import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.app.service.RcwlBPMRfxHeaderService;
import org.srm.source.cux.domain.entity.RcwlGetData;
import org.srm.source.cux.domain.entity.ResponseData;

import javax.annotation.Resource;

@Api(
        tags = {"Rcwl Rfx Header"}
)
@RestController("rcwlRfxHeaderController.v1")
@RequestMapping({"/v1/{organizationId}/rcwlrfx"})
public class RcwlCloseRfxHeaderController extends BaseController {

    @Resource
    private RcwlBPMRfxHeaderService rcwlRfxHeaderService;

    @ApiOperation("询报价控制关闭接口")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/rcwl-close"})
    @ProcessLovValue(
            targetField = {"body"}
    )
    public ResponseData controlClose(@PathVariable("organizationId") Long organizationId,@RequestBody RcwlGetData data) {
        ResponseData responseData = new ResponseData();
        responseData = rcwlRfxHeaderService.newClose(organizationId, data.getGetData().getRfxHeaderId(), data.getGetData().getRemark(),data.getGetData().getAttributeVarchar20());
        return responseData;
    }

}
