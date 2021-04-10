package org.srm.source.cux.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.cux.infra.mapper.RCWLShortlistHeaderMapper;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.rfx.infra.constant.SourceConstants;
import org.srm.source.share.domain.entity.User;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.cux.app.service.RCWLShortlistHeaderService;
import org.srm.source.cux.domain.entity.RCWLSample;
import org.srm.source.cux.domain.entity.RCWLShortlistHeader;
import org.srm.source.cux.domain.entity.RCWLSupplierHeader;
import org.srm.source.cux.domain.repository.RCWLShortlistHeaderRepository;
import org.srm.source.cux.infra.constant.RCWLShortlistContants;
import org.srm.web.annotation.Tenant;

import javax.annotation.Resource;
import java.util.List;

@Service
@Tenant(RCWLShortlistContants.TENANT_NUMBER)
public class RCWLShortlistHeaderServiceImpl implements RCWLShortlistHeaderService {
    @Resource
    private RCWLShortlistHeaderRepository rcwlShortlistHeaderRepository;
    @Autowired
    private CodeRuleBuilder codeRuleBuilder;
    @Autowired
    private CommonQueryRepository commonQueryRepository;

    @Autowired
    private RCWLShortlistHeaderMapper rcwlShortlistHeaderMapper;

    @Override
    public Page<RCWLShortlistHeader> listShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderRepository.listShortlistHeader(rcwlShortlistHeader));
    }

    @Override
    public Page<RCWLShortlistHeader> listShortlistHeaderSupplier(RCWLShortlistHeader rcwlShortlistHeader, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderRepository.listShortlistHeaderSupplier(rcwlShortlistHeader));
    }

    @Override
    public RCWLShortlistHeader selectOneShortlistHeader(Long shortlistHeaderId) {
        return rcwlShortlistHeaderRepository.selectOneShortlistHeader(shortlistHeaderId);
    }

    @Override
    public Page<RCWLSupplierHeader> listShortlistSupplier(RCWLSupplierHeader rcwLSupplierHeader, PageRequest pageRequest, Long shortlistHeaderId) {
        RCWLShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectOneShortlistHeader(shortlistHeaderId);
        rcwLSupplierHeader.setContacts(DetailsHelper.getUserDetails().getRealName());
        User user = commonQueryRepository.getUserInfoById(DetailsHelper.getUserDetails().getUserId());
        rcwLSupplierHeader.setPhone(user.getPhone());

        String str1 = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        if (rcwLSupplierHeader.getCapital() < rcwlShortlistHeader.getCapital()) {
            str1 = "注册资本不符合";
        }
        if (rcwLSupplierHeader.getYears() < rcwlShortlistHeader.getYears()) {
            str2 = "成立年限不符合";
        }
        if (rcwLSupplierHeader.getOneProfit() < rcwlShortlistHeader.getOneProfit()) {
            str2 = "一年营收不符合";
        }
        if (rcwLSupplierHeader.getTwoProfit() < rcwlShortlistHeader.getTwoProfit()) {
            str2 = "两年营收不符合";
        }
        if (str1 != null && str2 != null && str3 != null && str4 != null) {
            rcwLSupplierHeader.setQualificationInfo("全部符合");
            rcwLSupplierHeader.setQualification(1);
        } else {
            rcwLSupplierHeader.setQualificationInfo(str1 + str2 + str3 + str4);
            rcwLSupplierHeader.setQualification(0);
        }
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderRepository.listShortlistSupplier(rcwLSupplierHeader, shortlistHeaderId));
    }

    @Override
    public Page<PrLineVO> listPrline(Long tenantId,PrLineVO prLine, PageRequest pageRequest, Long shortlistHeaderId) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderRepository.listPrline(tenantId,prLine, shortlistHeaderId));
    }

    @Override
    public Page<RCWLSample> listSample(RCWLSample rcwlSample, PageRequest pageRequest, Long shortlistHeaderId) {
        return PageHelper.doPageAndSort(pageRequest, () -> rcwlShortlistHeaderRepository.listSample(rcwlSample, shortlistHeaderId));
    }

    @Override
    public RCWLSample selectOneSample(Long sampleId) {
        return rcwlShortlistHeaderRepository.selectOneSample(sampleId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RCWLShortlistHeader createOrUpdateShortlistHeader(RCWLShortlistHeader rcwLShortlistHeader) {
        rcwLShortlistHeader.setCreatedBy(DetailsHelper.getUserDetails().getUserId());
        if (rcwlShortlistHeaderRepository.selectOneShortlistHeader(rcwLShortlistHeader.getShortlistHeaderId()) == null) {
            rcwLShortlistHeader.setShortlistNum(codeRuleBuilder.generateCode(DetailsHelper.getUserDetails().getTenantId(), SourceConstants.CodeRule.RFX_NUM, "GLOBAL", "GLOBAL", null));
            rcwLShortlistHeader.setShortlistNum("RW" + rcwLShortlistHeader.getShortlistNum().substring(3));
            rcwlShortlistHeaderRepository.insertShortlistHeader(rcwLShortlistHeader);
        } else {
            rcwlShortlistHeaderRepository.updateShortlistHeader(rcwLShortlistHeader);
        }
        return rcwLShortlistHeader;
    }

    @Override
    public RCWLShortlistHeader purchaseRequisitionToBeShortlisted(Long tenantId,List<Long> prLineIds) {
        String str = codeRuleBuilder.generateCode(DetailsHelper.getUserDetails().getTenantId(), SourceConstants.CodeRule.RFX_NUM, "GLOBAL", "GLOBAL", null);
        String shortlistNum = "RW" + str.substring(3);
        RCWLShortlistHeader rcwlShortlistHeader = new RCWLShortlistHeader();
        for (Long prLineId : prLineIds) {
            PrLineVO prLine = rcwlShortlistHeaderRepository.selectOnePrline(tenantId,prLineId);
            if (prLine == null) {
                throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
            }
            prLine.setAttributeVarchar2(shortlistNum);
            //更新采购申请行的入围单号
            rcwlShortlistHeaderRepository.updatePrline(prLine);
        }
        //创建入围单
        rcwlShortlistHeader.setShortlistNum(shortlistNum);
        rcwlShortlistHeaderMapper.insertSelective(rcwlShortlistHeader);
        //返回入围id
        return rcwlShortlistHeader;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelShortlistHeader(Long shortlistHeaderId, List<PrLineVO> prLineList) {
        RCWLShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectOneShortlistHeader(shortlistHeaderId);
        if (rcwlShortlistHeader.getState().equals("NEW") || rcwlShortlistHeader.getState().equals("REJECTED")) {
            rcwlShortlistHeaderRepository.deleteShortlistHeader(shortlistHeaderId);
            for (PrLineVO prLine : prLineList) {
                rcwlShortlistHeaderRepository.updatePrline(prLine);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitShortlistHeader(Long shortlistHeaderId) {
        RCWLShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectOneShortlistHeader(shortlistHeaderId);
        if (rcwlShortlistHeader.getShortlistCategory().equals("INVITATION")) {
            rcwlShortlistHeader.setExamine(1);
            rcwlShortlistHeaderRepository.updateShortlistHeader(rcwlShortlistHeader);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseShortlistHeader(Long shortlistHeaderId) {
        RCWLShortlistHeader rcwlShortlistHeader = rcwlShortlistHeaderRepository.selectOneShortlistHeader(shortlistHeaderId);
        if (rcwlShortlistHeader.getShortlistCategory().equals("PUBLIC")) {
            rcwlShortlistHeader.setPublish(1);
            rcwlShortlistHeaderRepository.updateShortlistHeader(rcwlShortlistHeader);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrUpdateSample(RCWLSample rcwlSample, Long companyId) {
        rcwlSample.setCompanyId(companyId);
        if (rcwlShortlistHeaderRepository.selectOneSample(rcwlSample.getSampleId()) == null) {
            rcwlShortlistHeaderRepository.insertSample(rcwlSample);
        } else {
            rcwlShortlistHeaderRepository.updateSample(rcwlSample);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertSample(List<RCWLSample> rcwlSamples, Long companyId) {
        for (RCWLSample rcwlSample : rcwlSamples) {
            rcwlSample.setCompanyId(companyId);
            rcwlShortlistHeaderRepository.insertSample(rcwlSample);
        }
    }




   /*@Scheduled(fixedRate = 259200000)
    public void doPostJson() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();
        CloseableHttpResponse response = null;
        String resultString = "";
        Gson gson = new Gson();
        SendJson.IREQUESTDTO.REQBASEINFODTO reqBaseInfo = new SendJson.IREQUESTDTO.REQBASEINFODTO();
        SendJson.IREQUESTDTO.MESSAGEDTO.REQITEMDTO reqItem = new SendJson.IREQUESTDTO.MESSAGEDTO.REQITEMDTO();
        int pageNo = reqItem.getPageNo();
        int pageRowNo = reqItem.getPageRowNo();
        if (pageRowNo != 0) {
            do {
                try {
                    //url
                    HttpPost httpPost = new HttpPost("http://esbqas.sunac.com.cn:8001/WP_SUNAC/APP_PUBLIC_SERVICES/Proxy_Services/TA_IDM/PUBLIC_SUNAC_629_queryIdmUserRestful_PS");
                    httpPost.setConfig(requestConfig);
                    String str1 = gson.toJson(reqBaseInfo);
                    String str2 = gson.toJson(reqItem);
                    String strJson = "{\n" +
                            "  \"I_REQUEST\": {\n" +
                            "    \"REQ_BASEINFO\":" + str1 + ",\n" +
                            "    \"MESSAGE\": {\n" +
                            "      \"REQ_ITEM\": " + str2 + "\n" +
                            "    }\n" +
                            "  }\n" +
                            "}";
                    StringEntity entity = new StringEntity(strJson, ContentType.APPLICATION_JSON);
                    httpPost.setEntity(entity);
                    response = httpClient.execute(httpPost);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        resultString = EntityUtils.toString(response.getEntity(), "utf-8");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                ReceiveJson receiveJson = gson.fromJson(resultString, ReceiveJson.class);
                List<ReceiveJson.ERESPONSEDTO.MESSAGEDTO.LISTDTO> list = receiveJson.getEResponse().getMessage().getList();
                if (!list.equals("")) {
                    for (ReceiveJson.ERESPONSEDTO.MESSAGEDTO.LISTDTO lists : list) {

                    }
                }
                pageNo++;
                pageRowNo = pageRowNo - 100;
            } while (pageRowNo > 0);
        }*/
}

