package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.source.cux.app.service.RcwlSupplierAttachmentService;
import org.srm.source.cux.config.ShortlistSourceSwaggerApiConfig;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 入围供应商单附件 管理 API
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Api(ShortlistSourceSwaggerApiConfig.RCWL_SUPPLIER_ATTACHMENTS)
@RestController("rcwlSupplierAttachmentController.v1")
@RequestMapping("/v1/{organizationId}/rcwl-supplier-attachments")
public class RcwlSupplierAttachmentController extends BaseController {

    @Autowired
    private RcwlSupplierAttachmentRepository rcwlSupplierAttachmentRepository;

    @Autowired
    private RcwlSupplierAttachmentService rcwlSupplierAttachmentService;

    @ApiOperation(value = "入围供应商单附件列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RcwlSupplierAttachment>> list(RcwlSupplierAttachment rcwlSupplierAttachment, @RequestParam("shortlistId") Long shortlistId, @ApiIgnore @SortDefault(value = RcwlSupplierAttachment.FIELD_RCWL_SUPPLIER_ATTACHMENT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        rcwlSupplierAttachment.setShortListId(shortlistId);
        Page<RcwlSupplierAttachment> list = rcwlSupplierAttachmentRepository.pageAndSortByRcwlSupplierAttachment(pageRequest, rcwlSupplierAttachment);
        //若采购方未上传附件，添加默认数据，使供应商可上传附加
        if(CollectionUtils.isEmpty(list.getContent())){
            List<RcwlSupplierAttachment> attachmentList = new ArrayList<>();
            attachmentList.add(new RcwlSupplierAttachment());
            list.setContent(attachmentList);
        }
        return Results.success(list);
    }

    @ApiOperation(value = "创建入围供应商单附件")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<RcwlSupplierAttachment>> create(@RequestBody List<RcwlSupplierAttachment> rcwlSupplierAttachments) {
        rcwlSupplierAttachments.forEach(rcwlSupplierAttachment -> {
            rcwlSupplierAttachment.setUploadUserId(DetailsHelper.getUserDetails().getUserId());
        });
        rcwlSupplierAttachments.forEach(this::validObject);
        List<RcwlSupplierAttachment> rcwlSupplierAttachmentList = rcwlSupplierAttachmentService.createAndUpdate(rcwlSupplierAttachments);
        return Results.success(rcwlSupplierAttachmentList);
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
