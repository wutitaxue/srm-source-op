package org.srm.source.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.source.domain.entity.PrLine;
import org.srm.source.domain.repository.RCWLPrLineRepository;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 采购申请行 管理 API
 *
 * @author bin.zhang06@hand-china.com 2021-03-16 15:49:15
 */
@RestController("RcwlPrLineController.v1")
@RequestMapping("/v1/{organizationId}/pr-lines")
public class RCWLPrLineController extends BaseController {

    @Autowired
    private RCWLPrLineRepository RCWLPrLineRepository;

    @ApiOperation(value = "采购申请行列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<PrLine>> list(PrLine prLine, @ApiIgnore @SortDefault(value = PrLine.FIELD_PR_LINE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<PrLine> list = RCWLPrLineRepository.pageAndSort(pageRequest, prLine);
        return Results.success(list);
    }

    @ApiOperation(value = "采购申请行明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{prLineId}")
    public ResponseEntity<PrLine> detail(@PathVariable Long prLineId) {
        PrLine prLine = RCWLPrLineRepository.selectByPrimaryKey(prLineId);
        return Results.success(prLine);
    }

    @ApiOperation(value = "创建采购申请行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<PrLine> create(@RequestBody PrLine prLine) {
        validObject(prLine);
        RCWLPrLineRepository.insertSelective(prLine);
        return Results.success(prLine);
    }

    @ApiOperation(value = "修改采购申请行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<PrLine> update(@RequestBody PrLine prLine) {
        SecurityTokenHelper.validToken(prLine);
        RCWLPrLineRepository.updateByPrimaryKeySelective(prLine);
        return Results.success(prLine);
    }

    @ApiOperation(value = "删除采购申请行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody PrLine prLine) {
        SecurityTokenHelper.validToken(prLine);
        RCWLPrLineRepository.deleteByPrimaryKey(prLine);
        return Results.success();
    }

}
