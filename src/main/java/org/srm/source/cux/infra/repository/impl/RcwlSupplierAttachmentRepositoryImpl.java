package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;
import org.srm.source.cux.domain.repository.RcwlSupplierAttachmentRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.mapper.RcwlSupplierAttachmentMapper;

/**
 * 入围供应商单附件 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
public class RcwlSupplierAttachmentRepositoryImpl extends BaseRepositoryImpl<RcwlSupplierAttachment> implements RcwlSupplierAttachmentRepository {

    @Autowired
    private RcwlSupplierAttachmentMapper rcwlSupplierAttachmentMapper;

    @Override
    public Page<RcwlSupplierAttachment> pageAndSortByRcwlSupplierAttachment(PageRequest pageRequest, RcwlSupplierAttachment rcwlSupplierAttachment) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlSupplierAttachmentMapper.selectByRcwlSupplierAttachment(rcwlSupplierAttachment));
    }

    @Override
    public int deleteByShortlistAttachmentId(Long rcwlShortlistAttachmentId) {
        return rcwlSupplierAttachmentMapper.deleteByShortlistAttachmentId(rcwlShortlistAttachmentId);
    }
}
