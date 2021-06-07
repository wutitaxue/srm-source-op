package org.srm.source.cux.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListFilesDto;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListPrDTO;
import org.srm.source.cux.api.controller.v1.dto.RcwlBpmShortListSuppierDTO;
import org.srm.source.cux.app.service.RcwlSupplierHeaderService;
import org.springframework.stereotype.Service;
import org.srm.source.cux.infra.mapper.RcwlShortlistHeaderMapper;

import java.util.List;

/**
 * project code-program code对应关系定义应用服务默认实现
 *
 * @author furong.tang@hand-china.com 2021-04-15 19:39:45
 */
@Service
public class RcwlSupplierHeaderServiceImpl implements RcwlSupplierHeaderService {
    @Autowired
    RcwlShortlistHeaderMapper rcwlShortlistHeaderMapper;

    /**
     * 查询供应商
     *
     * @param tenantId
     * @param shortlistHeaderId
     * @return
     */
    @Override
    public List<RcwlBpmShortListSuppierDTO> rcwlSelectBpmSuppier(Long tenantId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.rcwlSelectBpmSuppier(tenantId, shortlistHeaderId);
    }

    /**
     * 查询pr
     *
     * @param tenantId
     * @param shortlistHeaderId
     * @return
     */
    @Override
    public List<RcwlBpmShortListPrDTO> rcwlSelectBpmPr(Long tenantId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.rcwlSelectBpmPr(tenantId, shortlistHeaderId);
    }

    /**
     * 查询附件
     *
     * @param tenantId
     * @param shortlistHeaderId
     * @return
     */
    @Override
    public List<RcwlBpmShortListFilesDto> rcwlSelectBpmFile(Long tenantId, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.rcwlSelectBpmFile(tenantId, shortlistHeaderId);
    }
}
