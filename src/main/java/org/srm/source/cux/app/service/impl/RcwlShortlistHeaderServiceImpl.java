package org.srm.source.cux.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.code.constant.CodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.app.service.RcwlShortlistAttachmentService;
import org.srm.source.cux.app.service.RcwlShortlistHeaderService;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.entity.RcwlSupplierHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistAttachmentRepository;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierHeaderRepository;
import org.srm.source.cux.infra.constant.RcwlShortlistContants;
import org.srm.source.cux.infra.feign.RcwlSpucRemoteService;
import org.srm.source.share.api.dto.PrLine;
import org.srm.source.share.domain.vo.PrLineVO;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.srm.source.cux.infra.constant.RcwlShortlistContants.CodeRule.SCUX_RCWL_SHORT_HEADER_NUM;
import static org.srm.source.cux.infra.constant.RcwlShortlistContants.LovCode.RW_STUTAS_DELETE;
import static org.srm.source.cux.infra.constant.RcwlShortlistContants.LovCode.RW_STUTAS_NEW;

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
    private RcwlSupplierHeaderRepository rcwlSupplierHeaderRepository;


    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        //获取用户信息
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long tenantId = userDetails.getTenantId();
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
            rcwlShortlistHeaderRepository.updateOptional(rcwlShortlistHeader, RcwlShortlistHeader.FIELD_STATE);
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
     * 校验入围单信息
     *
     * @param rcwlShortlistHeader 入围单信息
     */
    private void checkRcwlShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        Long shortlistHeaderId = rcwlShortlistHeader.getShortlistHeaderId();
        List<Long> prLineIds = rcwlShortlistHeader.getPrLineIds();
        //1.prLine是否已存在入围单
        if(CollectionUtils.isNotEmpty(prLineIds)) {
            prLineIds.forEach(prLineId -> {
                PrLineVO prLineVO = rcwlShortlistHeaderRepository.selectPrLineByIdDontShortHeaderId(prLineId, shortlistHeaderId);
                if(ObjectUtils.allNotNull(prLineVO)){
                    throw new CommonException("采购订单：" + prLineVO.getPrNum() + " 行号：" + prLineVO.getLineNum() + " 已存在其他入围单中！！");
                }
            });
        }
        //2.prLine 数量 校验
    }
}
