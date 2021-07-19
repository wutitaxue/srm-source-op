package org.srm.source.cux.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.srm.source.bid.app.service.common.BidSendMessageHandle;
import org.srm.source.bid.domain.entity.BidHeader;
import org.srm.source.bid.domain.repository.BidHeaderRepository;
import org.srm.source.cux.app.service.RcwlClarifyService;
import org.srm.source.cux.domain.entity.*;
import org.srm.source.cux.domain.repository.RcwlBPMRfxHeaderRepository;
import org.srm.source.cux.domain.repository.RcwlCalibrationApprovalRepository;
import org.srm.source.cux.domain.repository.RcwlClarifyRepository;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.share.domain.entity.Clarify;
import org.srm.source.share.domain.entity.IssueLine;
import org.srm.source.share.domain.repository.ClarifyRepository;
import org.srm.source.share.domain.repository.IssueLineRepository;
import org.srm.source.share.infra.utils.ShareEventUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RcwlClarifyServiceImpl implements RcwlClarifyService {
    @Autowired
    private RCWLGxBpmInterfaceService rcwlGxBpmInterfaceService;
    @Autowired
    private RcwlClarifyRepository rcwlClarifyRepository;
    @Autowired
    private RcwlBPMRfxHeaderRepository rcwlRfxHeaderRepository;
    @Autowired
    private RcwlCalibrationApprovalRepository rcwlCalibrationApprovalRepository;
    @Lazy
    private BidSendMessageHandle bidSendMessageHandle;
    //获取配置参数
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private BidHeaderRepository bidHeaderRepository;
    @Autowired
    private ShareEventUtil shareEventUtil;
    @Autowired
    private SendMessageHandle sendMessageHandle;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;
    @Autowired
    private IssueLineRepository issueLineRepository;
    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private ClarifyRepository clarifyRepository;

    @Override
    public ResponseData releaseClarifyByBPM(RcwlClarifyForBPM clarify) {
        String username = rcwlRfxHeaderRepository.getRealNameById(clarify.getTenantId());
        ResponseData responseData = new ResponseData();
        RcwlDataForBPM rcwlDataForBPM = new RcwlDataForBPM();
        responseData.setMessage("操作成功！");
        responseData.setCode("200");
        //固定获取BPM接口参数区-------------
        String reSrcSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQSRCSYS");
        String reqTarSys = profileClient.getProfileValueByOptions(DetailsHelper.getUserDetails().getTenantId(), DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(), "RCWL_BPM_REQTARSYS");
        String USERNAME = DetailsHelper.getUserDetails().getUsername();
        ResponsePayloadDTO responsePayloadDTO = new ResponsePayloadDTO();
        RCWLGxBpmStartDataDTO rcwlGxBpmStartDataDTO = new RCWLGxBpmStartDataDTO();
        //方法区，获取调用BPM接口所需值DATA并填充
            //根据寻源Id查找rfx_num，rfx_title和round_number以“+”拼接
        String dataBySourceId = rcwlClarifyRepository.getSourceNumAndNameAndClarifyNumberById(clarify.getSourceId());
            //查询此表相同source_id的refer_flag（是否引用问题）值为1的数量
        String countOfAlikeSourceId = rcwlClarifyRepository.getCountOfAlikeSourceId(clarify.getSourceId(),1l);
            //附件list
        List<RcwlAttachmentListData> attachmentList = rcwlClarifyRepository.getAttachmentList(clarify.getAttachmentUuid());
        String creatName = rcwlRfxHeaderRepository.getRealNameById(clarify.getCreatedBy());
        //方法区结束
        rcwlDataForBPM.setFILE(clarify.getReferFlag() == 0l ? "澄清":"答疑");//值为1传描述“答疑” 值为0传描述“ 澄清”
        //立项编号不确定----------
        rcwlDataForBPM.setFSUBJECT("答疑-"+ clarify.getTitle()+"-" + dataBySourceId.split("\\+")[0]);//答疑+标题+立项编号
        rcwlDataForBPM.setSUBMITTEDBY(creatName == null ? "":creatName);//CREATED_BY
        rcwlDataForBPM.setTITLE(clarify.getTitle());
        rcwlDataForBPM.setSOURCENUM(dataBySourceId == null ? "":dataBySourceId.split("\\+")[0]);//用source_id去去ssrc_rfx_header匹配rfx_header_id找到对应rfx_num
        rcwlDataForBPM.setSOURCENAME(dataBySourceId == null ? "":dataBySourceId.split("\\+")[1]);//用source_id去去ssrc_rfx_header匹配rfx_header_id找到对应rfx_title
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
            for(RcwlAttachmentListData attachmentListData : attachmentList){
                RcwlAttachmentListDataForBPM rald = new RcwlAttachmentListDataForBPM();
                rald.setFILENUMBER(Integer.toString(i));
                rald.setFILENAME(attachmentListData.getFileName());
                rald.setFILESIZE(attachmentListData.getFileSize());
                rald.setDESCRIPTION(attachmentListData.getFileName());
                rald.setURL(attachmentListData.getFileUrl());
                dataList.add(rald);
                i++;
            }
        }
        rcwlDataForBPM.setATTACHMENTS(dataList);
        //设置传输值
        rcwlGxBpmStartDataDTO.setReSrcSys(reSrcSys);
        rcwlGxBpmStartDataDTO.setReqTarSys(reqTarSys);
        rcwlGxBpmStartDataDTO.setUserId(USERNAME);
        rcwlGxBpmStartDataDTO.setBtid("RCWLSRMCQDY");
        rcwlGxBpmStartDataDTO.setBoid(clarify.getClarifyNum());
        rcwlGxBpmStartDataDTO.setProcinstId(clarify.getProcessInstanceId());
        rcwlGxBpmStartDataDTO.setData(rcwlDataForBPM.toString());
        String xx =  JSONObject.toJSONString(rcwlGxBpmStartDataDTO);
        //返回前台的跳转URL
        String rcwl_bpm_urlip = profileClient.getProfileValueByOptions("RCWL_BPM_URLIP");
        String rcwl_page_urlip = profileClient.getProfileValueByOptions("RCWL_CLARIFY_TO_PAGE_URL");
        String URL_BACK = "http://"+rcwl_bpm_urlip+rcwl_page_urlip+clarify.getClarifyNum();
        responseData.setUrl(URL_BACK);
        try{
            //调用bpm接口
            responsePayloadDTO = rcwlGxBpmInterfaceService. RcwlGxBpmInterfaceRequestData(rcwlGxBpmStartDataDTO);
            log.info("澄清答疑上传数据：{"+ JSONObject.toJSONString(rcwlGxBpmStartDataDTO) +"}");
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

    @Override
    public Long getSourceReleasedBy(Long sourceId) {
        return rcwlClarifyRepository.getSourceReleasedBy(sourceId);
    }

    @Override
    public Clarify releaseClarify(Long tenantId, Clarify clarify) {
        clarify.checkContextLength(524288);
        List<IssueLine> issueLines = this.validIssueLine(clarify.getIssueLineIdList(), clarify.getClarifyId());
        clarify.setIssueLineList(issueLines);
        Clarify releaseClarify;
        if (clarify.getClarifyId() == null) {
            releaseClarify = this.releaseNewClarify(tenantId, clarify);
        } else {
            releaseClarify = this.releaseUpdateClarify(clarify);
        }
        return releaseClarify;
    }

    public List<IssueLine> validIssueLine(List<Long> issueLineIdList, Long clarifyId) {
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(issueLineIdList)) {
            return null;
        } else {
            String ids = (String)issueLineIdList.stream().map(Objects::toString).collect(Collectors.joining(", "));
            List<IssueLine> issueLines = this.issueLineRepository.selectByIds(ids);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(issueLines)) {
                throw new CommonException("error.issueLine.cite_failed", new Object[0]);
            } else {
                Iterator var5 = issueLines.iterator();

                IssueLine issueLine;
                do {
                    if (!var5.hasNext()) {
                        return issueLines;
                    }

                    issueLine = (IssueLine)var5.next();
                } while((issueLine.getClarifyId() != null || "SUBMITTED".equals(issueLine.getIssueLineStatus())) && (issueLine.getClarifyId() == null || issueLine.getClarifyId().equals(clarifyId)));

                throw new CommonException("error.issueLine.cite_failed", new Object[0]);
            }
        }
    }

    public Clarify releaseNewClarify(Long tenantId, Clarify clarify) {
        clarify.init(tenantId, this.codeRuleBuilder.generateCode(tenantId, "SSRC.CLARIFY_NUM", "GLOBAL", "GLOBAL", (Map)null), "RELEASED", org.apache.commons.collections4.CollectionUtils.isEmpty(clarify.getIssueLineList()) ? 0 : 1, DetailsHelper.getUserDetails().getUserId(), new Date());
        this.clarifyRepository.insert(clarify);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(clarify.getIssueLineList())) {
            clarify.getIssueLineList().forEach((issueLine) -> {
                issueLine.setIssueLineStatus("CLARIFIED");
                issueLine.setClarifyId(clarify.getClarifyId());
            });
            this.issueLineRepository.batchUpdateOptional(clarify.getIssueLineList(), new String[]{"issueLineStatus", "clarifyId"});
        }

        return clarify;
    }

    public Clarify releaseUpdateClarify(Clarify clarify) {
        Clarify validClarify = (Clarify)this.clarifyRepository.selectByPrimaryKey(clarify);
        if (validClarify != null && !"RELEASED".equals(validClarify.getClarifyStatus())) {
            Integer referFlag = org.apache.commons.collections4.CollectionUtils.isEmpty(clarify.getIssueLineList()) ? 0 : 1;
            clarify.init("RELEASED", referFlag,0l, new Date());
            List<IssueLine> selectIssueLines = this.issueLineRepository.selectByCondition(Condition.builder(IssueLine.class).andWhere(Sqls.custom().andEqualTo("issueLineStatus", "CLARIFYING").andEqualTo("clarifyId", clarify.getClarifyId())).build());
            List issueLineIds;
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(clarify.getIssueLineList())) {
                issueLineIds = (List)clarify.getIssueLineList().stream().map(IssueLine::getIssueLineId).collect(Collectors.toList());
                String ids = (String)issueLineIds.stream().map(Objects::toString).collect(Collectors.joining(","));
                List<IssueLine> updateIssueLines = this.issueLineRepository.selectByIds(ids);
                updateIssueLines.forEach((issueLine) -> {
                    issueLine.setIssueLineStatus("CLARIFIED");
                    issueLine.setClarifyId(clarify.getClarifyId());
                });
                this.issueLineRepository.batchUpdateOptional(updateIssueLines, new String[]{"issueLineStatus", "clarifyId"});
            }
            this.clarifyRepository.updateOptional(clarify, new String[]{"title", "attachmentUuid", "context", "referFlag", "clarifyStatus", "submittedBy", "submittedDate"});
            return clarify;
        } else {
            throw new CommonException("error.clarify_release_failed", new Object[0]);
        }
    }
}
