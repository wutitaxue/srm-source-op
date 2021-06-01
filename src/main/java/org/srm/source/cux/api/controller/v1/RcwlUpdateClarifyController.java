package org.srm.source.cux.api.controller.v1;

import com.alibaba.fastjson.JSONObject;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.*;
import org.srm.source.cux.domain.repository.RcwlBPMRfxHeaderRepository;
import org.srm.source.share.app.service.ClarifyService;
import org.srm.source.share.domain.entity.Clarify;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
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
    @Autowired
    private RcwlBPMRfxHeaderRepository rcwlRfxHeaderRepository;

    @ApiOperation("更新数据字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-clarify"})
    public ResponseData updateClarifyData(@RequestBody RcwlUpdateVO rcwlUpdateVO) {
        String xx = JSONObject.toJSONString(rcwlUpdateVO);
        log.info("澄清(修改)传进来的值：rcwlUpdateVO====>"+rcwlUpdateVO);
        ResponseData responseData = new ResponseData();
        RcwlUpdateDataVO rcwlUpdateDataVO = rcwlUpdateVO.getRcwlUpdateDataVO() ;
        if(null == rcwlUpdateVO || null == rcwlUpdateDataVO){
            responseData.setCode("201");
            responseData.setMessage("数据接收为null,获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataVO.getClarifyNum() == null || "".equals(rcwlUpdateDataVO.getClarifyNum()))&&
                (rcwlUpdateDataVO.getTenantid() == null || "".equals(rcwlUpdateDataVO.getTenantid()))){
            responseData.setCode("201");
            responseData.setMessage("单据编号或人员ID获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataVO.getProcessInstanceId() == null || "".equals(rcwlUpdateDataVO.getProcessInstanceId()))&&
                (rcwlUpdateDataVO.getClarifyStatus() == null || "".equals(rcwlUpdateDataVO.getClarifyStatus()))&&
                (rcwlUpdateDataVO.getWebserviceUrl() == null || "".equals(rcwlUpdateDataVO.getWebserviceUrl()))){
            responseData.setCode("201");
            responseData.setMessage("所需更新数据至少有一个值！");
            return responseData;
        }
        //判断当前TenantId是否有圈先更改数据
        List<String> l = rcwlClarifyService.getTenantIdByclarifyNum(rcwlUpdateDataVO.getClarifyNum());
        if(l.contains(rcwlUpdateDataVO.getTenantid())){
            responseData = rcwlClarifyService.updateClarifyData(rcwlUpdateDataVO);
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
    public ResponseData releaseClarify(@RequestBody RcwlCarifyReleaseVO rcwlCarifyReleaseDTO) {
        String xx = JSONObject.toJSONString(rcwlCarifyReleaseDTO);
        log.info("澄清(发布)传进来的值：rcwlCarifyReleaseDTO====>"+rcwlCarifyReleaseDTO);
        ResponseData responseData = new ResponseData();
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        if(0l == rcwlCarifyReleaseDTO.getTenantid() || null == rcwlCarifyReleaseDTO.getClarifyNum() || "".equals(rcwlCarifyReleaseDTO.getClarifyNum())){
            responseData.setCode("201");
            responseData.setMessage("参数获取失败!");
        }
        DetailsHelper.setCustomUserDetails(rcwlCarifyReleaseDTO.getTenantid(),"zh_CN");
        Long clarifyId = rcwlClarifyService.getClarifyIdByClarifyNum(rcwlCarifyReleaseDTO.getClarifyNum());
        //基础数据
        Clarify clarify = clarifyService.queryClarifyDetail(rcwlCarifyReleaseDTO.getTenantid(),clarifyId);
        //问题行表数据
        List<Long> l = rcwlRfxHeaderRepository.getIssueLineIdListByClarifyId(clarify.getClarifyId());
        clarify.setIssueLineIdList(l);
        clarify.setSubmittedByUserName(rcwlRfxHeaderRepository.getRealNameById(clarify.getSubmittedBy()));
        ClarifyToReleaseDTO clarifyToReleaseDTO = rcwlRfxHeaderRepository.getClarifyToReleaseDTO(clarify.getClarifyId());
        clarify.setCompanyName(clarifyToReleaseDTO.getCompanyName());
        clarify.setSourceNum(clarifyToReleaseDTO.getSourceNum());
        this.validObject(clarify, new Class[0]);
        try{
            clarifyService.releaseClarify(rcwlCarifyReleaseDTO.getTenantid(), clarify);
        }catch(Exception e){
            responseData.setCode("201");
            responseData.setMessage("澄清答疑发布失败！");
        }
        return responseData;
    }
}
