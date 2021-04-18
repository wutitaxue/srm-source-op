package org.srm.source.cux.app.service.impl;

import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.app.service.RcwlShortlistAttachmentService;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlShortlistAttachment;
import org.srm.source.cux.domain.repository.RcwlShortlistAttachmentRepository;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierAttachmentRepository;
import org.srm.source.cux.infra.mapper.RcwlShortlistAttachmentMapper;
import org.srm.source.share.api.dto.User;

import java.util.List;

/**
 * 入围单附件模版应用服务默认实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Service
public class RcwlShortlistAttachmentServiceImpl implements RcwlShortlistAttachmentService {

    @Autowired
    private RcwlShortlistAttachmentRepository rcwlShortlistAttachmentRepository;

    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @Autowired
    private RcwlSupplierAttachmentRepository rcwlSupplierAttachmentRepository;

    @Override
    public List<RcwlShortlistAttachment> createShortlistAttachment(List<RcwlShortlistAttachment> rcwlShortlistAttachments) {
        if (CollectionUtils.isNotEmpty(rcwlShortlistAttachments)) {
            rcwlShortlistAttachments.forEach(rcwlShortlistAttachment -> {
                Long rcwlShortlistAttachmentId = rcwlShortlistAttachment.getRcwlShortlistAttachmentId();
                rcwlShortlistAttachment.setUploadUserId(DetailsHelper.getUserDetails().getUserId());
                if (rcwlShortlistAttachmentId == null) {
                    rcwlShortlistAttachmentRepository.insertSelective(rcwlShortlistAttachment);
                } else {
                    rcwlShortlistAttachmentRepository.updateByPrimaryKeySelective(rcwlShortlistAttachment);
                }
                String uploadUserName = rcwlShortlistAttachment.getUploadUserName();
                //用于前端显示
                if (StringUtils.isNotEmpty(uploadUserName)) {
                    User user = rcwlShortlistHeaderRepository.selectUserInfoById(rcwlShortlistAttachment.getUploadUserId());
                    rcwlShortlistAttachment.setUploadUserName(user.getRealName());
                }
            });
        }
        return rcwlShortlistAttachments;
    }

    @Override
    public void deleteRcwlShortlistAttachment(List<RcwlShortlistAttachment> rcwlShortlistAttachments) {

        if(CollectionUtils.isNotEmpty(rcwlShortlistAttachments)){
            for (RcwlShortlistAttachment rcwlShortlistAttachment : rcwlShortlistAttachments) {
                int i = rcwlShortlistAttachmentRepository.deleteByPrimaryKey(rcwlShortlistAttachment);
                if(i > 0){
                    //关联删除供应商下的附件信息
                    rcwlSupplierAttachmentRepository.deleteByShortlistAttachmentId(rcwlShortlistAttachment.getRcwlShortlistAttachmentId());
                    //TODO 删除附件，应该前端可以调用标准方法
                }
            }
        }

    }
}
