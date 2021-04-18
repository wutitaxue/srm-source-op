package org.srm.source.cux.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.srm.source.cux.infra.feign.fallback.RcwlSpucRemoteServiceImpl;
import org.srm.source.share.api.dto.PrLine;

import java.util.List;

/**
 * <p>
 * spuc feign
 * <p>
 *
 * @author furong.tang@hand-china.com
 */
@FeignClient(value = "srm-purchase-cooperation", fallback = RcwlSpucRemoteServiceImpl.class, path = "/v1/")
public interface RcwlSpucRemoteService {

    @PostMapping({"{organizationId}/purchase-requests/source/lines"})
    ResponseEntity<Void> feignUpdatePrLine(@PathVariable Long organizationId, @RequestBody List<PrLine> prLines);

}
