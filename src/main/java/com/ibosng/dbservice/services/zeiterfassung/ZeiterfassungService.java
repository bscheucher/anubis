package com.ibosng.dbservice.services.zeiterfassung;

import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.zeiterfassung.Zeiterfassung;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ZeiterfassungService extends BaseService<Zeiterfassung> {
    Optional<Zeiterfassung> findBySeminarTeilnehmerDatum(Seminar seminar, Teilnehmer teilnehmer, LocalDate datum);

    List<Zeiterfassung> findAllByZeiterfassungTransferIdAndStatus(Integer zeiterfassungTransferId, ZeiterfassungStatus status);
}
