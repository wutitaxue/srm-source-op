package org.srm.source.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.imported.domain.entity.ImportData;
import org.srm.source.api.dto.HeaderQueryDTO;
import org.srm.source.domain.entity.PlanHeader;
import org.srm.source.domain.vo.PlanHeaderImportVO;
import org.srm.source.domain.vo.PlanHeaderVO;

import java.util.List;

/**
 * 采购计划表应用服务
 *
 * @author bin.zhang06@hand-china.com 2021-03-15 12:30:00
 */
public interface RCWLPlanHeaderService {
    Page<PlanHeaderVO> listPlanHeader(PageRequest pageRequest, Long organizationId, HeaderQueryDTO planHeaderParam);

    List<PlanHeader> batchCancelPlanHeader(Long organizationId, List<PlanHeader> planHeaderList);

    void createAndUpdate(PlanHeader planHeader);

    void saveAttachment(Long tenantId, PlanHeader planHeader);

    boolean checkData(PlanHeaderImportVO planHeaderImportVO, ImportData importData, Long tenantId);
}
