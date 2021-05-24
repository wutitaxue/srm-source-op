package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.RcwlClarifyForBPM;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.share.app.service.impl.ClarifyServiceImpl;
import org.srm.source.share.domain.entity.Clarify;
import org.srm.source.share.domain.entity.IssueLine;

import javax.annotation.Resource;
import java.util.List;

@Api(
        tags = {"Rcwl Clarify"}
)
@RestController("rcwlClarifyController.v1")
@RequestMapping({"/v1/{organizationId}/rcwlClarifyController"})
public class RcwlClarifyController extends BaseController{
    @Resource
    private RcwlClarifyService rcwlClarifyService;
    @Autowired
    private ClarifyServiceImpl clarifyService;

    @ApiOperation("澄清函发布BPM模拟")
    @Permission(
//            permissionPublic = true
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/release-bpm-simulate"})
    public ResponseData releaseClarifyByBPM(@PathVariable String organizationId,@RequestBody Clarify clarify) {
        ResponseData responseData = new ResponseData();
        this.validObject(clarify, new Class[0]);
        List<IssueLine> issueLines = clarifyService.validIssueLine(clarify.getIssueLineIdList(), clarify.getClarifyId());
        clarify.setIssueLineList(issueLines);
        try{
            clarifyService.saveClarify(Long.valueOf(organizationId), clarify);
        }catch (Exception e){
            responseData.setMessage("澄清函保存失败！");
            responseData.setCode("201");
        }
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
        rcwlSonOfClarify.setIssueLineList(clarify.getIssueLineList());
        rcwlSonOfClarify.setCreatedBy(clarify.getCreatedBy());
        rcwlSonOfClarify.setProcessInstanceId("0");
        responseData = rcwlClarifyService.releaseClarifyByBPM(rcwlSonOfClarify);
        return responseData;
    }
}
