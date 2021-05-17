package org.srm.source.cux.app.service;

import org.srm.source.rfx.domain.vo.RfxFullHeader;

public interface RcwlRfxHeaderBpmService {

    String rcwlReleaseRfx (Long organizationId, RfxFullHeader rfxFullHeader);
}
