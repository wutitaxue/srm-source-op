package org.srm.source.cux.api.controller.v1;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.srm.source.cux.app.service.RcwlCalibrationApprovalService;
import org.srm.source.cux.domain.entity.RcwlDBSPTGDTO;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalVO;
import org.srm.source.cux.domain.entity.RcwlUpdateCalibrationApprovalDataVO;
import org.srm.source.cux.domain.entity.ResponseCalibrationApprovalData;
import org.srm.source.rfx.api.dto.CheckPriceDTO;
import org.srm.source.rfx.api.dto.CheckPriceHeaderDTO;
import org.srm.source.rfx.app.service.RfxHeaderService;
import org.srm.source.rfx.app.service.RfxLineItemService;
import org.srm.source.rfx.app.service.RfxQuotationHeaderService;
import org.srm.source.rfx.app.service.RfxQuotationLineService;
import org.srm.source.rfx.domain.entity.RfxHeader;
import org.srm.source.rfx.domain.entity.RfxLineItem;
import org.srm.source.rfx.domain.entity.RfxQuotationHeader;
import org.srm.source.rfx.domain.entity.RfxQuotationLine;
import org.srm.source.rfx.domain.repository.RfxHeaderRepository;

import java.util.ArrayList;
import java.util.List;

@Api(
        tags = {"Rcwl Update Calibration Approval"}
)
@RestController("rcwlUpdateCalibrationApprovalController.v1")
@RequestMapping({"/v1/rcwlUpdateCalibrationApprovalController"})
public class RcwlUpdateCalibrationApprovalController extends BaseController {
    @Autowired
    private RcwlCalibrationApprovalService rcwlCalibrationApprovalService;
    @Autowired
    private RfxHeaderService rfxHeaderService;
    @Autowired
    private RfxQuotationHeaderService rfxQuotationHeaderService;
    @Autowired
    private RfxQuotationLineService rfxQuotationLineService;
    @Autowired
    private RfxLineItemService rfxLineItemService;
    @Autowired
    private RfxHeaderRepository rfxHeaderRepository;

    @ApiOperation("更新定标字段")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/update-calibration-approval"})
    public ResponseCalibrationApprovalData updateClarifyData(@RequestBody RcwlUpdateCalibrationApprovalVO rcwlUpdateDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        RcwlUpdateCalibrationApprovalDataVO rcwlUpdateDataDTO = rcwlUpdateDTO.getRcwlUpdateCalibrationApprovalDataVO();
        if(rcwlUpdateDataDTO.getRfxNum() == null || "".equals(rcwlUpdateDataDTO.getRfxNum())){
            responseData.setCode("201");
            responseData.setMessage("单据编号获取异常！");
            return responseData;
        }
        if((rcwlUpdateDataDTO.getAttributeVarchar4() == null || "".equals(rcwlUpdateDataDTO.getAttributeVarchar4()))&&
                (rcwlUpdateDataDTO.getAttributeVarchar5() == null || "".equals(rcwlUpdateDataDTO.getAttributeVarchar5()))){
            responseData.setCode("201");
            responseData.setMessage("所需更新数据至少有一个值！");
            return responseData;
        }
        responseData = rcwlCalibrationApprovalService.updateClarifyData(rcwlUpdateDataDTO);
        return responseData;
    }


    @ApiOperation("定标审批通过")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/check/approved/for/bpm"})
    public ResponseCalibrationApprovalData checkPriceApproved(@RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        DetailsHelper.setCustomUserDetails(rcwlDBSPTGDTO.getTenantId(),"zh_CN");
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum());
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        try{
            RfxHeader rfxHeaderDb = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
            DetailsHelper.getUserDetails().setUserId(rfxHeaderDb.getCreatedBy());
            if (!"CHECK_REJECTED".equals(rfxHeaderDb.getRfxStatus()) && !"FINISHED".equals(rfxHeaderDb.getRfxStatus())) {
                rfxHeaderService.checkPriceApproved(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId);
            }else{
                responseData.setCode("201");
                responseData.setMessage("rfx status error!status:{"+rfxHeaderDb.getRfxStatus()+"}！");
            }
        }catch(Exception e){
            responseData.setCode("201");
            responseData.setMessage("操作失败！");
        }
        return responseData;
    }

    @ApiOperation("定标审批拒绝")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/check/reject/for/bpm"})
    public ResponseCalibrationApprovalData checkPriceReject(@RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum());
//        DetailsHelper.getUserDetails().setUserId(rcwlDBSPTGDTO.getTenantId());
        DetailsHelper.setCustomUserDetails(rcwlDBSPTGDTO.getTenantId(),"zh_CN");
        responseData.setCode("200");
        responseData.setMessage("操作成功！");
        try{
            rfxHeaderService.checkPriceReject(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId);
        }catch(Exception e){
            responseData.setCode("201");
            responseData.setMessage("操作失败！");
        }
        return responseData;
    }

    @ApiOperation("定标提交")
    @Permission(
            permissionPublic = true
    )
    @PostMapping({"/check/submit/for/bpm"})
    public ResponseCalibrationApprovalData checkPriceSubmit(@RequestBody RcwlDBSPTGDTO rcwlDBSPTGDTO) {
        ResponseCalibrationApprovalData responseData = new ResponseCalibrationApprovalData();
//        DetailsHelper.setCustomUserDetails(rcwlDBSPTGDTO.getTenantId(),"zh_CN");
        //获取头ID
        Long rfxHeaderId = rcwlCalibrationApprovalService.getRfxHeaderIdByRfxNum(rcwlDBSPTGDTO.getRfxNum());
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
                this.rfxHeaderService.checkPriceSubmit(rcwlDBSPTGDTO.getTenantId(), rfxHeaderId, checkPriceHeaderDTO);
            }catch (Exception e){
                responseData.setCode("201");
                responseData.setMessage("定标提交失败！");
            }
        }
            return responseData;
    }

    public CheckPriceHeaderDTO getCheckPriceHeaderDTOByData(Long rfxHeaderId,Long tenantId){
        CheckPriceHeaderDTO checkPriceHeaderDTO = new CheckPriceHeaderDTO();
        //获取询价单头表信息
        RfxHeader rfxHeader = (RfxHeader)this.rfxHeaderRepository.selectByPrimaryKey(rfxHeaderId);
//        RfxHeader rfxHeader = rfxHeaderService.selectSimpleRfxHeaderById(rfxHeaderId);
        checkPriceHeaderDTO.setRfxHeaderId(rfxHeader.getRfxHeaderId());
        checkPriceHeaderDTO.setTotalCost(rfxHeader.getTotalCost());
        checkPriceHeaderDTO.setTotalPrice(rfxHeader.getTotalPrice());
        checkPriceHeaderDTO.setCostRemark(rfxHeader.getCostRemark());
        checkPriceHeaderDTO.setObjectVersionNumber(rfxHeader.getObjectVersionNumber());
        checkPriceHeaderDTO.setCheckAttachmentUuid(rfxHeader.getCheckAttachmentUuid());
        checkPriceHeaderDTO.setSuppAttachmentUuid("");
        checkPriceHeaderDTO.setReleaseItemIds("");
        checkPriceHeaderDTO.setPriceEffectiveDate(rfxHeader.getPriceEffectiveDate() == null ? "":rfxHeader.getPriceEffectiveDate().toString());
        checkPriceHeaderDTO.setPriceExpiryDate(rfxHeader.getPriceExpiryDate() == null ? "":rfxHeader.getPriceExpiryDate().toString());
        checkPriceHeaderDTO.setCheckRemark(rfxHeader.getCheckRemark());
        checkPriceHeaderDTO.setCreateItemFlag(0);
        checkPriceHeaderDTO.setProjectName(rfxHeader.getSourceProjectName());
        checkPriceHeaderDTO.setSelectionStrategy("");
        checkPriceHeaderDTO.setSelectSectionReadFlag(rfxHeader.getSealedQuotationFlag());
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
        for(String id : quotationHeaderIDs){
            RfxQuotationHeader quotationHeader = rfxQuotationHeaderService.getQuotationHeader(Long.valueOf(id));
            RfxQuotationLine rfxQuotationLine = new RfxQuotationLine();
            CheckPriceDTO checkPriceDTO = new CheckPriceDTO();
            checkPriceDTO.setSupplierName(quotationHeader.getSupplierCompanyName());
            checkPriceDTO.setSelectionStrategy("RECOMMENDATION");//选择策略
            checkPriceDTO.setRfxLineItemId(null);//物料行IDrfxLineItemList.get(0).getRfxLineItemId()
            checkPriceDTO.setRfxLineItemNum(null);//rfxLineItemList.get(0).getRfxLineItemNum()
            checkPriceDTO.setType("");
            checkPriceDTO.setObjectVersionNumber(null);//物料行版本号//rfxLineItemList.get(0).getObjectVersionNumber()
            checkPriceDTO.setWholeSuggestFlag(null);//整包推荐//rfxQuotationLine
            checkPriceDTO.setRfxLineSupplierId(null);//rfxQuotationLine
            checkPriceDTO.setSupplierTenantId(null);//rfxQuotationLine
            checkPriceDTO.setQuotationHeaderId(quotationHeader.getQuotationHeaderId());
            checkPriceDTO.setAllottedQuantity(null);//rfxQuotationLine
            checkPriceDTO.setSuggestedRemark(null);//rfxQuotationLine
            checkPriceDTO.setAllottedRatio(null);//rfxQuotationLine
            checkPriceDTO.setChangePercent(null);//rfxQuotationLine
            //放一个RfxQuotationLine    list
            List<RfxQuotationLine> quotationLineList = rcwlCalibrationApprovalService.getQuotationLineListByQuotationHeaderID(Long.valueOf(id));
            checkPriceDTO.setQuotationLineList(quotationLineList);
            //加入上层对象
            checkPriceDTOLineList.add(checkPriceDTO);
        }
        checkPriceHeaderDTO.setCheckPriceDTOLineList(checkPriceDTOLineList);
        checkPriceHeaderDTO.setRfxLineItemList(rfxLineItemList);
        return  checkPriceHeaderDTO;
    }
}
