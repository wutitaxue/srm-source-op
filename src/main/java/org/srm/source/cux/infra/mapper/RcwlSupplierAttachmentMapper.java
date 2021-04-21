package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 入围供应商单附件Mapper
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierAttachmentMapper extends BaseMapper<RcwlSupplierAttachment> {

    /**
     * 查询供应商附件信息
     *
     * @param rcwlSupplierAttachment 查询条件
     * @return Page<RcwlSupplierAttachment>
     */
    Page<RcwlSupplierAttachment> selectByRcwlSupplierAttachment(RcwlSupplierAttachment rcwlSupplierAttachment);

    /**
     * 根据入围单附件模本删除供应商对应附件信息
     *
     * @param rcwlShortlistAttachmentId 模本ID
     * @return int
     */
    int deleteByShortlistAttachmentId(@Param("rcwlShortlistAttachmentId") Long rcwlShortlistAttachmentId);
}
