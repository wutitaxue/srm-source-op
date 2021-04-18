package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlSupplierHeaderRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.mapper.RcwlSupplierHeaderMapper;

/**
 * 入围单供应商头信息 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
public class RcwlSupplierHeaderRepositoryImpl extends BaseRepositoryImpl<RcwlSupplierHeader> implements RcwlSupplierHeaderRepository {

    @Autowired
    private RcwlSupplierHeaderMapper rcwlSupplierHeaderMapper;


    @Override
    public Page<RcwlSupplierHeader> pageAndSortRcwlSupplierHeader(PageRequest pageRequest, RcwlSupplierHeader rcwlSupplierHeader) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlSupplierHeaderMapper.selectRcwlSupplierHeader(rcwlSupplierHeader));
    }
}
