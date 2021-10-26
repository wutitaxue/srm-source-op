package org.srm.source.cux.rfx.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.rfx.api.controller.dto.UrlDTO;
import org.srm.source.cux.rfx.app.service.RcwlRfxHeaderAttachmentService;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.cux.rfx.app.service.RcwlRfxHeaderBpmService;
import org.srm.source.cux.rfx.infra.mapper.RcwlRfxHeaderBpmMapper;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxMemberService;
import org.srm.source.rfx.app.service.v2.RfxHeaderServiceV2;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxLineSupplier;
import org.srm.source.rfx.domain.entity.RfxMember;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.repository.RfxLineSupplierRepository;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.share.api.dto.EvaluateIndicDTO;
import org.srm.source.share.domain.entity.EvaluateIndic;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
import org.srm.web.annotation.Tenant;

import java.util.List;

@Api(
        tags = {"Rfx Header"}
)
@RestController("RcwlRfxHeaderController.v1")
@RequestMapping({"/v1/{organizationId}/rfx"})
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderController {
    @Autowired
    private RcwlRfxHeaderAttachmentService attachmentService;
    @Autowired
    private RcwlRfxHeaderBpmService rcwlRfxHeaderBpmService;
    @Autowired
    private RfxMemberService rfxMemberService;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;
    @Autowired
    private RfxLineSupplierRepository rfxLineSupplierRepository;
    @Autowired
    private RfxHeaderServiceV2 rfxHeaderServiceV2;
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private RcwlRfxHeaderBpmMapper rcwlRfxHeaderBpmMapper;


    @ApiOperation(value = "关闭附件保存")
    @Permission(level = ResourceLevel.ORGANIZATION)

    @PostMapping("/header/close-attachment")
    public ResponseEntity saveAttachment(@PathVariable("organizationId") Long tenantId, @RequestBody RfxHeaderDTO rfxHeaderDTO) {
             this.attachmentService.saveCloseAttachment(rfxHeaderDTO,tenantId);
        return Results.success();
    }


    @ApiOperation("新询价单发布")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/bpmRelease"})
    @FilterSupplier
    public ResponseEntity<UrlDTO> rcwlReleaseRfx(@PathVariable Long organizationId, @Encrypt @RequestBody RfxFullHeader rfxFullHeader) {

        String s = rcwlRfxHeaderBpmService.rcwlReleaseRfx(organizationId, rfxFullHeader);
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setBackUrl(s);
        return Results.success(urlDTO);
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
        RfxMember rfxMember = new RfxMember();
        rfxHeadertemp.setTenantId(organizationId);
        rfxHeadertemp.setRfxNum(rfxNum);
        //rfxHeader
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeadertemp);
        //rfxLineItem
        RfxLineItem rfxLineItem = new RfxLineItem();
        rfxLineItem.setTenantId(organizationId);
        rfxLineItem.setRfxHeaderId(rfxHeader.getRfxHeaderId());
        List<RfxLineItem> rfxLineItems = rfxLineItemRepository.select(rfxLineItem);
        rfxHeader.setRfxLineItemList(rfxLineItems);
        //rfxFullHeader
        RfxFullHeader rfxFullHeader = new RfxFullHeader();
        rfxFullHeader.setRfxHeader(rfxHeader);
        rfxFullHeader.setRfxLineItemList(rfxLineItems);
        rfxMember.setTemplateId(rfxHeader.getTemplateId());
        rfxFullHeader.setRfxMemberList(rfxMemberService.listRfxMember(organizationId, rfxHeader.getRfxHeaderId(), rfxMember));
        BiddingWorkDTO biddingWorkDTO = new BiddingWorkDTO();
        biddingWorkDTO.setEvaluateExpertList(evaluateExpertRepository.queryEvaluateExpert(organizationId, rfxHeader.getRfxHeaderId(), "RFX", null, null));
        rfxFullHeader.setEvaluateExperts(biddingWorkDTO);
        EvaluateIndicDTO evaluateIndicDTO = new EvaluateIndicDTO();
        evaluateIndicDTO.setTenantId(organizationId);
        evaluateIndicDTO.setIndicStatus("SUBMITTED");
        evaluateIndicDTO.setIndicateLevel("ONE");
        evaluateIndicDTO.setSourceFrom("RFX");
        evaluateIndicDTO.setSourceHeaderId(rfxHeader.getRfxHeaderId());
        List<EvaluateIndic> evaluateIndicS = rcwlRfxHeaderBpmMapper.rcwlQueryEvaluateIndicate(evaluateIndicDTO);
        rfxFullHeader.setEvaluateIndics(evaluateIndicS);
        RfxLineSupplier rfxLineSupplier = new RfxLineSupplier();
        rfxLineSupplier.setRfxHeaderId(rfxHeader.getRfxHeaderId());
        rfxLineSupplier.setTenantId(organizationId);
        List<RfxLineSupplier> RfxLineSuppliers = rfxLineSupplierRepository.select(rfxLineSupplier);
        rfxFullHeader.setRfxLineSupplierList(RfxLineSuppliers);
        //
        rfxHeaderServiceV2.releaseRfx(organizationId, rfxFullHeader);
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
        Long userid = rcwlRfxHeaderBpmMapper.selectUserId();
        CustomUserDetails customUserDetails = new CustomUserDetails("default", "default");
        customUserDetails.setUserId(userid);
        customUserDetails.setTenantId(organizationId);
        customUserDetails.setOrganizationId(organizationId);
        customUserDetails.setLanguage("zh_CN");
        DetailsHelper.setCustomUserDetails(customUserDetails);
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
        Long userid = rcwlRfxHeaderBpmMapper.selectUserId();
        CustomUserDetails customUserDetails = new CustomUserDetails("default", "default");
        customUserDetails.setUserId(userid);
        customUserDetails.setTenantId(organizationId);
        customUserDetails.setOrganizationId(organizationId);
        customUserDetails.setLanguage("zh_CN");
        DetailsHelper.setCustomUserDetails(customUserDetails);
        RfxHeader rfxHeader = rfxHeaderRepository.selectOne(rfxHeadertemp);
        this.rfxHeaderService.rfxReject(organizationId, rfxHeader.getRfxHeaderId());
        return Results.success();
    }


}
