package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;

import java.util.List;

public interface FileExportService {

    ReportResponse exportToCsv(List<PruefungCsvDto> pruefungCsvDtos);
    ReportResponse exportToXlsx(List<PruefungXlsxDto> pruefungCsvDtos);
}
