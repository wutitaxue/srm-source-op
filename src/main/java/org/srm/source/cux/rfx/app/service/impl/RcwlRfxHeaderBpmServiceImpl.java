package org.srm.source.cux.rfx.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.srm.source.cux.rfx.app.service.RcwlRfxHeaderBpmService;
import org.srm.source.cux.rfx.domain.vo.RcwlSendBpmData;
import org.srm.source.cux.rfx.infra.constant.RcwlMessageCode;
import org.srm.source.cux.rfx.infra.mapper.RcwlRfxHeaderBpmMapper;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.service.IRfxHeaderDomainService;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
import org.srm.source.share.app.service.SourceTemplateService;
import org.srm.source.share.domain.entity.PrequalHeader;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.service.IPrequelDomainService;
import org.srm.web.annotation.Tenant;

import java.io.IOException;


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
    private RfxHeaderService rfxHeaderService;

    @Override
    public String rcwlReleaseRfx(Long organizationId, RfxFullHeader rfxFullHeader) {
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        //原校验逻辑
        Assert.notNull(rfxHeader.getRfxHeaderId(), "header.not.presence");
        SourceTemplate sourceTemplate = this.sourceTemplateService.selectByPrimaryKey(rfxHeader.getTemplateId());
        Assert.notNull(sourceTemplate, "source.template.not.found");
        rfxHeader.beforeReleaseCheck(rfxFullHeader, sourceTemplate);
        rfxHeader.initRfxReleaseInfo(sourceTemplate.getReleaseApproveType());
        rfxHeader.initTotalCoast(rfxFullHeader.getRfxLineItemList());
//        RfxFullHeader rtnFullHeader = rfxHeaderService.saveOrUpdateFullHeader(rfxFullHeader);
        this.rfxHeaderDomainService.validateLineItemTaxRate(rfxFullHeader.getRfxHeader());
        if (BaseConstants.Flag.NO.equals(sourceTemplate.getScoreIndicFlag())) {
            rfxHeaderService.checkExpertScore(sourceTemplate, rfxHeader, rfxFullHeader);
        }
        rfxHeaderService.validateLadderInquiry(rfxFullHeader);
        this.rfxHeaderDomainService.removeOrValidRfxItemSupAssign(rfxFullHeader);
        PrequalHeader prequalHeader = rfxFullHeader.getPrequalHeader();
        if (null != prequalHeader) {
            prequalHeader.preData(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId(), "RFX");
        }
        this.prequelDomainService.checkPrequalHeader(sourceTemplate, rfxFullHeader.getPrequalHeader());



        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");

        //发送系统标识
        rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
        //接收系统标识
        rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
        //子账户账号
        rcwlGxBpmStartDataDTO.setUserId(DetailsHelper.getUserDetails().getUsername());
        //业务单据ID（业务类型）
        rcwlGxBpmStartDataDTO.setBtid("RCWLSRMYSDSP");
        //单据编号
        rcwlGxBpmStartDataDTO.setBoid(rfxFullHeader.getRfxHeader().getRfxNum());
        //流程id：默认0，如果是退回修改的流程，则需要将流程ID回传回来
        rcwlGxBpmStartDataDTO.setProcinstId("0");
        //业务数据
        RcwlSendBpmData rcwlSendBpmData = rfxHeaderBpmMapper.prepareDate(organizationId, rfxHeader);
        //设置头URL_MX
        String URL = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_SRM_URL");
        String RCWL_SRM_URL = URL + "app/ssrc/new-inquiry-hall/rfx-detail/"+ rfxFullHeader.getRfxHeader().getRfxHeaderId() +"?current=newInquiryHall";
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

        return URL+"Workflow/MTStart2.aspx?BSID=WLCGGXPT&BTID=RCWLSRMZBLX&BOID="+rfxHeader.getRfxNum();
    }
}
