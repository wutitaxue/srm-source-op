package org.srm.source.cux.api.controller.v1;

import com.alibaba.fastjson.JSONObject;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.RcwlDBSPTGDTO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;
import org.srm.source.rfx.api.dto.CheckPriceDTO;
import org.srm.source.rfx.api.dto.CheckPriceHeaderDTO;
import org.srm.source.rfx.api.dto.RfxHeaderDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxLineItemService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;
import org.srm.source.rfx.infra.mapper.RfxHeaderMapper;
import org.srm.source.share.domain.entity.ProjectLineSection;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(
        tags = {"Rcwl To Submit Calibration Approval"}
)
@RestController("rcwlToSubmitCalibrationApprovalController.v1")
@RequestMapping({"/v1/toSubmitCalibrationApproval"})
public class RcwlToSubmitCalibrationApprovalController extends BaseController {
    @Autowired
    private RcwlCalibrationApprovalService rcwlCalibrationApprovalService;
    @Resource
    private RfxHeaderService rfxHeaderService;
    @Resource
    private RfxHeaderRepository rfxHeaderRepository;
    @Resource
    private RfxHeaderMapper rfxHeaderMapper;
    @Autowired
    private RfxLineItemService rfxLineItemService;

    @ApiOperation("定标提交")
    @Permission(
            level = ResourceLevel.ORGANIZATION
    )
    @PostMapping({"/check/submit/for/bpm"})
    public ResponseCalibrationApprovalData checkPriceSubmit( @RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
//        rcwlDBSPTGDTO.setTenantId(organizationId);
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
//        DetailsHelper.setCustomUserDetails(rcwlDBSPTGDTO.getTenantId(),"zh_CN");
        //获取头ID
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum(),rcwlDBSPTGDTO.getTenantId());
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        //填充DTO数据
        CheckPriceHeaderDTO checkPriceHeaderDTO = this.getCheckPriceHeaderDTOByData(rfxHeaderId,rcwlDBSPTGDTO.getTenantId());
        try{
            this.rfxHeaderService.checkPriceSubmitValidate(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId, checkPriceHeaderDTO);
        }catch (Exception e){
            responseData.setCode("201");
            responseData.setMessage("数据校验失败！");
        }
        CheckPriceHeaderDTO validPriceHeaderDTO = this.rfxHeaderService.latestValidCreateItemCheck(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId, checkPriceHeaderDTO);
        if(!BaseConstants.Flag.NO.equals(validPriceHeaderDTO.getCreateItemFlag())) {
            responseData.setCode("201");
            responseData.setMessage("操作驳回！");
        } else {
            this.validObject(checkPriceHeaderDTO, new Class[0]);
            try{
                log.info("+++++++++++++++++++++++++++++++++++++++开始+++++++++++++++++++++++++++++++++++++++++++");
//                RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
//                this.rfxEventUtil.eventSend("SSRC_RFX_CHECK_SUBMIT", "CHECK_SUBMIT", rfxHeader);
                rcwlCalibrationApprovalService.checkSubmitCommon(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId, checkPriceHeaderDTO);
                log.info("++++++++++++++++++++++++++++++++++++++结束++++++++++++++++++++++++++++++++++++++++++++");

            }catch (Exception e){
                responseData.setCode("201");
                responseData.setMessage("定标提交失败！失败原因::::"+e.getMessage());
                log.info("++++++++++++++++++++++++++++++++++++++e.getMessage():"+e.getMessage()+"++++++++++++++++++++++++++++++++++++++++++++");
            }
        }
        return responseData;
    }

    public CheckPriceHeaderDTO getCheckPriceHeaderDTOByData(Long rfxHeaderId,Long tenantId){
        RfxHeaderDTO rfxHeaderDTO = new RfxHeaderDTO();
        Long roundNumber =  rcwlCalibrationApprovalService.getRoundNumber(rfxHeaderId,tenantId);
        rfxHeaderDTO.setRfxHeaderId(rfxHeaderId);
        rfxHeaderDTO.setRoundNumber(roundNumber);
        CheckPriceHeaderDTO checkPriceHeaderDTO = new CheckPriceHeaderDTO();
        //获取询价单头表信息
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
//        checkPriceHeaderDTO.setAttributeVarchar1(rfxHeader.getAttributeVarchar1());//招采模式
//        checkPriceHeaderDTO.setAttributeVarchar8(rfxHeader.getAttributeVarchar8());//招采模式
        //数值参数
        checkPriceHeaderDTO.setTotalPrice(rfxHeaderMapper.selectRfxTotalPrice(rfxHeaderDTO));
        checkPriceHeaderDTO.setTotalCost(rfxHeaderDTO.getTotalCost());
        checkPriceHeaderDTO.setRfxHeaderId(rfxHeader.getRfxHeaderId());
        checkPriceHeaderDTO.setCostRemark(rfxHeader.getCostRemark());
        checkPriceHeaderDTO.setObjectVersionNumber(rfxHeader.getObjectVersionNumber());
        checkPriceHeaderDTO.setCheckAttachmentUuid(rfxHeader.getCheckAttachmentUuid());
        checkPriceHeaderDTO.setSuppAttachmentUuid(rfxHeader.getTechAttachmentUuid() == null ? rfxHeader.getBusinessAttachmentUuid():rfxHeader.getTechAttachmentUuid());
        checkPriceHeaderDTO.setReleaseItemIds(null);
        checkPriceHeaderDTO.setPriceEffectiveDate(rfxHeader.getPriceEffectiveDate() == null ? "":rfxHeader.getPriceEffectiveDate().toString());
        checkPriceHeaderDTO.setPriceExpiryDate(rfxHeader.getPriceExpiryDate() == null ? "":rfxHeader.getPriceExpiryDate().toString());
        checkPriceHeaderDTO.setCheckRemark(rfxHeader.getCheckRemark());
        checkPriceHeaderDTO.setCreateItemFlag(rfxHeader.getItemGeneratePolicy() == null ? null:Integer.parseInt(rfxHeader.getItemGeneratePolicy()));
        checkPriceHeaderDTO.setProjectName(rfxHeader.getSourceProjectName());
        checkPriceHeaderDTO.setSelectionStrategy("RECOMMENDATION");
//        checkPriceHeaderDTO.setSelectSectionReadFlag(rfxHeader.getSealedQuotationFlag());
        checkPriceHeaderDTO.setOnlyAllowAllWinBids(rfxHeader.getOnlyAllowAllWinBids());
        //获取checkPriceDTOLineList所需值
        List<CheckPriceDTO> checkPriceDTOLineList = new ArrayList<>();
        //获取ssrc_rfx_quotation_header   ID
        List<String> quotationHeaderIDs =  rcwlCalibrationApprovalService.getQuotationHeaderIDByRfxHeaderId(rfxHeaderId,tenantId);

        List<RfxLineItem> rfxLineItemList = new ArrayList<>();
        //获取具体行ID
        List<Long> l = rcwlCalibrationApprovalService.getRfxLineItemIdByRfxHeaderId(rfxHeaderId);
        for(Long id : l){
            rfxLineItemList.add(rfxLineItemService.selectByPrimaryKey(id));
        }
//        for(String id : quotationHeaderIDs){
        for(int i = 0;i<rfxLineItemList.size();i++){
            RfxLineItem rfxLineItem = rfxLineItemList.get(i);
//            RfxQuotationHeader quotationHeader = (RfxQuotationHeader)rfxQuotationHeaderService.getQuotationHeader(Long.valueOf(id));
//            RfxQuotationLine rfxQuotationLineData = rcwlCalibrationApprovalService.getRfxQuotationLineDataByQuotationHeaderIDs(id);
            CheckPriceDTO checkPriceDTO = new CheckPriceDTO();
//            checkPriceDTO.setSupplierName(quotationHeader.getSupplierCompanyName());
            checkPriceDTO.setSelectionStrategy(rfxLineItem.getSelectionStrategy());//选择策略    物料明细
            checkPriceDTO.setRfxLineItemId(rfxLineItem.getRfxLineItemId());//物料行IDrfxLineItemList.get(0).getRfxLineItemId()
            checkPriceDTO.setRfxLineItemNum(rfxLineItem.getRfxLineItemNum());//rfxLineItemList.get(0).getRfxLineItemNum()
            checkPriceDTO.setType("ITEM");
            checkPriceDTO.setObjectVersionNumber(rfxLineItem.getObjectVersionNumber());//物料行版本号//rfxLineItemList.get(0).getObjectVersionNumber()
//            checkPriceDTO.setWholeSuggestFlag(rfxQuotationLineData.getSuggestedFlag());//整包推荐//ssrc_rfx_quotation_line.suggested_flag  1
//            checkPriceDTO.setSuggestedRemark(rfxQuotationLineData.getSuggestedRemark());//ssrc_rfx_quotation_line.Suggested_Remark
//            checkPriceDTO.setAllottedQuantity(rfxQuotationLineData.getAllottedQuantity());//ssrc_rfx_quotation_line
//            checkPriceDTO.setAllottedRatio(rfxQuotationLineData.getAllottedRatio());//ssrc_rfx_quotation_line
//            checkPriceDTO.setChangePercent(rfxQuotationLineData.getChangePercent());//ssrc_rfx_quotation_line
            checkPriceDTO.setRfxLineSupplierId(null);//rfxQuotationLine    整包推荐的行的勾选ID/物料明细
//            checkPriceDTO.setSupplierTenantId(quotationHeader.getSupplierTenantId());//rfxQuotationLine
//            checkPriceDTO.setQuotationHeaderId(quotationHeader.getQuotationHeaderId());
            //放一个RfxQuotationLine    list
//            List<RfxQuotationLine> quotationLineList = rcwlCalibrationApprovalService.getQuotationLineListByQuotationHeaderID(Long.valueOf(id));
//            List<RfxQuotationLine> quotationLineList = new ArrayList<>();
//            checkPriceDTO.setQuotationLineList(quotationLineList);
            //加入上层对象
            checkPriceDTOLineList.add(checkPriceDTO);
        }
        checkPriceHeaderDTO.setCheckPriceDTOLineList(checkPriceDTOLineList);
//        checkPriceHeaderDTO.setRfxLineItemList(rfxLineItemList);
        //项目集合
        List<ProjectLineSection> projectLineSectionList = new ArrayList<>();

//        checkPriceHeaderDTO.setProjectLineSectionList(projectLineSectionList);
        List<ProjectLineSection> allProjectLineSectionList = new ArrayList<>();

//        checkPriceHeaderDTO.setAllProjectLineSectionList(allProjectLineSectionList);
        String json = JSONObject.toJSONString(checkPriceHeaderDTO);
        log.info("====================一========================="+json);
        return  checkPriceHeaderDTO;
    }

}