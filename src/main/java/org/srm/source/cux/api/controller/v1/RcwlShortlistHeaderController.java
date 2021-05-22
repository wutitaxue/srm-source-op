package org.srm.source.cux.api.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.source.cux.api.controller.v1.dto.StaticTextDTO;
import org.srm.source.cux.app.service.RcwlShortlistHeaderService;
import org.srm.source.cux.config.ShortlistSourceSwaggerApiConfig;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.srm.source.cux.domain.vo.SupplierVO;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

/**
 * 入围单头表 管理 API
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Api(ShortlistSourceSwaggerApiConfig.RCWL_SHORTLIST_HEADERS)
@RestController("rcwlShortlistHeaderController.v1")
@RequestMapping("/v1/{organizationId}/rcwl-shortlist-headers")
public class RcwlShortlistHeaderController extends BaseController {

    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @Autowired
    private RcwlShortlistHeaderService rcwlShortlistHeaderService;

    @ApiOperation(value = "入围单头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RcwlShortlistHeader>> list(RcwlShortlistQueryDTO rcwlShortlistQueryDTO, @ApiIgnore @SortDefault(value = RcwlShortlistHeader.FIELD_SHORTLIST_HEADER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RcwlShortlistHeader> list = rcwlShortlistHeaderRepository.pageAndSortRcwlShortlistHeader(pageRequest, rcwlShortlistQueryDTO);
        return Results.success(list);
    }

    @ApiOperation(value = "入围单头表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{shortlistHeaderId}")
    public ResponseEntity<RcwlShortlistHeader> detail(@PathVariable Long shortlistHeaderId, @PathVariable Long organizationId) {
        RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectShortlistHeaderById(organizationId, shortlistHeaderId);
        return Results.success(rcwlShortlistHeader);
    }

    @ApiOperation(value = "入围单供应商明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/supplier-list/{shortlistHeaderId}")
    public ResponseEntity<Page<RcwlSupplierHeader>> detailPrLine(PageRequest pageRequest, @PathVariable Long shortlistHeaderId, @PathVariable Long organizationId) {
        Page<RcwlSupplierHeader> rcwlSupplierHeaderList = rcwlShortlistHeaderRepository.selectSupplierByShortlistHeaderId(pageRequest, organizationId, shortlistHeaderId);
        return Results.success(rcwlSupplierHeaderList);
    }


    @ApiOperation(value = "创建入围单头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RcwlShortlistHeader> create(@RequestBody RcwlShortlistHeader rcwlShortlistHeader) {
        //validObject(rcwlShortlistHeader);
        rcwlShortlistHeaderService.createShortlistHeader(rcwlShortlistHeader);
        return Results.success(rcwlShortlistHeader);
    }

    @ApiOperation(value = "批量删除入围单")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<RcwlShortlistHeader> rcwlShortlistHeaders) {
        rcwlShortlistHeaderService.deleteRcwlShortlistHeaderByIds(rcwlShortlistHeaders);
        return Results.success();
    }

    @ApiOperation(value = "获取静态文本信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/static-text")
    public ResponseEntity<StaticTextDTO> staticText(@PathVariable Long organizationId, String textCode) {
        StaticTextDTO value = rcwlShortlistHeaderRepository.selectStaticTextValueByCode(organizationId, textCode);
        return Results.success(value);
    }

    @ApiOperation(value = "审批")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/approve/{status}")
    public ResponseEntity<List<RcwlShortlistHeader>> approve(@PathVariable String status, @RequestBody List<RcwlShortlistHeader> rcwlShortlistHeaders) {
        List<RcwlShortlistHeader> rcwlShortlistHeader1 =  rcwlShortlistHeaderRepository.approve(rcwlShortlistHeaders, status);
        return Results.success(rcwlShortlistHeader1);
    }

    @ApiOperation(value = "提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/submit")
    public ResponseEntity<RcwlShortlistHeader> submit(@RequestBody RcwlShortlistHeader rcwlShortlistHeader) throws IOException {
        RcwlShortlistHeader rcwlShortlistHeader1 =  rcwlShortlistHeaderRepository.submit(rcwlShortlistHeader);
        return Results.success(rcwlShortlistHeader1);
    }

    @ApiOperation(value = "发布")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/published")
    public ResponseEntity<RcwlShortlistHeader> published(@RequestBody RcwlShortlistHeader rcwlShortlistHeader) {
        RcwlShortlistHeader rcwlShortlistHeader1 =  rcwlShortlistHeaderRepository.published(rcwlShortlistHeader);
        return Results.success(rcwlShortlistHeader1);
    }

    @ApiOperation(value = "通过用户获取供应商信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/current-supplier-info")
    public ResponseEntity<SupplierVO> currentSupplierInfo(@PathVariable Long organizationId) {
        SupplierVO value = rcwlShortlistHeaderRepository.currentSupplierInfo();
        return Results.success(value);
    }

    //--------------------BPM操作----------------


    @ApiOperation(value = "入围单BPM提交成功")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/submit-to-bpm-successed")
    public ResponseEntity<Void> ShortListsubmitToBpmSuccessed( @PathVariable("organizationId") Long tenantId, @RequestParam("ShorListNum") String ShorListNum ) {
        rcwlShortlistHeaderService.rcwlSubmitBpmSuccessed(tenantId, ShorListNum);
        return Results.success();
    }

    @ApiOperation(value = "入围单BPM审批通过")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/submit-to-bpm-approved")
    public ResponseEntity<Void> ShortListbpmApproved( @PathVariable("organizationId") Long tenantId, @RequestParam("ShorListNum") String ShorListNum ) {
        rcwlShortlistHeaderService.rcwlSubmitBpmApproved(tenantId, ShorListNum);
        return Results.success();
    }

    @ApiOperation(value = "入围单BPM审批拒绝")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/submit-to-bpm-rejected")
    public ResponseEntity<Void> ShortListbpmReject( @PathVariable("organizationId") Long tenantId, @RequestParam("ShorListNum") String ShorListNum ) {
        rcwlShortlistHeaderService.rcwlSubmitBpmReject(tenantId, ShorListNum);
        return Results.success();
    }
    @ApiOperation(value = "入围单BPM审批修改id接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/submit-to-bpm-rejected3")
    public ResponseEntity<Void> ShortListbpmUpdate( @PathVariable("organizationId") Long tenantId,@RequestParam("ShorListNum") String ShorListNum ,@RequestParam("attributeVarchar8") String attributeVarchar8,@RequestParam("attributeVarchar9") String attributeVarchar9) {
        rcwlShortlistHeaderService.RcwlBpmUpateInstance(tenantId, ShorListNum,attributeVarchar8,attributeVarchar9);
        return Results.success();
    }


}
