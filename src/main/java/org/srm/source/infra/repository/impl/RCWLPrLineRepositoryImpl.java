package org.srm.source.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.api.dto.PrLineDTO;
import org.srm.source.domain.entity.PrLine;
import org.srm.source.domain.repository.RCWLPrLineRepository;
import org.srm.source.domain.vo.PrHeaderVO;

/**
 * 采购申请行 资源库实现
 *
 * @author bin.zhang06@hand-china.com 2021-03-16 15:49:15
 */
@Component
public class RCWLPrLineRepositoryImpl extends BaseRepositoryImpl<PrLine> implements RCWLPrLineRepository {

    @Autowired
    private org.srm.source.infra.mapper.RCWLPrLineMapper RCWLPrLineMapper;
    /**
     * 根据计划编号查询采购申请行
     *
     * @param planId
     * @return
     */
    @Override
    public PrLineDTO selectPrLine(Long planId) {
        return RCWLPrLineMapper.selectPrLine(planId);
    }

    /**
     * 把计划编号设置为空
     *
     * @param planId
     * @return
     */
    @Override
    public void updatePrLine(Long planId ,Long tenantId) {
        RCWLPrLineMapper.updatePrLine(planId,tenantId);
    }

    /**
     * 通过申请头id和行号查找行id
     *
     * @param prHeaderId
     * @param lineNum
     * @return
     */
    @Override
    public Long selectPrLineId(Long prHeaderId, String lineNum,Long tenantId) {
        return RCWLPrLineMapper.selectPrLineId(prHeaderId,lineNum,tenantId);
    }

    /**
     * 通过申请头和行号查找头行id
     *
     * @param prNum
     * @param lineNum
     * @param tenantId
     * @return
     */
    @Override
    public PrHeaderVO selectByNum(String prNum, String lineNum, Long tenantId) {
        return RCWLPrLineMapper.selectByNum(prNum,lineNum,tenantId);
    }
}
