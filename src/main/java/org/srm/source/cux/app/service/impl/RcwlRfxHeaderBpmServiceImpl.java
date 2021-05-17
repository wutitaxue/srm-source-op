package org.srm.source.cux.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.app.service.RcwlRfxHeaderBpmService;
import org.srm.source.cux.domain.vo.RcwlSendBpmData;
import org.srm.source.cux.infra.constant.RcwlMessageCode;
import org.srm.source.cux.infra.mapper.RcwlRfxHeaderBpmMapper;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.vo.RfxFullHeader;
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

    @Override
    public String rcwlReleaseRfx(Long organizationId, RfxFullHeader rfxFullHeader) {
        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");

        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        RfxHeader rfxHeader = rfxFullHeader.getRfxHeader();
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
        String RCWL_SRM_URL = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_SRM_URL");
        RCWL_SRM_URL = RCWL_SRM_URL + "app/ssrc/new-inquiry-hall/rfx-detail/"+ rfxFullHeader.getRfxHeader().getRfxHeaderId() +"?current=newInquiryHall";
        rcwlSendBpmData.setUrlMx(RCWL_SRM_URL);
        //设置行参数
        rcwlSendBpmData.setRcwlScoringTeamDataList(rfxHeaderBpmMapper.prepareScoringTeamData(organizationId, rfxHeader));
        rcwlSendBpmData.setRcwlDetailDataList(rfxHeaderBpmMapper.prepareDetailData(organizationId, rfxHeader));
        rcwlSendBpmData.setRcwlAttachmentDataList(rfxHeaderBpmMapper.prepareAttachmentData(organizationId, rfxHeader));
        rcwlSendBpmData.setRcwlSupplierDataList(rfxHeaderBpmMapper.prepareSupplierData(organizationId, rfxHeader));

        String data = JSONObject.toJSONString(rcwlSendBpmData);
        rcwlGxBpmStartDataDTO.setData(data);

        //调用bpm接口
        try {
            rcwlGxBpmInterfaceService.RcwlGxBpmInterfaceRequestData(rcwlGxBpmStartDataDTO);
        } catch (IOException e) {
//            e.printStackTrace();
            throw new CommonException(RcwlMessageCode.RCWL_BPM_ITF_ERROR);
        }

        return null;
    }
}
