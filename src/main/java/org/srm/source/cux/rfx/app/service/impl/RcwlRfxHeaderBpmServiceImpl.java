package org.srm.source.cux.rfx.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.source.bid.api.dto.BiddingWorkDTO;
import org.srm.source.cux.rfx.app.service.RcwlRfxHeaderBpmService;
import org.srm.source.cux.rfx.domain.vo.RcwlSendBpmData;
import org.srm.source.cux.rfx.infra.constant.RcwlMessageCode;
import org.srm.source.cux.rfx.infra.mapper.RcwlRfxHeaderBpmMapper;
import org.srm.source.cux.share.infra.constant.SourceBaseConstant;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.v2.RfxHeaderServiceV2;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.service.IRfxHeaderDomainService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.rfx.infra.util.RfxEventUtil;
import org.srm.source.share.app.service.SourceTemplateService;
import org.srm.source.share.domain.entity.EvaluateExpert;
import org.srm.source.share.domain.entity.PrequalHeader;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.service.IPrequelDomainService;
import org.srm.web.annotation.Tenant;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Service
@Tenant("SRM-RCWL")
public class RcwlRfxHeaderBpmServiceImpl implements RcwlRfxHeaderBpmService {


    @Autowired
    private RCWLGxBpmInterfaceService rcwlGxBpmInterfaceService;
    //获取配置参数
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private RcwlRfxHeaderBpmMapper rfxHeaderBpmMapper;
    @Autowired
    private SourceTemplateService sourceTemplateService;
    @Autowired
    private IRfxHeaderDomainService rfxHeaderDomainService;
    @Autowired
    @Lazy
    private IPrequelDomainService prequelDomainService;
    @Autowired
    private RfxHeaderService rfxHeaderServiceV1;
    @Autowired
    private RfxHeaderServiceV2 rfxHeaderServiceV2;
    @Autowired
    private RfxEventUtil rfxEventUtil;

    @Override
    public String rcwlReleaseRfx(Long organizationId, RfxFullHeader rfxFullHeader) {
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        Assert.notNull(rfxHeader.getRfxHeaderId(), "header.not.presence");
        SourceTemplate sourceTemplate = this.sourceTemplateService.selectByPrimaryKey(rfxHeader.getTemplateId());
        Assert.notNull(sourceTemplate, "source.template.not.found");
        rfxHeader.beforeReleaseCheck(rfxFullHeader, sourceTemplate);
        rfxHeader.initRfxReleaseInfo(sourceTemplate.getReleaseApproveType());
        rfxHeader.setRfxStatus("NEW");
        rfxHeader.initTotalCoast(rfxFullHeader.getRfxLineItemList());
        RfxFullHeader rtnFullHeader = rfxHeaderServiceV2.saveOrUpdateFullHeader(rfxFullHeader);
        if(checkStatus(rtnFullHeader)){
            throw new CommonException(RcwlMessageCode.RCWL_SUBMIT_ERROR);
        }
        this.rfxHeaderDomainService.validRfxHeaderBeforeSave(rfxHeader, sourceTemplate);
        this.rfxHeaderDomainService.validateLineItemTaxRate(rfxFullHeader.getRfxHeader());
        if (BaseConstants.Flag.NO.equals(sourceTemplate.getScoreIndicFlag())) {
            this.rfxHeaderServiceV1.checkExpertScore(sourceTemplate, rfxHeader, rfxFullHeader);
        }
        this.rfxHeaderServiceV1.validateLadderInquiry(rtnFullHeader);
        this.rfxHeaderDomainService.removeOrValidRfxItemSupAssign(rfxFullHeader);
        PrequalHeader prequalHeader = rfxFullHeader.getPrequalHeader();
        if (null != prequalHeader) {
            prequalHeader.preData(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX");
        }
        if ("PRE".equals(sourceTemplate.getQualificationType()) || "PRE_POST".equals(sourceTemplate.getQualificationType())) {
            Assert.notNull(rfxHeader.getQuotationStartDate(), "error.quotation_start_time_not_found");
            prequalHeader.checkBeforeUpdate(Objects.isNull(rfxHeader.getQuotationStartDate()) ? rfxHeader.getEstimatedStartTime() : rfxHeader.getQuotationStartDate());
        }
        this.prequelDomainService.checkPrequalHeader(sourceTemplate, rfxFullHeader.getPrequalHeader());
        if (!"SELF".equals(sourceTemplate.getReleaseApproveType())) {
            this.rfxEventUtil.eventSend("SSRC_RFX_RELEASE", "RELEASE", rfxHeader);
        }
        if (!"SELF".equals(sourceTemplate.getReleaseApproveType())) {
            //获取系统配置
            String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
            String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");

            //发送系统标识
            rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
            //接收系统标识
            rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
            //子账户账号
            rcwlGxBpmStartDataDTO.setUserId(DetailsHelper.getUserDetails().getUsername());
            //业务单据ID（业务类型）
            rcwlGxBpmStartDataDTO.setBtid("RCWLSRMZBLX");
            //单据编号
            rcwlGxBpmStartDataDTO.setBoid(rfxFullHeader.getRfxHeader().getRfxNum());
            //流程id：默认0，如果是退回修改的流程，则需要将流程ID回传回来
            rcwlGxBpmStartDataDTO.setProcinstId("0");
            //业务数据
            RcwlSendBpmData rcwlSendBpmData = rfxHeaderBpmMapper.prepareDate(organizationId, rfxHeader);
            //设置头URL_MX
            String URL = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_SRM_URL");
            String URL2 = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_URLIP");
            String RCWL_SRM_URL = URL + "app/ssrc/new-inquiry-hall/rfx-detail/" + rfxFullHeader.getRfxHeader().getRfxHeaderId() + "?current=newInquiryHall";
            rcwlSendBpmData.setUrlMx(RCWL_SRM_URL);
            //设置行参数
            rcwlSendBpmData.setRcwlScoringTeamDataList(rfxHeaderBpmMapper.prepareScoringTeamData(organizationId, rfxHeader));
            rcwlSendBpmData.setRcwlDetailDataList(rfxHeaderBpmMapper.prepareDetailData(organizationId, rfxHeader));
            rcwlSendBpmData.setRcwlSupplierDataList(rfxHeaderBpmMapper.prepareSupplierData(organizationId, rfxHeader));
            rcwlSendBpmData.setRcwlAttachmentDataList(rfxHeaderBpmMapper.prepareAttachmentData(organizationId, rfxHeader));

            String data = JSONObject.toJSONString(rcwlSendBpmData);
            rcwlGxBpmStartDataDTO.setData(data);

            //调用bpm接口
            try {
                rcwlGxBpmInterfaceService.RcwlGxBpmInterfaceRequestData(rcwlGxBpmStartDataDTO);
            } catch (IOException e) {
//            e.printStackTrace();
                throw new CommonException(RcwlMessageCode.RCWL_BPM_ITF_ERROR);
            }
            return "http://" + URL2 + "/Workflow/MTStart2.aspx?BSID=WLCGGXPT&BTID=RCWLSRMZBLX&BOID=" + rfxHeader.getRfxNum();
        } else {
            rfxHeaderServiceV1.chooseReleaseApproveType(organizationId, rfxHeader.getRfxHeaderId(), sourceTemplate);
            return "";
        }
    }


    /**
     * 招采工作台点击发布时增加校验
     * @param rfxFullHeader
     * @return
     */
    public Boolean checkStatus(RfxFullHeader rfxFullHeader){
        boolean flag = false;
        boolean BusinessTechnologyFlag = true;
        boolean BusinessFlag = true;
        boolean TechnologyFlag = true;
        //寻源类别为“招标”
        if(StringUtils.equals(rfxFullHeader.getRfxHeader().getSourceCategory(), SourceBaseConstant.SourceCategory.RFQ)){
            //评标方法为“综合评分法”
            if(StringUtils.equals(rfxFullHeader.getRfxHeader().getAttributeVarchar17(), SourceBaseConstant.BidEvalMethod.COMPREHENSIVE_SCORE)){
                //商务技术组、商务组、技术组都至少需要维护一位成员
                flag = true;
                List<EvaluateExpert> evaluateExperts = rfxFullHeader.getEvaluateExperts().getEvaluateExpertList();
                for(EvaluateExpert evaluateExpert : evaluateExperts){
                    if(StringUtils.equals(evaluateExpert.getTeamMeaning(), SourceBaseConstant.TeamMeaning.BUSINESS_TECHNOLOGY_GROUP)){
                        BusinessTechnologyFlag = false;
                    } else if(StringUtils.equals(evaluateExpert.getTeamMeaning(), SourceBaseConstant.TeamMeaning.BUSINESS_GROUP)){
                        BusinessFlag = false;
                    } else if(StringUtils.equals(evaluateExpert.getTeamMeaning(), SourceBaseConstant.TeamMeaning.TECHNOLOGY_GROUP)){
                        TechnologyFlag = false;
                    }
                }
            }
        }
        return (BusinessTechnologyFlag || BusinessFlag || TechnologyFlag) && flag;
    }
}
