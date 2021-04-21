package org.srm.source.cux.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.imported.domain.entity.ImportData;
import org.srm.source.cux.api.dto.HeaderQueryDTO;
import org.srm.source.cux.domain.entity.PlanHeader;
import org.srm.source.cux.domain.vo.PlanHeaderImportVO;
import org.srm.source.cux.domain.vo.PlanHeaderVO;

import java.util.List;

/**
 * 采购计划表应用服务
 *
 * @author bin.zhang06@hand-china.com 2021-03-15 12:30:00
 */
public interface RCWLPlanHeaderService {
    /**
     * 查询全部
     * @param pageRequest
     * @param organizationId
     * @param planHeaderParam
     * @return
     */
    Page<PlanHeaderVO> listPlanHeader(PageRequest pageRequest, Long organizationId, HeaderQueryDTO planHeaderParam);

    /**
     * 批量取消
     * @param organizationId
     * @param planHeaderList
     * @return
     */
    List<PlanHeader> batchCancelPlanHeader(Long organizationId, List<PlanHeader> planHeaderList);

    /**
     * 创建保存
     * @param planHeader
     */
    void createAndUpdate(PlanHeader planHeader);

    /**
     * 附件保存
     * @param tenantId
     * @param planHeader
     */
    void saveAttachment(Long tenantId, PlanHeader planHeader);

    /**
     * 导入校验
     * @param planHeaderImportVO
     * @param importData
     * @param tenantId
     * @return
     */
    boolean checkData(PlanHeaderImportVO planHeaderImportVO, ImportData importData, Long tenantId);
}
