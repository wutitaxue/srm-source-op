package org.srm.source.cux.app.service;

import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListFilesDto;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListPrDTO;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListSuppierDTO;

import java.util.List;

/**
 * project code-program code对应关系定义应用服务
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
public interface RcwlSupplierHeaderService {

    /**
     * 查询供应商
     * @param tenantId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlBpmShortListSuppierDTO> rcwlSelectBpmSuppier(Long tenantId, Long shortlistHeaderId);

    /**
     * 查询pr
     * @param tenantId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlBpmShortListPrDTO> rcwlSelectBpmPr(Long tenantId, Long shortlistHeaderId);

    /**
     * 查询附件
     * @param tenantId
     * @param shortlistHeaderId
     * @return
     */
    List<RcwlBpmShortListFilesDto> rcwlSelectBpmFile(Long tenantId, Long shortlistHeaderId);
}
