package org.srm.source.cux.shortlist.api.dto;

import org.hzero.core.algorithm.tree.Child;


/**
 * @author lu.cheng01@hand-china.com
 * @description
 * @date 2021/5/28 15:33
 * @version:1.0
 */
public class RcwlSampleInfoItemDTO extends Child<RcwlSampleInfoItemDTO> {
    private Long categoryId;
    private Long tenantId;
    private String categoryCode;
    private String categoryName;
    private Long parentCategoryId;
    private Long enabledFlag;
    private Long templateEnabledFlag;
    private String levelPath;
    private String impStandard;
    private Long objectVersionNumber;
    private String sourceCode;
    private String externalSystemCode;
    private String hasChild;

    public RcwlSampleInfoItemDTO() {
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCategoryCode() {
        return this.categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getParentCategoryId() {
        return this.parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Long getEnabledFlag() {
        return this.enabledFlag;
    }

    public void setEnabledFlag(Long enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Long getTemplateEnabledFlag() {
        return this.templateEnabledFlag;
    }

    public void setTemplateEnabledFlag(Long templateEnabledFlag) {
        this.templateEnabledFlag = templateEnabledFlag;
    }

    public String getLevelPath() {
        return this.levelPath;
    }

    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath;
    }

    public String getImpStandard() {
        return this.impStandard;
    }

    public void setImpStandard(String impStandard) {
        this.impStandard = impStandard;
    }

    @Override
    public Long getObjectVersionNumber() {
        return this.objectVersionNumber;
    }

    @Override
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getSourceCode() {
        return this.sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getExternalSystemCode() {
        return this.externalSystemCode;
    }

    public void setExternalSystemCode(String externalSystemCode) {
        this.externalSystemCode = externalSystemCode;
    }

    public String getHasChild() {
        return this.hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

//    public static List<RcwlSampleInfoItemDTO> buildTree(List<RcwlSampleInfoItemDTO> itemCategoryDTOList) {
//        return TreeBuilder.buildTree(itemCategoryDTOList, new Node<Long, RcwlSampleInfoItemDTO>() {
//            @Override
//            public Long getKey(RcwlSampleInfoItemDTO obj) {
//                return obj.getCategoryId();
//            }
//
//            @Override
//            public Long getParentKey(RcwlSampleInfoItemDTO obj) {
//                return obj.getParentCategoryId();
//            }
//        });
//    }
}
