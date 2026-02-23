package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportType;
import com.ibosng.dbservice.repositories.ReportRepository;
import com.ibosng.dbservice.services.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report findByReportName(String reportName){
        return reportRepository.findByReportName(reportName);
    }

    @Override
    public List<Report> findAllByReportType(ReportType reportType) {
        return reportRepository.findAllByReportType(reportType);
    }

    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public Optional<Report> findById(Integer id) {
        return reportRepository.findById(id);
    }

    @Override
    public Report save(Report object) {
        return reportRepository.save(object);
    }

    @Override
    public List<Report> saveAll(List<Report> objects) {
        return reportRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        reportRepository.deleteById(id);
    }

    @Override
    public List<Report> findAllByIdentifier(String identifier) {
        return null;
    }
}
