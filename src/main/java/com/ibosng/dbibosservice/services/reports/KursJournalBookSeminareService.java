package com.ibosng.dbibosservice.services.reports;

import com.ibosng.dbibosservice.dtos.reports.KursJournalBookSeminareDto;

import java.util.List;

public interface KursJournalBookSeminareService {

    List<KursJournalBookSeminareDto> getKursJournalBooksByProjekt(int sem1, int sem2, int sem3);
}
