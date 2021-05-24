package org.srm.source.cux.app.service.impl;

import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.*;
import org.srm.source.cux.domain.repository.RcwlClarifyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RcwlClarifyServiceImpl implements RcwlClarifyService {
    @Autowired
    private RCWLGxBpmInterfaceService rcwlGxBpmInterfaceService;
    @Autowired
    private RcwlClarifyRepository rcwlClarifyRepository;
    //获取配置参数
    @Autowired
    private ProfileClient profileClient;

    @Override
    public ResponseData releaseClarifyByBPM(RcwlClarifyForBPM clarify) {

        String username = DetailsHelper.getUserDetails().getUsername();
        ResponseData responseData = new ResponseData();
        RcwlDataForBPM rcwlDataForBPM = new RcwlDataForBPM();
        responseData.setMessage("操作成功！");
        responseData.setCode("200");
        //固定获取BPM接口参数区-------------
        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");
        ResponsePayloadDTO responsePayloadDTO = new ResponsePayloadDTO();
        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        //方法区，获取调用BPM接口所需值DATA并填充
            //根据寻源Id查找rfx_num，rfx_title和round_number以“+”拼接
        String dataBySourceId = rcwlClarifyRepository.getSourceNumAndNameAndClarifyNumberById(clarify.getSourceId());
            //查询此表相同source_id的refer_flag（是否引用问题）值为1的数量
        String countOfAlikeSourceId = rcwlClarifyRepository.getCountOfAlikeSourceId(clarify.getSourceId(),1l);
            //附件list
        List<String> attachmentList = rcwlClarifyRepository.getAttachmentList(clarify.getAttachmentUuid());
        //方法区结束
        rcwlDataForBPM.setFILE(clarify.getReferFlag() == 0l ? "澄清":"答疑");//值为1传描述“答疑” 值为0传描述“ 澄清”
        //立项编号不确定----------
        rcwlDataForBPM.setFSUBJECT(rcwlDataForBPM.getTITLE() + clarify.getTitle() + dataBySourceId == "" ? "":dataBySourceId.split("\\+")[0]);//答疑+标题+立项编号
        rcwlDataForBPM.setSUBMITTEDBY(clarify.getCreatedBy() == null ? "":clarify.getCreatedBy().toString());//CREATED_BY
        rcwlDataForBPM.setTITLE(clarify.getTitle());
        rcwlDataForBPM.setSOURCENUM(dataBySourceId == "" ? "":dataBySourceId.split("\\+")[0]);//用source_id去去ssrc_rfx_header匹配rfx_header_id找到对应rfx_num
        rcwlDataForBPM.setSOURCENAME(dataBySourceId == "" ? "":dataBySourceId.split("\\+")[1]);//用source_id去去ssrc_rfx_header匹配rfx_header_id找到对应rfx_title
        rcwlDataForBPM.setCLARIFYNUM(clarify.getClarifyNum());//clarify_num
        rcwlDataForBPM.setCLARIFYNUMBER(countOfAlikeSourceId);//查询此表相同source_id的refer_flag（是否引用问题）值为1的数量
//        rcwlDataForBPM.setROUNDNUMBER(dataBySourceId == "" ? "":dataBySourceId.split("\\+")[2]);//用source_id去去ssrc_rfx_header匹配rfx_header_id找到对应round_number
        rcwlDataForBPM.setROUNDNUMBER(rcwlClarifyRepository.getRoundNumber(clarify.getSourceId()));//用source_id去去ssrc_rfx_header匹配rfx_header_id找到对应round_number
        rcwlDataForBPM.setCONTEXT(clarify.getContext());//context
        //https://pssc-dev.sunacctg.com/app/ssrc/new-inquiry-hall/clarify-detail/       URL_MX_HEADER
        //32/RFX2021051200002/刘婷专用单002/1/4?current=newInquiryHall&createFlag=1
        String URL_MX_HEADER = profileClient.getProfileValueByOptions("RCWL_CLARIFY_TO_BPM_URL");
        StringBuilder sb = new StringBuilder();
        sb.append(URL_MX_HEADER).append(clarify.getSourceId()).append("/").append(dataBySourceId == "" ? "":dataBySourceId.split("\\+")[1])
                .append("/1/").append("1?").append("current=newInquiryHall&createFlag=1");
        rcwlDataForBPM.setURL_MX(URL_MX_HEADER+sb.toString());//甄云链接-澄清函详情URL（路径招采工作台-进行中-操作-更多-澄清答疑-澄清函维护-澄清单号）
        //data数据填充附件信息
        List<RcwlAttachmentListDataForBPM> dataList = new ArrayList<RcwlAttachmentListDataForBPM>();
        if(!CollectionUtils.isEmpty(attachmentList)){
                int i = 1;
            for(String attachmentListData : attachmentList){
                RcwlAttachmentListDataForBPM rald = new RcwlAttachmentListDataForBPM();
                rald.setFILENUMBER(Integer.toString(i));
                rald.setFILENAME(attachmentListData.split("|\\+|")[0]);
                rald.setFILESIZE(attachmentListData.split("|\\+|")[1]);
                rald.setDESCRIPTION(attachmentListData.split("|\\+|")[0]);
                rald.setURL(attachmentListData.split("|\\+|")[2]);
                dataList.add(rald);
                i++;
            }
        }
        rcwlDataForBPM.setATTACHMENTS(dataList);
        //设置传输值
        rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
        rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
        rcwlGxBpmStartDataDTO.setUserId(username);
        rcwlGxBpmStartDataDTO.setBtid("RCWLSRMCQDY");
        rcwlGxBpmStartDataDTO.setBoid(clarify.getTitle());
        rcwlGxBpmStartDataDTO.setProcinstId(clarify.getProcessInstanceId());
        rcwlGxBpmStartDataDTO.setData(rcwlDataForBPM.toString());
        //返回前台的跳转URL
        String rcwl_bpm_urlip = profileClient.getProfileValueByOptions("RCWL_BPM_URLIP");
        String rcwl_page_urlip = profileClient.getProfileValueByOptions("RCWL_CLARIFY_TO_PAGE_URL");
        String URL_BACK = "http://"+rcwl_bpm_urlip+rcwl_page_urlip+clarify.getClarifyNum();
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

    @Override
    public ResponseData updateClarifyData(RcwlUpdateDataVO rcwlUpdateDTO) {
        ResponseData responseData = new ResponseData();
        responseData.setMessage("数据更新成功！");
        responseData.setCode("200");
        try{
            rcwlClarifyRepository.updateClarifyData(rcwlUpdateDTO);
        }catch (Exception e){
            responseData.setMessage("数据更新失败！");
            responseData.setCode("201");
        }
        return responseData;
    }

    @Override
    public Long getClarifyIdByClarifyNum(String clarifyNum) {
        return rcwlClarifyRepository.getClarifyIdByClarifyNum(clarifyNum);
    }

    @Override
    public List<String> getTenantIdByclarifyNum(String clarifyNum) {
        List<String> l  = rcwlClarifyRepository.getTenantIdByclarifyNum(clarifyNum);
        return l;
    }
}
