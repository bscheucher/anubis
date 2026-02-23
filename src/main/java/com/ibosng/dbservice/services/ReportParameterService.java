package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportParameter;

import java.util.List;

public interface ReportParameterService extends BaseService<ReportParameter> {

    List<ReportParameter> findAllByReport(Report report);
}
