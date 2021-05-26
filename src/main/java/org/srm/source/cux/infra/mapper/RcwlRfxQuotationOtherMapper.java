package org.srm.source.cux.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;

import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/26 15:04
 * @version:1.0
 */
@Component
public interface RcwlRfxQuotationOtherMapper {

    void updateSuppierFlag(@Param("quotationHeaderId") Long quotationHeaderId,@Param("attributeVarchar2") String attributeVarchar2);

}
