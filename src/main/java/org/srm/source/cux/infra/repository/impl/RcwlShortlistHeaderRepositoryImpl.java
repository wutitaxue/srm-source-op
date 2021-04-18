package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.mapper.RcwlShortlistHeaderMapper;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.domain.vo.PrLineVO;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 入围单头表 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
public class RcwlShortlistHeaderRepositoryImpl extends BaseRepositoryImpl<RcwlShortlistHeader> implements RcwlShortlistHeaderRepository {


    @Autowired
    private RcwlShortlistHeaderMapper rcwlShortlistHeaderMapper;

    @Override
    public void updatePrLineByShortlistHeaderId(Long shortlistHeaderId, List<Long> prLineIds) {
        for (Long prLineId : prLineIds) {
            rcwlShortlistHeaderMapper.updatePrLineByShortlistHeaderId(shortlistHeaderId, prLineId);

            //TODO 修改采购订单数量

        }
    }

    @Override
    public PrLineVO selectPrLineByIdDontShortHeaderId(Long prLineId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.selectPrLineByIdDontShortHeaderId(prLineId, shortlistHeaderId);
    }

    @Override
    public RcwlShortlistHeader selectShortlistHeaderById(Long organizationId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.selectShortlistHeaderById(organizationId, shortlistHeaderId);
    }

    @Override
    public Page<PrLineVO> selectPrLineByShortlistHeaderId(PageRequest pageRequest, Long organizationId, Long shortlistHeaderId) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectPrLineByShortlistHeaderId(organizationId, shortlistHeaderId));
    }

    @Override
    public Page<RcwlSupplierHeader> selectSupplierByShortlistHeaderId(PageRequest pageRequest, Long organizationId, Long shortlistHeaderId) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectSupplierByShortlistHeaderId(organizationId, shortlistHeaderId));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteRcwlShortlistHeaderByIds(List<RcwlShortlistHeader> rcwlShortlistHeaders) {
        for (RcwlShortlistHeader rcwlShortlistHeader : rcwlShortlistHeaders) {
            //1。恢复prLine的值
            rcwlShortlistHeaderMapper.updatePrLineByShortlistHeader(rcwlShortlistHeader);
            //2。删除入围单头信息
            rcwlShortlistHeaderMapper.deleteByPrimaryKey(rcwlShortlistHeader);

            //4。删除入围单附件信息

            //查询附件信息

            //3。删除附件

            //5。删除供应商信息

            //查询附件信息

            //6。删除附件

            //7。删除供应商附件信息



        }
    }

    @Override
    public User selectUserInfoById(Long userId) {
        return rcwlShortlistHeaderMapper.selectUserInfoById(userId);
    }

    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectRcwlShortlistHeader(rcwlShortlistQueryDTO));
    }
}
