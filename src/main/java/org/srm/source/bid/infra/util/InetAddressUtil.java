package org.srm.source.bid.infra.util;


import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.srm.source.rfx.app.service.impl.RfxHeaderServiceImpl;
import org.srm.source.share.infra.constant.ShareConstants;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

@Component
public class InetAddressUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RfxHeaderServiceImpl.class);

    public InetAddress getInetAddress() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        try {
            //获取服务器域名
            String serverName = httpServletRequest.getServerName();
            InetAddress inetAddress = InetAddress.getByName(serverName);
            return inetAddress;
        } catch (Exception e) {
            throw new CommonException(ShareConstants.ErrorCode.ERROR_INET_ADDRESS);
        }
    }

    /**
     * 隆剑哥把真实ip放在 X-Real-IP 中所以直接从这里取值
     * 改造从X-Forwarded-For获取
     * @return java.lang.String
     * @author zhenyu.yang@hand-china.com 2020-07-28 13:57
     */
    public String getLocalHostIp() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = httpServletRequest.getHeader("X-Real-IP");
        LOGGER.debug("ip1:{}", httpServletRequest.getHeader("X-Forwarded-For"));
        LOGGER.debug("ip2:{}", httpServletRequest.getHeader("X-Real-IP"));
        LOGGER.debug("ip3:{}", httpServletRequest.getRemoteAddr());
//        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
        String forwardedIp = httpServletRequest.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(forwardedIp) && !"unKnown".equalsIgnoreCase(forwardedIp)) {
            if(StringUtils.contains(forwardedIp,",")){
                List<String> strings = Arrays.asList(forwardedIp.split(","));
                ip = strings.get(0);
                return ip;
            }
        }

        return httpServletRequest.getRemoteAddr();
    }

}