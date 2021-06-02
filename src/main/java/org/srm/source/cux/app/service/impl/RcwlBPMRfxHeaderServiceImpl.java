package org.srm.source.cux.app.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.app.service.RcwlBPMRfxHeaderService;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlRfxHeaderAttachmentListDataForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.cux.domain.repository.RcwlBPMRfxHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlClarifyRepository;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RcwlBPMRfxHeaderServiceImpl implements RcwlBPMRfxHeaderService {

    @Autowired
    private RCWLGxBpmInterfaceService rcwlGxBpmInterfaceService;
    @Autowired
    private RcwlBPMRfxHeaderRepository rcwlRfxHeaderRepository;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RcwlClarifyRepository rcwlClarifyRepository;
    //获取配置参数
    @Autowired
    private ProfileClient profileClient;

    @Override
    public ResponseData newClose(Long tenantId, Long rfxHeaderId, String remark) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage("操作成功！");
        responseData.setCode("200");
        List<RcwlAttachmentListData> listData = new ArrayList<RcwlAttachmentListData>();
        List<RcwlRfxHeaderAttachmentListDataForBPM> list = new ArrayList<RcwlRfxHeaderAttachmentListDataForBPM>();
        //方法区，获取调用BPM接口所需值DATA并填充
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
        String userName =rcwlRfxHeaderRepository.getRealNameById(rfxHeader.getCreatedBy());
        listData = rcwlRfxHeaderRepository.getAttachmentList(rfxHeader.getAttributeVarchar20());
        rcwlRfxHeaderRepository.updateRfxHeader(rfxHeader.getRfxHeaderId(), remark,tenantId);
        //方法区结束
        //data数据填充附件信息
        if(!CollectionUtils.isEmpty(listData)){
            int i = 1;
            for(RcwlAttachmentListData attachmentListData : listData){
                RcwlRfxHeaderAttachmentListDataForBPM rald = new RcwlRfxHeaderAttachmentListDataForBPM();
                rald.setFILENUMBER(Integer.toString(i));
                rald.setFILENAME(attachmentListData.getFileName());
                rald.setFILESIZE(attachmentListData.getFileSize());
                rald.setDESCRIPTION(attachmentListData.getFileName());
                rald.setURL(attachmentListData.getFileUrl());
                list.add(rald);
                i++;
            }
        }
        //固定获取BPM接口参数区-------------
        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");
        ResponsePayloadDTO responsePayloadDTO = new ResponsePayloadDTO();
        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        //设置传输值
        rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
        rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
        rcwlGxBpmStartDataDTO.setUserId(userName);
        rcwlGxBpmStartDataDTO.setBtid("RCWLSRMLBSP");
        rcwlGxBpmStartDataDTO.setBoid(rfxHeader.getRfxNum());
        rcwlGxBpmStartDataDTO.setProcinstId(rfxHeader.getAttributeVarchar6());
        rcwlGxBpmStartDataDTO.setData(this.rfxHeaderToString(rfxHeader,userName,list));
        //返回前台的跳转URL
        String rcwl_bpm_urlip = profileClient.getProfileValueByOptions("RCWL_BPM_URLIP");
        String rcwl_page_urlip = profileClient.getProfileValueByOptions("RCWL_LBSP_TO_PAGE_URL");
        String URL_BACK = "http://"+rcwl_bpm_urlip+rcwl_page_urlip+rfxHeader.getRfxNum();
        responseData.setUrl(URL_BACK);
        try{
            //调用bpm接口
            responsePayloadDTO = rcwlGxBpmInterfaceService. RcwlGxBpmInterfaceRequestData(rcwlGxBpmStartDataDTO);
        }catch (Exception e){
            responseData.setMessage("调用BPM接口失败！");
            responseData.setCode("201");
        }
        return responseData;
    }

    public String rfxHeaderToString(RfxHeader rfxHeader, String userName, List<RcwlRfxHeaderAttachmentListDataForBPM> list){
        //详情路径
        String URL_MX_HEADER = profileClient.getProfileValueByOptions("RCWL_LBSP_TO_BPM_URL");
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(URL_MX_HEADER).append(rfxHeader.getRfxHeaderId()).append("?").append("current=newInquiryHall");
        //甄云链接-澄清函详情URL（招采-招采-招采过程控制-蓝色单据编号）

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        StringBuffer sb = new StringBuffer();
        sb.append("{\"FSubject\":\"").append(rfxHeader.getCompanyName())
                .append("-").append(rfxHeader.getRfxTitle()).append("-").append(rfxHeader.getRfxNum()).append("\",");
        sb.append("\"COMPANYID\":\"").append(rfxHeader.getCompanyName()).append("\",");
        sb.append("\"RFXTITLE\":\"").append(rfxHeader.getRfxTitle()).append("\",");
        sb.append("\"RFXNUM\":\"").append(rfxHeader.getRfxNum()).append("\",");
        sb.append("\"BIDDINGMODE\":\"").append(rfxHeader.getAttributeVarchar8() == null ? "":rcwlClarifyRepository.getMeaningByLovCodeAndValue("SCUX.RCWL.SCEC.JH_BIDDING",rfxHeader.getAttributeVarchar8())).append("\",");
        sb.append("\"EVALMETHODNAME\":\"").append(rfxHeader.getScoreWay() == null ? "":rcwlClarifyRepository.getMeaningByLovCodeAndValue("SSRC.RCWL.BID_EVAL_METHOD",rfxHeader.getScoreWay())).append("\",");
        sb.append("\"SOURCECATEGORY\":\"").append(rfxHeader.getSourceCategory() == null ? "":rcwlClarifyRepository.getMeaningByLovCodeAndValue("SSRC.SOURCE_CATEGORY",rfxHeader.getSourceCategory())).append("\",");
        sb.append("\"TERMINATEDBY\":\"").append(userName).append("\",");
        sb.append("\"TERMINATEDDATE\":\"").append(df.format(new Date())).append("\",");
        sb.append("\"TERMINATEDREMARK\":\"").append(rfxHeader.getTerminatedRemark() == null ? "":rfxHeader.getTerminatedRemark()).append("\",");
        sb.append("\"URL_MX\":\"").append(sbUrl.toString()).append("\",");
        sb.append("\"ATTACHMENTS1\":[");
        if(!CollectionUtils.isEmpty(list) && list.size()>0){
            for(RcwlRfxHeaderAttachmentListDataForBPM ra : list){
                sb.append(ra.toString()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("]}");
        return  sb.toString();
    }

    @Override
    public ResponseData updateRfxHeaderData(RcwlUpdateRfxHeaderDataVO rcwlUpdateDataDTO) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage("数据更新成功！");
        responseData.setCode("200");
        try{
            rcwlRfxHeaderRepository.updateRfxHeaderData(rcwlUpdateDataDTO);
        }catch (Exception e){
            responseData.setMessage("数据更新失败！");
            responseData.setCode("201");
        }
        return responseData;
    }

    @Override
    public Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId) {
        Long l = rcwlRfxHeaderRepository.getRfxHeaderIdByRfxNum(rfxNum,tenantId);
        return l == null ? 0l:l;
    }
}
