package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.boot.platform.configcenter.CnfHelper;
import org.srm.source.cux.app.service.RcwlBPMRfxHeaderService;
import org.srm.source.cux.domain.entity.RcwlGetDataCloseDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateCloseRfxVO;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(
        tags = {"Rcwl Update Rfx Header"}
)
@RestController("rcwlUpdateRfxHeaderController.v1")
@RequestMapping({"/v1/rcwlUpdateRfxHeaderController"})
public class RcwlUpdateRfxHeaderController {

    @Resource
    private RcwlBPMRfxHeaderService rcwlRfxHeaderService;
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;


    @ApiOperation("更新询价单头表数据字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-rfxHeader"})
    public ResponseData updateRfxHeaderData(@RequestBody RcwlUpdateCloseRfxVO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateDataDTO();
        if(null ==  rcwlUpdateDTO || null == rcwlUpdateDataDTO){
            responseData.setCode("201");
            responseData.setMessage("参数为null，获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getRfxNum() == null || "".equals(rcwlUpdateDataDTO.getRfxNum()))&&
                (rcwlUpdateDataDTO.getTenantId() == null || "".equals(rcwlUpdateDataDTO.getTenantId()))){
            responseData.setCode("201");
            responseData.setMessage("询价单编号或人员信息获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getAttributeVarchar6() == null || "".equals(rcwlUpdateDataDTO.getAttributeVarchar6()))&&
                (rcwlUpdateDataDTO.getAttributeVarchar7() == null || "".equals(rcwlUpdateDataDTO.getAttributeVarchar7()))&&
                (rcwlUpdateDataDTO.getStatus() == null || "".equals(rcwlUpdateDataDTO.getStatus()))){
            responseData.setCode("201");
            responseData.setMessage("所需更新数据至少有一个值！");
            return responseData;
        }
        responseData = rcwlRfxHeaderService.updateRfxHeaderData(rcwlUpdateDataDTO);
        return responseData;
    }

    @ApiOperation("流标关闭")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/close-fxHeader"})
    @ProcessLovValue(
            targetField = {"body"}
    )
    public ResponseData controlClose(@RequestBody RcwlGetDataCloseDTO rcwlGetDataCloseDTO) {
        DetailsHelper.setCustomUserDetails(rcwlGetDataCloseDTO.getTenantId(),"zh_CN");
        if(rcwlGetDataCloseDTO.getRemark() == null){
            rcwlGetDataCloseDTO.setRemark("备注");
        }
        ResponseData responseData = new ResponseData();
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        Long rfxHeaderIds = rcwlRfxHeaderService.getRfxHeaderIdByRfxNum(rcwlGetDataCloseDTO.getRfxNum(),rcwlGetDataCloseDTO.getTenantId());
        try{
            rcwlRfxHeaderService.chooseRfxCloseApproveType(rcwlGetDataCloseDTO.getTenantId(), rfxHeaderIds, rcwlGetDataCloseDTO.getRemark());
            rcwlRfxHeaderService.updateTerminatedBy(rfxHeaderIds);
        }catch (Exception e){
            responseData.setCode("201");
            responseData.setMessage("操作失败！");
        }
        return responseData;
    }

}
