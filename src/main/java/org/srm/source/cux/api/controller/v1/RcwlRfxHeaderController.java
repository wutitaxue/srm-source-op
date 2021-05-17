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
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
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
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxHeaderService rfxHeaderService;

    @ApiOperation("询价单发布")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/release"})
    @FilterSupplier
    public ResponseEntity<String> releaseRfx(@PathVariable Long organizationId, @Encrypt @RequestBody RfxFullHeader rfxFullHeader) {
        String s = rcwlRfxHeaderBpmService.rcwlReleaseRfx(organizationId, rfxFullHeader);
        return Results.success(s);
    }

    @ApiOperation("bpm立项拒绝后修改字段")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/updateInRefuse"})
    public ResponseEntity updateInRefuse(@PathVariable Long organizationId,@RequestParam("rfxNum")String rfxNum) {
        RfxHeader rfxHeaderTemp = new RfxHeader();
        rfxHeaderTemp.setTenantId(organizationId);
        rfxHeaderTemp.setRfxNum(rfxNum);
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeaderTemp);
        rfxHeader.setAttributeVarchar2("0");
        rfxHeaderRepository.updateByPrimaryKeySelective(rfxHeader);
        return Results.success();
    }
    @ApiOperation("bpm立项提交后修改字段")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping({"/updateInSubmitt"})
    public ResponseEntity updateInSubmitt(@PathVariable Long organizationId, @RequestParam("rfxNum")String rfxNum,@RequestParam("attributeVarchar2")String attributeVarchar2,@RequestParam("attributeVarchar3") String attributeVarchar3) {
        RfxHeader rfxHeaderTemp = new RfxHeader();
        rfxHeaderTemp.setTenantId(organizationId);
        rfxHeaderTemp.setRfxNum(rfxNum);
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeaderTemp);
        rfxHeader.setAttributeVarchar2(attributeVarchar2);
        rfxHeader.setAttributeVarchar3(attributeVarchar3);
        rfxHeaderRepository.updateByPrimaryKeySelective(rfxHeader);
        return Results.success();
    }

    @ApiOperation("bpm立项提交")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )

    @PostMapping({"/bpmRfxSubmit"})
    public ResponseEntity bpmRfxSubmit(@PathVariable Long organizationId, @RequestParam("rfxNum")String rfxNum) {
        RfxHeader rfxHeadertemp = new RfxHeader();
        rfxHeadertemp.setTenantId(organizationId);
        rfxHeadertemp.setRfxNum(rfxNum);
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeadertemp);
        RfxFullHeader rfxFullHeader = new RfxFullHeader();
        rfxFullHeader.setRfxHeader(rfxHeader);
        rfxHeaderService.releaseRfx(organizationId, rfxFullHeader);
        return Results.success();
    }

    @ApiOperation("bpm立项同意")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/bpmRfxApprove"})
    public ResponseEntity bpmRfxApprove(@PathVariable Long organizationId, @RequestParam("rfxNum")String rfxNum) {
        RfxHeader rfxHeadertemp = new RfxHeader();
        rfxHeadertemp.setTenantId(organizationId);
        rfxHeadertemp.setRfxNum(rfxNum);
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeadertemp);
        this.rfxHeaderService.rfxApproval(organizationId, rfxHeader.getRfxHeaderId(), 0);
        return Results.success();
    }

    @ApiOperation("bpm立项拒绝")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/bpmRfxRefuse"})
    public ResponseEntity bpmRfxRefuse(@PathVariable Long organizationId, @RequestParam("rfxNum")String rfxNum) {
        RfxHeader rfxHeadertemp = new RfxHeader();
        rfxHeadertemp.setTenantId(organizationId);
        rfxHeadertemp.setRfxNum(rfxNum);
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeadertemp);
        this.rfxHeaderService.rfxReject(organizationId, rfxHeader.getRfxHeaderId());
        return Results.success();
    }


}
