package com.ibosng.dbservice.repositories.zeiterfassung;

import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.zeiterfassung.Zeiterfassung;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZeiterfassungRepository extends JpaRepository<Zeiterfassung, Integer> {
    Optional<Zeiterfassung> findBySeminarAndTeilnehmerAndDatum(Seminar seminar, Teilnehmer teilnehmer, LocalDate datum);

    @Query("select zet from Zeiterfassung zet where zet.zeiterfassungTransfer.id = :zeiterfassungTransferId and zet.status = :status")
    List<Zeiterfassung> findAllByZeiterfassungTransferIdAndStatus(Integer zeiterfassungTransferId, ZeiterfassungStatus status);
}
