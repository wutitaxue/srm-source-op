package org.srm.source.cux.api.controller.v1;

import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.source.cux.app.service.RcwlRfxHeaderService;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;

import javax.annotation.Resource;

@Api(
        tags = {"Rcwl Update Rfx Header"}
)
@RestController("rcwlUpdateRfxHeaderController.v1")
@RequestMapping({"/v1/rcwlUpdateRfxHeaderController"})
public class RcwlUpdateRfxHeaderController {

    @Resource
    private RcwlRfxHeaderService rcwlRfxHeaderService;

    @ApiOperation("更新询价单头表数据字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-rfxHeader"})
    public ResponseData updateRfxHeaderData(@RequestBody RcwlUpdateDTO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        RcwlUpdateDataDTO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateDataDTO();
        if(rcwlUpdateDataDTO.getRfxHeaderId() == null || "".equals(rcwlUpdateDataDTO.getRfxHeaderId())){
            responseData.setCode("201");
            responseData.setMessage("询价单编号获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getBoId() == null || "".equals(rcwlUpdateDataDTO.getBoId()))&&
                (rcwlUpdateDataDTO.getBoIdUrl() == null || "".equals(rcwlUpdateDataDTO.getBoIdUrl()))){
            responseData.setCode("201");
            responseData.setMessage("所需更新数据至少有一个值！");
            return responseData;
        }
        responseData = rcwlRfxHeaderService.updateRfxHeaderData(rcwlUpdateDataDTO);
        return responseData;
    }

}
