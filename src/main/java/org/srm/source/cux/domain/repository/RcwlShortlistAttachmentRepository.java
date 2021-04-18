package org.srm.source.cux.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;

import java.util.List;

/**
 * 入围单附件模版资源库
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlShortlistAttachmentRepository extends BaseRepository<RcwlShortlistAttachment> {

    /**
     * 查询入围单附件模版信息
     *
     * @param pageRequest             分页
     * @param rcwlShortlistAttachment 查询条件
     * @return Page<RcwlShortlistAttachment>
     */
    Page<RcwlShortlistAttachment> pageAndSortShortlistAttachment(PageRequest pageRequest, RcwlShortlistAttachment rcwlShortlistAttachment);


}
