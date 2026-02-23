package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.ReportDto;
import com.ibosng.dbservice.entities.reports.ReportType;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface JasperReportService {
    void uploadReportTemplate(MultipartFile file, String reportName) throws IOException;

    PayloadResponse getReports(ReportType reportType);

    ReportResponse generateReport(ReportRequestDto reportRequestDto);

    PayloadResponse getReportParameters(String reportName);

    PayloadResponse createReport(ReportDto reportDto, String createdBy);
}
