package org.srm.source.cux.share.infra.mapper;

import org.springframework.stereotype.Component;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.infra.mapper.SourceTemplateMapper;
import org.srm.web.annotation.Tenant;
import org.srm.web.dynamic.ExtendMapper;

import java.util.List;

/**
 * @description:
 * @author:yuanping.zhang
 * @createTime:2021/5/17 15:38
 * @version:1.0
 */
@Tenant("SRM-RCWL")
public interface RCWLSourceTemplateMapper extends SourceTemplateMapper, ExtendMapper<SourceTemplate> {
    /**
     * 融创RFQ拆分修改
     * @param sourceTemplate
     * @return
     */
    @Override
    List<SourceTemplate> listLatestSourceTemplate(SourceTemplate sourceTemplate);
}
