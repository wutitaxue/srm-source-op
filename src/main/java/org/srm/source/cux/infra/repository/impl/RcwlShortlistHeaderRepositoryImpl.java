package org.srm.source.cux.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.api.controller.v1.dto.RcwlShortlistQueryDTO;
import org.srm.source.cux.api.controller.v1.dto.StaticTextDTO;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.vo.CompanyContactVO;
import org.srm.source.cux.domain.vo.SupplierVO;
import org.srm.source.cux.infra.mapper.RcwlShortlistHeaderMapper;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.domain.vo.PrLineVO;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.srm.source.cux.infra.constant.RcwlShortlistContants.LovCode.*;

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

            String str1 = "";
            String str2 = "";
            String str3 = "";
            String str4 = "";
            if(ObjectUtils.allNotNull(rcwlSupplierHeader.getCapital())){
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

        }
        return page;
    }


    @Override
    public User selectUserInfoById(Long userId) {
        return rcwlShortlistHeaderMapper.selectUserInfoById(userId);
    }

    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectRcwlShortlistHeader(rcwlShortlistQueryDTO));
    }

    @Override
    public void updatePrLineByShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        rcwlShortlistHeaderMapper.updatePrLineByShortlistHeader(rcwlShortlistHeader);
    }

    @Override
    public void updatePrLineByShortlistHeaderId(Long shortlistHeaderId, List<Long> prLineIds) {
        for (Long prLineId : prLineIds) {
            rcwlShortlistHeaderMapper.updatePrLineByShortlistHeaderId(shortlistHeaderId, prLineId);
        }

    }

    @Override
    public StaticTextDTO selectStaticTextValueByCode(Long organizationId, String textCode) {
        String s = rcwlShortlistHeaderMapper.selectStaticTextValueByCode(organizationId, textCode);
        StaticTextDTO staticTextDTO = new StaticTextDTO();
        staticTextDTO.setTextCode(textCode);
        staticTextDTO.setValue(s);
        return staticTextDTO;
    }

    @Override
    public List<RcwlShortlistHeader> approve(List<RcwlShortlistHeader> rcwlShortlistHeaders, String status) {
        RcwlShortlistHeader header;
        for (RcwlShortlistHeader rcwlShortlistHeader : rcwlShortlistHeaders) {
            Long shortlistHeaderId = rcwlShortlistHeader.getShortlistHeaderId();
            if(shortlistHeaderId == null){
                throw new CommonException("shortlistHeaderId not null");
            }
            //审批中的才可以审
            if(!RW_STUTAS_APPROVING.equals(rcwlShortlistHeader.getState())){
                throw new CommonException("当前状态下不允许审批！");
            }
            header = new RcwlShortlistHeader();
            header.setShortlistHeaderId(shortlistHeaderId);
            //获取版本信息
            if(!ObjectUtils.allNotNull(rcwlShortlistHeader.getObjectVersionNumber())){
                RcwlShortlistHeader headerSelect = rcwlShortlistHeaderMapper.selectByPrimaryKey(rcwlShortlistHeader);
                header.setObjectVersionNumber(headerSelect.getObjectVersionNumber());
            }
            //修改状态
            header.setState(status);
            rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(header);
        }
        return rcwlShortlistHeaders;
    }

    @Override
    public RcwlShortlistHeader published(RcwlShortlistHeader rcwlShortlistHeader) {
        //公开征集 才可以发布
        String shortlistCategory = rcwlShortlistHeader.getShortlistCategory();
        if(!SHORTLIST_CATEGEORY_SOLICITATION.equals(shortlistCategory)){
            throw new CommonException("入围方式为公开征集才可发布！");
        }
        //修改状态
        rcwlShortlistHeader.setState(RW_STUTAS_PUBLISHED);
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);
        return rcwlShortlistHeader;
    }

    @Override
    public RcwlShortlistHeader submit(RcwlShortlistHeader rcwlShortlistHeader) {
        //公开征集时，报名时间截止后才可提交
        String shortlistCategory = rcwlShortlistHeader.getShortlistCategory();
        if(SHORTLIST_CATEGEORY_SOLICITATION.equals(shortlistCategory)){
            Date finishDate = rcwlShortlistHeader.getFinishDate();
            if(finishDate.compareTo(new Date()) <= 0){
                throw new CommonException("未到报名截止时间，无法发布！");
            }
        }
        //将状态修改为审批中
        rcwlShortlistHeader.setState(RW_STUTAS_APPROVING);
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);

        //TODO 后续业务逻辑处理
        return rcwlShortlistHeader;
    }

    @Override
    public SupplierVO currentSupplierInfo() {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        //获取用户公司基本信息
        SupplierVO supplierVO = rcwlShortlistHeaderMapper.currentSupplierInfo(userDetails.getUserId());
        //获取联系人及电话
        if(ObjectUtils.allNotNull(supplierVO)){
            Long companyId = supplierVO.getCompanyId();
            List<CompanyContactVO> companyContactVOS = rcwlShortlistHeaderMapper.selectContactsByCompanyId(companyId);
            if(CollectionUtils.isNotEmpty(companyContactVOS)){
                supplierVO.setContact(companyContactVOS.get(0).getContact());
                supplierVO.setPhone(companyContactVOS.get(0).getPhone());
            }
        }
        return supplierVO;
    }
}
