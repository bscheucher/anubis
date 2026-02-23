package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.NewParticipantsSummaryDto;
import com.ibosng.dbservice.dtos.teilnehmer.SimpleTNDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerFilterSummaryDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TeilnehmerService extends BaseService<Teilnehmer> {

    List<PruefungCsvDto> findTeilnehmerForPruefungCsv(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, Sort.Direction direction, int page, int size);
    List<PruefungXlsxDto> findTeilnehmerForPruefungXlsx(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, Sort.Direction direction, int page, int size);
    List<Teilnehmer> getBySVN(String svn);

    List<Teilnehmer> getByVorname(String vorname);

    List<Teilnehmer> getByNachname(String nachname);

    List<Teilnehmer> getByVornameAndNachname(String vorname, String nachname);

    List<NewParticipantsSummaryDto> getSummaryImportedTeilnehmer(LocalDate date);

    Page<Teilnehmer> findAllErroneousParticipants(Pageable pageable, String sortBy, Sort.Direction direction);

    List<Teilnehmer> findAllCreatedOrChangedAfter(LocalDateTime after);

    List<String> findSeminarsForParticipantsWithStatus(TeilnehmerStatus status);

    List<String> findMassnahmenummersForParticipantsWithStatus(TeilnehmerStatus status);

    List<Teilnehmer> findTeilnehmerWithStatusAndSeminar(TeilnehmerStatus status, String seminar, int size, int offset);

    List<Teilnehmer> findTeilnehmerWithStatusAndMassnahmenummer(TeilnehmerStatus status, String massnahmenummer, int size, int offset);

    Integer getCountTeilnehmerWithStatusAndMassnahmenummer(TeilnehmerStatus status, String massnahmenummer);

    Integer getCountTeilnehmerWithStatusAndSeminar(TeilnehmerStatus status, String seminar);

    TeilnehmerSeminarDto findTeilnehmerDtoById(Integer id);

    Teilnehmer findByPersonalnummerString(String personalnummer);

    Teilnehmer findCompleteTnByPersonalnummerString(String personalnummer);

    Teilnehmer findByPersonalnummerId(Integer personalnummer);

    Page<TeilnehmerFilterSummaryDto> findTeilnehmerFiltered(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, String sortProperty, Sort.Direction direction, int page, int size);

    Page<Teilnehmer> findUebaTeilnehmerByCriteria(String searchTerm, String sortBy, Sort.Direction direction, int page, int size);

    void updateHasBisDocument(boolean hasBisDocument, Integer id);

    List<Teilnehmer> findAllByVornameAndNachnameAndSvNummer(String vorname, String nachname, String svNummer);

    List<Teilnehmer> findAllByVornameAndNachnameAndGeburtsdatum(String vorname, String nachname, LocalDate geburtsdatum);

    List<SimpleTNDto> findAllByAbgemeldetenTeilnehmer(AbmeldungStatus status);

    List<Teilnehmer> findTeilnehmersWithIds(List<Integer> ids);

    Teilnehmer saveAndFlush(Teilnehmer teilnehmer);
}
