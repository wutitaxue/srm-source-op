package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlRfxHeaderBpmService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.web.annotation.Tenant;

@Api(
        tags = {"Rfx Header"}
)
@RestController("rfxHeaderController.v1")
@RequestMapping({"/v1/{organizationId}/rfx"})
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderController {

    @Autowired
    private RcwlRfxHeaderBpmService rcwlRfxHeaderBpmService;

    @ApiOperation("询价单发布")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/release"})
    @FilterSupplier
    public ResponseEntity releaseRfx(@PathVariable Long organizationId, @Encrypt @RequestBody RfxFullHeader rfxFullHeader) {
        rcwlRfxHeaderBpmService.rcwlReleaseRfx(organizationId,rfxFullHeader);
        return Results.success();
    }

}
