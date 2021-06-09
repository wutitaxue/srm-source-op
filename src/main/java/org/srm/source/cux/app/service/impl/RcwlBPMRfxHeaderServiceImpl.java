package org.srm.source.cux.app.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.app.service.RcwlBPMRfxHeaderService;
import org.srm.source.cux.domain.entity.RcwlAttachmentListData;
import org.srm.source.cux.domain.entity.RcwlRfxHeaderAttachmentListDataForBPM;
import org.srm.source.cux.domain.entity.RcwlUpdateRfxHeaderDataVO;
import org.srm.source.cux.domain.entity.ResponseData;
import org.srm.source.cux.domain.repository.RcwlBPMRfxHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlClarifyRepository;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.domain.repository.RfxLineItemRepository;
import org.srm.source.rfx.domain.service.IRfxActionDomainService;
import org.srm.source.rfx.domain.service.IRfxLineItemDomainService;
import org.srm.source.share.domain.entity.ProjectLineSection;
import org.srm.source.share.domain.entity.SourceProject;
import org.srm.source.share.domain.repository.ProjectLineSectionRepository;
import org.srm.source.share.domain.repository.SourceProjectRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
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
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;
    @Autowired
    private IRfxLineItemDomainService rfxLineItemDomainService;
    @Autowired
    private IRfxActionDomainService rfxActionDomainService;
    @Autowired
    private SourceProjectRepository sourceProjectRepository;
    @Autowired
    private ProjectLineSectionRepository projectLineSectionRepository;

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
            log.info("流标上传数据：{"+ JSONObject.toJSONString(rcwlGxBpmStartDataDTO) +"}");
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
        sb.append("\"EVALMETHODNAME\":\"").append(rfxHeader.getSourceCategory() == null ? "":rcwlClarifyRepository.getMeaningByLovCodeAndValue("SSRC.SOURCE_CATEGORY",rfxHeader.getSourceCategory())).append("\",");
        sb.append("\"SOURCECATEGORY\":\"").append(rfxHeader.getAttributeVarchar17() == null ? "":rcwlClarifyRepository.getMeaningByLovCodeAndValue("SSRC.RCWL.BID_EVAL_METHOD",rfxHeader.getAttributeVarchar17())).append("\",");
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

    @Override
    public void chooseRfxCloseApproveType(Long tenantId, Long rfxHeaderId, String remark) {
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
        rfxHeader.setTerminatedRemark(remark);
//        rfxHeader.setTerminatedBy(0l);
        Map<String, String> cnfArgs = new HashMap();
        cnfArgs.put("approveType", "RFX_CLOSE");
        this.rfxClose(tenantId, rfxHeaderId, rfxHeader);
    }

    @Override
    public void updateSubmitBy(long l, Long rfxHeaderIds) {
        rcwlRfxHeaderRepository.updateSubmitBy(l, rfxHeaderIds);
    }

    @Override
    public void updateTerminatedBy(Long rfxHeaderIds) {
        rcwlRfxHeaderRepository.updateTerminatedBy(rfxHeaderIds);
    }

    @Override
    public void updateCheckedBy(Long rfxHeaderId) {
        rcwlRfxHeaderRepository.updateCheckedBy(rfxHeaderId);
    }

    @Override
    public void updateActionBy(String id) {
        rcwlRfxHeaderRepository.updateActionBy(id);
    }

    @Override
    public String getActionId(Long rfxHeaderIds) {
        return rcwlRfxHeaderRepository.getActionId(rfxHeaderIds);
    }

    public void rfxClose(Long tenantId, Long rfxHeaderId, RfxHeader rfxHeader) {
        log.info("进入rfxClose方法=================================");
        List<RfxLineItem> rfxLineItems = this.rfxLineItemRepository.select(new RfxLineItem(tenantId, rfxHeaderId));
//        rfxHeader.validClose();
        rfxHeader.initClose(rfxHeader.getTerminatedRemark());
        this.rfxLineItemDomainService.rfxBatchReleasePrLines(rfxHeader, rfxLineItems);
        log.info("查看是否进入releaseSourceProject方法==============================="+rfxHeader.getSourceFrom()+"==========");
        if ("PROJECT".equals(rfxHeader.getSourceFrom())) {
            this.releaseSourceProject(rfxHeader);
        }
        log.info("===============数量=rfxLineItems"+rfxLineItems.size()+"=================");
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(rfxLineItems)) {

            Iterator var5 = rfxLineItems.iterator();

            while(var5.hasNext()) {
                RfxLineItem rfxLineItem = (RfxLineItem)var5.next();
                rfxLineItem.setFinishedFlag(BaseConstants.Flag.YES);
            }

            CustomizeHelper.ignore(() -> {
                return this.rfxLineItemRepository.batchUpdateOptional(rfxLineItems, new String[]{"finishedFlag"});
            });
        }

        CustomizeHelper.ignore(() -> {
            return this.rfxHeaderRepository.updateOptional(rfxHeader, new String[]{"rfxStatus", "closedFlag", "terminatedBy", "terminatedDate", "terminatedRemark"});
        });
        log.info("进入rfxHeaderRepository.updateOptional方法===================================以下是rfxActionDomainService.insertAction");
        this.rfxActionDomainService.insertAction(rfxHeader, "CLOSE", rfxHeader.getTerminatedRemark());
        log.info("===============insertAction方法结束=================");
//        this.sendMessageHandle.sendMessageForOperation(rfxHeader, "SSRC.RFX_CLOSE");
//        this.rfxEventUtil.eventSend("SSRC_RFX_CLOSE", "CLOSE", rfxHeader);
    }

    public void releaseSourceProject(RfxHeader rfxHeader) {
        SourceProject sourceProject = (SourceProject)this.sourceProjectRepository.selectByPrimaryKey(rfxHeader.getSourceProjectId());
        if (!"PACK".equals(sourceProject.getSubjectMatterRule())) {
            sourceProject.setReferenceFlag(BaseConstants.Flag.NO);
            sourceProject.setSourceHeaderNum((String)null);
            sourceProject.setSourceHeaderId((Long)null);
            this.sourceProjectRepository.updateByPrimaryKey(sourceProject);
        } else {
            ProjectLineSection projectLineSectionParam = new ProjectLineSection();
            projectLineSectionParam.setSourceProjectId(sourceProject.getSourceProjectId());
            projectLineSectionParam.setSourceHeaderId(rfxHeader.getRfxHeaderId());
            projectLineSectionParam.setReferenceFlag(BaseConstants.Flag.YES);
            List<ProjectLineSection> projectLineSections = this.projectLineSectionRepository.selectRefenceSections(projectLineSectionParam);
            Iterator var5 = projectLineSections.iterator();

            while(var5.hasNext()) {
                ProjectLineSection projectLineSection = (ProjectLineSection)var5.next();
                projectLineSection.setCreateSourceFlag(BaseConstants.Flag.NO);
                projectLineSection.setReferenceFlag(BaseConstants.Flag.NO);
                projectLineSection.setSourceFrom((String)null);
                projectLineSection.setSourceHeaderId((Long)null);
                projectLineSection.setSourceHeaderNum((String)null);
                projectLineSection.setTempSourceHeaderId((Long)null);
            }

            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(projectLineSections)) {
                this.projectLineSectionRepository.batchUpdateOptional(projectLineSections, new String[]{"referenceFlag", "createSourceFlag", "sourceHeaderId", "sourceHeaderNum", "tempSourceHeaderId", "sourceFrom"});
            }

            int referenceCount = this.projectLineSectionRepository.selectCountByCondition(Condition.builder(ProjectLineSection.class).where(Sqls.custom().andEqualTo("sourceProjectId", sourceProject.getSourceProjectId()).andEqualTo("referenceFlag", BaseConstants.Flag.YES)).build());
            if (referenceCount == 0) {
                sourceProject.setReferenceFlag(BaseConstants.Flag.NO);
                sourceProject.setSourceHeaderNum((String)null);
                sourceProject.setSourceHeaderId((Long)null);
                sourceProject.setSourceProjectStatus("APPROVED");
                this.sourceProjectRepository.updateByPrimaryKey(sourceProject);
            }
        }

    }

}
