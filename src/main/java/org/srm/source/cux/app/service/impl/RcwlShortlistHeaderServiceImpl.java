package org.srm.source.cux.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import javassist.Loader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.code.constant.CodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.api.dto.RcwlAbilityHeadDTO;
import org.srm.source.cux.api.dto.RcwlAbilityLineDTO;
import org.srm.source.cux.api.dto.SupplierPoolDTO;
import org.srm.source.cux.app.service.RcwlShortlistAttachmentService;
import org.srm.source.cux.app.service.RcwlShortlistHeaderService;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RCWLPrLineRepository;
import org.srm.source.cux.domain.repository.RcwlShortlistAttachmentRepository;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierHeaderRepository;
import org.srm.source.cux.infra.feign.RcwlSpucRemoteService;
import org.srm.source.cux.infra.mapper.RcwlShortListSelectMapper;
import org.srm.source.cux.infra.mapper.RcwlShortlistHeaderMapper;
import org.srm.source.share.api.dto.PrLine;
import org.srm.source.share.domain.vo.PrLineVO;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.srm.source.cux.infra.constant.RcwlShortlistContants.CodeRule.SCUX_RCWL_SHORT_HEADER_NUM;
import static org.srm.source.cux.infra.constant.RcwlShortlistContants.LovCode.*;

/**
 * 入围单头表应用服务默认实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Service
public class RcwlShortlistHeaderServiceImpl implements RcwlShortlistHeaderService {

    @Autowired
    private RcwlShortlistAttachmentService rcwlShortlistAttachmentService;

    @Autowired
    private RcwlShortlistAttachmentRepository rcwlShortlistAttachmentRepository;

    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @Autowired
    private CodeRuleBuilder codeRuleBuilder;

    @Autowired
    private RcwlSpucRemoteService rcwlSpucRemoteService;

    @Autowired
    private RcwlShortlistHeaderMapper rcwlShortlistHeaderMapper;

    @Autowired
    private RcwlSupplierHeaderRepository rcwlSupplierHeaderRepository;

    @Autowired
    private RcwlShortListSelectMapper rcwlShortListSelectMapper;

    @Autowired
    private RCWLPrLineRepository  rcwlPrLineRepository;

    private static final Logger logger = LoggerFactory.getLogger(RcwlShortlistHeaderServiceImpl.class);

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        //获取用户信息
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long tenantId = userDetails.getTenantId();
       // Long tenantId = 16L;
        rcwlShortlistHeader.setTenantId(tenantId);

        //校验
        this.checkRcwlShortlistHeader(rcwlShortlistHeader);

        if (rcwlShortlistHeader.getShortlistHeaderId() == null) {
            //生成编码
            String ruleCode = codeRuleBuilder.generateCode(tenantId, SCUX_RCWL_SHORT_HEADER_NUM, CodeConstants.CodeRuleLevelCode.GLOBAL, CodeConstants.CodeRuleLevelCode.GLOBAL, null);
            //String ruleCode = "RW202104210001";
            rcwlShortlistHeader.setShortlistNum(ruleCode);
            //默认新建状态
            rcwlShortlistHeader.setState(RW_STUTAS_NEW);
            rcwlShortlistHeaderRepository.insertSelective(rcwlShortlistHeader);
            //更新prLine 信息
            if (CollectionUtils.isNotEmpty(rcwlShortlistHeader.getPrLineIds())) {
                List<PrLine> prLines = new ArrayList<>();
                PrLine prLine;
                for (Long prLineId : rcwlShortlistHeader.getPrLineIds()) {
                    prLine = new PrLine();
                    prLine.setPrLineId(prLineId);
                    prLine.setAttributeVarchar2(rcwlShortlistHeader.getShortlistNum());
                    prLine.setAttributeBigint1(rcwlShortlistHeader.getShortlistHeaderId());
                    prLines.add(prLine);
                }
                //rcwlSpucRemoteService.feignUpdatePrLine(tenantId, prLines);
                rcwlShortlistHeaderRepository.updatePrLineByShortlistHeaderId(rcwlShortlistHeader.getShortlistHeaderId(), rcwlShortlistHeader.getPrLineIds());
            }
        } else {
            //add by 21420 融创二期二开，保存时根据采购申请行信息自动带出供应商信息
            //获取入围单关联的采购申请的品类集合

            List<Long> categoryIds =new ArrayList<>();
            List<Long> companyIds =new ArrayList<>();
            List<PrLineVO> prInfos = rcwlShortlistHeaderRepository.pageAssignList(rcwlShortlistHeader.getShortlistHeaderId());
            for(PrLineVO prInfo : prInfos){
                long categoryId = prInfo.getCategoryId();
                long companyId = prInfo.getCompanyId();
                categoryIds.add(categoryId);
                companyIds.add(companyId);
            }
            //去重
            List<Long> newCategoryIds = categoryIds.stream().distinct().collect(Collectors.toList());
            List<Long> newCompanyIds = companyIds.stream().distinct().collect(Collectors.toList());

            logger.info("去重之后的品类ID：{}",newCategoryIds);
            logger.info("去重之后的公司ID：{}",newCompanyIds);
            //判断入围方式
            String shortlistCategoryType = rcwlShortlistHeader.getShortlistCategory();
            List<String> params = new ArrayList<>();
            if(SHORTLIST_CATEGEORY_SOLICITATION.equals(shortlistCategoryType)){
                params.add("REGISTER");
                params.add("POTENTIAL");
                params.add("QUALIFIED");
            }else if(SHORTLIST_CATEGEORY_INVITATION.equals(shortlistCategoryType)){
                params.add("POTENTIAL");
                params.add("QUALIFIED");
            }
            List<Long> stageIds = rcwlShortlistHeaderRepository.selectStageIdsByCodeList(tenantId,params);
            //查询供货能力清单中符合条件的供应商(去重)
            List<SupplierPoolDTO> supplierInfos = rcwlShortlistHeaderRepository.getSuppliersInfo(tenantId,newCategoryIds,newCompanyIds,stageIds);
            //获取已存在的供应商
            List<RcwlSupplierHeader> existsSuppliers = rcwlShortlistHeaderRepository.selectSupplierInfoByShortlistHeaderId(tenantId,rcwlShortlistHeader.getShortlistHeaderId());
           List<Long> existsSuppliersIds = new ArrayList<>();
            existsSuppliers.forEach(existsSupplier->{
                existsSuppliersIds.add(existsSupplier.getSupplierId());
            });
            //移除已存在的供应商
            supplierInfos.removeIf(s->
                existsSuppliersIds.contains(s.getSupplierCompanyId())
            );
            logger.info("剩余的供应商：{}",supplierInfos);
            //如果有剩余的供应商则新增
            if(CollectionUtils.isNotEmpty(supplierInfos)){
                //设置传递参数
                supplierInfos.forEach(supplierInfo->{
                    RcwlSupplierHeader supplierHeader = new RcwlSupplierHeader();
                    supplierHeader.setTenantId(tenantId);
                    supplierHeader.setSupplierId(supplierInfo.getSupplierCompanyId());
                    supplierHeader.setSupplierNum(supplierInfo.getSupplierCompanyNum());
                    supplierHeader.setStageId(supplierInfo.getStageId());
                    supplierHeader.setShortlistHeaderId(rcwlShortlistHeader.getShortlistHeaderId());
                    supplierHeader.setPhone(supplierInfo.getContactPhone());
                    supplierHeader.setContacts(supplierInfo.getContactName());
                    supplierHeader.setCompanyName(supplierInfo.getCompanyNameMeaning());
                    supplierHeader.setCompanyNum(supplierInfo.getCompanyNum());
                    supplierHeader.setCompanyId(supplierInfo.getCompanyId());
                    supplierHeader.setAttributeBigint4(supplierInfo.getSupplierTenantId());
                    supplierHeader.setAttributeBigint3(supplierInfo.getSpfmSupplierCompanyId());
                    supplierHeader.setAttributeBigint2(supplierInfo.getSpfmCompanyId());
                    supplierHeader.setAttributeBigint1(supplierInfo.getSupplierCompanyId());
                    //调用方法
                    rcwlSupplierHeaderRepository.createAndUpdateSupplierHeader(supplierHeader);
                });
            }
            rcwlShortlistHeaderRepository.updateByPrimaryKeySelective(rcwlShortlistHeader);
        }

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteRcwlShortlistHeaderByIds(List<RcwlShortlistHeader> rcwlShortlistHeaders) {
        for (RcwlShortlistHeader rcwlShortlistHeader : rcwlShortlistHeaders) {
            //恢复prLine的值
            rcwlShortlistHeaderRepository.updatePrLineByShortlistHeader(rcwlShortlistHeader);
            Long shortlistHeaderId = rcwlShortlistHeader.getShortlistHeaderId();
            RcwlShortlistHeader rcwlShortlistHeaderSelect = rcwlShortlistHeaderRepository.selectByPrimaryKey(shortlistHeaderId);
            rcwlShortlistHeaderSelect.setState(RW_STUTAS_DELETE);
            //更新入围单头信息
            rcwlShortlistHeaderRepository.updateOptional(rcwlShortlistHeaderSelect, RcwlShortlistHeader.FIELD_STATE);
//            //查询附件模版信息
//            RcwlShortlistAttachment rcwlShortlistAttachment = new RcwlShortlistAttachment();
//            rcwlShortlistAttachment.setShortlistId(rcwlShortlistHeader.getShortlistHeaderId());
//            List<RcwlShortlistAttachment> selectShortlistAttachment = rcwlShortlistAttachmentRepository.select(rcwlShortlistAttachment);
//            if(CollectionUtils.isNotEmpty(selectShortlistAttachment)){
//                //删除入围单附件信息
//                rcwlShortlistAttachmentService.deleteRcwlShortlistAttachment(selectShortlistAttachment);
//                //TODO 删除附件
//            }
//
//            RcwlSupplierHeader rcwlSupplierHeader = new RcwlSupplierHeader();
//            rcwlSupplierHeader.setShortlistHeaderId(rcwlShortlistHeader.getShortlistHeaderId());
//            List<RcwlSupplierHeader> selectSupplierHeaders = rcwlSupplierHeaderRepository.select(rcwlSupplierHeader);
//            if(CollectionUtils.isNotEmpty(selectSupplierHeaders)){
//                //删除供应商信息
//                rcwlSupplierHeaderRepository.batchDeleteBySupplierHeader(selectSupplierHeaders);
//            }


        }
    }

    /**
     * 提交成功
     *
     * @param tenantId
     * @param ShorListNum
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rcwlSubmitBpmSuccessed(Long tenantId, String ShorListNum) {
        Long shortListHeaderId = rcwlShortlistHeaderRepository.rcwlSelectShortListHeaderIdByCode(tenantId, ShorListNum);

        RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectShortlistHeaderById(tenantId, shortListHeaderId);
        rcwlShortlistHeader.setState(RW_STUTAS_APPROVING);
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);
    }

    /**
     * 审批拒绝
     *
     * @param tenantId
     * @param ShorListNum
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rcwlSubmitBpmReject(Long tenantId, String ShorListNum) {
        Long shortListHeaderId = rcwlShortlistHeaderRepository.rcwlSelectShortListHeaderIdByCode(tenantId, ShorListNum);

        RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectShortlistHeaderById(tenantId, shortListHeaderId);
        rcwlShortlistHeader.setState(RW_STUTAS_REJECTED);
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);

    }

    /**
     * 审批通过
     *
     * @param tenantId
     * @param ShorListNum
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rcwlSubmitBpmApproved(Long tenantId, String ShorListNum) {
        Long shortListHeaderId = rcwlShortlistHeaderRepository.rcwlSelectShortListHeaderIdByCode(tenantId, ShorListNum);

        RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectShortlistHeaderById(tenantId, shortListHeaderId);
        rcwlShortlistHeader.setState(RW_STUTAS_APPROVED);
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);

        //查询selected为1的供应商
        List<RcwlSupplierHeader> rcwlSupplierHeaders = rcwlShortListSelectMapper.selectSuppliers(tenantId, shortListHeaderId);
        logger.info("25140===================rcwlSupplierHeaders"+rcwlSupplierHeaders);
        //查询关联采购申请的物品行
        List<RcwlAbilityLineDTO> rcwlCategoryDTOS = rcwlShortListSelectMapper.selectItems(tenantId, shortListHeaderId);
        logger.info("25140===================rcwlCategoryDTOS"+rcwlCategoryDTOS);

        //判断供应商是否在sslm_supply_ability中存在
        rcwlSupplierHeaders.forEach(supplierHeader -> {
            Long exist = rcwlShortListSelectMapper.checkExistAbility(tenantId, supplierHeader.getSupplierId());
            logger.info("25140=======判断供应商是否存在============exist"+exist);
            //存在则只更新品类
            if(exist > 0){
                rcwlCategoryDTOS.forEach(category -> {
                    //判断供应商是否在sslm_supply_ability中存在
                    Long existline = rcwlShortListSelectMapper.checkExistAbilityLine(tenantId, rcwlShortListSelectMapper.selectHeaderId(supplierHeader.getSupplierTenantId(),supplierHeader.getSupplierId()), category.getCategoryId());
                    logger.info("25140=======情况1============existline"+existline);
                    if(existline == 0){
                        RcwlAbilityLineDTO rcwlAbilityLineDTO = new RcwlAbilityLineDTO();
                        rcwlAbilityLineDTO.setTenantId(tenantId);
                        rcwlAbilityLineDTO.setCategoryCode(category.getCategoryCode());
                        rcwlAbilityLineDTO.setCategoryId(category.getCategoryId());
                        rcwlAbilityLineDTO.setSupplyAbilityId(rcwlShortListSelectMapper.selectHeaderId(supplierHeader.getSupplierTenantId(),supplierHeader.getSupplierId()));
                        rcwlAbilityLineDTO.setCreatedBy(supplierHeader.getCreatedBy());
                        rcwlAbilityLineDTO.setLastUpdatedBy(supplierHeader.getLastUpdatedBy());
                        logger.info("25140========情况1===========rcwlAbilityLineDTO"+rcwlAbilityLineDTO);
                        rcwlShortListSelectMapper.insetAbilityLine(rcwlAbilityLineDTO);
                    }
                });
            //不存在则新增头行
            }else {
                //创建能力头
                RcwlAbilityHeadDTO rcwlAbilityHeadDTO = new RcwlAbilityHeadDTO();
                rcwlAbilityHeadDTO.setSupplierCompanyId(supplierHeader.getSupplierId());
                rcwlAbilityHeadDTO.setSupplierTenantId(supplierHeader.getSupplierTenantId());
                rcwlAbilityHeadDTO.setCreatedBy(supplierHeader.getCreatedBy());
                rcwlAbilityHeadDTO.setLastUpdatedBy(supplierHeader.getLastUpdatedBy());
                rcwlAbilityHeadDTO.setTenantId(tenantId);
                logger.info("25140=======情况2============rcwlAbilityHeadDTO"+rcwlAbilityHeadDTO);
                rcwlShortListSelectMapper.insetAbilityHead(rcwlAbilityHeadDTO);
                Long headId = rcwlShortListSelectMapper.selectHeaderId(supplierHeader.getSupplierTenantId(),supplierHeader.getSupplierId());
                //创建能力行
                rcwlCategoryDTOS.forEach(category -> {
                        RcwlAbilityLineDTO rcwlAbilityLineDTO = new RcwlAbilityLineDTO();
                        rcwlAbilityLineDTO.setTenantId(tenantId);
                        rcwlAbilityLineDTO.setCategoryCode(category.getCategoryCode());
                        rcwlAbilityLineDTO.setCategoryId(category.getCategoryId());
                        rcwlAbilityLineDTO.setSupplyAbilityId(headId);
                        rcwlAbilityLineDTO.setCreatedBy(supplierHeader.getCreatedBy());
                        rcwlAbilityLineDTO.setLastUpdatedBy(supplierHeader.getLastUpdatedBy());
                        logger.info("25140======情况2=============rcwlAbilityLineDTO"+rcwlAbilityLineDTO);
                        rcwlShortListSelectMapper.insetAbilityLine(rcwlAbilityLineDTO);
                });
            }
        });
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void RcwlBpmUpateInstance(Long tenantId, String shorListNum, String attributeVarchar8, String attributeVarchar9) {
        Long shortListHeaderId = rcwlShortlistHeaderRepository.rcwlSelectShortListHeaderIdByCode(tenantId, shorListNum);

        RcwlShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectShortlistHeaderById(tenantId, shortListHeaderId);
        if (null != attributeVarchar8 && !"".equals(attributeVarchar8)) {
            rcwlShortlistHeader.setAttributeVarchar8(attributeVarchar8);
        }
        if (null != attributeVarchar9 && !"".equals(attributeVarchar9)) {
            rcwlShortlistHeader.setAttributeVarchar9(attributeVarchar9);
        }
        rcwlShortlistHeaderMapper.updateByPrimaryKeySelective(rcwlShortlistHeader);
    }

    /**
     * 校验入围单信息
     *
     * @param rcwlShortlistHeader 入围单信息
     */
    private void checkRcwlShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        Long shortlistHeaderId = rcwlShortlistHeader.getShortlistHeaderId();
        List<Long> prLineIds = rcwlShortlistHeader.getPrLineIds();
        //1.prLine是否已存在入围单
        if (CollectionUtils.isNotEmpty(prLineIds)) {
            prLineIds.forEach(prLineId -> {
                PrLineVO prLineVO = rcwlShortlistHeaderRepository.selectPrLineByIdDontShortHeaderId(prLineId, shortlistHeaderId);
                if (ObjectUtils.allNotNull(prLineVO)) {
                    throw new CommonException("采购订单：" + prLineVO.getPrNum() + " 行号：" + prLineVO.getLineNum() + " 已存在其他入围单中！！");
                }
            });
        }
        //2.prLine 数量 校验
    }


}
