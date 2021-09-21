package org.srm.source.cux.rfx.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.source.cux.rfx.infra.handle.RcwlEvaluateIndicHandler;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.share.api.dto.AutoScoreDTO;
import org.srm.source.share.domain.entity.EvaluateSummary;
import org.srm.source.share.domain.entity.ScoreIndic;
import org.srm.web.annotation.Tenant;

/**
 * @author kaibo.li
 * @date 2021-05-13 17:21
 */
@Api(
        tags = {"Test Auto Price"}
)
@RestController("testAutoPriceController.v1")
@RequestMapping({"/v1/{organizationId}/auto-price"})
@Tenant("SRM-RCWL")
public class TestAutoPrice {


    /**
     * 原始的
     */
//    @Autowired
//    private RfxHeaderRepository rfxHeaderRepository;

    /**
     * 新写的
     */
    @Autowired
    private IRcwlEvaluateScoreLineService rcwlEvaluateScoreLineService;

    @Autowired
    private RcwlEvaluateIndicHandler rcwlEvaluateIndicHandler;

    @ApiOperation("自动评分")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @GetMapping
    @ProcessLovValue(
            targetField = {"body"}
    )
     public ResponseEntity<ReturnT> test(@PathVariable Long organizationId) {
//        this.rcwlEvaluateScoreLineService.multipleRoundAutoEvaluateScore(new AutoScoreDTO(organizationId, "RFX", 281L, null));
        this.rcwlEvaluateScoreLineService.updateEvaluateSummary(null, "RFX", 233L, 51L);
        return Results.success(ReturnT.SUCCESS);
    }
   /* public ResponseEntity<ReturnT> test(@PathVariable Long organizationId) {
        ReturnT returnT = this.rcwlEvaluateIndicHandler.execute( null,null);
        return Results.success(returnT);
    }*/
}