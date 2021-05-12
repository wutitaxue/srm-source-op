package org.srm.source.cux.api.controller.v1;


import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.infra.constant.RcwlSourceConstants;
import org.srm.web.annotation.Tenant;
import springfox.documentation.annotations.ApiIgnore;



@Api(RcwlSourceConstants.RCWL_SHORTLIST_HEADERS)
@RestController("rcwlShortlistHeaderController.v1")
@RequestMapping("/v1/{organizationId}")
@Tenant("SRM-RCWL")
public class RcwlShortlistHeaderController {

    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @ApiOperation(value = "标段入围单头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/rcwl-shortlist-headers/source")
    public ResponseEntity<Page<RcwlShortlistHeader>> list(@PathVariable Long organizationId, RcwlShortlistQueryDTO rcwlShortlistQueryDTO, @ApiIgnore @SortDefault(value = RcwlShortlistHeader.FIELD_SHORTLIST_HEADER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        rcwlShortlistQueryDTO.setTenantId(organizationId);
        Page<RcwlShortlistHeader> list = rcwlShortlistHeaderRepository.pageAndSortRcwlShortlistHeader(pageRequest, rcwlShortlistQueryDTO);
        return Results.success(list);
    }


}
