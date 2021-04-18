package org.srm.source.cux.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import org.srm.source.cux.app.service.RcwlShortlistHeaderService;
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
import org.srm.source.share.domain.vo.PrLineVO;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 入围单头表 管理 API
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
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

    @ApiOperation(value = "入围单采购订单行明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/pr-line-list/{shortlistHeaderId}")
    public ResponseEntity<Page<PrLineVO>> prLineDetail(PageRequest pageRequest, @PathVariable Long shortlistHeaderId, @PathVariable Long organizationId) {
        Page<PrLineVO> prLineVOS = rcwlShortlistHeaderRepository.selectPrLineByShortlistHeaderId(pageRequest, organizationId, shortlistHeaderId);
        return Results.success(prLineVOS);
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
        validObject(rcwlShortlistHeader);
        rcwlShortlistHeaderService.createShortlistHeader(rcwlShortlistHeader);
        return Results.success(rcwlShortlistHeader);
    }

    @ApiOperation(value = "修改入围单头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<RcwlShortlistHeader> update(@RequestBody RcwlShortlistHeader rcwlShortlistHeader) {
        SecurityTokenHelper.validToken(rcwlShortlistHeader);
        rcwlShortlistHeaderRepository.updateByPrimaryKeySelective(rcwlShortlistHeader);
        return Results.success(rcwlShortlistHeader);
    }

    @ApiOperation(value = "删除入围单")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody List<RcwlShortlistHeader> rcwlShortlistHeaders) {
        rcwlShortlistHeaderRepository.deleteRcwlShortlistHeaderByIds(rcwlShortlistHeaders);
        return Results.success();
    }

    //TODO 供应商LOV

    //TODO 获取静态文本 信息


}
