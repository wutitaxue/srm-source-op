package org.srm.source.cux.bid.domian.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srm.source.bid.api.dto.BidMemberDTO;
import org.srm.source.bid.domain.entity.BidMember;
import org.srm.source.bid.domain.repository.BidHeaderRepository;
import org.srm.source.bid.domain.service.impl.IBidHeaderDomainServiceImpl;
import org.srm.source.bid.infra.constant.MessageCode;
import org.srm.source.cux.bid.api.dto.RfxHeaderInfoDTO;
import org.srm.source.cux.bid.infra.mapper.RcwlIBidHeaderMapper;
import org.srm.source.rfx.app.service.common.SendMessageHandle;
import org.srm.source.rfx.domain.repository.CommonQueryRepository;
import org.srm.source.share.infra.convertor.CommonConvertor;
import org.srm.source.share.infra.utils.PasswordCreate;
import org.srm.web.annotation.Tenant;

/**
 * description
 *
 * @author xiubing.wang@hand-china.com 2021/09/27 16:44
 */
@Service
@Tenant("SRM-RCWL")
public class RcwlIBidHeaderDomainServiceImpl extends IBidHeaderDomainServiceImpl {
    @Autowired
    private BidHeaderRepository bidHeaderRepository;
    @Autowired
    private CommonQueryRepository commonQueryRepository;
    @Autowired
    private RcwlIBidHeaderMapper rcwlIBidHeaderMapper;
    @Autowired
    private MessageClient messageClient;

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void sendOpenPasswordMailToOpener() {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Date tenMinuteBefore = DateUtils.truncate(new Date(System.currentTimeMillis() + 1200000L), 13);
        List<RfxHeaderInfoDTO> rfxHeaderInfoDTOList = new ArrayList<>();
        rfxHeaderInfoDTOList = rcwlIBidHeaderMapper.selectRfxInfo(tenMinuteBefore);
        rfxHeaderInfoDTOList.forEach((rfxHeaderInfoDTO) -> {
            sendMessageToOpener(rfxHeaderInfoDTO, userDetails);
        });
    }

    public void sendMessageToOpener(RfxHeaderInfoDTO rfxHeaderInfoDTO, CustomUserDetails userDetails) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> map = new ConcurrentHashMap(16);
        map.put("BID_NUMBER", rfxHeaderInfoDTO.getRfxNum());
        map.put("BID_OPEN_DATE", format.format(rfxHeaderInfoDTO.getQuotationEndDate()));
        map.put("BID_TITLE", rfxHeaderInfoDTO.getRfxTitle());
        map.put("currentTime", format.format(new Date()));
        map.put("bidHeaderId", String.valueOf(rfxHeaderInfoDTO.getRfxHeaderId()));
        String serverName = "http://" + this.commonQueryRepository.selectServerName(rfxHeaderInfoDTO.getTenantId());
        map.put("sourceAddress", "<a target='_blank' href='" + serverName + "/app/ssrc/new-inquiry-hall/rfx-detail/"+rfxHeaderInfoDTO.getRfxHeaderId()+"?current=newInquiryHall" + "'>" + SendMessageHandle.getLinkString(userDetails) + "</a>");

        List<Receiver> receivers = new ArrayList<>();
        List<RfxHeaderInfoDTO> openerList = new ArrayList<>();
        openerList = rcwlIBidHeaderMapper.selectRfxOpeners(rfxHeaderInfoDTO.getRfxHeaderId(),rfxHeaderInfoDTO.getTenantId());

        Receiver receiver = new Receiver();
        openerList.forEach(x->{
            receiver.setEmail(x.getEmail());
            receiver.setPhone(x.getPhone());
            receiver.setTargetUserTenantId(0L);
            receivers.add(receiver);
        });

        if (BaseConstants.Flag.YES.equals(rfxHeaderInfoDTO.getPasswordFlag())) {
            String bidPassword;
            for(bidPassword = PasswordCreate.createPassword(); !Pattern.matches("^(?![0-9]+$)(?![a-zA-Z]+$)(?![a-z]+$)(?![!@#$%^&*=]+$)[0-9A-Za-z!@#$%^&*=]{6,30}$", bidPassword); bidPassword = PasswordCreate.createPassword()) {
            }
            map.put("OPEN_PASSWORD", bidPassword);
            messageClient.sendMessage(0L, MessageCode.BID_OPEN_PWD.getMessageCode(), receivers, map);
        } else {
            messageClient.sendMessage(0L,  MessageCode.BID_OPEN_NONE_PWD.getMessageCode(), receivers, map);
        }

        rcwlIBidHeaderMapper.updateSendFlag(rfxHeaderInfoDTO.getTenantId(), rfxHeaderInfoDTO.getRfxHeaderId());
    }
}
