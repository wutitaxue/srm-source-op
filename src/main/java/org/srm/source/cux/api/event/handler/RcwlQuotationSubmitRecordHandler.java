package org.srm.source.cux.api.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.srm.boot.event.infra.wrapper.EventDataWrapper;
import org.srm.boot.event.service.listener.EventListenerHandle;
import org.srm.source.cux.infra.constant.RcwlShareConstants;
import org.srm.source.rfx.api.dto.QuotationSubmitDTO;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.service.IRfxQuotationRecordDomainService;
import org.srm.source.share.infra.constant.ShareConstants;
import org.srm.web.annotation.Tenant;

import java.util.List;

/**
 * description
 *
 * @author xuan.zhang03@hand-china.com 2021/02/12 10:51
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Tenant("SRM-RCWL")
public class RcwlQuotationSubmitRecordHandler implements EventListenerHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(org.srm.source.rfx.api.event.handler.QuotationSubmitRecordHandler.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IRfxQuotationRecordDomainService iRfxQuotationRecordDomainService;

    @Override
    public void eventHandle(EventDataWrapper eventDataWrapper) {
        long processStartTimes = System.currentTimeMillis();
        if (eventDataWrapper == null || eventDataWrapper.getData() == null) {
            LOGGER.info("receivingQuotation is null");
            return;
        }
        try {
            QuotationSubmitDTO quotationSubmitDTO = new QuotationSubmitDTO();
            if (eventDataWrapper.getData() != null) {
                quotationSubmitDTO = objectMapper.readValue(eventDataWrapper.getData(), QuotationSubmitDTO.class);
            }
            List<RfxQuotationLine> rfxQuotationLines = quotationSubmitDTO.getQuotationLines();
            RfxHeader rfxHeader = quotationSubmitDTO.getRfxHeader();
            if (!ShareConstants.SourceTemplate.CategoryType.RFQ.equals(rfxHeader.getSourceCategory())
                    &&!RcwlShareConstants.CategoryType.RCBJ.equals(rfxHeader.getSourceCategory())
                    &&!RcwlShareConstants.CategoryType.RCZB.equals(rfxHeader.getSourceCategory())
                    &&!RcwlShareConstants.CategoryType.RCZW.equals(rfxHeader.getSourceCategory())) {
                return;
            }
            iRfxQuotationRecordDomainService.generateQuotationRecords(rfxHeader, rfxQuotationLines);
            LOGGER.info("quotationSubmitRecordProcessTimes{}",System.currentTimeMillis() - processStartTimes);
        } catch (Exception e) {
            LOGGER.error("quotationSubmitRecordError:{0}",e);
        }
    }


}
