package org.srm.source.cux.infra.feign.fallback;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.srm.source.cux.infra.feign.RcwlSpucRemoteService;
import org.srm.source.share.api.dto.PrLine;

import java.util.List;

/**
 *
 * spuc feign调用
 *
 * @author furong.tang@hand-china.com
 */
@Component
public class RcwlSpucRemoteServiceImpl implements RcwlSpucRemoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcwlSpucRemoteServiceImpl.class);

    @Override
    public ResponseEntity<Void> feignUpdatePrLine(Long organizationId, List<PrLine> prLines) {
        LOGGER.error("Failed to get feignUpdatePrLine param:{}", JSON.toJSONString(prLines));
        return null;
    }
}
