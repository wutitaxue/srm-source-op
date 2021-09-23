package org.srm.source.cux.infra.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import javassist.Loader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.srm.source.cux.api.controller.v1.dto.*;
import org.srm.source.cux.app.service.RcwlSupplierHeaderService;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.vo.CompanyContactVO;
import org.srm.source.cux.domain.vo.SupplierVO;
import org.srm.source.cux.infra.mapper.RcwlShortlistHeaderMapper;
import org.srm.source.share.api.dto.User;
import org.srm.source.share.api.dto.UserDefaultDTO;
import org.srm.source.share.domain.vo.PrLineVO;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.srm.source.cux.infra.constant.RcwlShortlistContants.LovCode.*;

/**
 * 入围单头表 资源库实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Component
public class RcwlShortlistHeaderRepositoryImpl extends BaseRepositoryImpl<RcwlShortlistHeader> implements RcwlShortlistHeaderRepository {

    private static final Logger logger = LoggerFactory.getLogger(Loader.class);

    @Autowired
    private RCWLGxBpmInterfaceService rcwlGxBpmInterfaceService;


    //获取配置参数
    @Autowired
    private ProfileClient profileClient;


    @Autowired
    private RcwlShortlistHeaderMapper rcwlShortlistHeaderMapper;

    @Autowired
    private RcwlSupplierHeaderService rcwlSupplierHeaderService;


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
            logger.info("-------------rcwlSupplierHeader.getCapital():" + rcwlSupplierHeader.getCapital());
            if (ObjectUtils.allNotNull(rcwlSupplierHeader.getCapital())) {
                logger.info("-------------供应商getCapital():{0},入围单getCapital()：{1}", rcwlSupplierHeader.getCapital(), rcwlShortlistHeader.getCapital());
                if (rcwlSupplierHeader.getCapital() < rcwlShortlistHeader.getCapital()) {
                    str1 = "注册资本不符合";
                }
                logger.info("-------------getYears():{0},getYears()：{1}", rcwlSupplierHeader.getYears(), rcwlShortlistHeader.getYears());
                if (rcwlSupplierHeader.getYears() < rcwlShortlistHeader.getYears()) {
                    str2 = "成立年限不符合";
                }
                logger.info("-------------getOneProfit():{0},getOneProfit()：{1}", rcwlSupplierHeader.getOneProfit(), rcwlShortlistHeader.getOneProfit());
                if (rcwlSupplierHeader.getOneProfit().compareTo(rcwlShortlistHeader.getOneProfit()) == -1){
                    str3 = "一年营收不符合";
                }
                logger.info("-------------getTwoProfit():{0},getTwoProfit()：{1}", rcwlSupplierHeader.getTwoProfit(), rcwlShortlistHeader.getTwoProfit());
                if (rcwlSupplierHeader.getTwoProfit().compareTo(rcwlShortlistHeader.getTwoProfit()) == -1){
                    str4 = "两年营收不符合";
                }
                logger.info("-------------str1:{0},str2:{1},str3:{3},str4:{4}", str1, str2, str3, str4);
                if (org.springframework.util.ObjectUtils.isEmpty(str1) && org.springframework.util.ObjectUtils.isEmpty(str2) && org.springframework.util.ObjectUtils.isEmpty(str3) && org.springframework.util.ObjectUtils.isEmpty(str4)) {
                    rcwlSupplierHeader.setQualificationInfo("全部符合");
                    rcwlSupplierHeader.setQualification(1);
                } else {
                    rcwlSupplierHeader.setQualificationInfo(str1 + str2 + str3 + str4);
                    rcwlSupplierHeader.setQualification(0);
                }
            }

        }
//        logger.info("-------------page:" + page.toString());
        return page;
    }


    @Override
    public User selectUserInfoById(Long userId) {
        return rcwlShortlistHeaderMapper.selectUserInfoById(userId);
    }

    @Override
    public Page<RcwlShortlistHeader> pageAndSortRcwlShortlistHeader(PageRequest pageRequest, RcwlShortlistQueryDTO rcwlShortlistQueryDTO) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = httpServletRequest.getHeader("X-Real-IP");
        logger.info("ip1:{}", httpServletRequest.getHeader("X-Forwarded-For"));
        logger.info("ip2:{}", httpServletRequest.getHeader("X-Real-IP"));
        logger.info("ip3:{}", httpServletRequest.getRemoteAddr());
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderMapper.selectRcwlShortlistHeader(rcwlShortlistQueryDTO));
    }

    /**
     * 转询价的供应商ß
     *
     * @param organizationId
     * @param shortlistHeaderId
     * @return
     */
    @Override
    public List<RcwlSupplierHeader> rcwlSelectToRfxSuppier(Long organizationId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.rcwlSelectToRfxSuppier(organizationId, shortlistHeaderId);
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
            if (shortlistHeaderId == null) {
                throw new CommonException("shortlistHeaderId not null");
            }
            //审批中的才可以审
            if (!RW_STUTAS_APPROVING.equals(rcwlShortlistHeader.getState())) {
                throw new CommonException("当前状态下不允许审批！");
            }
            header = new RcwlShortlistHeader();
            header.setShortlistHeaderId(shortlistHeaderId);
            //获取版本信息
            if (!ObjectUtils.allNotNull(rcwlShortlistHeader.getObjectVersionNumber())) {
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
        if (!SHORTLIST_CATEGEORY_SOLICITATION.equals(shortlistCategory)) {
            throw new CommonException("入围方式为公开征集才可发布！");
        }
        //修改状态
        rcwlShortlistHeader.setState(RW_STUTAS_PUBLISHED);
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);
        return rcwlShortlistHeader;
    }

    @Override
    public RcwlShortlistHeader submit(RcwlShortlistHeader rcwlShortlistHeader) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        //公开征集时，报名时间截止后才可提交
        String shortlistCategory = rcwlShortlistHeader.getShortlistCategory();
        //查询供应个数
        Long supplierCount = rcwlShortlistHeaderMapper.supplierCount(rcwlShortlistHeader.getShortlistHeaderId());
        if (SHORTLIST_CATEGEORY_SOLICITATION.equals(shortlistCategory)) {
            Date finishDate = rcwlShortlistHeader.getFinishDate();
            logger.info("-------------finishDate:" + finishDate);
            logger.info("-------------finishDate.compareTo:" + finishDate.compareTo(new Date()));
            if (finishDate.compareTo(new Date()) <= 0) {
                throw new CommonException("未到报名截止时间，无法发布！");
            }
        }
        //供应商数量<标段数*2+2 则报错
        Long bidNumber;
        if (null != rcwlShortlistHeader.getAttributeBigint2() && !"".equals(rcwlShortlistHeader.getAttributeBigint2())) {
            bidNumber = rcwlShortlistHeader.getAttributeBigint2();
        } else {
            bidNumber = 0L;
        }
        logger.info("标段数：" + rcwlShortlistHeader.getAttributeBigint2() + "---供应商数量：" + supplierCount);
        if (ObjectUtils.allNotNull(rcwlShortlistHeader.getSourceCategory()) && "Attract".equals(rcwlShortlistHeader.getSourceCategory())) {
            if (supplierCount < bidNumber * 2 + 2) {
                throw new CommonException("入围供应商数量不满足，请重新维护！");
            }
        } else {
            if (supplierCount < bidNumber * 2) {
                throw new CommonException("入围供应商数量不满足，请重新维护！");
            }
        }
        //将状态修改为审批中
//        rcwlShortlistHeader.setState(RW_STUTAS_APPROVING);

        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);


        //---------------------设置bpm字段并且提交审批---------------------
        String ip = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_URLIP");
        rcwlShortlistHeader.setUrl("http://" + ip + "/Workflow/MTStart2.aspx?BSID=WLCGGXPT&BTID=RCWLSRMRWD2&BOID=" + rcwlShortlistHeader.getShortlistNum());
        RcwlShortlistHeader bpmHeaderData = rcwlShortlistHeaderMapper.selectShortlistHeaderById(DetailsHelper.getUserDetails().getTenantId(), rcwlShortlistHeader.getShortlistHeaderId());

        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");
        String prUrl = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_SHORT_LIST_BPM_URL");
        String userName = DetailsHelper.getUserDetails().getUsername();

        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        RcwlBpmShortListHeaderDTO rcwlBpmShortListHeaderDTO = new RcwlBpmShortListHeaderDTO();
        List<RcwlBpmShortListSuppierDTO> rcwlBpmShortListSuppierDTOList = new ArrayList<>();
        List<RcwlBpmShortListPrDTO> rcwlBpmShortListPrDTOList = new ArrayList<>();
        List<RcwlBpmShortListFilesDto> rcwlBpmShortListFilesDtoList = new ArrayList<>();
        rcwlBpmShortListSuppierDTOList = rcwlSupplierHeaderService.rcwlSelectBpmSuppier(DetailsHelper.getUserDetails().getTenantId(), bpmHeaderData.getShortlistHeaderId());
        rcwlBpmShortListPrDTOList = rcwlSupplierHeaderService.rcwlSelectBpmPr(DetailsHelper.getUserDetails().getTenantId(), bpmHeaderData.getShortlistHeaderId());
        rcwlBpmShortListFilesDtoList = rcwlSupplierHeaderService.rcwlSelectBpmFile(DetailsHelper.getUserDetails().getTenantId(), bpmHeaderData.getShortlistHeaderId());

        if (rcwlBpmShortListFilesDtoList != null && rcwlBpmShortListFilesDtoList.size() > 0) {
            int fileNumber = 1;
            for (RcwlBpmShortListFilesDto e : rcwlBpmShortListFilesDtoList) {
                /*设置文件序号，自动增长，1，2，3，4...*/
                e.setFileNumber(String.valueOf(fileNumber));
                fileNumber++;
            }
        }
//        if (null != rcwlBpmShortListPrDTOList && rcwlBpmShortListPrDTOList.size() > 0) {
//            for (RcwlBpmShortListPrDTO e : rcwlBpmShortListPrDTOList
//            ) {
//                e.setUrlMx(prUrl + e.getUrlMx());
//            }
//        }
        //设置传输值
        rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
        rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
        rcwlGxBpmStartDataDTO.setUserId(userName);
        rcwlGxBpmStartDataDTO.setBtid("RCWLSRMRWD2");
        rcwlGxBpmStartDataDTO.setBoid(bpmHeaderData.getShortlistNum());

        if (null != bpmHeaderData.getAttributeVarchar8() && !"".equals(bpmHeaderData.getAttributeVarchar8())) {
            rcwlGxBpmStartDataDTO.setProcinstId(bpmHeaderData.getAttributeVarchar8());
        } else {
            rcwlGxBpmStartDataDTO.setProcinstId("0");
        }
        //设置数据
        rcwlBpmShortListHeaderDTO.setfSubject("入围单" + bpmHeaderData.getShortlistNum() + bpmHeaderData.getCompanyName());
        rcwlBpmShortListHeaderDTO.setShortListNum(bpmHeaderData.getShortlistNum());
        rcwlBpmShortListHeaderDTO.setProjectname(bpmHeaderData.getProjectName());
        rcwlBpmShortListHeaderDTO.setBusinessentity(bpmHeaderData.getOuName());
        rcwlBpmShortListHeaderDTO.setShortlistcategory(bpmHeaderData.getSourceCategoryMeaning());
        rcwlBpmShortListHeaderDTO.setStartdate(simpleDateFormat.format(bpmHeaderData.getStartDate()));
        rcwlBpmShortListHeaderDTO.setFinishdate(simpleDateFormat.format(bpmHeaderData.getFinishDate()));
        rcwlBpmShortListHeaderDTO.setCapital(Long.toString(bpmHeaderData.getCapital()));
        rcwlBpmShortListHeaderDTO.setYears(Long.toString(bpmHeaderData.getYears()));
        rcwlBpmShortListHeaderDTO.setOneprofit(bpmHeaderData.getOneProfit() != null ? bpmHeaderData.getOneProfit().toString() : "");
        rcwlBpmShortListHeaderDTO.setTwoprofit(bpmHeaderData.getTwoProfit() != null ? bpmHeaderData.getTwoProfit().toString() : "");
        rcwlBpmShortListHeaderDTO.setRequirements(bpmHeaderData.getRequestContent());
        rcwlBpmShortListHeaderDTO.setCompany(bpmHeaderData.getCompanyName());
        rcwlBpmShortListHeaderDTO.setUrlMx(prUrl + bpmHeaderData.getShortlistHeaderId());


        rcwlBpmShortListHeaderDTO.setRcwlBpmShortListFilesDtoList(rcwlBpmShortListFilesDtoList);
        rcwlBpmShortListHeaderDTO.setRcwlBpmShortListSuppierDTOList(rcwlBpmShortListSuppierDTOList);
        rcwlBpmShortListHeaderDTO.setRcwlBpmShortListPrDTOList(rcwlBpmShortListPrDTOList);

        String data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rcwlBpmShortListHeaderDTO);
        logger.info("业务数据：" + data);


        rcwlGxBpmStartDataDTO.setData(data);
        ResponsePayloadDTO responsePayloadDTO = new ResponsePayloadDTO();
        //调用bpm接口
        responsePayloadDTO = rcwlGxBpmInterfaceService.RcwlGxBpmInterfaceRequestData(rcwlGxBpmStartDataDTO);

        logger.info("返回结果：" + responsePayloadDTO);

        //TODO 后续业务逻辑处理
        return rcwlShortlistHeader;
    }

    @Override
    public SupplierVO currentSupplierInfo() {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        //获取用户公司基本信息
        SupplierVO supplierVO = rcwlShortlistHeaderMapper.currentSupplierInfo(userDetails.getUserId());
        //获取联系人及电话
        if (ObjectUtils.allNotNull(supplierVO)) {
            Long companyId = supplierVO.getCompanyId();
            List<CompanyContactVO> companyContactVOS = rcwlShortlistHeaderMapper.selectContactsByCompanyId(companyId);
            if (CollectionUtils.isNotEmpty(companyContactVOS)) {
                supplierVO.setContact(companyContactVOS.get(0).getContact());
                supplierVO.setPhone(companyContactVOS.get(0).getPhone());
            }
        }
        return supplierVO;
    }

    @Override
    public Set<Long> queryPrLine(Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.queryPrLine(shortlistHeaderId);
    }

    @Override
    public List<PrLineVO> pageAssignList(Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.pageAssignList(shortlistHeaderId);
    }

    /**
     * 更新寻源方式
     *
     * @param sourceMethod
     * @param rfxHeaderId
     */
    @Override
    public void updateRfxSourceMethod(String sourceMethod, Long rfxHeaderId, String shotListNum) {
        rcwlShortlistHeaderMapper.updateRfxSourceMethod(sourceMethod, rfxHeaderId, shotListNum);
    }

    /**
     * 通过code查询ID
     *
     * @param organizationId
     * @param shotListNum
     * @return
     */
    @Override
    public Long rcwlSelectShortListHeaderIdByCode(Long organizationId, String shotListNum) {
        return rcwlShortlistHeaderMapper.rcwlSelectShortListHeaderIdByCode(organizationId, shotListNum);
    }

    @Override
    public UserDefaultDTO getPurchaseAgentByExpertInfo(Long organizationId, Long purchaserId) {
        return this.rcwlShortlistHeaderMapper.getPurchaseAgentByExpertInfo(organizationId, purchaserId);
    }
}
