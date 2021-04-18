package org.srm.source.cux.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.source.cux.app.service.RcwlShortlistAttachmentService;
import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;
import org.srm.source.cux.domain.repository.RcwlShortlistAttachmentRepository;
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

import java.util.List;

/**
 * 入围单附件模版 管理 API
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@RestController("rcwlShortlistAttachmentController.v1")
@RequestMapping("/v1/{organizationId}/rcwl-shortlist-attachments")
public class RcwlShortlistAttachmentController extends BaseController {

    @Autowired
    private RcwlShortlistAttachmentRepository rcwlShortlistAttachmentRepository;

    @Autowired
    private RcwlShortlistAttachmentService rcwlShortlistAttachmentService;

    @ApiOperation(value = "入围单附件模版列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<RcwlShortlistAttachment>> list(RcwlShortlistAttachment rcwlShortlistAttachment, @ApiIgnore @SortDefault(value = RcwlShortlistAttachment.FIELD_RCWL_SHORTLIST_ATTACHMENT_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<RcwlShortlistAttachment> list = rcwlShortlistAttachmentRepository.pageAndSortShortlistAttachment(pageRequest, rcwlShortlistAttachment);
        return Results.success(list);
    }

    @ApiOperation(value = "创建入围单附件模版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<RcwlShortlistAttachment>> create(@RequestBody List<RcwlShortlistAttachment> rcwlShortlistAttachments) {
        rcwlShortlistAttachments.forEach(this::validObject);
        List<RcwlShortlistAttachment> rcwlShortlistAttachmentList = rcwlShortlistAttachmentService.createShortlistAttachment(rcwlShortlistAttachments);
        return Results.success(rcwlShortlistAttachmentList);
    }

    @ApiOperation(value = "删除入围单附件模版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<RcwlShortlistAttachment> rcwlShortlistAttachments) {
        //SecurityTokenHelper.validToken(rcwlShortlistAttachment);
        rcwlShortlistAttachmentService.deleteRcwlShortlistAttachment(rcwlShortlistAttachments);
        return Results.success();
    }

}
