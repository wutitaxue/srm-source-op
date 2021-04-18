package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlSupplierAttachmentRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierHeaderRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.mapper.RcwlSupplierHeaderMapper;
import org.srm.source.rfx.api.dto.CompanyDTO;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 入围单供应商头信息 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
public class RcwlSupplierHeaderRepositoryImpl extends BaseRepositoryImpl<RcwlSupplierHeader> implements RcwlSupplierHeaderRepository, AopProxy<RcwlSupplierHeaderRepositoryImpl> {

    @Autowired
    private RcwlSupplierHeaderMapper rcwlSupplierHeaderMapper;

    @Autowired
    private RcwlSupplierAttachmentRepository rcwlSupplierAttachmentRepository;


    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlSupplierHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlSupplierHeaderMapper.selectRcwlSupplierHeader(rcwlShortlistQueryDTO));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void batchDeleteBySupplierHeader(List<RcwlSupplierHeader> rcwlSupplierHeaders) {
        for (RcwlSupplierHeader rcwlSupplierHeader : rcwlSupplierHeaders) {
            this.self().deleteByPrimaryKey(rcwlSupplierHeader);
            //删除其附件
            RcwlSupplierAttachment rcwlSupplierAttachment = new RcwlSupplierAttachment();
            rcwlSupplierAttachment.setRcwlSupplierHeaderId(rcwlSupplierHeader.getShortlistHeaderId());
            List<RcwlSupplierAttachment> selectAttachments = rcwlSupplierAttachmentRepository.select(rcwlSupplierAttachment);
            if (CollectionUtils.isNotEmpty(selectAttachments)) {
                rcwlSupplierAttachmentRepository.batchDeleteByPrimaryKey(selectAttachments);
            }
            //TODO 删除附件
        }

    }

    @Override
    public RcwlSupplierHeader detailRcwlSupplierHeader(Long supplierHeaderId) {
        return rcwlSupplierHeaderMapper.selectRcwlSupplierHeaderById(supplierHeaderId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RcwlSupplierHeader createAndUpdateSupplierHeader(RcwlSupplierHeader rcwlSupplierHeader) {
        Long shortlistHeaderId = rcwlSupplierHeader.getShortlistHeaderId();
        if (shortlistHeaderId == null) {
            this.self().insertSelective(rcwlSupplierHeader);
        } else {
            this.self().updateByPrimaryKeySelective(rcwlSupplierHeader);
        }
        return rcwlSupplierHeaderMapper.selectRcwlSupplierHeaderById(rcwlSupplierHeader.getShortlistHeaderId());
    }
}
