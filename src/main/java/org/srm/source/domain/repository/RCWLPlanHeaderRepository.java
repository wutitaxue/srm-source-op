package org.srm.source.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.api.dto.HeaderQueryDTO;
import org.srm.source.api.dto.PlanHeaderExportDTO;
import org.srm.source.domain.entity.PlanHeader;
import org.srm.source.domain.vo.PlanHeaderExportVO;
import org.srm.source.domain.vo.PlanHeaderVO;

/**
 * 采购计划表资源库
 *
 * @author bin.zhang06@hand-china.com 2021-03-15 12:30:00
 */
public interface RCWLPlanHeaderRepository extends BaseRepository<PlanHeader> {

    Page<PlanHeaderVO> listPlanHeader(PageRequest pageRequest, HeaderQueryDTO planHeaderParam);

    Page<PlanHeaderExportDTO> pageAllPlanHeader(PlanHeader planHeader, PageRequest pageRequest);

    Page<PlanHeaderExportVO> exportPlanHeaders(PageRequest pageRequest, PlanHeaderExportDTO planHeaderExportDTO);

    Long selectPlanIdByPrIdAndLineNum(Long prHeaderId, String lineNum);


    void updatePlanHeader(PlanHeader planHeaderParam);
  /**
  * 校验采购申请头行是否重复
  *
  */
    boolean checkPrNumRep(String prNum, String lineNum, Long tenantId);
    /**
     * 校验该公司code是否存在
     *导入时字段是name，实际输入的是公司编码
     */
    boolean checkCompanyExist(String companyNum);
    /**
     * 校验预算科目是否存在
     *
     */
    boolean checkBudgetAccountExist(String budgetAccount, Long tenantId);
    /**
     * 校验需求人是否存在
     *
     */
    boolean checkDemandersExist(String demanders, Long tenantId);
    /**
     * 校验经办人是否存在
     *
     */
    boolean checkAgentExist(String agent, Long tenantId);

    String selectCompanyName(String companyName, Long tenantId);

    Long selectCompanyId(String companyName, Long tenantId);

    String selectBudgetAccount(String budgetAccount, Long tenantId);

    String selectDemanders(String demanders, Long tenantId);

    String selectAgent(String agent, Long tenantId);
}
