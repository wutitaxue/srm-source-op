package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
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
    public PrLineVO selectPrLineByIdDontShortHeaderId(Long prLineId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.selectPrLineByIdDontShortHeaderId(prLineId, shortlistHeaderId);
    }

    @Override
    public RcwlShortlistHeader selectShortlistHeaderById(Long organizationId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.selectShortlistHeaderById(organizationId, shortlistHeaderId);
    }


    @Override
    public Page<RcwlSupplierHeader> selectSupplierByShortlistHeaderId(PageRequest pageRequest, Long organizationId, Long shortlistHeaderId) {
        Page<RcwlSupplierHeader> page = PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectSupplierByShortlistHeaderId(organizationId, shortlistHeaderId));
        RcwlShortlistHeader rcwlShortlistHeader;
        for (RcwlSupplierHeader rcwlSupplierHeader : page) {
            rcwlShortlistHeader = this.selectByPrimaryKey(shortlistHeaderId);
            rcwlSupplierHeader.setContacts(DetailsHelper.getUserDetails().getRealName());
            User user = rcwlShortlistHeaderMapper.selectUserInfoById(DetailsHelper.getUserDetails().getUserId());
            rcwlSupplierHeader.setPhone(user.getPhone());

            String str1 = "";
            String str2 = "";
            String str3 = "";
            String str4 = "";
            if (rcwlSupplierHeader.getCapital() < rcwlShortlistHeader.getCapital()) {
                str1 = "注册资本不符合";
            }
            if (rcwlSupplierHeader.getYears() < rcwlShortlistHeader.getYears()) {
                str2 = "成立年限不符合";
            }
            if (rcwlSupplierHeader.getOneProfit() < rcwlShortlistHeader.getOneProfit()) {
                str2 = "一年营收不符合";
            }
            if (rcwlSupplierHeader.getTwoProfit() < rcwlShortlistHeader.getTwoProfit()) {
                str2 = "两年营收不符合";
            }
            if (str1 != null && str2 != null && str3 != null && str4 != null) {
                rcwlSupplierHeader.setQualificationInfo("全部符合");
                rcwlSupplierHeader.setQualification(1);
            } else {
                rcwlSupplierHeader.setQualificationInfo(str1 + str2 + str3 + str4);
                rcwlSupplierHeader.setQualification(0);
            }
        }
        return page;
    }


    @Override
    public User selectUserInfoById(Long userId) {
        return rcwlShortlistHeaderMapper.selectUserInfoById(userId);
    }

    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectRcwlShortlistHeader(rcwlShortlistQueryDTO));
    }

    @Override
    public void updatePrLineByShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        rcwlShortlistHeaderMapper.updatePrLineByShortlistHeader(rcwlShortlistHeader);
    }
}
