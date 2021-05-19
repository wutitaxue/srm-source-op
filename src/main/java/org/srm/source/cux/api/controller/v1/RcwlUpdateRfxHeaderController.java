package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlBPMRfxHeaderService;
import org.srm.source.cux.domain.entity.RcwlGetDataCloseDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateCloseRfxDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.rfx.api.dto.RfxDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @ApiOperation("更新询价单头表数据字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-rfxHeader"})
    public ResponseData updateRfxHeaderData(@RequestBody RcwlUpdateCloseRfxDTO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        RcwlUpdateRfxHeaderDataDTO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateDataDTO();
        if(rcwlUpdateDataDTO.getRfxNum() == null || "".equals(rcwlUpdateDataDTO.getRfxNum())){
            responseData.setCode("201");
            responseData.setMessage("询价单编号获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getAttributeVarchar6() == null || "".equals(rcwlUpdateDataDTO.getAttributeVarchar6()))&&
                (rcwlUpdateDataDTO.getAttributeVarchar7() == null || "".equals(rcwlUpdateDataDTO.getAttributeVarchar7()))){
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
        ResponseData responseData = new ResponseData();
        List<Long> rfxHeaderIds = new ArrayList<>();
        rfxHeaderIds.add(rcwlRfxHeaderService.getRfxHeaderIdByRfxNum(rcwlGetDataCloseDTO.getRfxNum()));
        rfxHeaderService.close(rcwlGetDataCloseDTO.getTenantId(), rfxHeaderIds, rcwlGetDataCloseDTO.getRemark());
        return responseData;
    }

}
