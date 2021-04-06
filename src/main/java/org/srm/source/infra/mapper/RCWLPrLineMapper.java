package org.srm.source.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.srm.source.api.dto.PrLineDTO;
import org.srm.source.domain.entity.PrLine;
import org.srm.source.domain.vo.PrHeaderVO;

/**
 * 采购申请行Mapper
 *
 * @author bin.zhang06@hand-china.com 2021-03-16 15:49:15
 */
public interface RCWLPrLineMapper extends BaseMapper<PrLine> {

    PrLineDTO selectPrLine(@Param("planId") Long planId);

    void updatePrLine(@Param("planId") Long planId, @Param("tenantId") Long tenantId);
    /**
     * 通过申请头id和行号查找行id
     *
     * @param prHeaderId
     * @param lineNum
     * @return
     */
    Long selectPrLineId(@Param("prHeaderId") Long prHeaderId, @Param("lineNum") String lineNum, @Param("tenantId") Long tenantId);

    PrHeaderVO selectByNum(@Param("prNum") String prNum, @Param("lineNum") String lineNum, @Param("tenantId") Long tenantId);
}
