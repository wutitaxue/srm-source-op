package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;
import org.srm.source.cux.domain.repository.RcwlShortlistAttachmentRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.mapper.RcwlShortlistAttachmentMapper;

import java.util.List;

/**
 * 入围单附件模版 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
public class RcwlShortlistAttachmentRepositoryImpl extends BaseRepositoryImpl<RcwlShortlistAttachment> implements RcwlShortlistAttachmentRepository {


    @Autowired
    private RcwlShortlistAttachmentMapper rcwlShortlistAttachmentMapper;


    @Override
    public Page<RcwlShortlistAttachment> pageAndSortShortlistAttachment(PageRequest pageRequest, RcwlShortlistAttachment rcwlShortlistAttachment) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistAttachmentMapper.selectShortlistAttachment(rcwlShortlistAttachment));
    }


}
