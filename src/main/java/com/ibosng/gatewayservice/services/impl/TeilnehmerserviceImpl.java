package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbibosservice.dtos.TeilnahmeBasicDto;
import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbibosservice.services.StandortIbosService;
import com.ibosng.dbibosservice.services.TeilnahmeService;
import com.ibosng.dbservice.dtos.*;
import com.ibosng.dbservice.dtos.teilnehmer.*;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.dbservice.entities.Sprachkenntnis;
import com.ibosng.dbservice.entities.lhr.Abmeldung;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.seminar.SeminarPruefung;
import com.ibosng.dbservice.entities.teilnehmer.*;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.exceptions.DBRuntimeException;
import com.ibosng.dbservice.services.*;
import com.ibosng.dbservice.services.lhr.AbmeldungService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungReasonService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungTransferService;
import com.ibosng.gatewayservice.dtos.response.Pagination;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.gatewayservice.services.Teilnehmerservice;
import com.ibosng.lhrservice.services.LHRDienstnehmerService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.natifservice.services.NatifService;
import com.ibosng.workflowservice.enums.SWorkflowGroups;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Constants.FN_TEILNEHMERINNEN_BEARBEITEN;
import static com.ibosng.gatewayservice.utils.Helpers.*;
import static com.ibosng.gatewayservice.utils.Parsers.isValidDate;
import static com.ibosng.gatewayservice.utils.Parsers.parseDate;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeilnehmerserviceImpl implements Teilnehmerservice {
    private final TeilnehmerService teilnehmerService;
    private final Gateway2Validation gateway2Validation;
    private final ZeiterfassungTransferService zeiterfassungTransferService;
    private final LHRDienstnehmerService dienstnehmerService;
    private final WFStartService wfStartService;
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final MailService mailService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final BenutzerDetailsService benutzerDetailsService;
    private final TeilnehmerStagingService teilnehmerStagingService;
    private final BenutzerIbosService benutzerIbosService;
    private final StandortIbosService standortIbosService;
    private final AbmeldungService abmeldungService;
    private final TeilnahmeService teilnahmeService;
    private final WWorkflowService wWorkflowService;
    private final TeilnehmerNotizService teilnehmerNotizService;
    private final TeilnehmerAusbildungService teilnehmerAusbildungService;
    private final TeilnehmerBerufserfahrungService teilnehmerBerufserfahrungService;
    private final SprachkenntnisService sprachkenntnisService;
    private final TeilnehmerZertifikatService teilnehmerZertifikatService;
    private final SeminarPruefungService seminarPruefungService;
    private final ZeiterfassungReasonService zeiterfassungReasonService;
    private final AzureSSOService azureSSOService;
    private final NatifService natifService;

    @Override
    public List<PruefungCsvDto> getTeilnehmerPruefungListCsv(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, String sortDirection) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }

        Sort.Direction direction = getSortDirection(sortDirection);
        int page = 0;
        int size = 200;
        List<PruefungCsvDto> allResults = new ArrayList<>();

        List<PruefungCsvDto> currentPage;
        do {
            currentPage = teilnehmerService.findTeilnehmerForPruefungCsv(
                    identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer,
                    isAngemeldet, geschlecht, sortProperty, direction, page, size
            );
            allResults.addAll(currentPage);
            page++;
        } while (currentPage.size() == size);

        return allResults;


    }

    @Override
    public List<PruefungXlsxDto> getTeilnehmerPruefungListXlsx(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, String sortDirection) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }

        Sort.Direction direction = getSortDirection(sortDirection);
        int page = 0;
        int size = 200;
        List<PruefungXlsxDto> allResults = new ArrayList<>();

        List<PruefungXlsxDto> currentPage;
        do {
            currentPage = teilnehmerService.findTeilnehmerForPruefungXlsx(
                    identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer,
                    isAngemeldet, geschlecht, sortProperty, direction, page, size
            );
            allResults.addAll(currentPage);
            page++;
        } while (currentPage.size() == size);

        return allResults;


    }

    @Override
    public PayloadResponse getTeilnehmerFilterSummaryDto(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, String sortProperty, String sortDirection, int page, int size) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }
        Page<TeilnehmerFilterSummaryDto> teilnehmerFilterSummaryDtos = teilnehmerService.findTeilnehmerFiltered(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, sortProperty, getSortDirection(sortDirection), page, size);
        PayloadTypeList<TeilnehmerFilterSummaryDto> teilnehmerFilterSummaryDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_SUMMARY.getValue());
        teilnehmerFilterSummaryDtoPayloadTypeList.setAttributes(teilnehmerFilterSummaryDtos.getContent());
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        Pagination pagination = new Pagination((int) teilnehmerFilterSummaryDtos.getTotalElements(), size, page);
        response.setPagination(pagination);
        response.setData(List.of(teilnehmerFilterSummaryDtoPayloadTypeList));
        return response;
    }


    @Override
    public PayloadResponse getTeilnehmerById(Integer id, Boolean isKorrigieren, String seminarName) {
        TeilnehmerSeminarDto teilnehmerDto = teilnehmerService.findTeilnehmerDtoById(id);
        if (isKorrigieren != null && isKorrigieren) {
            mapSeminarData(teilnehmerDto, seminarName);
        }
        if (teilnehmerDto != null) {
            PayloadTypeList<TeilnehmerSeminarDto> teilnehmerDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_SEMINARS.getValue());
            teilnehmerDtoPayloadTypeList.setAttributes(List.of(teilnehmerDto));
            PayloadResponse response = new PayloadResponse();
            response.setSuccess(true);
            response.setData(List.of(teilnehmerDtoPayloadTypeList));
            return response;
        }
        return null;
    }

    private void setMissingFields(TeilnehmerDto teilnehmerDto, TeilnehmerStaging teilnehmerStaging) {
        if (teilnehmerDto == null || teilnehmerStaging == null) return;

        List<String> errors = teilnehmerDto.getErrors();
        if (errors == null || errors.isEmpty()) return;

        if (errors.contains("betreuerVorname")) {
            teilnehmerDto.setBetreuerVorname(teilnehmerStaging.getBetreuerVorname());
        }
        if (errors.contains("betreuerNachname")) {
            teilnehmerDto.setBetreuerNachname(teilnehmerStaging.getBetreuerNachname());
        }
        if (errors.contains("eintritt")) {
            teilnehmerDto.setEintritt(teilnehmerStaging.getEintritt());
        }
        if (errors.contains("austritt")) {
            teilnehmerDto.setAustritt(teilnehmerStaging.getAustritt());
        }
        if (errors.contains("geplant")) {
            teilnehmerDto.setGeplant(teilnehmerStaging.getGeplant());
        }
        if (errors.contains("nation")) {
            teilnehmerDto.setNation(teilnehmerStaging.getNation());
        }
        if (errors.contains("rgs")) {
            teilnehmerDto.setRgs(teilnehmerStaging.getRgs());
        }
        if (errors.contains("seminarBezeichnung")) {
            teilnehmerDto.setSeminarBezeichnung(teilnehmerStaging.getSeminarIdentifier()); // Assuming this maps
        }
        if (errors.contains("strasse")) {
            teilnehmerDto.setStrasse(teilnehmerStaging.getStrasse());
        }
        if (errors.contains("plz")) {
            teilnehmerDto.setPlz(teilnehmerStaging.getPlz());
        }
        if (errors.contains("ort")) {
            teilnehmerDto.setOrt(teilnehmerStaging.getOrt());
        }
        if (errors.contains("anrede")) {
            teilnehmerDto.setAnrede(teilnehmerStaging.getAnrede());
        }
        if (errors.contains("email")) {
            teilnehmerDto.setEmail(teilnehmerStaging.getEmail());
        }
        if (errors.contains("svNummer")) {
            teilnehmerDto.setSvNummer(teilnehmerStaging.getSvNummer());
        }
        if (errors.contains("geburtsdatum")) {
            if (!isNullOrBlank(teilnehmerStaging.getGeburtsdatum()) && isValidDate(teilnehmerStaging.getGeburtsdatum())) {
                LocalDate gerburtsdatumDate = parseDate(teilnehmerStaging.getGeburtsdatum());
                if (gerburtsdatumDate != null) {
                    teilnehmerDto.setGeburtsdatum(gerburtsdatumDate.toString());
                }
            }
        }
        if (errors.contains("geschlecht")) {
            teilnehmerDto.setGeschlecht(teilnehmerStaging.getGeschlecht());
        }
        if (errors.contains("nachname")) {
            teilnehmerDto.setNachname(teilnehmerStaging.getNachname());
        }
        if (errors.contains("telefon")) {
            teilnehmerDto.setTelefon(teilnehmerStaging.getTelefon());
        }
        if (errors.contains("vorname")) {
            teilnehmerDto.setVorname(teilnehmerStaging.getVorname());
        }
        if (errors.contains("zubuchung")) {
            teilnehmerDto.setZubuchung(teilnehmerStaging.getZubuchung());
        }
    }

    private void mapSeminarData(TeilnehmerSeminarDto teilnehmerDto, String seminarName) {
        if (teilnehmerDto != null && teilnehmerDto.getSeminarDtos() != null && !teilnehmerDto.getSeminarDtos().isEmpty()) {

            List<SeminarDto> seminars = teilnehmerDto.getSeminarDtos();
            SeminarDto seminarDto = null;
            if (!isNullOrBlank(seminarName) && seminars != null && !seminars.isEmpty()) {
                seminarDto = seminars.stream().filter(sem -> sem.getSeminarBezeichnung().equals(seminarName)).findFirst().orElse(null);
            }
            if (seminarDto == null) {
                if (!isNullOrBlank(seminarName)) {
                    List<TeilnehmerStaging> teilnehmerStagingList = teilnehmerStagingService.findByTeilnehmerIdAndSeminarIdentifierOrderByCreatedOnDesc(teilnehmerDto.getTeilnehmerDto().getId(), seminarName);
                    if (teilnehmerStagingList != null && !teilnehmerStagingList.isEmpty()) {
                        TeilnehmerStaging teilnehmerStaging = teilnehmerStagingList.get(0);
                        // Check fields in error and set those based on staging table, since those will be missing from tn table
                        setMissingFields(teilnehmerDto.getTeilnehmerDto(), teilnehmerStaging);
                        seminarDto = new SeminarDto();
                        seminarDto.setSeminarBezeichnung(seminarName);
                        if (!isNullOrBlank(teilnehmerStaging.getSeminarStartDate())) {
                            seminarDto.setEintritt(teilnehmerStaging.getSeminarStartDate());
                        }
                        if (!isNullOrBlank(teilnehmerStaging.getSeminarEndDate())) {
                            seminarDto.setAustritt(teilnehmerStaging.getSeminarEndDate());
                        }
                    }
                }
            }
            if (!isNullOrBlank(seminarName)) {
                List<TeilnehmerStaging> teilnehmerStagingList = teilnehmerStagingService.findByTeilnehmerIdAndSeminarIdentifierOrderByCreatedOnDesc(teilnehmerDto.getTeilnehmerDto().getId(), seminarName);
                if (teilnehmerStagingList != null && !teilnehmerStagingList.isEmpty()) {
                    // Check fields in error and set those based on staging table, since those will be missing from tn table
                    setMissingFields(teilnehmerDto.getTeilnehmerDto(), teilnehmerStagingList.get(0));
                }
            }
            if (isNullOrBlank(seminarName)) {
                seminarDto = teilnehmerDto.getSeminarDtos().get(0);
            }
            if (seminarDto == null) {
                log.info("No seminar data found for teilnehmer id {}", teilnehmerDto.getTeilnehmerDto().getId());
                return;
            }
            if (seminarDto.getSeminarNumber() != null) {
                teilnehmerDto.getTeilnehmerDto().setSeminarNumber(seminarDto.getSeminarNumber());
            }
            if (!isNullOrBlank(seminarDto.getSeminarBezeichnung())) {
                teilnehmerDto.getTeilnehmerDto().setSeminarBezeichnung(seminarDto.getSeminarBezeichnung());
            }
            if (!isNullOrBlank(seminarDto.getBuchungsstatus())) {
                teilnehmerDto.getTeilnehmerDto().setBuchungsstatus(seminarDto.getBuchungsstatus());
            }
            if (!isNullOrBlank(seminarDto.getMassnahmennummer())) {
                teilnehmerDto.getTeilnehmerDto().setMassnahmennummer(seminarDto.getMassnahmennummer());
            }
            if (!isNullOrBlank(seminarDto.getVeranstaltungsnummer())) {
                teilnehmerDto.getTeilnehmerDto().setVeranstaltungsnummer(seminarDto.getVeranstaltungsnummer());
            }
            if (!isNullOrBlank(seminarDto.getAnmerkung())) {
                teilnehmerDto.getTeilnehmerDto().setAnmerkung(seminarDto.getAnmerkung());
            }
            if (!isNullOrBlank(seminarDto.getZubuchung())) {
                teilnehmerDto.getTeilnehmerDto().setZubuchung(seminarDto.getZubuchung());
            }
            if (!isNullOrBlank(seminarDto.getGeplant())) {
                teilnehmerDto.getTeilnehmerDto().setGeplant(seminarDto.getGeplant());
            }
            if (!isNullOrBlank(seminarDto.getEintritt())) {
                teilnehmerDto.getTeilnehmerDto().setEintritt(seminarDto.getEintritt());
            }
            if (!isNullOrBlank(seminarDto.getAustritt())) {
                teilnehmerDto.getTeilnehmerDto().setAustritt(seminarDto.getAustritt());
            }
            if (!isNullOrBlank(seminarDto.getRgs())) {
                teilnehmerDto.getTeilnehmerDto().setRgs(seminarDto.getRgs());
            }
            if (!isNullOrBlank(seminarDto.getBetreuerVorname())) {
                teilnehmerDto.getTeilnehmerDto().setBetreuerVorname(seminarDto.getBetreuerVorname());
            }
            if (!isNullOrBlank(seminarDto.getBetreuerNachname())) {
                teilnehmerDto.getTeilnehmerDto().setBetreuerNachname(seminarDto.getBetreuerNachname());
            }
            if (!isNullOrBlank(seminarDto.getBetreuerTitel())) {
                teilnehmerDto.getTeilnehmerDto().setBetreuerTitel(seminarDto.getBetreuerTitel());
            }
        }
    }

    @Override
    public ResponseEntity<PayloadResponse> getTeilnehmerNotizenByTeilnehmerId(Integer teilnehmerId) {
        PayloadResponse response = new PayloadResponse();
        List<TeilnehmerNotiz> teilnehmerNotizen = teilnehmerNotizService.findAllByTeilnehmerId(teilnehmerId);
        List<TeilnehmerNotizDto> teilnehmerNotizenDtos = teilnehmerNotizen.stream().map(teilnehmerNotizService::mapTeilnehmerNotizToDto).toList();
        PayloadTypeList<TeilnehmerNotizDto> teilnehmerNotizenDtoPayloadType = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_NOTIZ.getValue());
        teilnehmerNotizenDtoPayloadType.setAttributes(teilnehmerNotizenDtos);
        response.setSuccess(true);
        response.setData(Collections.singletonList(teilnehmerNotizenDtoPayloadType));
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> getTeilnehmerAusbildungenByTeilnehmerId(Integer teilnehmerId) {
        PayloadResponse response = new PayloadResponse();
        List<TnAusbildung> teilnehmerAusbildungen = teilnehmerAusbildungService.findAllByTeilnehmerId(teilnehmerId);
        List<TnAusbildungDto> teilnehmerAusbildungenDtos = teilnehmerAusbildungen.stream().map(teilnehmerAusbildungService::mapTeilnehmerAusbildungToDto).toList();
        PayloadTypeList<TnAusbildungDto> teilnehmerAusbildungenDtoPayloadType = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_AUSBILDUNG.getValue());
        teilnehmerAusbildungenDtoPayloadType.setAttributes(teilnehmerAusbildungenDtos);
        response.setSuccess(true);
        response.setData(Collections.singletonList(teilnehmerAusbildungenDtoPayloadType));
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> getTeilnehmerBerufserfahrungenByTeilnehmerId(Integer teilnehmerId) {
        PayloadResponse response = new PayloadResponse();
        List<TnBerufserfahrung> teilnehmerBerufserfahrungen = teilnehmerBerufserfahrungService.findAllByTeilnehmerId(teilnehmerId);
        List<TnBerufserfahrungDto> teilnehmerBerufserfahrungDtos = teilnehmerBerufserfahrungen.stream().map(teilnehmerBerufserfahrungService::mapTeilnehmerBerufserfahrungToDto).toList();
        PayloadTypeList<TnBerufserfahrungDto> teilnehmerBerufserfahrungDtoPayloadType = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_BERUFSERFAHRUNG.getValue());
        teilnehmerBerufserfahrungDtoPayloadType.setAttributes(teilnehmerBerufserfahrungDtos);
        response.setSuccess(true);
        response.setData(Collections.singletonList(teilnehmerBerufserfahrungDtoPayloadType));
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> getTeilnehmerSprachkenntnisseByTeilnehmerId(Integer teilnehmerId) {
        PayloadResponse response = new PayloadResponse();
        List<Sprachkenntnis> teilnehmerSprachkenntnisse = sprachkenntnisService.findAllByTeilnehmerId(teilnehmerId);
        List<SprachkenntnisDto> teilnehmerSprachkenntnisDtos = teilnehmerSprachkenntnisse.stream().map(sprachkenntnisService::mapTeilnehmerSprachkenntnisToDto).toList();
        PayloadTypeList<SprachkenntnisDto> teilnehmerSprachkenntnisDtoPayloadType = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_SPRACHKENNTNIS.getValue());
        teilnehmerSprachkenntnisDtoPayloadType.setAttributes(teilnehmerSprachkenntnisDtos);
        response.setSuccess(true);
        response.setData(Collections.singletonList(teilnehmerSprachkenntnisDtoPayloadType));
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> getTeilnehmerZertifikateByTeilnehmerId(Integer teilnehmerId) {
        PayloadResponse response = new PayloadResponse();
        List<TnZertifikat> teilnehmerZertifikate = teilnehmerZertifikatService.findAllByTeilnehmerId(teilnehmerId);
        List<TnZertifikatDto> teilnehmerZertifikatDtos = teilnehmerZertifikate.stream().map(teilnehmerZertifikatService::mapTeilnehmerZertifikatToDto).toList();
        PayloadTypeList<TnZertifikatDto> teilnehmerZertifikatDtoPayloadType = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_ZERTIFIKAT.getValue());
        teilnehmerZertifikatDtoPayloadType.setAttributes(teilnehmerZertifikatDtos);
        response.setSuccess(true);
        response.setData(Collections.singletonList(teilnehmerZertifikatDtoPayloadType));
        return checkResultIfNull(response);
    }


    @Override
    public PayloadResponse validateTeilnehmer(TeilnehmerSeminarDto teilnehmerSeminarDto, String token) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        TeilnehmerDto validatedTeilnehmer = gateway2Validation.validateTeilnehmerDto(teilnehmerSeminarDto.getTeilnehmerDto(), changedBy);
        TeilnehmerSeminarDto proccessedTeilnehmerSeminarDto = teilnehmerService.findTeilnehmerDtoById(validatedTeilnehmer.getId());
        proccessedTeilnehmerSeminarDto.setTeilnehmerDto(validatedTeilnehmer);

        PayloadTypeList<TeilnehmerSeminarDto> teilnehmerDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_SEMINARS.getValue());
        teilnehmerDtoPayloadTypeList.setAttributes(List.of(proccessedTeilnehmerSeminarDto));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(teilnehmerDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse validateSeminarByTeilnehmer(SeminarDto seminarDto, String token, Integer teilnehmerId) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        SeminarDto validatedSeminarByTeilnehmer = gateway2Validation.validateSeminarByTeilnehmer(seminarDto, changedBy, teilnehmerId);

        PayloadTypeList<SeminarDto> seminarDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.SEMINAR.getValue());
        seminarDtoPayloadTypeList.setAttributes(List.of(validatedSeminarByTeilnehmer));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(seminarDtoPayloadTypeList));
        return response;
    }

    @Override
    public ResponseEntity<PayloadResponse> getSeminarPruefungenByTeilnehmerIdAndSeminarId(Integer teilnehmerId, Integer seminarId) {
        PayloadResponse response = new PayloadResponse();
        List<SeminarPruefung> seminarPruefungen = seminarPruefungService.findAllByTeilnehmerIdAndSeminarId(teilnehmerId, seminarId);
        List<SeminarPruefungDto> seminarPruefungDtos = seminarPruefungen.stream().map(seminarPruefungService::mapSeminarPruefungToDto).toList();
        PayloadTypeList<SeminarPruefungDto> seminarPruefungDtoPayloadType = new PayloadTypeList<>(PayloadTypes.SEMINAR_PRUEFUNG.getValue());
        seminarPruefungDtoPayloadType.setAttributes(seminarPruefungDtos);
        response.setSuccess(true);
        response.setData(Collections.singletonList(seminarPruefungDtoPayloadType));
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteSeminarPruefung(Integer pruefungId, String authorizationHeader) {
        return deleteEntityById(
                pruefungId,
                authorizationHeader,
                seminarPruefungService::deleteById
        );
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteTeilnehmerZertifikat(Integer zertifikatId, String authorizationHeader) {
        return deleteEntityById(
                zertifikatId,
                authorizationHeader,
                teilnehmerZertifikatService::deleteById
        );
    }

    @Override
    public PayloadResponse validateSeminarPruefung(SeminarPruefungDto seminarPruefungDto, String token, Integer teilnehmerId, Integer seminarId) {
        PayloadResponse response = new PayloadResponse();
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        SeminarPruefungDto validatedSeminarPruefungDto = gateway2Validation.validateSeminarPruefung(seminarPruefungDto, changedBy, teilnehmerId, seminarId);

        PayloadTypeList<SeminarPruefungDto> seminarPruefungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.SEMINAR_PRUEFUNG.getValue());
        seminarPruefungDtoPayloadTypeList.setAttributes(List.of(validatedSeminarPruefungDto));
        response.setSuccess(true);
        response.setData(List.of(seminarPruefungDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse validateTeilnehmerNotiz(TeilnehmerNotizDto teilnehmerNotizDto, String token, Integer teilnehmerId) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        TeilnehmerNotizDto validatedTeilnehmerNotiz = gateway2Validation.validateTeilnehmerNotiz(teilnehmerNotizDto, changedBy, teilnehmerId);

        PayloadTypeList<TeilnehmerNotizDto> teilnehmerNotizDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_NOTIZ.getValue());
        teilnehmerNotizDtoPayloadTypeList.setAttributes(List.of(validatedTeilnehmerNotiz));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(teilnehmerNotizDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse validateTeilnehmerAusbildung(TnAusbildungDto tnAusbildungDto, String token, Integer teilnehmerId) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        TnAusbildungDto validatedTeilnehmerAusbildung = gateway2Validation.validateTeilnehmerAusbildung(tnAusbildungDto, changedBy, teilnehmerId);
        PayloadTypeList<TnAusbildungDto> teilnehmerAusbildungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_AUSBILDUNG.getValue());
        teilnehmerAusbildungDtoPayloadTypeList.setAttributes(List.of(validatedTeilnehmerAusbildung));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(teilnehmerAusbildungDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse validateTeilnehmerBerufserfahrung(TnBerufserfahrungDto tnBerufserfahrungDto, String token, Integer teilnehmerId) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        TnBerufserfahrungDto validatedTeilnehmerBerufserfahrung = gateway2Validation.validateTeilnehmerBerufserfahrung(tnBerufserfahrungDto, changedBy, teilnehmerId);

        PayloadTypeList<TnBerufserfahrungDto> teilnehmerBerufserfahrungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_BERUFSERFAHRUNG.getValue());
        teilnehmerBerufserfahrungDtoPayloadTypeList.setAttributes(List.of(validatedTeilnehmerBerufserfahrung));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(teilnehmerBerufserfahrungDtoPayloadTypeList));
        return response;
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteTeilnehmerNotiz(Integer notizId, String authorizationHeader) {
        return deleteEntityById(
                notizId,
                authorizationHeader,
                teilnehmerNotizService::deleteById
        );
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteTeilnehmerAusbildung(Integer ausbildungId, String authorizationHeader) {
        return deleteEntityById(
                ausbildungId,
                authorizationHeader,
                teilnehmerAusbildungService::deleteById
        );
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteTeilnehmerBerufserfahrung(Integer berufserfahrungId, String authorizationHeader) {
        return deleteEntityById(
                berufserfahrungId,
                authorizationHeader,
                teilnehmerBerufserfahrungService::deleteById
        );
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteTeilnehmerSprachkenntnis(Integer sprachkenntnisId, String authorizationHeader) {
        return deleteEntityById(
                sprachkenntnisId,
                authorizationHeader,
                sprachkenntnisService::deleteById
        );
    }

    private ResponseEntity<PayloadResponse> deleteEntityById(
            Integer id,
            String authorizationHeader,
            Consumer<Integer> deleteFunction
    ) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Collections.singletonList(FN_TEILNEHMERINNEN_BEARBEITEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PayloadResponse response = new PayloadResponse();
        if (id == null) {
            response.setSuccess(false);
            return checkResultIfNull(response);
        }
        deleteFunction.accept(id);
        response.setSuccess(true);

        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }


    @Override
    public PayloadResponse validateTeilnehmerSprachkenntnis(SprachkenntnisDto sprachkenntnisDto, String token, Integer teilnehmerId) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        SprachkenntnisDto validatedSprachkenntnis = gateway2Validation.validateTeilnehmerSprachkenntnis(sprachkenntnisDto, changedBy, teilnehmerId);

        PayloadTypeList<SprachkenntnisDto> teilnehmerSprachkenntnisDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_SPRACHKENNTNIS.getValue());
        teilnehmerSprachkenntnisDtoPayloadTypeList.setAttributes(List.of(validatedSprachkenntnis));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(teilnehmerSprachkenntnisDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse validateTeilnehmerZertifikat(TnZertifikatDto tnZertifikatDto, String token, Integer teilnehmerId) {
        String changedBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        TnZertifikatDto validatedZertifikat = gateway2Validation.validateTeilnehmerZertifikat(tnZertifikatDto, changedBy, teilnehmerId);

        PayloadTypeList<TnZertifikatDto> teilnehmerZertifikatDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER_ZERTIFIKAT.getValue());
        teilnehmerZertifikatDtoPayloadTypeList.setAttributes(List.of(validatedZertifikat));
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(List.of(teilnehmerZertifikatDtoPayloadTypeList));
        return response;
    }

    private ZeiterfassungTransfer createNewZeiterfassungTransfer(String createdBy) {
        ZeiterfassungTransfer zeiterfassungTransfer = new ZeiterfassungTransfer();
        zeiterfassungTransfer.setStatus(ZeiterfassungStatus.NEW);
        zeiterfassungTransfer.setCreatedBy(createdBy);
        return zeiterfassungTransferService.save(zeiterfassungTransfer);
    }

    @Override
    public PayloadResponse getTeilnehmersZeiterfassung(ZeiterfassungTransferDto zeiterfassungDto, Boolean shouldSubmit, String createdBy) {
        PayloadResponse response = new PayloadResponse();
        try {
            zeiterfassungTransferService.checkForOverlappingSeminar(zeiterfassungDto);
        } catch (DBRuntimeException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
            return response;
        }
        PayloadTypeList<ZeiterfassungTransferDto> zeiterfassungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TN_ZEITERFASSUNG_TRANSFER.getValue());
        if (shouldSubmit != null && shouldSubmit) {
            ZeiterfassungTransfer zeiterfassungTransferNew = createNewZeiterfassungTransfer(createdBy);
            zeiterfassungDto.setId(zeiterfassungTransferNew.getId());
            gateway2Validation.validateTeilnehmersZeiterfassung(zeiterfassungDto, createdBy).doOnNext(success -> {
                        if (success) {
                            log.info("Request was successfully received by the validation service.");
                            zeiterfassungDto.setUserName(createdBy);
                            Optional<ZeiterfassungTransfer> zeiterfassungTransferOptional = zeiterfassungTransferService.findById(zeiterfassungTransferNew.getId());
                            ZeiterfassungTransfer zeiterfassungTransfer;
                            if (zeiterfassungTransferOptional.isEmpty()) {
                                log.error("No Zeiterfassung Transfer with id {} found", zeiterfassungTransferNew.getId());
                                zeiterfassungTransfer = zeiterfassungTransferNew;
                            } else {
                                zeiterfassungTransfer = zeiterfassungTransferOptional.get();
                            }
                            zeiterfassungTransfer.setStatus(ZeiterfassungStatus.IN_PROGRESS);
                            zeiterfassungTransfer = zeiterfassungTransferService.save(zeiterfassungTransfer);

                            WWorkflowGroup wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(SWorkflowGroups.TN_AN_ABWESENHEITEN_TRANSFER.getValue(), String.valueOf(zeiterfassungTransfer.getId()), createdBy);
                            wWorkflowGroup.setStatus(WWorkflowStatus.IN_PROGRESS);
                            wWorkflowGroupService.save(wWorkflowGroup);
                            WWorkflow wWorkflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.TN_AN_ABWESENHEITEN_TRANSFER_LHR, WWorkflowStatus.IN_PROGRESS, createdBy);
                        } else {
                            log.warn("Request was not accepted by the validation service.");
                        }
                    })
                    .subscribe();
            zeiterfassungTransferService.findById(zeiterfassungTransferNew.getId()).ifPresent(transfer -> zeiterfassungDtoPayloadTypeList.setAttributes(List.of(zeiterfassungTransferService.mapZeiterfassungTransferToDto(transfer))));
        } else {
            List<Integer> seminars = zeiterfassungDto.getSeminars().stream().map(BasicSeminarDto::getSeminarNumber).toList();
            List<Integer> reasons = zeiterfassungReasonService.findAllReasonsWithIbosidNotNull();
            TeilnahmeBasicDto teilnahmeBasicDto = teilnahmeService.getTeilnehmerSeminarSummary(seminars, reasons, parseDate(zeiterfassungDto.getDatumVon()), parseDate(zeiterfassungDto.getDatumBis()));
            List<BasicSeminarDto> existingSeminars = zeiterfassungDto.getSeminars().stream().filter(sem -> teilnahmeBasicDto.getSeminars().contains(sem.getSeminarNumber())).toList();
            zeiterfassungDto.setTeilnehmerNumber(teilnahmeBasicDto.getTeilnehmerNumber());
            zeiterfassungDto.setSeminars(existingSeminars);
            zeiterfassungDtoPayloadTypeList.setAttributes(List.of(zeiterfassungDto));
        }

        response.setSuccess(true);
        response.setData(List.of(zeiterfassungDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse getZeiterfassungTransfers(String sortProperty, String sortDirection, int page, int size) {
        Pageable pageable = createPageable(sortProperty, sortDirection, page, size);
        Page<ZeiterfassungTransferDto> dtos = zeiterfassungTransferService.findAllDtos(pageable);
        PayloadTypeList<ZeiterfassungTransferDto> zeiterfassungTransferDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.ZEITERFASSUNG_TRANSFER_LIST.getValue());
        zeiterfassungTransferDtoPayloadTypeList.setAttributes(dtos.getContent());
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setPagination(createPagination(dtos));
        response.setData(List.of(zeiterfassungTransferDtoPayloadTypeList));
        return response;
    }

    @Override
    public PayloadResponse getFilteredTeilnehmer(String searchTerm, String sortProperty, String sortDirection, int page, int size) {
        Page<Teilnehmer> teilnehmerPage = teilnehmerService.findUebaTeilnehmerByCriteria(searchTerm, sortProperty, getSortDirection(sortDirection), page, size);
        List<TeilnehmerDto> teilnehmerDtoList = teilnehmerPage.getContent().stream().map(teilnehmer -> {
            TeilnehmerDto dto = new TeilnehmerDto();
            teilnehmerStagingService.mapTeilnehmerToDto(teilnehmer, dto, false);  // assuming you have the boolean flag 'isSeminarPresent'
            return dto;
        }).collect(Collectors.toList());

        PayloadTypeList<TeilnehmerDto> teilnehmerDtoListPayloadTypeList = new PayloadTypeList<>(PayloadTypes.TEILNEHMER.getValue());
/*        // If no results found in ibosNG, search in ibos
        if (teilnehmerDtoList.isEmpty()) {

            List<AdresseIbos> ibosEntries = adresseIbosService.getFilteredTeilnehmer(searchTerm, validateTnSortProperty(sortProperty), sortDirection);
            if (!ibosEntries.isEmpty()) {
                List<SimpleTNDto> alreadyAbgemeldeten = teilnehmerService.findAllByAbgemeldetenTeilnehmer(AbmeldungStatus.ABGEMELDET);
                teilnehmerDtoList = ibosEntries.stream().map(adresse -> adresseIbosToTeilnehmerDto(adresse, alreadyAbgemeldeten)).collect(Collectors.toList());
                teilnehmerDtoList.removeIf(Objects::isNull);
            }
        }*/
        teilnehmerDtoListPayloadTypeList.setAttributes(teilnehmerDtoList);

        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);

        response.setData(List.of(teilnehmerDtoListPayloadTypeList));
        response.setPagination(createPagination(teilnehmerPage));
        return response;
    }

    @Override
    public PayloadResponse postUebaAbmeldung(String token, AbmeldungDto abmeldungDto) {
        String createdBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        Abmeldung abmeldung = null;
        if (abmeldungDto.getTeilnehmerId() != null) {
            Optional<Abmeldung> abmeldungOptional = abmeldungService.findAllTeilnehmerID(abmeldungDto.getTeilnehmerId());
            if (abmeldungOptional.isPresent()) {
                abmeldung = abmeldungOptional.get();
            }
        }
        if (abmeldung == null && abmeldungDto.getSvNummer() != null) {
            abmeldung = abmeldungService.findAllBySvNummer(abmeldungDto.getSvNummer());
        }
        if (abmeldung == null) {
            abmeldung = abmeldungService.mapToAbmeldung(abmeldungDto, AbmeldungStatus.NEW);
        } else {
            abmeldung.setStatus(AbmeldungStatus.NEW);
        }
        WWorkflowGroup wWorkflowGroup;
        boolean isNewWF = false;
        if (abmeldung.getWorkflow() == null) {
            wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(SWorkflowGroups.TN_TRITT_AUS.getValue(), String.valueOf(abmeldung.getId()), createdBy);
            wWorkflowGroup.setStatus(WWorkflowStatus.IN_PROGRESS);
            wWorkflowGroup = wWorkflowGroupService.save(wWorkflowGroup);
            WWorkflow wWorkflow = wWorkflowService.findAllByWorkflowGroup(wWorkflowGroup).stream().filter(ww -> ww.getWorkflow().getName().equals(SWorkflows.TN_TRITT_SICHERLICH_AUS.getValue())).findFirst().orElse(null);
            if (wWorkflow != null) {
                abmeldung.setWorkflow(wWorkflow);
            }
            isNewWF = true;
        } else {
            wWorkflowGroup = abmeldung.getWorkflow().getWorkflowGroup();
        }
        abmeldung = abmeldungService.save(abmeldung);
        abmeldungDto.setId(abmeldung.getId());

        WWorkflow wWorkflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.TN_TRITT_SICHERLICH_AUS, WWorkflowStatus.IN_PROGRESS, createdBy);
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_TRITT_AUS_INFORM_LOHNVERRECHNUNG, WWorkflowStatus.IN_PROGRESS, createdBy);
        if (!isNewWF) {
            manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_TRITT_AUS_SEND_AUSTRITT_TO_LHR, WWorkflowStatus.NEW, createdBy);
            manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_TRITT_AUS_CHANGE_STATUS, WWorkflowStatus.NEW, createdBy);
        }
        Optional<Teilnehmer> teilnehmer = teilnehmerService.findById(abmeldungDto.getTeilnehmerId());
        String name = "";
        if (teilnehmer.isPresent()) {
            name = teilnehmer.get().getVorname() + " " + teilnehmer.get().getNachname();
        }
        String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
        mailService.sendEmail("gateway-service.lhr.tn-ueba-abmelden",
                "german",
                null,
                mailLohnverrechnungRecipients,
                toObjectArray(),
                toObjectArray(name, abmeldungDto.getAustrittsDatum()));
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.TN_TRITT_AUS_INFORM_LOHNVERRECHNUNG, WWorkflowStatus.COMPLETED, createdBy);
        ResponseEntity<?> responseEntity = dienstnehmerService.mapAndSendUebaAbmeldung(abmeldungDto);
        ResponseEntity<String> response = (ResponseEntity<String>) responseEntity;
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return PayloadResponse.builder()
                    .success(false)
                    .message(response.getBody())
                    .build();
        }

        PayloadTypeList<AbmeldungDto> abmeldungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.UEBAABMELDUNGEN.getValue());
        abmeldungDtoPayloadTypeList.setAttributes(List.of(abmeldungDto));

        return PayloadResponse.builder()
                .success(response.getStatusCode().is2xxSuccessful())
                .data(Collections.singletonList(abmeldungDtoPayloadTypeList))
                .build();

    }


    @Override
    public PayloadResponse postKompetenzUebersicht(MultipartFile file, String documentType, String language, String generatePdf, String processDefinitionKey, Integer teilnehmerId) {

        ResponseEntity<?> responseEntity = natifService.sendDocumentToNatif(file, teilnehmerId);
        ResponseEntity<String> response = (ResponseEntity<String>) responseEntity;

        if (response.getStatusCode().is2xxSuccessful()) {

            return getTeilnehmerById(teilnehmerId, false, null);
        }

        return PayloadResponse.builder()
                .success(false)
                .data(List.of())
                .message(response.getBody())
                .build();
    }

    public PayloadResponse getUebaAbmeldung(int page, int size) {

        Page<AbmeldungDto> abmeldungDtoPage = abmeldungService.findAll(PageRequest.of(page, size));

        PayloadTypeList<AbmeldungDto> abmeldungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.UEBAABMELDUNGEN.getValue(),
                abmeldungDtoPage.getContent());

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(abmeldungDtoPayloadTypeList))
                .pagination(createPagination(abmeldungDtoPage))
                .build();

    }

    public PayloadResponse getUebaAbmeldungById(Integer id) {

        Optional<Abmeldung> abmeldung = abmeldungService.findById(id);

        AbmeldungDto abmeldungDto = null;

        if (abmeldung.isPresent()) {
            abmeldungDto = abmeldungService.mapToAbmeldungDto(abmeldung.get());
        }

        if (abmeldungDto == null) {
            return PayloadResponse.builder()
                    .success(false)
                    .data(List.of())
                    .build();
        }

        PayloadTypeList<AbmeldungDto> abmeldungDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.UEBAABMELDUNGEN.getValue());
        abmeldungDtoPayloadTypeList.setAttributes(List.of(abmeldungDto));

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(abmeldungDtoPayloadTypeList))
                .build();
    }


    private String validateTnSortProperty(String sortProp) {
        String[] validProps = {"vorname", "nachname", "svNummer", "personalnummer"};
        if (Arrays.stream(validProps).anyMatch(s -> s.equalsIgnoreCase(sortProp))) {
            return sortProp;
        } else {
            return "nachname";
        }
    }


    private SimpleTNDto adresseToSimpleTN(AdresseIbos adresseIbos) {
        SimpleTNDto simpleTN = new SimpleTNDto();
        if (!isNullOrBlank(adresseIbos.getAdvnf2())) {
            simpleTN.setVorname(adresseIbos.getAdvnf2());
        }
        if (!isNullOrBlank(adresseIbos.getAdznf1())) {
            simpleTN.setNachname(adresseIbos.getAdznf1());
        }
        if (!isNullOrBlank(adresseIbos.getAdsvnr())) {
            simpleTN.setSvn(adresseIbos.getAdsvnr().replace(" ", ""));
        }
        if (adresseIbos.getAdgebdatum() != null) {
            simpleTN.setGeburtsdatum(adresseIbos.getAdgebdatum().toString());
        }
        return simpleTN;
    }

    private TeilnehmerDto adresseIbosToTeilnehmerDto(AdresseIbos adresseIbos, List<SimpleTNDto> alreadyAbgemeldeten) {
        SimpleTNDto simpleTN = adresseToSimpleTN(adresseIbos);
        if (alreadyAbgemeldeten.contains(simpleTN)) {
            return null;
        }
        TeilnehmerDto teilnehmerDto = new TeilnehmerDto();
        if (!isNullOrBlank(adresseIbos.getAdvnf2())) {
            teilnehmerDto.setVorname(adresseIbos.getAdvnf2());
        }
        if (!isNullOrBlank(adresseIbos.getAdznf1())) {
            teilnehmerDto.setNachname(adresseIbos.getAdznf1());
        }
        if (adresseIbos.getAdgebdatum() != null) {
            teilnehmerDto.setGeburtsdatum(adresseIbos.getAdgebdatum().toString());
        }
        if (!isNullOrBlank(adresseIbos.getAdmobil1())) {
            teilnehmerDto.setTelefon(adresseIbos.getAdmobil1());
        } else if (!isNullOrBlank(adresseIbos.getAdmobil2())) {
            teilnehmerDto.setTelefon(adresseIbos.getAdmobil2());
        } else if (!isNullOrBlank(adresseIbos.getAdtelp())) {
            teilnehmerDto.setTelefon(adresseIbos.getAdtelp());
        }
        if (!isNullOrBlank(adresseIbos.getAdsvnr())) {
            teilnehmerDto.setSvNummer(adresseIbos.getAdsvnr().replace(" ", ""));
        }
        if (!isNullOrBlank(adresseIbos.getAdemail1())) {
            teilnehmerDto.setEmail(adresseIbos.getAdemail1());
        }
        if (!isNullOrBlank(adresseIbos.getAdgeschlecht())) {
            teilnehmerDto.setGeschlecht(adresseIbos.getAdgeschlecht());
        }
        if (!isNullOrBlank(adresseIbos.getAdort())) {
            teilnehmerDto.setOrt(adresseIbos.getAdort());
        }
        if (!isNullOrBlank(adresseIbos.getAdplz())) {
            teilnehmerDto.setPlz(adresseIbos.getAdplz());
        }
        if (!isNullOrBlank(adresseIbos.getAdstrasse())) {
            teilnehmerDto.setStrasse(adresseIbos.getAdstrasse());
        }
        if (!isNullOrBlank(adresseIbos.getAdpersnr())) {
            teilnehmerDto.setPersonalnummer(adresseIbos.getAdpersnr());
        }
        if (adresseIbos.getAdadnr() != null) {
            teilnehmerDto.setId(adresseIbos.getAdadnr());
        }
        if (adresseIbos.getAdtitel() != null) {
            String titel = benutzerIbosService.getTitelFromId(adresseIbos.getAdtitel());
            if (!isNullOrBlank(titel)) {
                teilnehmerDto.setTitel(titel);
            }
        }
        if (adresseIbos.getAdtitelv() != null) {
            String titel = benutzerIbosService.getTitelFromId(adresseIbos.getAdtitelv());
            if (!isNullOrBlank(titel)) {
                if (isNullOrBlank(teilnehmerDto.getTitel())) {
                    teilnehmerDto.setTitel(titel);
                } else {
                    teilnehmerDto.setTitel(teilnehmerDto.getTitel() + "," + titel);
                }
            }
        }
        if (adresseIbos.getAdstaatsb() != null) {
            String nation = standortIbosService.getLandIdFromId(adresseIbos.getAdstaatsb());
            if (!isNullOrBlank(nation)) {
                teilnehmerDto.setNation(nation);
            }
        }
        List<TeilnehmerStaging> tns = teilnehmerStagingService.findByVornameAndNachname(
                !isNullOrBlank(adresseIbos.getAdvnf2()) ? adresseIbos.getAdvnf2() : null,
                !isNullOrBlank(adresseIbos.getAdznf1()) ? adresseIbos.getAdznf1() : null);
        Optional<TeilnehmerStaging> tn = tns.stream().findFirst();
        tn.ifPresent(staging -> teilnehmerDto.setId(staging.getTeilnehmerId()));

        return teilnehmerDto;
    }

}
