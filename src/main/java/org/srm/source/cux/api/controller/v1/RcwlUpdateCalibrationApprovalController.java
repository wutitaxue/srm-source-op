package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.RcwlDBSPTGDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataDTO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;
import org.srm.source.rfx.api.dto.CheckPriceDTO;
import org.srm.source.rfx.api.dto.CheckPriceHeaderDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;

import java.util.ArrayList;
import java.util.List;

@Api(
        tags = {"Rcwl Update Calibration Approval"}
)
@RestController("rcwlUpdateCalibrationApprovalController.v1")
@RequestMapping({"/v1/rcwlUpdateCalibrationApprovalController"})
public class RcwlUpdateCalibrationApprovalController extends BaseController {
    @Autowired
    private RcwlCalibrationApprovalService rcwlCalibrationApprovalService;
    @Autowired
    private RfxHeaderService rfxHeaderService;

    @ApiOperation("更新定标字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-calibration-approval"})
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


    @ApiOperation("定标审批通过")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/check/approved/for/bpm"})
    @FilterSupplier
    public ResponseCalibrationApprovalData checkPriceApproved(@RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum());
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        try{
            rfxHeaderService.checkPriceApproved(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId);
        }catch(Exception e){
            responseData.setCode("201");
            responseData.setMessage("操作失败！");
        }
        return responseData;
    }

    @ApiOperation("定标审批拒绝")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/check/reject/for/bpm"})
    @FilterSupplier
    public ResponseCalibrationApprovalData checkPriceReject(@RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum());
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        try{
            rfxHeaderService.checkPriceReject(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId);
        }catch(Exception e){
            responseData.setCode("201");
            responseData.setMessage("操作失败！");
        }
        return responseData;
    }

    @ApiOperation("定标提交")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/check/submit/for/bpm"})
    @FilterSupplier
    public ResponseCalibrationApprovalData checkPriceSubmit(@RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum());
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        CheckPriceHeaderDTO checkPriceHeaderDTO = new CheckPriceHeaderDTO();
        CheckPriceHeaderDTO validPriceHeaderDTO = this.rfxHeaderService.latestValidCreateItemCheck(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId, checkPriceHeaderDTO);
        if(!BaseConstants.Flag.NO.equals(validPriceHeaderDTO.getCreateItemFlag())) {
            responseData.setCode("201");
            responseData.setMessage("操作驳回！");
        } else {
            this.validObject(checkPriceHeaderDTO, new Class[0]);
            this.rfxHeaderService.checkPriceSubmit(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId, checkPriceHeaderDTO);
        }
            return responseData;
    }

    public CheckPriceHeaderDTO getCheckPriceHeaderDTOByData(Long rfxHeaderId){
        CheckPriceHeaderDTO checkPriceHeaderDTO = new CheckPriceHeaderDTO();
        RfxHeader rfxHeader = rfxHeaderService.selectSimpleRfxHeaderById(rfxHeaderId);
        checkPriceHeaderDTO.setRfxHeaderId(rfxHeader.getRfxHeaderId());
        checkPriceHeaderDTO.setTotalCost(rfxHeader.getTotalCost());
        checkPriceHeaderDTO.setTotalPrice(rfxHeader.getTotalPrice());
        checkPriceHeaderDTO.setCostRemark(rfxHeader.getCostRemark());
        checkPriceHeaderDTO.setObjectVersionNumber(rfxHeader.getObjectVersionNumber());
        checkPriceHeaderDTO.setCheckAttachmentUuid(rfxHeader.getCheckAttachmentUuid());
        checkPriceHeaderDTO.setSuppAttachmentUuid("");
        checkPriceHeaderDTO.setReleaseItemIds("");
        checkPriceHeaderDTO.setPriceEffectiveDate(rfxHeader.getPriceEffectiveDate().toString());
        checkPriceHeaderDTO.setPriceExpiryDate(rfxHeader.getPriceExpiryDate().toString());
        checkPriceHeaderDTO.setCheckRemark(rfxHeader.getCheckRemark());
        checkPriceHeaderDTO.setCreateItemFlag(0);
        checkPriceHeaderDTO.setProjectName(rfxHeader.getSourceProjectName());
        checkPriceHeaderDTO.setSelectionStrategy("");
        checkPriceHeaderDTO.setOnlyAllowAllWinBids(rfxHeader.getOnlyAllowAllWinBids());
        List<CheckPriceDTO> checkPriceDTOLineList = new ArrayList<>();
        CheckPriceDTO checkPriceDTO = new CheckPriceDTO();

        checkPriceHeaderDTO.setCheckPriceDTOLineList(checkPriceDTOLineList);
        List<RfxLineItem> rfxLineItemList = new ArrayList<>();

        checkPriceHeaderDTO.setRfxLineItemList(rfxLineItemList);
        return  checkPriceHeaderDTO;
    }
}
