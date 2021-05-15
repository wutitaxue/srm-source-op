package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.share.domain.entity.Clarify;

import javax.annotation.Resource;

@Api(
        tags = {"Rcwl Clarify"}
)
@RestController("rcwlClarifyController.v1")
@RequestMapping({"/v1/{organizationId}/rcwlClarifyController"})
public class RcwlClarifyController extends BaseController{
    @Resource
    private RcwlClarifyService rcwlClarifyService;

    @ApiOperation("澄清函发布BPM模拟")
    @Permission(
//            permissionPublic = true
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/release-bpm-simulate"})
    public ResponseData releaseClarifyByBPM(@RequestBody Clarify clarify) {
        this.validObject(clarify, new Class[0]);
        RcwlClarifyForBPM rcwlSonOfClarify = new RcwlClarifyForBPM();
        rcwlSonOfClarify.setClarifyId(clarify.getClarifyId());
        rcwlSonOfClarify.setTenantId(clarify.getTenantId());
        rcwlSonOfClarify.setTitle(clarify.getTitle());
        rcwlSonOfClarify.setClarifyNum(clarify.getClarifyNum());
        rcwlSonOfClarify.setCompanyId(clarify.getCompanyId());
        rcwlSonOfClarify.setClarifyStatus(clarify.getClarifyStatus());
        rcwlSonOfClarify.setSubmittedBy(clarify.getSubmittedBy());
        rcwlSonOfClarify.setSubmittedDate(clarify.getSubmittedDate());
        rcwlSonOfClarify.setSourceId(clarify.getSourceId());
        rcwlSonOfClarify.setAttachmentUuid(clarify.getAttachmentUuid());
        rcwlSonOfClarify.setContext(clarify.getContext());
        rcwlSonOfClarify.setReferFlag(clarify.getReferFlag());
        rcwlSonOfClarify.setObjectVersionNumber(clarify.getObjectVersionNumber());
        rcwlSonOfClarify.setProcessInstanceId("0");
        ResponseData responseData = new ResponseData();
        responseData = rcwlClarifyService.releaseClarifyByBPM(rcwlSonOfClarify);
        return responseData;
    }

    @ApiOperation("更新数据字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-clarify"})
    public ResponseData updateClarifyData(@RequestBody RcwlUpdateDTO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        RcwlUpdateDataDTO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateDataDTO();
        if(rcwlUpdateDataDTO.getClarifyNum() == null || "".equals(rcwlUpdateDataDTO.getClarifyNum())){
            responseData.setCode("201");
            responseData.setMessage("单据编号获取异常！");
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
}
