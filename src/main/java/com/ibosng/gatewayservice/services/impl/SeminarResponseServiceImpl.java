package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.SeminarAnAbwesenheitDto;
import com.ibosng.dbservice.dtos.TrainerDto;
import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeCreationDto;
import com.ibosng.dbservice.dtos.teilnahme.TeilnahmeOverviewDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.services.SeminarService;
import com.ibosng.dbservice.services.TeilnahmerTeilnahmeService;
import com.ibosng.dbservice.services.Teilnehmer2SeminarService;
import com.ibosng.dbservice.services.impl.TeilnehmerServiceImpl;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.dbservice.services.masterdata.TitelService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.gatewayservice.dtos.response.Pagination;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadType;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.dtos.seminar.SeminarKey;
import com.ibosng.gatewayservice.dtos.teilnehmerPayload.MassnahmenummerPayload;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.gatewayservice.services.PLZOrtService;
import com.ibosng.gatewayservice.services.SeminarResponseService;
import com.ibosng.gatewayservice.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.gatewayservice.enums.PayloadTypes.TEILNAHME_OVERVIEW;
import static com.ibosng.gatewayservice.utils.Helpers.createPagination;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeminarResponseServiceImpl implements SeminarResponseService {

    private final TeilnehmerServiceImpl teilnehmerService;
    private final TeilnehmerStagingServiceImpl teilnehmerStagingService;
    private final Gateway2Validation gateway2Validation;
    private final PLZOrtService plzOrtService;
    private final SeminarService seminarService;
    private final StammdatenService stammdatenService;
    private final TitelService titelService;
    private final BenutzerDetailsServiceImpl benutzerDetailsService;
    private final Teilnehmer2SeminarService teilnehmer2SeminarService;
    private final TeilnahmerTeilnahmeService teilnahmerTeilnahmeService;

    @Override
    public PayloadResponse validateTeilnehmer(TeilnehmerDto invalidTeilnehmerDto, String changedBy, Optional<Boolean> savePLZOrt) {
        if (savePLZOrt.isPresent() && savePLZOrt.get()) {
            boolean isPLZSaved = plzOrtService.addPLZAndOrtAssociation(invalidTeilnehmerDto, changedBy);
            if (!isPLZSaved) {
                log.error("Could not save plz and ort association");
            }
        }
        log.info("Preparing to send a request to validation service.");
        TeilnehmerDto teilnehmer = gateway2Validation.validateSingleTeilnehmer(invalidTeilnehmerDto, changedBy);

        List<TeilnehmerDto> teilnehmerList = Collections.singletonList(teilnehmer);
        //TODO Update Stammdaten
        if (teilnehmer != null) {
            Optional<Teilnehmer> teilnehmerOptional = teilnehmerService.findById(teilnehmer.getId());
            if (teilnehmerOptional.isPresent()) {
                Teilnehmer persistedTeilnehmer = teilnehmerOptional.get();
                // Check if the teilnehmer has a personalnummer
                if (persistedTeilnehmer.getPersonalnummer() != null) {
                    updateMitarbeiterStammdaten(persistedTeilnehmer, changedBy);
                }
            }
        }
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<TeilnehmerDto> teilnehmerPage = new PageImpl<>(teilnehmerList, pageRequest, teilnehmerList.size());
        PayloadTypeList<TeilnehmerSeminarDto> participantDtoPayloadType = new PayloadTypeList<>("teilnehmerSeminars");
        TeilnehmerSeminarDto response = new TeilnehmerSeminarDto();
        response.setTeilnehmerDto(teilnehmer);
        participantDtoPayloadType.setAttributes(Collections.singletonList(response));
        return createPayloadResponse(Collections.singletonList(participantDtoPayloadType), teilnehmerPage);
    }

    private void updateMitarbeiterStammdaten(Teilnehmer teilnehmer, String changedBy) {
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(teilnehmer.getPersonalnummer().getPersonalnummer());
        if (stammdaten != null) {
            //TODO Check if titel can be set
            Helpers.updateStammdatenFromTeilnehmer(teilnehmer, stammdaten);
            if (teilnehmer.getTitel() != null) {
                stammdaten.setTitel(titelService.findByName(teilnehmer.getTitel()));
            }
            stammdaten.setChangedBy(changedBy);
            stammdatenService.save(stammdaten);
        }
    }

    @Override
    public PayloadResponse getSeminarByStatusAndProjectName(Boolean isActive, Boolean isKorrigieren, String projectName, Benutzer benutzer) {
        List<String> seminars = seminarService.findAllByStatusAndProjectName(projectName, isActive, isKorrigieren, benutzer);
        PayloadTypeList<String> seminarPayloadPayloadType = new PayloadTypeList<>(PayloadTypes.SEMINARS.getValue(), seminars);
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(seminarPayloadPayloadType))
                .build();
    }

    @Override
    public PayloadResponse getAllSeminars(Boolean isUeba, int page, int size) {
        Page<BasicSeminarDto> resp = seminarService.getAllSeminarsPageable(isUeba, page, size);
        PayloadTypeList<BasicSeminarDto> basicSeminarDtoPayloadType = new PayloadTypeList<>(PayloadTypes.SEMINARS.getValue(), resp.stream().toList());
        Pagination pagination = new Pagination(resp.getTotalElements(), size, page);
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(basicSeminarDtoPayloadType))
                .pagination(pagination)
                .build();
    }

    @Override
    public PayloadResponse getSeminarAnAbwesenheitDto(String token, boolean isAdmin, Boolean isActive,
                                                      String projectName, String seminarName,
                                                      String kursEndeFrom, String kursEndeTo, Boolean verzoegerung,
                                                      String sortProperty,
                                                      Direction sortDirection, int page, int size) {

        Integer benutzerId = benutzerDetailsService.getUserFromToken(token).getId();

        Page<SeminarAnAbwesenheitDto> resp = seminarService.findAllSeminarAnUndAbwesenheit(
                isAdmin, benutzerId, isActive, projectName, seminarName,
                kursEndeFrom != null ? parseDate(kursEndeFrom) : null,
                kursEndeTo != null ? parseDate(kursEndeTo) : null,
                verzoegerung, sortProperty, sortDirection, page, size);
        List<SeminarAnAbwesenheitDto> responseList = resp.stream().toList();
        if (isAdmin) {
            for (SeminarAnAbwesenheitDto entry : responseList) {
                Seminar foundSeminar = seminarService.findById(entry.getSeminarId()).orElse(null);
                if (foundSeminar != null) {
                    entry.setTrainers(foundSeminar.getTrainerSeminars().stream().map(this::toDto)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                }
            }
        }

        PayloadTypeList<SeminarAnAbwesenheitDto> seminarAnAbwesenheitDtoPayloadTypeList = new PayloadTypeList<>(
                PayloadTypes.SEMINARS.getValue(), responseList);

        Pagination pagination = new Pagination(resp.getTotalElements(), size, page);

        return PayloadResponse.builder()
                .success(true)
                .data(List.of(seminarAnAbwesenheitDtoPayloadTypeList))
                .pagination(pagination)
                .build();
    }

    @Override
    public PayloadResponse getAllMassnahmenummer() {
        List<String> response = teilnehmer2SeminarService.getAllMassnahmenummer();
        PayloadTypeList<String> massnahmenummerDtoPayloadType = new PayloadTypeList<>(PayloadTypes.MASSNAHMENUMMER.getValue(), response.stream().toList());
        Pagination pagination = new Pagination();
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(massnahmenummerDtoPayloadType))
                .pagination(pagination)
                .build();
    }

    @Override
    public PayloadResponse getTeilnahme(int id, String date) {
        LocalDate localDate = parseDate(date);
        if (localDate == null) {
            return PayloadResponse.builder().success(false).message("Date-%s can't parsed".formatted(date)).build();
        }
        TeilnahmeOverviewDto overwievDto = teilnahmerTeilnahmeService.formOverview(id, localDate);
        PayloadTypeList<TeilnahmeOverviewDto> payloadTypeList = new PayloadTypeList<>(TEILNAHME_OVERVIEW.getValue(), List.of(overwievDto));
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(payloadTypeList))
                .build();
    }

    @Override
    public PayloadResponse postTeilnahme(Benutzer benutzer, TeilnahmeCreationDto teilnahmeCreationDto) {
        teilnahmerTeilnahmeService.saveTeilnahme(teilnahmeCreationDto, benutzer);
        TeilnahmeOverviewDto overviewDto = teilnahmerTeilnahmeService.formOverview(teilnahmeCreationDto.getSeminarId(), teilnahmeCreationDto.getDate());
        PayloadTypeList<TeilnahmeOverviewDto> payloadTypeList = new PayloadTypeList<>(TEILNAHME_OVERVIEW.getValue(), List.of(overviewDto));
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(payloadTypeList))
                .build();
    }

    private TrainerDto toDto(Seminar2Trainer seminar2Trainer) {
        if (seminar2Trainer == null || seminar2Trainer.getTrainer() == null) {
            return null;
        }

        Benutzer trainer = seminar2Trainer.getTrainer();

        TrainerDto dto = new TrainerDto();
        dto.setId(trainer.getId());
        dto.setName((trainer.getFirstName() + " " + trainer.getLastName()).trim());
        dto.setEmail(trainer.getEmail());
        dto.setFunktion(seminar2Trainer.getTrainerFunktion());

        // Set Telefonnummer
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(trainer.getPersonalnummer().getPersonalnummer());
        if (stammdaten != null && stammdaten.getMobilnummer() != null && stammdaten.getMobilnummer().getLand() != null) {
            Telefon mobilNummer = stammdaten.getMobilnummer();
            String mobilNummerString = mobilNummer.getLand().getTelefonvorwahl() + mobilNummer.getTelefonnummer();
            dto.setTelefon(mobilNummerString);
        }


        return dto;
    }

    private PayloadResponse constructPayloadForFilteredTeilnehmer(int page, int size, List<Teilnehmer> teilnehmerList, Integer totalNumberOfTeilnehmer, boolean isSeminarPresent, String seminarIdentifier) {
        Map<SeminarKey, List<Integer>> seminarParticipantsMap = new HashMap<>();
        Map<String, List<Integer>> massnahmenummerMap = new HashMap<>();

        List<TeilnehmerDto> participantDtos = teilnehmerList.stream()
                .flatMap(teilnehmer -> teilnehmerStagingService.mapTeilnehmer2TeilnehmerDto(teilnehmer, true, isSeminarPresent, seminarIdentifier, false).stream().peek(dto -> {
                    if (teilnehmer.getErrors().stream().noneMatch(error -> error.getError().equals("seminar"))) {
                        SeminarKey seminarKeyValue = new SeminarKey(
                                Optional.ofNullable(dto.getSeminarNumber()).orElse(0),
                                Optional.ofNullable(dto.getSeminarBezeichnung()).orElse("")
                        );
                        seminarParticipantsMap.computeIfAbsent(seminarKeyValue, k -> new ArrayList<>()).add(teilnehmer.getId());
                    }
                    if (!isNullOrBlank(dto.getMassnahmennummer())) {
                        massnahmenummerMap.computeIfAbsent(dto.getMassnahmennummer(), k -> new ArrayList<>()).add(teilnehmer.getId());
                    }
                }))
                .toList();

        PayloadTypeList<TeilnehmerDto> participantDtoPayloadType = new PayloadTypeList<>(PayloadTypes.TEILNEHMER.getValue());
        participantDtoPayloadType.setAttributes(participantDtos);
        Pagination pagination = new Pagination(totalNumberOfTeilnehmer, size, page);
        List<String> seminars = teilnehmerService.findSeminarsForParticipantsWithStatus(TeilnehmerStatus.INVALID);
        List<String> massnahmenummern = teilnehmerService.findMassnahmenummersForParticipantsWithStatus(TeilnehmerStatus.INVALID);
        PayloadTypeList<String> seminarPayloadPayloadType = new PayloadTypeList<>(PayloadTypes.SEMINARS.getValue(), seminars);
        PayloadTypeList<String> massnahmenummerPayloadPayloadType = new PayloadTypeList<>(PayloadTypes.MASSNAHMENUMMER.getValue(), massnahmenummern);
        return createTeilnehmerFilteredPayloadResponse(Arrays.asList(participantDtoPayloadType, seminarPayloadPayloadType, massnahmenummerPayloadPayloadType), pagination);
    }

    private PayloadTypeList<MassnahmenummerPayload> createMassnahmenummerPayloadResponse(Map<String, List<Integer>> massnahmenummerMap) {
        List<MassnahmenummerPayload> massnahmenummerPayloads = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : massnahmenummerMap.entrySet()) {
            massnahmenummerPayloads.add(new MassnahmenummerPayload(entry.getKey(), entry.getValue()));
        }

        return new PayloadTypeList<>(PayloadTypes.MASSNAHMENUMMER.getValue(), massnahmenummerPayloads);
    }

    private <T> PayloadResponse createPayloadResponse(List<PayloadType> payloadTypes, Page<T> page) {
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(payloadTypes);
        response.setPagination(createPagination(page));
        return response;
    }

    private PayloadResponse createTeilnehmerFilteredPayloadResponse(List<PayloadType> payloadTypes, Pagination pagination) {
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        response.setData(payloadTypes);
        response.setPagination(pagination);
        return response;
    }
}
