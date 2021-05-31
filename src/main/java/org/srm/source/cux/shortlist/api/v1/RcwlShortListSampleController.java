package org.srm.source.cux.shortlist.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.shortlist.api.dto.RcwlShortListSampleDTO;
import org.srm.source.cux.shortlist.service.RcwlShortListSampleService;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/31 15:34
 * @version:1.0
 */
@Api("入围单转询价")
@RestController("RcwlShortToRfxController.v1")
@RequestMapping({"/v1/{organizationId}/shortlist/sample"})
public class RcwlShortListSampleController {
    @Autowired
    private RcwlShortListSampleService rcwlShortListSampleService;

    @ApiOperation("入围单采购申请查询")
    @GetMapping("/batch-create")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    public ResponseEntity<Void> rcwlListCreateSample(@PathVariable("organizationId") Long tenantId, @RequestBody RcwlShortListSampleDTO rcwlShortListSampleDTO) {
        rcwlShortListSampleService.rcwlBanthCreateSample(tenantId, rcwlShortListSampleDTO);
        return Results.success();
    }

}
