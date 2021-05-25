package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srm.common.annotation.FilterSupplier;
import org.srm.source.cux.app.service.RcwlRoundHeaderService;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.share.api.dto.BatchValidateDTO;
import org.srm.source.share.domain.entity.ProjectLineSection;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.web.annotation.Tenant;

import java.util.Date;
import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/17 14:14
 * @version:1.0
 */
@Slf4j
@RestController("RcwlRoundHeaderController.v1")
@RequestMapping({"/v1/{organizationId}/round-headers"})
@Tenant("SRM-RCWL")
public class RcwlRoundHeaderController extends BaseController {
    @Autowired
    private RcwlRoundHeaderService rcwlRoundHeaderService;
    @ApiOperation("发起新一轮报价-批量")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/section/start-quotation"})
    @FilterSupplier
    public ResponseEntity<RoundHeader> rcwlStartQuotation(@PathVariable Long organizationId, @PathVariable @Encrypt Long sourceHeaderId, @RequestParam Date roundQuotationEndDate, @RequestParam String startingReason, @RequestParam List<RfxQuotationHeader> eliminateSupplier) {
        log.info("eliminateSupplier=========================>"+eliminateSupplier.toString());
        log.info("eliminateSupplier=========================>"+eliminateSupplier);
        this.rcwlRoundHeaderService.startQuotation(organizationId, sourceHeaderId, roundQuotationEndDate, startingReason,eliminateSupplier);
        return Results.success();
    }
}
