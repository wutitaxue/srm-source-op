package org.srm.source.cux.infra.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.srm.source.cux.domain.repository.RcwlRfxQuotationOtherRepository;
import org.srm.source.cux.infra.mapper.RcwlRfxQuotationOtherMapper;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;

import java.util.List;

/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/26 15:02
 * @version:1.0
 */
@Component
public class RcwlRfxQuotationOtherRepositoryImpl implements RcwlRfxQuotationOtherRepository {
    @Autowired
    private RcwlRfxQuotationOtherMapper rcwlRfxQuotationOtherMapper;


    @Override
    public void updateSuppierFlag(List<RfxQuotationHeader> rfxQuotationHeaderList) {
        for (RfxQuotationHeader e : rfxQuotationHeaderList
        ) {
            rcwlRfxQuotationOtherMapper.updateSuppierFlag(e.getQuotationHeaderId(), e.getAttributeVarchar2());
        }

    }
}
