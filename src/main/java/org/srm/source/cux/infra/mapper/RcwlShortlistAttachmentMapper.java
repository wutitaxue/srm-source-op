package org.srm.source.cux.infra.mapper;

import io.choerodon.core.domain.Page;
import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 入围单附件模版Mapper
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistAttachmentMapper extends BaseMapper<RcwlShortlistAttachment> {

    /**
     * 查询入围单附件模版信息
     *
     * @param rcwlShortlistAttachment 查询条件
     * @return Page<RcwlShortlistAttachment>
     */
    Page<RcwlShortlistAttachment> selectShortlistAttachment(RcwlShortlistAttachment rcwlShortlistAttachment);
}
