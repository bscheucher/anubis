package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.workflowservice.dtos.WorkflowPayload;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ValidatorService {

    ResponseEntity<String> validateImportedParticipants(WorkflowPayload workflowPayload);

    ResponseEntity<TeilnehmerDto> validateSingleParticipant(TeilnehmerStaging teilnehmerStaging, String changedBy);

    ResponseEntity<StammdatenDto> validateMitarbeiterStammdaten(StammdatenDto stammdatenDto, Boolean isOnboarding, String changedBy);

    ResponseEntity<VertragsdatenDto> validateMitarbeiterVertragsdaten(VertragsdatenDto vertragsdatenDto, Boolean isOnboarding, String changedBy);

    ResponseEntity<VordienstzeitenDto> validateMitarbeiterVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String changedBy);

    ResponseEntity<UnterhaltsberechtigteDto> validateMitarbeiterUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String changedBy);

    ResponseEntity<TeilnehmerDto> validateTeilnehmer(TeilnehmerDto teilnehmerSummaryDto, String changedBy);

    ResponseEntity<SeminarDto> validateSeminarByTeilnehmerDto(SeminarDto seminarDto, String changedBy, String teilnehmerId);

    ResponseEntity<TeilnehmerNotizDto> validateTeilnehmerNotizDto(TeilnehmerNotizDto teilnehmerNotizDto, String changedBy, String teilnehmerId);

    ResponseEntity<TnAusbildungDto> validateTeilnehmerAusbildungDto(TnAusbildungDto tnAusbildungDto, String changedBy, String teilnehmerId);

    ResponseEntity<TnBerufserfahrungDto> validateTeilnehmerBerufserfahrungDto(TnBerufserfahrungDto tnBerufserfahrungDto, String changedBy, String teilnehmerId);

    ResponseEntity<SeminarPruefungDto> validateSeminarPruefungDto(SeminarPruefungDto seminarPruefungDto, String changedBy, String teilnehmerId, String seminarId);

    ResponseEntity<SprachkenntnisDto> validateTeilnehmerSprachkenntnisDto(SprachkenntnisDto sprachkenntnisDto, String teilnehmerId, String changedBy);

    ResponseEntity<TnZertifikatDto> validateTeilnehmerZertifikatDto(TnZertifikatDto tnZertifikatDto, String changedBy, String teilnehmerId);

    Mono<ResponseEntity<String>> validateZeiterfassungAsync(ZeiterfassungTransferDto zeiterfassungDto, String changedBy);

    ResponseEntity<ZeitbuchungenDto> validateZeitbuchung(ZeitbuchungenDto zeitbuchungenDto, String changedBy);
}
