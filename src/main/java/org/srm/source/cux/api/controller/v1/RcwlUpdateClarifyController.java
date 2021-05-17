package org.srm.source.cux.api.controller.v1;

import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.RcwlUpdateDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataDTO;
import org.srm.source.cux.domain.entity.ResponseData;

import javax.annotation.Resource;

@Api(
        tags = {"Rcwl Update Clarify"}
)
@RestController("rcwlUpdateClarifyController.v1")
@RequestMapping({"/v1/rcwlUpdateClarifyController"})
public class RcwlUpdateClarifyController extends BaseController {
    @Resource
    private RcwlClarifyService rcwlClarifyService;

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
