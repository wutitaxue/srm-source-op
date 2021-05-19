package org.srm.source.cux.rfx.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.rfx.app.service.RcwlRfxHeaderAttachmentService;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;

@Api(
        tags = {"Rfx Header"}
)
@RestController("RcwlRfxHeaderController.v1")
@RequestMapping({"/v1/{organizationId}/rfx"})
public class RcwlRfxHeaderController {
    @Autowired
    private RcwlRfxHeaderAttachmentService attachmentService;

    @ApiOperation(value = "关闭附件保存")
    @Permission(level = ResourceLevel.ORGANIZATION)

    @PostMapping("/header/close-attachment")
    public ResponseEntity saveAttachment(@PathVariable("organizationId") Long tenantId, @RequestBody RfxHeaderDTO rfxHeaderDTO) {
             this.attachmentService.saveCloseAttachment(rfxHeaderDTO,tenantId);
        return Results.success();
    }
}
