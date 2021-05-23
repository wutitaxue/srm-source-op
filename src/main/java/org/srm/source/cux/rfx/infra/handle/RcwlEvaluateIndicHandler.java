package org.srm.source.cux.rfx.infra.handle;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.scheduler.infra.annotation.JobHandler;
import org.hzero.boot.scheduler.infra.enums.ReturnT;
import org.hzero.boot.scheduler.infra.handler.IJobHandler;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.cux.share.app.service.IRcwlEvaluateScoreLineService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.api.dto.AutoScoreDTO;
//import org.srm.source.share.app.service.EvaluateScoreLineService;
import org.srm.source.share.app.service.impl.EvaluateScoreLineServiceImpl;
import org.srm.source.share.infra.handle.EvaluateIndicHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * RCWL 寻源执行器
 * @author kaibo.li
 * @date 2021-05-18 18:47
 */
@JobHandler("rcwlEvaluateIndicHandler")
public class RcwlEvaluateIndicHandler implements IJobHandler {
    /**
     * 原始的
     */
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;

    @Autowired
    private IRcwlEvaluateScoreLineService rcwlEvaluateScoreLineService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluateIndicHandler.class);

    public RcwlEvaluateIndicHandler() {
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class}
    )
    @Override
    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        LOGGER.info("RCWL HELLO rcwlEvaluateIndicHandler");
        // 获取需要自动评分的所有数据
        List<RfxHeader> rfxHeaders = this.rfxHeaderRepository.selectRfxForAutoScore();
        if (CollectionUtils.isEmpty(rfxHeaders)) {
            LOGGER.debug("no record found");
            return ReturnT.SUCCESS;
        } else {
            LOGGER.debug("find {} records,process start", rfxHeaders.size());
            EvaluateScoreLineServiceImpl.AUTO_FLAG.set(Boolean.TRUE);
            Iterator var4 = rfxHeaders.iterator();

            while(var4.hasNext()) {
                RfxHeader rfxHeader = (RfxHeader)var4.next();
                LOGGER.debug("rfx header :{}", rfxHeaders);

                try {
                    this.rcwlEvaluateScoreLineService._autoEvaluateScore(new AutoScoreDTO(rfxHeader.getTenantId(), "RFX", rfxHeader.getRfxHeaderId()));
                    rfxHeader.setScoreProcessFlag(1);
                } catch (Exception var10) {
                    LOGGER.error("rfx header auto score process error", var10);
                    rfxHeader.setScoreProcessFlag(0);
                } finally {
                    LOGGER.debug("update rfx status");
                    this.rfxHeaderRepository.updateOptional(rfxHeader, "scoreProcessFlag");
                }
            }

            EvaluateScoreLineServiceImpl.AUTO_FLAG.remove();
            return ReturnT.SUCCESS;
        }
    }
}
