package com.ibosng.dbibosservice.services.reports;

import com.ibosng.dbibosservice.dtos.reports.PepStatusMoxisDto;

import java.util.List;

public interface PepStatusMoxisReportService {

    public List<PepStatusMoxisDto> getPepStatusMoxisData(String Jahr, String Monat, String KST, boolean nicht_erfolgreiche);
}
