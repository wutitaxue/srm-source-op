package org.srm.source.cux.app.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.*;
import org.srm.source.cux.domain.repository.RcwlCalibrationApprovalRepository;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RcwlCalibrationApprovalServiceImpl implements RcwlCalibrationApprovalService {

    @Autowired
    private RCWLGxBpmInterfaceService rcwlGxBpmInterfaceService;
    //获取配置参数
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private RcwlCalibrationApprovalRepository rcwlCalibrationApprovalRepository;

    @Override
    public ResponseCalibrationApprovalData connectBPM(String organizationId, Long rfxHeaderId) {
        //根据id查出数据
        RfxHeader rfxHeader = rfxHeaderRepository.selectSimpleRfxHeaderById(rfxHeaderId);
        CalibrationApprovalForBPMData forBPMData =new CalibrationApprovalForBPMData();
        CalibrationApprovalDbdbjgDataForBPM dbdbjgDataForBPM = new CalibrationApprovalDbdbjgDataForBPM();
        CalibrationApprovalAttachmentDataForBPM attachmentDataForBPM = new CalibrationApprovalAttachmentDataForBPM();
        List<CalibrationApprovalDbdbjgDataForBPM> dbdbjgDataForBPMList = new ArrayList<CalibrationApprovalDbdbjgDataForBPM>();
        List<RcwlDBGetDataFromDatabase> listDbdbjgData = new ArrayList<>();
        List<CalibrationApprovalAttachmentDataForBPM> attachmentDataForBPMList = new ArrayList<CalibrationApprovalAttachmentDataForBPM>();
        List<RcwlAttachmentListData> listAttachmentData = new ArrayList<RcwlAttachmentListData>();
        //详情路径
        String URL_MX_HEADER = profileClient.getProfileValueByOptions("RCWL_DBSP_TO_BPM_URL");
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(URL_MX_HEADER).append(rfxHeader.getRfxHeaderId()).append("?").append("current=newInquiryHall&projectLineSectionId=");
        //方法区
        listDbdbjgData = rcwlCalibrationApprovalRepository.getDbdbjgList(rfxHeader.getRfxHeaderId());
        listAttachmentData = rcwlCalibrationApprovalRepository.getAttachmentList(rfxHeader.getCheckAttachmentUuid());
            //中标供应商数
        String winningSupplyNum = rcwlCalibrationApprovalRepository.getWinningSupplyNum(rfxHeader.getRfxHeaderId());
            //发标轮数
        String quotationRoundNumber = rcwlCalibrationApprovalRepository.getquotationRoundNum(rfxHeader.getRfxHeaderId());
        //方法结束
        //组装DATA区
        forBPMData.setFSUBJECT("定标审批"+"-"+rfxHeader.getCompanyName()+"-"+rfxHeader.getRfxNum()+"-"+rfxHeader.getRfxTitle());
        forBPMData.setCOMPANYID(rfxHeader.getCompanyName());
        forBPMData.setRFXNAME(rfxHeader.getRfxTitle());
        forBPMData.setRFXNUM(rfxHeader.getRfxNum());
        forBPMData.setBIDDINGMODE(rfxHeader.getAttributeVarchar8());
        forBPMData.setSHORTLISTCATEGORY(rfxHeader.getSourceCategory());
        forBPMData.setMETHODREMARK(rfxHeader.getAttributeVarchar17());
        forBPMData.setATTRIBUTEVARCHAR9(rfxHeader.getAttributeVarchar9());
        forBPMData.setPROJECTAMOUNT(rfxHeader.getBudgetAmount() == null ? "":rfxHeader.getBudgetAmount().toString());
        forBPMData.setATTRIBUTEVARCHAR12(rfxHeader.getAttributeVarchar10());
            //根据quotation_header_id去ssrc_rfx_quotation_line查询suggested_flag
        forBPMData.setATTRIBUTEVARCHAR13(winningSupplyNum);
            //根据source_header_id去ssrc_round_header表中的quotation_round_number字段
        forBPMData.setROUNDNUMBER(quotationRoundNumber);
        forBPMData.setOPINION(rfxHeader.getCheckRemark());
        forBPMData.setURL_MX(sbUrl.toString());
            //组装供应商列表
        if(!CollectionUtils.isEmpty(listDbdbjgData)){
            for(RcwlDBGetDataFromDatabase dbdbjgListData : listDbdbjgData){
                CalibrationApprovalDbdbjgDataForBPM rald = new CalibrationApprovalDbdbjgDataForBPM();
                rald.setSECTIONNAME(dbdbjgListData.getSupplierCompanyName());
                rald.setSUPPLIERCOMPANYNUM(dbdbjgListData.getCompanyNum());
                rald.setIP(dbdbjgListData.getSupplierCompanyIp());
                rald.setAPPENDREMARK(this.getAppendRemark(dbdbjgListData.getQuotationHeaderId()) == 0 ? "0":"1");
                rald.setTECHNICALSCORE(dbdbjgListData.getTechnologyScore());
                rald.setBUSINESSSCORE(dbdbjgListData.getBusinessScore());
                rald.setCOMPREHENSIVE(dbdbjgListData.getScore());
                rald.setCOMPREHENSIVERANK(dbdbjgListData.getScoreRank());
                rald.setBIDPRICE(rcwlCalibrationApprovalRepository.getQuotationAmount(dbdbjgListData.getSupplierCompanyName()));
                rald.setFIXEDPRICE(dbdbjgListData.getTotal_amount());
                rald.setREMARKS(this.getRemark(dbdbjgListData.getQuotationHeaderId()));
                dbdbjgDataForBPMList.add(rald);
            }
            forBPMData.setDBDBJGS(dbdbjgDataForBPMList);
        }
            //组装附件列表
        if(!CollectionUtils.isEmpty(listAttachmentData)){
            int i = 1;
            for(RcwlAttachmentListData attachmentListData : listAttachmentData){
                CalibrationApprovalAttachmentDataForBPM rald = new CalibrationApprovalAttachmentDataForBPM();
                rald.setFILENUMBER(Integer.toString(i));
                rald.setFILENAME(attachmentListData.getFileName());
                rald.setFILESIZE(attachmentListData.getFileSize());
                rald.setDESCRIPTION(attachmentListData.getFileName());
                rald.setURL(attachmentListData.getFileUrl());
                attachmentDataForBPMList.add(rald);
                i++;
            }
            forBPMData.setATTACHMENTS(attachmentDataForBPMList);
        }
        //组装结束
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        responseData.setMessage("操作成功！");
        responseData.setCode("200");
        String userName = DetailsHelper.getUserDetails().getUsername();
        //固定获取BPM接口参数区-------------
        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");
        ResponsePayloadDTO responsePayloadDTO = new ResponsePayloadDTO();
        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        //设置传输值
        rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
        rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
        rcwlGxBpmStartDataDTO.setUserId(userName);
        rcwlGxBpmStartDataDTO.setBtid("RCWLSRMDBSP");
        rcwlGxBpmStartDataDTO.setBoid(rfxHeader.getRfxNum());
        rcwlGxBpmStartDataDTO.setProcinstId(rfxHeader.getAttributeVarchar6());
        rcwlGxBpmStartDataDTO.setData(forBPMData.toString());
        //返回前台的跳转URL
        String rcwl_bpm_urlip = profileClient.getProfileValueByOptions("RCWL_BPM_URLIP");
        String rcwl_page_urlip = profileClient.getProfileValueByOptions("RCWL_DBSP_TO_PAGE_URL");
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

    @Override
    public ResponseCalibrationApprovalData updateClarifyData(RcwlUpdateCalibrationApprovalDataVO rcwlUpdateDataDTO) {
        ResponseCalibrationApprovalData responseCalibrationApprovalData = new ResponseCalibrationApprovalData();
        responseCalibrationApprovalData.setMessage("数据更新成功！");
        responseCalibrationApprovalData.setCode("200");
        try{
            rcwlCalibrationApprovalRepository.updateClarifyData(rcwlUpdateDataDTO);
        }catch (Exception e){
            responseCalibrationApprovalData.setMessage("数据更新失败！");
            responseCalibrationApprovalData.setCode("201");
        }
        return responseCalibrationApprovalData;
    }

    @Override
    public Long getRfxHeaderIdByRfxNum(String rfxNum) {
        return rcwlCalibrationApprovalRepository.getRfxHeaderIdByRfxNum(rfxNum);
    }

    @Override
    public List<String> getQuotationHeaderIDByRfxHeaderId(Long rfxHeaderId,Long tenantId) {
        List<String> qQuotationHeaderIDs = rcwlCalibrationApprovalRepository.getQuotationHeaderIDByRfxHeaderId(rfxHeaderId,tenantId);
        return qQuotationHeaderIDs;
    }

    @Override
    public List<Long> getRfxLineItemIdByRfxHeaderId(Long rfxHeaderId) {
        List<Long> l = rcwlCalibrationApprovalRepository.getRfxLineItemIdByRfxHeaderId(rfxHeaderId);
        return l;
    }

    @Override
    public List<RfxQuotationLine> getQuotationLineListByQuotationHeaderID(Long id) {
        List<RfxQuotationLine> l = rcwlCalibrationApprovalRepository.getQuotationLineListByQuotationHeaderID(id);
        return l;
    }

    @Override
    public RfxQuotationLine getRfxQuotationLineDataByQuotationHeaderIDs(String id) {
        return  rcwlCalibrationApprovalRepository.getRfxQuotationLineDataByQuotationHeaderIDs(id);
    }

    private String getRemark(String s) {
        return rcwlCalibrationApprovalRepository.getRemark(s);
    }

    private Integer getAppendRemark(String id) {
        Integer s = rcwlCalibrationApprovalRepository.getAppendRemark(id);
        return s ;
    }
}
