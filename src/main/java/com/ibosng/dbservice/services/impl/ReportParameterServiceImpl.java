package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportParameter;
import com.ibosng.dbservice.repositories.ReportParameterRepository;
import com.ibosng.dbservice.services.ReportParameterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportParameterServiceImpl implements ReportParameterService {

    private final ReportParameterRepository reportParameterRepository;

    public ReportParameterServiceImpl(ReportParameterRepository reportParameterRepository) {
        this.reportParameterRepository = reportParameterRepository;
    }


    @Override
    public List<ReportParameter> findAll() {
        return reportParameterRepository.findAll();
    }

    @Override
    public Optional<ReportParameter> findById(Integer id) {
        return reportParameterRepository.findById(id);
    }

    @Override
    public ReportParameter save(ReportParameter object) {
        return reportParameterRepository.save(object);
    }

    @Override
    public List<ReportParameter> saveAll(List<ReportParameter> objects) {
        return reportParameterRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        reportParameterRepository.deleteById(id);
    }

    @Override
    public List<ReportParameter> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<ReportParameter> findAllByReport(Report report) {
        return reportParameterRepository.findAllByReport(report);
    }
}
