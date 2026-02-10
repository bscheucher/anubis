package com.ibosng.dbibosservice.services.reports;

import com.ibosng.dbibosservice.dtos.reports.KursJournalDto;

import java.util.List;

public interface KursJournalReportService {
    List<KursJournalDto> getSeminarBuchData(Long projektNr, Integer kalenderwoche);
}
