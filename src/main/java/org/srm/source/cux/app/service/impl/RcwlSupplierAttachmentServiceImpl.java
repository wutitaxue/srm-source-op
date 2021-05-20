package org.srm.source.cux.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.app.service.RcwlSupplierAttachmentService;
import org.springframework.stereotype.Service;
import org.srm.source.cux.domain.entity.RcwlSupplierAttachment;
import org.srm.source.cux.domain.repository.RcwlShortlistHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlSupplierAttachmentRepository;
import org.srm.source.share.api.dto.User;

import java.util.List;

/**
 * 入围供应商单附件应用服务默认实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Service
public class RcwlSupplierAttachmentServiceImpl implements RcwlSupplierAttachmentService {

    @Autowired
    private RcwlSupplierAttachmentRepository rcwlSupplierAttachmentRepository;
    @Autowired
    private RcwlShortlistHeaderRepository rcwlShortlistHeaderRepository;

    @Override
    public List<RcwlSupplierAttachment> createAndUpdate(List<RcwlSupplierAttachment> rcwlSupplierAttachments) {
        if(CollectionUtils.isNotEmpty(rcwlSupplierAttachments)){
            for (RcwlSupplierAttachment rcwlSupplierAttachment : rcwlSupplierAttachments) {
                Long rcwlSupplierAttachmentId = rcwlSupplierAttachment.getRcwlSupplierAttachmentId();
                if(rcwlSupplierAttachmentId == null){
                    rcwlSupplierAttachmentRepository.insertSelective(rcwlSupplierAttachment);
                } else {
                    rcwlSupplierAttachmentRepository.updateByPrimaryKey(rcwlSupplierAttachment);
                }
                String uploadUserName = rcwlSupplierAttachment.getUploadUserName();
                //用于前端显示
                if (StringUtils.isNotEmpty(uploadUserName)) {
                    User user = rcwlShortlistHeaderRepository.selectUserInfoById(rcwlSupplierAttachment.getUploadUserId());
                    rcwlSupplierAttachment.setUploadUserName(user.getRealName());
                }
            }
        }
        return rcwlSupplierAttachments;
    }
}
