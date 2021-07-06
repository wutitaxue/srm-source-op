package org.srm.source.cux.rfx.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.EncryptionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.cux.rfx.infra.constant.RcwlMessageCode;
import org.srm.source.cux.rfx.infra.mapper.RcwlOpenCheckMapper;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.impl.SourceOpenPasswordServiceImpl;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxMember;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxMemberRepository;
import org.srm.source.rfx.domain.repository.RfxQuotationHeaderRepository;
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
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;


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

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void checkOpenPassword(Long rfxHeaderId, String password) {
        //校验开标密码前，先校验供应商有效投标方数量
         RfxHeader rfxHeader = this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
         //查询有效供应商报价数量
      List<RfxQuotationHeader> quotationHeaderList =  this.rfxQuotationHeaderRepository.selectByCondition(Condition.builder(RfxQuotationHeader.class).andWhere(Sqls.custom().andEqualTo(RfxQuotationHeader.FIELD_RFX_HEADER_ID,rfxHeaderId)).build());
        Long quotedCount =Long.valueOf(quotationHeaderList.size());
         Long count1 = rfxHeader.getAttributeBigint2()*2+1;
         Long count2 = rfxHeader.getAttributeBigint2()*2;

         if("RFQ".equals(rfxHeader.getSourceCategory()))
         {
            if(quotedCount<count1){
                throw new CommonException("有效投标供方数量不得小于2N+1，N为中标供应商数量");
            }
         }
         if("RCZB".equals(rfxHeader.getSourceCategory())){
             if(quotedCount<count2){
                 throw new CommonException("有效投标供方数量不得小于2N，N为中标供应商数量");
             }
         }

        RfxMember query = new RfxMember();
        query.setRfxHeaderId(rfxHeaderId);
        query.setUserId(DetailsHelper.getUserDetails().getUserId());
        query.setRfxRole("OPENED_BY");
        RfxMember rfxMember = (RfxMember)this.rfxMemberRepository.selectOne(query);
        if (null == rfxMember) {
            throw new CommonException("user.is.not.opener.error", new Object[0]);
        } else if (Constants.DefaultFlagValue.OPENED_FLAG.equals(rfxMember.getOpenedFlag())) {
            throw new CommonException("user.has.opened.error", new Object[0]);
        } else {
            if (Constants.DefaultFlagValue.PASSWORD_FLAG.equals(rfxMember.getPasswordFlag())) {
                String key = "bVWuT5y2m4zrnucpshiIiA==";
                String secretKey = EncryptionUtils.AES.decrypt(rfxMember.getOpenPassword(), key);
                if (!StringUtils.isNotBlank(secretKey) || !StringUtils.isNotBlank(password) || !secretKey.equals(password)) {
                    throw new CommonException("error.wrong_open_pw", new Object[0]);
                }

                this.openRfx(rfxMember);
            } else {
                this.openRfx(rfxMember);
            }

        }
    }
}
