package org.srm.source.cux.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;
import org.srm.source.cux.domain.repository.RcwlSupplierAttachmentRepository;
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
import springfox.documentation.annotations.ApiIgnore;

/**
 * 入围供应商单附件 管理 API
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@RestController("rcwlSupplierAttachmentController.v1")
@RequestMapping("/v1/{organizationId}/rcwl-supplier-attachments")
public class RcwlSupplierAttachmentController extends BaseController {

    @Autowired
    private RcwlSupplierAttachmentRepository rcwlSupplierAttachmentRepository;

    @ApiOperation(value = "入围供应商单附件列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RcwlSupplierAttachment>> list(RcwlSupplierAttachment rcwlSupplierAttachment, @ApiIgnore @SortDefault(value = RcwlSupplierAttachment.FIELD_RCWL_SUPPLIER_ATTACHMENT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RcwlSupplierAttachment> list = rcwlSupplierAttachmentRepository.pageAndSort(pageRequest, rcwlSupplierAttachment);
        return Results.success(list);
    }

    @ApiOperation(value = "入围供应商单附件明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{rcwlSupplierAttachmentId}")
    public ResponseEntity<RcwlSupplierAttachment> detail(@PathVariable Long rcwlSupplierAttachmentId) {
        RcwlSupplierAttachment rcwlSupplierAttachment = rcwlSupplierAttachmentRepository.selectByPrimaryKey(rcwlSupplierAttachmentId);
        return Results.success(rcwlSupplierAttachment);
    }

    @ApiOperation(value = "创建入围供应商单附件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<RcwlSupplierAttachment> create(@RequestBody RcwlSupplierAttachment rcwlSupplierAttachment) {
        validObject(rcwlSupplierAttachment);
        rcwlSupplierAttachmentRepository.insertSelective(rcwlSupplierAttachment);
        return Results.success(rcwlSupplierAttachment);
    }

    @ApiOperation(value = "修改入围供应商单附件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RcwlSupplierAttachment> update(@RequestBody RcwlSupplierAttachment rcwlSupplierAttachment) {
        SecurityTokenHelper.validToken(rcwlSupplierAttachment);
        rcwlSupplierAttachmentRepository.updateByPrimaryKeySelective(rcwlSupplierAttachment);
        return Results.success(rcwlSupplierAttachment);
    }

    @ApiOperation(value = "删除入围供应商单附件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody RcwlSupplierAttachment rcwlSupplierAttachment) {
        SecurityTokenHelper.validToken(rcwlSupplierAttachment);
        rcwlSupplierAttachmentRepository.deleteByPrimaryKey(rcwlSupplierAttachment);
        return Results.success();
    }

}
