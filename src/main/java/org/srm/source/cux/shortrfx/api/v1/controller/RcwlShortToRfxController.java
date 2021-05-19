package org.srm.source.cux.shortrfx.api.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.rfx.api.controller.config.TransactionSwaggerApiConfig;
import org.srm.source.cux.rfx.api.controller.dto.RcwlRfxSupplierQueryDTO;
import org.srm.source.cux.shortrfx.app.service.RcwlShortListToRfxService;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.share.api.dto.PrLineDTO;
import org.srm.source.share.api.dto.PreSourceHeaderDTO;
import org.srm.source.share.app.service.PrLineService;
import org.srm.source.share.domain.vo.PrLineVO;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description 入围单转询价
 * @date 2021/5/12 14:34
 * @version:1.0
 */
@Api("入围单转询价")
@RestController("RcwlShortToRfxController.v1")
@RequestMapping({"/v1/{organizationId}/rfx/shortlist"})
public class RcwlShortToRfxController {
    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @Autowired
    private PrLineService prLineService;


    @Autowired
    private RcwlShortListToRfxService rcwlShortListToRfxService;


    @ApiOperation("入围单转询报价")
    @GetMapping("/to-rfx")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    public ResponseEntity<PreSourceHeaderDTO> rcwlShortToRfx(@PathVariable("organizationId") Long organizationId, @RequestParam("shortlistHeaderId") Long shortlistHeaderId) {
//        return Results.success(this.rfxHeaderService.createRfxHeaderFromPurchase(organizationId, preFullSourceHeaderDTO));
        return null;
    }

    @ApiOperation("入围单转询报价查询")
    @GetMapping("/getlist")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    public ResponseEntity<Page<RcwlShortlistHeader>> rcwlShortToRfxGetListController(RcwlShortlistQueryDTO rcwlShortlistQueryDTO, @ApiIgnore @SortDefault(value = RcwlShortlistHeader.FIELD_SHORTLIST_HEADER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        //审批通过到状态
        rcwlShortlistQueryDTO.setState("approved");
        Page<RcwlShortlistHeader> list = rcwlShortlistHeaderRepository.pageAndSortRcwlShortlistHeader(pageRequest, rcwlShortlistQueryDTO);
        return Results.success(list);

    }


    @ApiOperation("入围单采购申请查询")
    @GetMapping("/get-short-pr")
    @ProcessLovValue(
            targetField = {"body"}
    )
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    public ResponseEntity<PreSourceHeaderDTO> RcwllistPreRfxHeaders(@PathVariable("organizationId") Long tenantId, @RequestParam Long ShortListId, @RequestParam Long templateId) throws JsonProcessingException {
        return Results.success(this.rcwlShortListToRfxService.rcwlShortListToRfx(tenantId, ShortListId, templateId));
    }


}
