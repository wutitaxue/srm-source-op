package org.srm.source.cux.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.code.constant.CodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.app.service.RcwlShortlistHeaderService;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlShortlistHeader;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.share.domain.vo.PrLineVO;

import javax.transaction.Transactional;

import java.util.List;

import static org.srm.source.cux.infra.constant.RcwlShortlistContants.CodeRule.SCUX_RCWL_SHORT_HEADER_NUM;

/**
 * 入围单头表应用服务默认实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Service
public class RcwlShortlistHeaderServiceImpl implements RcwlShortlistHeaderService {

    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @Autowired
    private CodeRuleBuilder codeRuleBuilder;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createShortlistHeader(RcwlShortlistHeader rcwlShortlistHeader) {
        //获取用户信息
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long tenantId = userDetails.getTenantId();
        rcwlShortlistHeader.setTenantId(tenantId);

        //校验
        this.checkRcwlShortlistHeader(rcwlShortlistHeader);

        if (rcwlShortlistHeader.getShortlistHeaderId() != null) {
            //生成编码
            String ruleCode = codeRuleBuilder.generateCode(tenantId, SCUX_RCWL_SHORT_HEADER_NUM, CodeConstants.CodeRuleLevelCode.GLOBAL, CodeConstants.CodeRuleLevelCode.GLOBAL, null);
            rcwlShortlistHeader.setShortlistNum(ruleCode);
            rcwlShortlistHeaderRepository.insertSelective(rcwlShortlistHeader);
        } else {
            rcwlShortlistHeaderRepository.updateByPrimaryKeySelective(rcwlShortlistHeader);
        }
        //更新prLine 信息
        if (CollectionUtils.isNotEmpty(rcwlShortlistHeader.getPrLineIds())) {
            rcwlShortlistHeaderRepository.updatePrLineByShortlistHeaderId(rcwlShortlistHeader.getShortlistHeaderId(), rcwlShortlistHeader.getPrLineIds());
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
