package org.srm.source.cux.rfx.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.cux.rfx.infra.constant.RcwlMessageCode;
import org.srm.source.cux.rfx.infra.mapper.RcwlOpenCheckMapper;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.impl.SourceOpenPasswordServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxMember;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxMemberRepository;
import org.srm.source.rfx.domain.service.IRfxActionDomainService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.constant.Constants;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.EvaluateExpertRepository;
import org.srm.source.share.domain.repository.SourceTemplateRepository;
import org.srm.web.annotation.Tenant;

import java.util.List;


@Service
@Tenant("SRM-RCWL")
public class RcwlSourceOpenPasswordServiceImpl extends SourceOpenPasswordServiceImpl {

    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private SourceTemplateRepository sourceTemplateRepository;
    @Autowired
    private EvaluateExpertRepository evaluateExpertRepository;
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private RfxMemberRepository rfxMemberRepository;
    @Autowired
    private IRfxActionDomainService rfxActionDomainService;
    @Autowired
    private RcwlOpenCheckMapper rcwlOpenCheckMapper;


    @Override
    public void openRfx(RfxMember rfxMember) {
        RfxHeader rfxHeader = (RfxHeader) this.rfxHeaderRepository.selectByPrimaryKey(rfxMember.getRfxHeaderId());
        SourceTemplate sourceTemplate = (SourceTemplate) this.sourceTemplateRepository.selectByPrimaryKey(rfxHeader.getTemplateId());
        //校验澄清答疑全部解决才能开标
        Long checkFlag = rcwlOpenCheckMapper.selectOpenCheck(rfxHeader.getTenantId(),rfxHeader.getRfxHeaderId());
        if (checkFlag > 0) {
            throw new CommonException(RcwlMessageCode.RCWL_OPEN_ERROR, new Object[0]);
        }
        if (BaseConstants.Flag.YES.equals(sourceTemplate.getScoreIndicFlag())) {
            RfxFullHeader rfxFullHeader = new RfxFullHeader();
            List<EvaluateExpert> evaluateExperts = this.evaluateExpertRepository.select(new EvaluateExpert(rfxHeader.getRfxHeaderId(), "RFX"));
            if (CollectionUtils.isNotEmpty(evaluateExperts)) {
                BiddingWorkDTO biddingWorkDTO = new BiddingWorkDTO();
                biddingWorkDTO.setEvaluateExpertList(evaluateExperts);
                rfxFullHeader.setEvaluateExperts(biddingWorkDTO);
            }

            rfxFullHeader.setOpenSourceFlag(BaseConstants.Flag.YES);
            this.rfxHeaderService.checkExpertScore(sourceTemplate, rfxHeader, rfxFullHeader);
        }

        int count = this.rfxMemberRepository.selectCountByCondition(Condition.builder(RfxMember.class).andWhere(Sqls.custom().andEqualTo("rfxHeaderId", rfxMember.getRfxHeaderId()).andEqualTo("rfxRole", "OPENED_BY").andEqualTo("openedFlag", Constants.DefaultFlagValue.DEFAULT_OPENED_FLAG).andNotEqualTo("userId", DetailsHelper.getUserDetails().getUserId())).build());
        this.rfxActionDomainService.insertAction(rfxHeader, "OPEN", (String) null);
        if (count == 0) {
            rfxHeader.setRfxStatus("OPENED");
            CustomizeHelper.ignore(() -> {
                return this.rfxHeaderRepository.updateOptional(rfxHeader, new String[]{"rfxStatus"});
            });
        }

        rfxMember.setOpenedFlag(Constants.DefaultFlagValue.OPENED_FLAG);
        this.rfxMemberRepository.updateOptional(rfxMember, new String[]{"openedFlag"});
    }
}
