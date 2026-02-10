package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Teilnehmerservice {

    List<PruefungCsvDto> getTeilnehmerPruefungListCsv(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, String sortDirection);

    List<PruefungXlsxDto> getTeilnehmerPruefungListXlsx(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, String sortDirection);

    PayloadResponse getTeilnehmerFilterSummaryDto(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, String sortProperty, String sortDirection, int page, int size);

    PayloadResponse getTeilnehmerById(Integer id, Boolean isKorrigieren, String seminarName);

    PayloadResponse validateTeilnehmer(TeilnehmerSeminarDto teilnehmerDto, String token);

    PayloadResponse validateSeminarByTeilnehmer(SeminarDto seminarDto, String token, Integer teilnehmerId);

    ResponseEntity<PayloadResponse> getSeminarPruefungenByTeilnehmerIdAndSeminarId(Integer teilnehmerId, Integer seminarId);

    ResponseEntity<PayloadResponse> deleteSeminarPruefung(Integer pruefungId, String authorizationHeader);

    ResponseEntity<PayloadResponse> deleteTeilnehmerZertifikat(Integer zertifikatId, String authorizationHeader);

    PayloadResponse validateSeminarPruefung(SeminarPruefungDto seminarPruefungDto, String token, Integer teilnehmerId, Integer seminarId);

    PayloadResponse validateTeilnehmerNotiz(TeilnehmerNotizDto teilnehmerNotizDto, String token, Integer teilnehmerId);

    PayloadResponse validateTeilnehmerAusbildung(TnAusbildungDto tnAusbildungDto, String token, Integer teilnehmerId);

    PayloadResponse validateTeilnehmerBerufserfahrung(TnBerufserfahrungDto tnBerufserfahrungDto, String token, Integer teilnehmerId);

    ResponseEntity<PayloadResponse> deleteTeilnehmerNotiz(Integer notizId, String authorizationHeader);

    ResponseEntity<PayloadResponse> deleteTeilnehmerAusbildung(Integer ausbildungId, String authorizationHeader);

    ResponseEntity<PayloadResponse> deleteTeilnehmerBerufserfahrung(Integer berufserfahrungId, String authorizationHeader);

    ResponseEntity<PayloadResponse> deleteTeilnehmerSprachkenntnis(Integer sprachkenntnisId, String authorizationHeader);

    ResponseEntity<PayloadResponse> getTeilnehmerNotizenByTeilnehmerId(Integer teilnehmerId);

    ResponseEntity<PayloadResponse> getTeilnehmerAusbildungenByTeilnehmerId(Integer teilnehmerId);

    ResponseEntity<PayloadResponse> getTeilnehmerBerufserfahrungenByTeilnehmerId(Integer teilnehmerId);

    ResponseEntity<PayloadResponse> getTeilnehmerSprachkenntnisseByTeilnehmerId(Integer teilnehmerId);

    ResponseEntity<PayloadResponse> getTeilnehmerZertifikateByTeilnehmerId(Integer teilnehmerId);

    PayloadResponse validateTeilnehmerSprachkenntnis(SprachkenntnisDto sprachkenntnisDto, String token, Integer teilnehmerId);

    PayloadResponse validateTeilnehmerZertifikat(TnZertifikatDto tnZertifikatDto, String token, Integer teilnehmerId);

    PayloadResponse getTeilnehmersZeiterfassung(ZeiterfassungTransferDto zeiterfassungDto, Boolean shouldSubmit, String createdBy);

    PayloadResponse getZeiterfassungTransfers(String sortProperty, String sortDirection, int page, int size);

    PayloadResponse postUebaAbmeldung(String token, AbmeldungDto abmeldungDto);

    PayloadResponse getUebaAbmeldung(int page, int size);

    PayloadResponse getUebaAbmeldungById(Integer id);

    PayloadResponse getFilteredTeilnehmer(String searchTerm, String sortProperty, String sortDirection, int page, int size);

    PayloadResponse postKompetenzUebersicht(MultipartFile file, String document_type, String language, String generate_pdf,
                                            String process_definifion_key, Integer teilnehmerId);
}
