package org.srm.source.app.service;

import org.srm.source.domain.entity.PlanHeader;

/**
 * 采购申请行应用服务
 *
 * @author bin.zhang06@hand-china.com 2021-03-16 15:49:15
 */
public interface RCWLPrLineService {

    void updatePlanNum(PlanHeader planHeader);
}
