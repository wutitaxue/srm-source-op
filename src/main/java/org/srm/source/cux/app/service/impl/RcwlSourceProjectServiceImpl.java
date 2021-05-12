package org.srm.source.cux.app.service.impl;

import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.srm.source.cux.infra.constant.RcwlSourceConstants;
import org.srm.source.cux.infra.mapper.RcwlSourceProjectMapper;
import org.srm.source.share.api.dto.FullSourceProjectDTO;
import org.srm.source.share.app.service.*;
import org.srm.source.share.app.service.impl.SourceProjectServiceImpl;
import org.srm.source.share.domain.entity.ProjectLineItem;
import org.srm.source.share.domain.entity.ProjectLineSupplier;
import org.srm.source.share.domain.entity.SourceProject;
import org.srm.web.annotation.Tenant;

import java.util.List;


@Service
@Tenant("SRM-RCWL")
public class RcwlSourceProjectServiceImpl extends SourceProjectServiceImpl {


    @Autowired
    private ProjectLineItemService projectLineItemService;
    @Autowired
    private ProjectLineSupplierService projectLineSupplierService;
    @Autowired
    private ProjectLinePlanService projectLinePlanService;
    @Autowired
    private ProjectLineSectionService projectLineSectionService;
    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private RcwlSourceProjectMapper rcwlSourceProjectMapper;

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public FullSourceProjectDTO saveSourceProject(Long tenantId, FullSourceProjectDTO fullSourceProjectDTO) {
        SourceProject sourceProject = fullSourceProjectDTO.getSourceProject();
        Assert.notNull(sourceProject, "error.data_not_exists");
        if (((SourceProjectService)this.self()).checkSourceProjectChange(tenantId, sourceProject.getSourceProjectId(), 1) == 1) {
            sourceProject.setSourceProjectStatus("CHANGING");
        }
        //如果是询价单类型（入围转的）并且头编码没有值的情况下根据编码规则生成编码
        String srCode = codeRuleBuilder.generateCode(DetailsHelper.getUserDetails().getTenantId(),RcwlSourceConstants.RCWL_PJCODE,"GLOBAL","GLOBAL", null);
        sourceProject.setSourceProjectNum(srCode);
        sourceProject = ((SourceProjectService)this.self()).createOrUpdate(tenantId, sourceProject);

        //获取入围单号
        String shortlistNum = sourceProject.getAttributeVarchar1();
        //设置行上的头id和租户
        Long sourceProjectId = sourceProject.getSourceProjectId();
        List<ProjectLineItem> projectLineItems = fullSourceProjectDTO.getProjectLineItems();
        projectLineItems.forEach(projectLineItem -> {
            projectLineItem.setTenantId(tenantId);
            projectLineItem.setSourceProjectId(sourceProjectId);
        });
        List<ProjectLineSupplier> projectLineSuppliers = fullSourceProjectDTO.getProjectLineSuppliers();
        projectLineSuppliers.forEach(projectLineSupplier -> {
            projectLineSupplier.setTenantId(tenantId);
            projectLineSupplier.setSourceProjectId(sourceProjectId);
        });
        //判断供应商和物料是否传值[无值代表第一次保存，根据入围单号查找供应商和物料行，如果有值代表第二次保存则直接存入不做处理]
        if(projectLineItems == null){
            //通过入围单号查找物料行信息
            projectLineItems = rcwlSourceProjectMapper.selectItemByShortlistNum(tenantId,shortlistNum);
        }
        if(projectLineSuppliers == null){
            //通过入围单号查找供应商行信息
            projectLineSuppliers = rcwlSourceProjectMapper.selectSupplierByShortlistNum(tenantId,shortlistNum);
        }
        this.projectLineItemService.batchInsertAndUpdateItemsForFullSave(sourceProject, projectLineItems);
        this.projectLineSupplierService.batchInsertAndUpdateSuppliersForFullSave(sourceProject, projectLineSuppliers);
        this.projectLineSectionService.createOrUpdateProjectLineSections(tenantId, sourceProject.getSourceProjectId(), fullSourceProjectDTO.getProjectLineSections());
        this.projectLinePlanService.batchInsertAndUpdatePlansForFullSave(sourceProject, fullSourceProjectDTO.getProjectLinePlans());
        return fullSourceProjectDTO;
    }
}
