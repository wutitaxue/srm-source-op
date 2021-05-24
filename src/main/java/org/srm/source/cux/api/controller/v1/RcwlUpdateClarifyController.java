package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.RcwlCarifyReleaseDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateVO;
import org.srm.source.cux.domain.entity.RcwlUpdateDataVO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.share.app.service.ClarifyService;
import org.srm.source.share.domain.entity.Clarify;

import javax.annotation.Resource;
import java.util.List;

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
    public ResponseData updateClarifyData(@RequestBody RcwlUpdateVO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        RcwlUpdateDataVO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateDataDTO() ;
        if(null == rcwlUpdateDTO || null == rcwlUpdateDataDTO){
            responseData.setCode("201");
            responseData.setMessage("数据接收为null,获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getClarifyNum() == null || "".equals(rcwlUpdateDataDTO.getClarifyNum()))&&
                (rcwlUpdateDataDTO.getTenantId() == null || "".equals(rcwlUpdateDataDTO.getTenantId()))){
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
        //判断当前TenantId是否有圈先更改数据
        List<String> l = rcwlClarifyService.getTenantIdByclarifyNum(rcwlUpdateDataDTO.getClarifyNum());
        if(l.contains(rcwlUpdateDataDTO.getTenantId())){
            responseData = rcwlClarifyService.updateClarifyData(rcwlUpdateDataDTO);
        }else {
            responseData.setCode("201");
            responseData.setMessage("当前角色没有修改权限！");
        }
        return responseData;
    }

    @ApiOperation("澄清答疑发布")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/release/cqdy"})
    public ResponseData releaseClarify(@RequestBody RcwlCarifyReleaseDTO rcwlCarifyReleaseDTO) {
        ResponseData responseData = new ResponseData();
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        if(0l == rcwlCarifyReleaseDTO.getTenantId() || null == rcwlCarifyReleaseDTO.getClarifyNum() || "".equals(rcwlCarifyReleaseDTO.getClarifyNum())){
            responseData.setCode("201");
            responseData.setMessage("参数获取失败!");
        }
        DetailsHelper.setCustomUserDetails(rcwlCarifyReleaseDTO.getTenantId(),"zh_CN");
        Long clarifyId = rcwlClarifyService.getClarifyIdByClarifyNum(rcwlCarifyReleaseDTO.getClarifyNum());
        Clarify clarify = clarifyService.queryClarifyDetail(rcwlCarifyReleaseDTO.getTenantId(),clarifyId);
        this.validObject(clarify, new Class[0]);
        try{
            clarifyService.releaseClarify(rcwlCarifyReleaseDTO.getTenantId(), clarify);
        }catch(Exception e){
            responseData.setCode("201");
            responseData.setMessage("澄清答疑发布失败！");
        }
        return responseData;
    }
}
