package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.RcwlCarifyReleaseDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.share.app.service.ClarifyService;
import org.srm.source.share.domain.entity.Clarify;

import javax.annotation.Resource;

@Api(
        tags = {"Rcwl Update Clarify"}
)
@RestController("rcwlUpdateClarifyController.v1")
@RequestMapping({"/v1/rcwlUpdateClarifyController"})
public class RcwlUpdateClarifyController extends BaseController {
    @Resource
    private RcwlClarifyService rcwlClarifyService;
    @Autowired
    private ClarifyService clarifyService;

    @ApiOperation("更新数据字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-clarify"})
    public ResponseData updateClarifyData(@RequestBody RcwlUpdateDTO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        RcwlUpdateDataDTO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateDataDTO();
        if((rcwlUpdateDataDTO.getClarifyNum() == null || "".equals(rcwlUpdateDataDTO.getClarifyNum()))&&
                (rcwlUpdateDataDTO.getTenantid() == null || "".equals(rcwlUpdateDataDTO.getTenantid()))){
            responseData.setCode("201");
            responseData.setMessage("单据编号或人员ID获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getProcessInstanceId() == null || "".equals(rcwlUpdateDataDTO.getProcessInstanceId()))&&
                (rcwlUpdateDataDTO.getClarifyStatus() == null || "".equals(rcwlUpdateDataDTO.getClarifyStatus()))&&
                (rcwlUpdateDataDTO.getWebserviceUrl() == null || "".equals(rcwlUpdateDataDTO.getWebserviceUrl()))){
            responseData.setCode("201");
            responseData.setMessage("所需更新数据至少有一个值！");
            return responseData;
        }
        responseData = rcwlClarifyService.updateClarifyData(rcwlUpdateDataDTO);
        return responseData;
    }

    @ApiOperation("澄清答疑发布")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/release"})
    @FilterSupplier
    public ResponseEntity<Clarify> releaseClarify(@RequestBody RcwlCarifyReleaseDTO rcwlCarifyReleaseDTO) {
        Long clarifyId = rcwlClarifyService.getClarifyIdByClarifyNum(rcwlCarifyReleaseDTO.getClarifyNum());
        Clarify clarify = clarifyService.queryClarifyDetail(rcwlCarifyReleaseDTO.getTenantid(),clarifyId);
        this.validObject(clarify, new Class[0]);
        return Results.success(clarifyService.releaseClarify(rcwlCarifyReleaseDTO.getTenantid(), clarify));
    }
}
