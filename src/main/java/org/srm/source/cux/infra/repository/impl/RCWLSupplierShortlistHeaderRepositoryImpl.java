package org.srm.source.cux.infra.repository.impl;

import org.springframework.stereotype.Component;
import org.srm.source.share.domain.vo.PrLineVO;
import org.srm.source.cux.domain.entity.RCWLSample;
import org.srm.source.cux.domain.entity.RCWLShortlistHeader;
import org.srm.source.cux.domain.entity.RCWLSupplierHeader;
import org.srm.source.cux.domain.repository.RCWLShortlistHeaderRepository;
import org.srm.source.cux.infra.constant.RCWLShortlistContants;
import org.srm.source.cux.infra.mapper.RCWLShortlistHeaderMapper;
import org.srm.web.annotation.Tenant;

import javax.annotation.Resource;
import java.util.List;

@Component
@Tenant(RCWLShortlistContants.TENANT_NUMBER)
public class RCWLSupplierShortlistHeaderRepositoryImpl implements RCWLShortlistHeaderRepository {
    @Resource
    private RCWLShortlistHeaderMapper rcwlShortlistHeaderMapper;

    @Override
    public List<RCWLShortlistHeader> listShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader) {
        return rcwlShortlistHeaderMapper.listShortlistHeader(rcwlShortlistHeader);
    }

    @Override
    public List<RCWLShortlistHeader> listShortlistHeaderSupplier(RCWLShortlistHeader rcwlShortlistHeader) {
        return rcwlShortlistHeaderMapper.listShortlistHeaderSupplier(rcwlShortlistHeader);
    }

    @Override
    public RCWLShortlistHeader selectOneShortlistHeader(Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.selectOneShortlistHeader(shortlistHeaderId);
    }

    @Override
    public List<RCWLSupplierHeader> listShortlistSupplier(RCWLSupplierHeader rcwLSupplierHeader, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.listShortlistSupplier(rcwLSupplierHeader, shortlistHeaderId);
    }

    @Override
    public List<PrLineVO> listPrline(PrLineVO prline, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.listPrline(prline, shortlistHeaderId);
    }

    @Override
    public List<RCWLSample> listSample(RCWLSample rcwlSample, Long shortlistHeaderId) {
        return rcwlShortlistHeaderMapper.listSample(rcwlSample, shortlistHeaderId);
    }

    @Override
    public RCWLSample selectOneSample(Long sampleId) {
        return rcwlShortlistHeaderMapper.selectOneSample(sampleId);
    }

    @Override
    public void insertShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader) {
        rcwlShortlistHeaderMapper.insertShortlistHeader(rcwlShortlistHeader);
    }

    @Override
    public void updateShortlistHeader(RCWLShortlistHeader rcwlShortlistHeader) {
        rcwlShortlistHeaderMapper.updateShortlistHeader(rcwlShortlistHeader);
    }

    @Override
    public void deleteShortlistHeader(Long shortlistHeaderId) {
        rcwlShortlistHeaderMapper.deleteShortlistHeader(shortlistHeaderId);
    }

    @Override
    public PrLineVO selectOnePrline(Long prLineId) {
        return rcwlShortlistHeaderMapper.selectOnePrline(prLineId);
    }

    @Override
    public void updatePrline(PrLineVO prLine) {
        rcwlShortlistHeaderMapper.updatePrline(prLine);
    }

    @Override
    public void insertSample(RCWLSample rcwlSample) {
        rcwlShortlistHeaderMapper.insertSample(rcwlSample);
    }

    @Override
    public void updateSample(RCWLSample rcwlSample) {
        rcwlShortlistHeaderMapper.updateSample(rcwlSample);
    }

}
