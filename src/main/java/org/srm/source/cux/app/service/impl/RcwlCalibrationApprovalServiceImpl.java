package org.srm.source.cux.app.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import groovy.lang.Lazy;
import gxbpm.dto.RCWLGxBpmStartDataDTO;
import gxbpm.service.RCWLGxBpmInterfaceService;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.customize.service.CustomizeClient;
import org.hzero.boot.customize.util.CustomizeHelper;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.*;
import org.srm.source.cux.domain.repository.RcwlCalibrationApprovalRepository;
import org.srm.source.priceLib.app.service.PriceLibServiceService;
import org.srm.source.priceLib.domain.vo.PriceServiceParamsVO;
import org.srm.source.priceLib.domain.vo.PriceServiceVO;
import org.srm.source.rfx.api.dto.*;
import org.srm.source.rfx.api.dto.common.ItfBaseBO;
import org.srm.source.rfx.api.dto.common.ResponseDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxLineItemService;
import org.srm.source.rfx.app.service.UserConfigService;
import org.srm.source.rfx.app.service.v2.RfxHeaderServiceV2;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.entity.UserConfig;
import org.srm.source.rfx.domain.repository.*;
import org.srm.source.rfx.domain.service.IRfxActionDomainService;
import org.srm.source.rfx.domain.service.IRfxHeaderDomainService;
import org.srm.source.rfx.infra.mapper.RfxHeaderMapper;
import org.srm.source.rfx.infra.util.RfxEventUtil;
import org.srm.source.share.domain.entity.PrequalHeader;
import org.srm.source.share.domain.entity.PrequalLine;
import org.srm.source.share.domain.entity.RoundHeader;
import org.srm.source.share.domain.entity.SourceTemplate;
import org.srm.source.share.domain.repository.PrequalHeaderRepository;
import org.srm.source.share.domain.repository.PrequalLineRepository;
import org.srm.source.share.domain.repository.RoundHeaderRepository;
import org.srm.source.share.domain.repository.SourceTemplateRepository;
import org.srm.source.share.infra.utils.BusinessKeyUtil;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private PrequalHeaderRepository prequalHeaderRepository;
    @Autowired
    private PrequalLineRepository prequalLineRepository;
    @Autowired
    private RfxLineItemRepository rfxLineItemRepository;
    @Autowired
    private RfxQuotationHeaderRepository rfxQuotationHeaderRepository;
    @Autowired
    private RfxQuotationLineRepository rfxQuotationLineRepository;
    @Autowired
    private RfxLadderQuotationRepository rfxLadderQuotationRepository;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    @Lazy
    private RfxHeaderServiceV2 rfxHeaderServiceV2;
    @Autowired
    private SourceTemplateRepository sourceTemplateRepository;
    @Autowired
    private IRfxHeaderDomainService rfxHeaderDomainService;
    @Autowired
    private CustomizeClient customizeClient;
    @Autowired
    private PriceLibServiceService priceLibServiceService;
    @Autowired
    private RfxMemberRepository rfxMemberRepository;
    @Autowired
    private RfxEventUtil rfxEventUtil;
    @Autowired
    private RfxLineItemService rfxLineItemService;
    @Autowired
    private IRfxActionDomainService rfxActionDomainService;
    @Autowired
    private RoundHeaderRepository roundHeaderRepository;
    @Autowired
    private RfxHeaderMapper rfxHeaderMapper;


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
    public Long getRfxHeaderIdByRfxNum(String rfxNum,Long tenantId) {
        return rcwlCalibrationApprovalRepository.getRfxHeaderIdByRfxNum(rfxNum,tenantId);
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

    @Override
    public Long getRoundNumber(Long rfxHeaderId, Long tenantId) {
        return rcwlCalibrationApprovalRepository.getRoundNumber(rfxHeaderId,tenantId);
    }

    private String getRemark(String s) {
        return rcwlCalibrationApprovalRepository.getRemark(s);
    }

    private Integer getAppendRemark(String id) {
        Integer s = rcwlCalibrationApprovalRepository.getAppendRemark(id);
        return s ;
    }


    public void checkSubmitCommon(Long organizationId, Long rfxHeaderId, CheckPriceHeaderDTO checkPriceHeaderDTO) {
        if (BaseConstants.Flag.YES.equals(checkPriceHeaderDTO.getSelectSectionReadFlag())) {
            UserConfig userConfig = new UserConfig();
            userConfig.setConfigKey("sectionRead");
            userConfig.setConfigDesc("SelectSectionReadFlag");
            userConfig.setConfigValue(BaseConstants.Flag.YES.toString());
            this.userConfigService.createOrUpdate(new UserConfig());
        }

//        if (checkPriceHeaderDTO.getCreateItemFlag() != null && Objects.equals(checkPriceHeaderDTO.getCreateItemFlag(), 1)) {
//            ((RfxHeaderService)this.self()).validItemCodeRepetition(organizationId, checkPriceHeaderDTO.getRfxLineItemList());
//        }

        this.rfxHeaderServiceV2.switchCheckPriceSave(organizationId, checkPriceHeaderDTO);
        checkPriceHeaderDTO.validationAllottedQuantityAndRemark(rfxHeaderId, this.rfxQuotationLineRepository);
        this.updateItemSelectionStrategy(organizationId, rfxHeaderId, checkPriceHeaderDTO);
        this.rfxHeaderDomainService.validStrategyLineItems(organizationId, rfxHeaderId);
        this.saveServicePrice(organizationId, rfxHeaderId);
        List<CheckPriceDTO> checkPriceDTOList = checkPriceHeaderDTO.getCheckPriceDTOLineList();
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
        rfxHeader.setRfxStatus(this.generateRfxStatus(rfxHeaderId));
        if (null != checkPriceHeaderDTO.getTotalPrice()) {
            rfxHeader.setTotalPrice(checkPriceHeaderDTO.getTotalPrice());
        } else {
            rfxHeader.setTotalPrice(this.rfxHeaderMapper.selectRfxTotalPrice(new RfxHeaderDTO(rfxHeader.getRfxHeaderId(), rfxHeader.getRoundNumber())));
        }

        rfxHeader.setCheckAttachmentUuid(checkPriceHeaderDTO.getCheckAttachmentUuid());
        rfxHeader.validationCheckPriceStatus();
        SourceTemplate sourceTemplate = (SourceTemplate)this.sourceTemplateRepository.selectByPrimaryKey(rfxHeader.getTemplateId());
        RoundHeader roundHeader = (RoundHeader)this.roundHeaderRepository.selectOne(new RoundHeader(organizationId, rfxHeaderId, "RFX"));
        if ("CHECK".equals(sourceTemplate.getRoundQuotationRule()) && "DOUBLE".equals(sourceTemplate.getSourceStage())) {
            roundHeader.validationCheckRound();
        }

        this.rfxActionDomainService.insertAction(rfxHeader, "SUBMIT", (String)null);
        if (StringUtils.isBlank(checkPriceHeaderDTO.getReleaseItemIds())) {
            String itemIds = (String)checkPriceHeaderDTO.getCheckPriceDTOLineList().stream().filter((item) -> {
                return "RELEASE".equals(item.getSelectionStrategy());
            }).map((item) -> {
                return item.getRfxLineItemId().toString();
            }).collect(Collectors.joining(","));
            checkPriceHeaderDTO.setReleaseItemIds(itemIds);
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(checkPriceHeaderDTO.getRfxLineItemList())) {
            checkPriceHeaderDTO.getRfxLineItemList().forEach((rfxLineItem) -> {
                rfxLineItem.setCreateItemFlag(checkPriceHeaderDTO.getCreateItemFlag());
            });
        }

        List<RfxLineItem> rfxLineItems = this.rfxLineItemService.createItemList(sourceTemplate.getResultApproveType(), rfxHeader.getTenantId(), checkPriceHeaderDTO.getRfxLineItemList());
        checkPriceHeaderDTO.setRfxLineItemList(rfxLineItems);
        //todo
        this.externalApprove(rfxHeader);
        //((RfxHeaderService)this.self()).chooseResultApproveType(sourceTemplate.getResultApproveType(), rfxHeader, checkPriceHeaderDTO);
        if (!"SELF".equals(sourceTemplate.getResultApproveType())) {
            this.rfxEventUtil.eventSend("SSRC_RFX_CHECK_SUBMIT", "CHECK_SUBMIT", rfxHeader);
        }
    }
    public void externalApprove(RfxHeader rfxHeader) {
        SourceExternalFullDataDTO sourceExternalFullDataDTO = new SourceExternalFullDataDTO();
        ExternalFiles externalFiles = new ExternalFiles();
        ExternalRfxHeaderDTO externalRfxHeaderDTO = (ExternalRfxHeaderDTO) CustomizeHelper.ignore(() -> {
            return this.rfxHeaderRepository.selectRfxHeaderData(rfxHeader.getRfxHeaderId());
        });
        externalFiles.setRfxBusinessAttachmentUuid(externalRfxHeaderDTO.getBusinessAttachmentUuid());
        externalFiles.setRfxTechAttachmentUuid(externalRfxHeaderDTO.getTechAttachmentUuid());
        externalFiles.setPretrialUuid(externalRfxHeaderDTO.getPretrialUuid());
        externalFiles.setCheckAttachmentUuid(externalRfxHeaderDTO.getCheckAttachmentUuid());
        externalFiles.setPreAttachmentUuid(externalRfxHeaderDTO.getPreAttachmentUuid());
        PrequalHeader prequalHeader = (PrequalHeader)CustomizeHelper.ignore(() -> {
            return (PrequalHeader)this.prequalHeaderRepository.selectOne(new PrequalHeader(rfxHeader.getTenantId(), rfxHeader.getRfxHeaderId()));
        });
        if (prequalHeader != null) {
            externalFiles.setPrequalAttachmentUuid(prequalHeader.getPrequalAttachmentUuid());
            List<PrequalLine> prequalLineList = (List)CustomizeHelper.ignore(() -> {
                return this.prequalLineRepository.select(new PrequalLine(prequalHeader.getPrequalHeaderId()));
            });
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(prequalLineList)) {
                externalFiles.setPrequalValidAttachmentUuids((Set)prequalLineList.stream().map(PrequalLine::getValidAttachmentUuid).collect(Collectors.toSet()));
            }
        }

        String releaseDocumentId = BusinessKeyUtil.generateBusinessKey("SSRC_RFX_%s_%s_RELEASE", rfxHeader.getRfxHeaderId(), rfxHeader.getTenantId());
        externalFiles.setReleaseAttachmentUuids(this.rfxHeaderRepository.getRfxWorkflowHistoryAttachmentUuid(rfxHeader.getRfxHeaderId(), releaseDocumentId));
        String checkDocumentId = BusinessKeyUtil.generateBusinessKey("SSRC_RFX_%s_%s_CHECK", rfxHeader.getRfxHeaderId(), rfxHeader.getTenantId());
        externalFiles.setCheckAttachmentUuids(this.rfxHeaderRepository.getRfxWorkflowHistoryAttachmentUuid(rfxHeader.getRfxHeaderId(), checkDocumentId));
        List<ExternalRfxLineItemDTO> externalRfxLineItemDTOS = (List)CustomizeHelper.ignore(() -> {
            return this.rfxLineItemRepository.selectRfxLineItem(rfxHeader.getRfxHeaderId());
        });
        List<ExternalQuotationHeaderDTO> externalQuotationHeaderDTOS = (List)CustomizeHelper.ignore(() -> {
            return this.rfxQuotationHeaderRepository.selectQuotationData(rfxHeader.getRfxHeaderId());
        });
        List<String> businessAttachmentUuids = new ArrayList();
        List<String> techAttachmentUuids = new ArrayList();
        Iterator var12 = externalQuotationHeaderDTOS.iterator();

        while(var12.hasNext()) {
            ExternalQuotationHeaderDTO externalQuotationHeaderDTO = (ExternalQuotationHeaderDTO)var12.next();
            businessAttachmentUuids.add(externalQuotationHeaderDTO.getBusinessAttachmentUuid());
            techAttachmentUuids.add(externalQuotationHeaderDTO.getTechAttachmentUuid());
        }

        externalFiles.setSupplierBusinessAttachmentUuid(businessAttachmentUuids);
        externalFiles.setSupplierTechAttachmentUuid(techAttachmentUuids);
        List<ExternalQuotationLineDTO> externalQuotationLineDTOS = (List)CustomizeHelper.ignore(() -> {
            return this.rfxQuotationLineRepository.selectQuotationLineData(rfxHeader.getRfxHeaderId());
        });
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(externalQuotationLineDTOS)) {
            List<ExternalLadderQuotationDTO> externalLadderQuotationDTOS = (List)CustomizeHelper.ignore(() -> {
                return this.rfxLadderQuotationRepository.selectLadderQuotationData((List)externalQuotationLineDTOS.stream().map(ExternalQuotationLineDTO::getQuotationLineId).collect(Collectors.toList()));
            });
            Map<Long, List<ExternalLadderQuotationDTO>> collect1 = (Map)externalLadderQuotationDTOS.stream().collect(Collectors.groupingBy(ExternalLadderQuotationDTO::getQuotationLineId));
            Iterator var15 = externalQuotationLineDTOS.iterator();

            while(var15.hasNext()) {
                ExternalQuotationLineDTO externalQuotationLineDTO = (ExternalQuotationLineDTO)var15.next();
                externalQuotationLineDTO.setExternalLadderQuotationDTOS((List)collect1.get(externalQuotationLineDTO.getQuotationLineId()));
            }
        }

        Map<Long, List<ExternalQuotationLineDTO>> collect = (Map)externalQuotationLineDTOS.stream().collect(Collectors.groupingBy(ExternalQuotationLineDTO::getQuotationHeaderId));
        Iterator var22 = externalQuotationHeaderDTOS.iterator();

        while(var22.hasNext()) {
            ExternalQuotationHeaderDTO externalQuotationHeaderDTO = (ExternalQuotationHeaderDTO)var22.next();
            externalQuotationHeaderDTO.setRfxQuotationLineDTOS((List)collect.get(externalQuotationHeaderDTO.getQuotationHeaderId()));
        }
            RfxHeader rfxHeader1 = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeader.getRfxHeaderId());
            rfxHeader1.setRfxStatus("CHECK_APPROVING");
            this.rfxHeaderRepository.updateByPrimaryKeySelective(rfxHeader1);
    }
    //拷贝标准方法
    private void updateItemSelectionStrategy(Long organizationId, Long rfxHeaderId, CheckPriceHeaderDTO checkPriceHeaderDTO) {
        List<RfxLineItem> items = (List)CustomizeHelper.ignore(() -> {
            return this.rfxLineItemRepository.checkItemNeed(organizationId, rfxHeaderId);
        });
        if (StringUtils.isNotBlank(checkPriceHeaderDTO.getSelectionStrategy())) {
            items.forEach((item) -> {
                item.setSelectionStrategy(checkPriceHeaderDTO.getSelectionStrategy());
            });
        } else {
            SourceTemplate sourceTemplate = (SourceTemplate)this.sourceTemplateRepository.selectByPrimaryKey(((RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId)).getTemplateId());
            items.forEach((rfxLineItem) -> {
                if (0 == rfxLineItem.getSuggestCount() && "RECOMMENDATION".equals(rfxLineItem.getSelectionStrategy())) {
                    rfxLineItem.setSelectionStrategy(sourceTemplate.getSelectionStrategy());
                }

            });
        }

        if ("RELEASE".equals(checkPriceHeaderDTO.getSelectionStrategy())) {
            checkPriceHeaderDTO.setReleaseItemIds(StringUtils.join((Iterable)items.stream().map((item) -> {
                return item.getRfxLineItemId().toString();
            }).collect(Collectors.toList()), ","));
        }

        this.rfxLineItemRepository.batchUpdateOptional(items, new String[]{"selectionStrategy"});
    }

    private void saveServicePrice(Long tenantId, Long rfxHeaderId) {
        List<RfxQuotationLine> rfxQuotationLineList = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("SSRC.INQUIRY_HALL_CHECK_PRICE.TAB_ALL_QUOTATION_DETAIL", RfxQuotationLine.class);
        boolean isVisibleChangePercent = this.customizeClient.isVisibleField("SSRC.INQUIRY_HALL_CHECK_PRICE.TAB_ALL_QUOTATION_DETAIL", "changePercent", map);
        boolean isVisibleMinPrice = this.customizeClient.isVisibleField("SSRC.INQUIRY_HALL_CHECK_PRICE.TAB_ALL_QUOTATION_DETAIL", "minPrice", map);
        boolean isVisibleNewPrice = this.customizeClient.isVisibleField("SSRC.INQUIRY_HALL_CHECK_PRICE.TAB_ALL_QUOTATION_DETAIL", "newPrice", map);
        if (isVisibleChangePercent) {
            this.saveChangePercent(tenantId, rfxHeaderId, rfxQuotationLineList);
        }

        if (isVisibleMinPrice) {
            this.saveMinPrice(tenantId, rfxHeaderId, rfxQuotationLineList);
        }

        if (isVisibleNewPrice) {
            this.saveNewPrice(tenantId, rfxHeaderId, rfxQuotationLineList);
        }

        this.rfxQuotationLineRepository.batchUpdateOptional(rfxQuotationLineList, new String[]{"changePercent", "minPrice", "newPrice"});
    }
    private void saveChangePercent(Long tenantId, Long rfxHeaderId, List<RfxQuotationLine> rfxQuotationLineList) {
        PriceServiceVO priceServiceVO = new PriceServiceVO("RFX", "RFX_LINE_CHANGE_PERCENT", tenantId, rfxHeaderId);
        Map<Long, PriceServiceParamsVO> priceLibDTOMap = (Map)this.priceLibServiceService.convertPrice(tenantId, priceServiceVO).stream().collect(Collectors.toMap(PriceServiceParamsVO::getQuotationLineId, (e) -> {
            return e;
        }));
        this.initRfxQuotationLineList(priceServiceVO, rfxQuotationLineList, priceLibDTOMap);
    }

    private void saveMinPrice(Long tenantId, Long rfxHeaderId, List<RfxQuotationLine> rfxQuotationLineList) {
        PriceServiceVO priceServiceVO = new PriceServiceVO("RFX", "SSRC_MIN_PRICE", tenantId, rfxHeaderId);
        Map<Long, PriceServiceParamsVO> priceLibDTOMap = (Map)this.priceLibServiceService.convertPrice(tenantId, priceServiceVO).stream().collect(Collectors.toMap(PriceServiceParamsVO::getQuotationLineId, (e) -> {
            return e;
        }));
        this.initRfxQuotationLineList(priceServiceVO, rfxQuotationLineList, priceLibDTOMap);
    }

    private void saveNewPrice(Long tenantId, Long rfxHeaderId, List<RfxQuotationLine> rfxQuotationLineList) {
        PriceServiceVO priceServiceVO = new PriceServiceVO("RFX", "SSRC_NEW_PRICE", tenantId, rfxHeaderId);
        Map<Long, PriceServiceParamsVO> priceLibDTOMap = (Map)this.priceLibServiceService.convertPrice(tenantId, priceServiceVO).stream().collect(Collectors.toMap(PriceServiceParamsVO::getQuotationLineId, (e) -> {
            return e;
        }));
        this.initRfxQuotationLineList(priceServiceVO, rfxQuotationLineList, priceLibDTOMap);
    }

    private void initRfxQuotationLineList(PriceServiceVO priceServiceVO, List<RfxQuotationLine> rfxQuotationLineList, Map<Long, PriceServiceParamsVO> priceLibDTOMap) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(rfxQuotationLineList)) {
            priceServiceVO.getPriceQueryParamsVOS().forEach((priceServiceParamsVO) -> {
                RfxQuotationLine rfxQuotationLine = (RfxQuotationLine)this.rfxQuotationLineRepository.selectByPrimaryKey(priceServiceParamsVO.getQuotationLineId());
                this.initServicePrice(priceServiceVO, priceServiceParamsVO, rfxQuotationLine, priceLibDTOMap);
                rfxQuotationLineList.add(rfxQuotationLine);
            });
        } else {
            Map<Long, RfxQuotationLine> rfxQuotationLineMap = (Map)rfxQuotationLineList.stream().collect(Collectors.toMap(RfxQuotationLine::getQuotationLineId, (e) -> {
                return e;
            }));
            priceServiceVO.getPriceQueryParamsVOS().forEach((priceServiceParamsVO) -> {
                RfxQuotationLine rfxQuotationLine = (RfxQuotationLine)rfxQuotationLineMap.get(priceServiceParamsVO.getQuotationLineId());
                this.initServicePrice(priceServiceVO, priceServiceParamsVO, rfxQuotationLine, priceLibDTOMap);
            });
        }

    }
    private void initServicePrice(PriceServiceVO priceServiceVO, PriceServiceParamsVO priceServiceParamsVO, RfxQuotationLine rfxQuotationLine, Map<Long, PriceServiceParamsVO> priceLibDTOMap) {
        if (null != priceLibDTOMap.get(priceServiceParamsVO.getQuotationLineId())) {
            if ("RFX_LINE_CHANGE_PERCENT".equals(priceServiceVO.getServiceCode())) {
                rfxQuotationLine.setChangePercent(((PriceServiceParamsVO)priceLibDTOMap.get(priceServiceParamsVO.getQuotationLineId())).getValue());
            } else if ("SSRC_MIN_PRICE".equals(priceServiceVO.getServiceCode())) {
                rfxQuotationLine.setMinPrice(((PriceServiceParamsVO)priceLibDTOMap.get(priceServiceParamsVO.getQuotationLineId())).getValue());
            } else if ("SSRC_NEW_PRICE".equals(priceServiceVO.getServiceCode())) {
                rfxQuotationLine.setNewPrice(((PriceServiceParamsVO)priceLibDTOMap.get(priceServiceParamsVO.getQuotationLineId())).getValue());
            }
        }

    }

    public String generateRfxStatus(Long refHeaderId) {
        RfxHeaderDTO rfxHeaderDTO = this.rfxHeaderRepository.selectOneByRfxHeaderId(new HeaderQueryDTO(refHeaderId));
        rfxHeaderDTO.initSealedQuotationInfo(this.rfxMemberRepository);
        rfxHeaderDTO.setHeaderProperties();
        return rfxHeaderDTO.getRfxStatus();
    }
}
