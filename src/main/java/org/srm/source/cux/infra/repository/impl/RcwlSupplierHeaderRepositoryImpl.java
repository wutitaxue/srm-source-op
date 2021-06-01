package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import javassist.Loader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierAttachmentRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierHeaderRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.mapper.RcwlSupplierHeaderMapper;
import org.srm.source.rfx.api.dto.CompanyDTO;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.srm.source.cux.infra.constant.RcwlShortlistContants.LovCode.*;

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

    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    private static final Logger logger = LoggerFactory.getLogger(Loader.class);


    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlSupplierHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        rcwlShortlistQueryDTO.setSuppilerId(DetailsHelper.getUserDetails().getOrganizationId());
        Page<RcwlShortlistHeader> page = PageHelper.doPageAndSort(pageRequest, () -> rcwlSupplierHeaderMapper.selectRcwlSupplierHeader(rcwlShortlistQueryDTO));
        for (RcwlShortlistHeader rcwlShortlistHeader : page) {
            this.updateStatus(rcwlShortlistHeader);
        }
        return page;
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

        Long CheckSupplierHeaderId = this.checkRcwlSupplierHeader(rcwlSupplierHeader);

        Long supplierHeaderId = CheckSupplierHeaderId;
        if (supplierHeaderId == null) {
            rcwlSupplierHeader.setStatus(RCWL_RWENROLL_STUTAS_NOTSUBMITTED);
            //入围方式"邀请" 默认已报名
            RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectByPrimaryKey(rcwlSupplierHeader.getShortlistHeaderId());
            if (SHORTLIST_CATEGEORY_INVITATION.equals(rcwlShortlistHeader.getShortlistCategory())) {
                rcwlSupplierHeader.setStatus(RCWL_RWENROLL_STUTAS_PARTICIPATED);
            }
            this.self().insertSelective(rcwlSupplierHeader);
        } else {
            if (ObjectUtils.allNotNull(rcwlSupplierHeader.getObjectVersionNumber())) {

            }
            this.self().updateByPrimaryKeySelective(rcwlSupplierHeader);
        }
        return rcwlSupplierHeaderMapper.selectRcwlSupplierHeaderById(rcwlSupplierHeader.getShortlistHeaderId());
    }

    @Override
    public Long checkRcwlSupplierHeader(RcwlSupplierHeader rcwlSupplierHeader) {
        //校验重复 同一入围单下不可重复 新增时校验
        if (rcwlSupplierHeader.getSupplierHeaderId() == null) {
            RcwlSupplierHeader rcwlSupplierHeaderSelect = new RcwlSupplierHeader();
            rcwlSupplierHeaderSelect.setSupplierId(rcwlSupplierHeader.getSupplierId());
            rcwlSupplierHeaderSelect.setShortlistHeaderId(rcwlSupplierHeader.getShortlistHeaderId());
            int i = rcwlSupplierHeaderMapper.selectCount(rcwlSupplierHeaderSelect);
            //...无语代码，不知如何处理，傻狗代码
            if (i > 0) {
                throw new CommonException("供应商：" + rcwlSupplierHeader.getSupplierNum() + "已存在：");
            } else {
                return rcwlSupplierHeaderMapper.selectOne(rcwlSupplierHeaderSelect).getSupplierHeaderId();
            }
        }
        return null;
    }

    @Override
    public void updateStatus(RcwlShortlistHeader rcwlShortlistHeader) {
        Date startDate = rcwlShortlistHeader.getStartDate();
        Date finishDate = rcwlShortlistHeader.getFinishDate();
        if (rcwlShortlistHeader.getShortlistHeaderId() == null) {
            //已取消
            rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_PREREFUSED);
        } else {
            if (StringUtils.isEmpty(rcwlShortlistHeader.getSupplierStatus())) {
                if (startDate.compareTo(new Date()) > 0) {
                    //未开始
                    rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_UNSTART);
                } else if ((startDate.compareTo(new Date()) <= 0) && (finishDate.compareTo(new Date()) > 0)) {
                    //未报名
                    rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_UNPARTICIPATED);
                } else {
                    //已放弃
                    rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_ABANDONED);
                }
            } else {
                if (RCWL_RWENROLL_STUTAS_NOTSUBMITTED.equals(rcwlShortlistHeader.getSupplierStatus()) && (finishDate.compareTo(new Date()) < 0)) {
                    //已放弃
                    rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_ABANDONED);
                } else if (RW_STUTAS_APPROVED.equals(rcwlShortlistHeader.getState())) {
                    if (rcwlShortlistHeader.getSelected() == 1) {
                        //已入围
                        rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_SHORTLISTED);
                    } else {
                        //未入围
                        rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_PREREFUSED);
                    }
                } else if (RW_STUTAS_REJECTED.equals(rcwlShortlistHeader.getState())) {
                    //已取消
                    rcwlShortlistHeader.setSupplierStatus(RCWL_RWENROLL_STUTAS_PREREFUSED);
                }
            }
        }


    }

    @Override
    public RcwlSupplierHeader submit(RcwlSupplierHeader rcwlSupplierHeader) {
        rcwlSupplierHeader.setStatus(RCWL_RWENROLL_STUTAS_SUBMITTED);
        this.self().updateOptional(rcwlSupplierHeader, "status");
        return rcwlSupplierHeader;
    }
}
