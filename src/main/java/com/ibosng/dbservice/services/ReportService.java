package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportType;

import java.util.List;

public interface ReportService extends BaseService<Report> {
    Report findByReportName(String reportName);

    List<Report> findAllByReportType(ReportType reportType);
}
