package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.infra.mapper.RcwlShortlistHeaderMapper;
import org.srm.web.annotation.Tenant;

/**
 * 入围单头表 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
@Tenant("SRM-RCWL")
public class RcwlShortlistHeaderRepositoryImpl extends BaseRepositoryImpl<RcwlShortlistHeader> implements RcwlShortlistHeaderRepository {

    @Autowired
    private RcwlShortlistHeaderMapper rcwlShortlistHeaderMapper;

    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectRcwlShortlistHeader(rcwlShortlistQueryDTO));
    }
}
