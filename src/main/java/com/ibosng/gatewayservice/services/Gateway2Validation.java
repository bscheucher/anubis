package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.dtos.ZeitbuchungSyncRequestDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface Gateway2Validation {
    List<ZeitbuchungenDto> validateZeitbuchungen(ZeitbuchungSyncRequestDto zeitbuchungSyncRequestDto);

    TeilnehmerDto validateSingleTeilnehmer(TeilnehmerDto invalidTeilnehmerDto, String changedBy);

    StammdatenDto validateMitarbeiterStammdaten(StammdatenDto stammdatenDto, Boolean isOnboarding, String changedBy);

    VertragsdatenDto validateMitarbeiterVertragsdaten(VertragsdatenDto vertragsdatenDto, Boolean isOnboarding, String changedBy);

    VordienstzeitenDto validateMitarbeiterVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String changedBy);

    UnterhaltsberechtigteDto validateMitarbeiterUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String changedBy);

    WWorkflowStatus calculateKVEinstufung(String personalNummer, String changedBy, Boolean isOnboarding);

    TeilnehmerDto validateTeilnehmerDto(TeilnehmerDto teilnehmerDto, String changedBy);

    SeminarDto validateSeminarByTeilnehmer(SeminarDto seminarDto, String changedBy, Integer teilnehmerId);

    TeilnehmerNotizDto validateTeilnehmerNotiz(TeilnehmerNotizDto teilnehmerNotizDto, String changedBy, Integer teilnehmerId);

    TnAusbildungDto validateTeilnehmerAusbildung(TnAusbildungDto tnAusbildungDto, String changedBy, Integer teilnehmerId);

    TnBerufserfahrungDto validateTeilnehmerBerufserfahrung(TnBerufserfahrungDto tnBerufserfahrungDto, String changedBy, Integer teilnehmerId);

    SeminarPruefungDto validateSeminarPruefung(SeminarPruefungDto seminarPruefungDto, String changedBy, Integer teilnehmerId, Integer seminarId);

    SprachkenntnisDto validateTeilnehmerSprachkenntnis(SprachkenntnisDto sprachkenntnisDto, String changedBy, Integer teilnehmerId);

    TnZertifikatDto validateTeilnehmerZertifikat(TnZertifikatDto tnZertifikatDto, String changedBy, Integer teilnehmerId);

    Mono<Boolean> validateTeilnehmersZeiterfassung(ZeiterfassungTransferDto zeiterfassungDto, String changedBy);

    ResponseEntity<String> validateSyncMitarbeiterWithUPN(String upn);

}
