package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.teilnahme.*;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeReason;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmerTeilnahme;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.repositories.TeilnahmerTeilnahmeRepository;
import com.ibosng.dbservice.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeilnahmerTeilnahmeServiceImpl implements TeilnahmerTeilnahmeService {

    private final TeilnahmerTeilnahmeRepository teilnahmerTeilnahmeRepository;
    private final TeilnahmeStatusService teilnahmeStatusService;
    private final TeilnahmeReasonService teilnahmeReasonService;
    private final TeilnehmerService teilnehmerService;

    private final SeminarService seminarService;

    @Override
    public List<TeilnahmerTeilnahme> findAll() {
        return teilnahmerTeilnahmeRepository.findAll();
    }

    @Override
    public Optional<TeilnahmerTeilnahme> findById(Integer id) {
        return teilnahmerTeilnahmeRepository.findById(id);
    }

    @Override
    public TeilnahmerTeilnahme save(TeilnahmerTeilnahme object) {
        return teilnahmerTeilnahmeRepository.save(object);
    }

    @Override
    public List<TeilnahmerTeilnahme> saveAll(List<TeilnahmerTeilnahme> objects) {
        return teilnahmerTeilnahmeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnahmerTeilnahmeRepository.deleteById(id);
    }

    @Override
    public List<TeilnahmerTeilnahme> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public TeilnahmeOverviewDto formOverview(int seminarId, LocalDate date) {
        TeilnahmerTeilnahme teilnahmerTeilnahme = teilnahmerTeilnahmeRepository.findBySeminar_IdAndDatum(seminarId, date);
        if (teilnahmerTeilnahme == null) {
            log.info("Teilnahme entry not found for seminar {}, and date {}. Data will be formed", seminarId, date);

            Optional<Seminar> seminarOptional = seminarService.findById(seminarId);
            if (seminarOptional.isEmpty()) {
                log.info("Seminar not found for id {}", seminarId);
                return null;
            }

            teilnahmerTeilnahme = new TeilnahmerTeilnahme();
            teilnahmerTeilnahme.setDatum(date);
            teilnahmerTeilnahme.setSeminar(seminarOptional.get());
            teilnahmerTeilnahme.setStatus(Status.NEW);
        }

        TeilnahmeMetadataDto seminarOverviewDto = map(teilnahmerTeilnahme);
        List<TeilnehmerTeilnahmeOverviewDto> teilnehmerTeilnahmeOverviewDto = teilnahmeStatusService
                .formByTnTeilnahme(teilnahmerTeilnahme)
                .stream()
                .map(teilnahmeStatusService::map)
                .toList();

        return TeilnahmeOverviewDto.builder()
                .teilnahmeMetadata(seminarOverviewDto)
                .teilnehmers(teilnehmerTeilnahmeOverviewDto)
                .build();
    }

    @Override
    public TeilnahmeMetadataDto map(TeilnahmerTeilnahme teilnahmerTeilnahme) {
        TeilnahmeMetadataDto seminarOverviewDto = new TeilnahmeMetadataDto();
        if (teilnahmerTeilnahme != null) {
            seminarOverviewDto.setStatus(teilnahmerTeilnahme.getStatus());
            if (teilnahmerTeilnahme.getSeminar() != null) {
                Seminar seminar = teilnahmerTeilnahme.getSeminar();
                seminarOverviewDto.setBezeichnung(seminar.getBezeichnung());
                seminarOverviewDto.setSeminarId(seminar.getId());
                if (seminar.getProject() != null) {
                    seminarOverviewDto.setProjekt(seminar.getProject().getBezeichnung());
                }
            }
            if (isNullOrBlank(teilnahmerTeilnahme.getChangedBy())) {
                seminarOverviewDto.setChangedBy(teilnahmerTeilnahme.getCreatedBy());
            } else {
                seminarOverviewDto.setChangedBy(teilnahmerTeilnahme.getChangedBy());
            }
            if (teilnahmerTeilnahme.getChangedOn() == null) {
                seminarOverviewDto.setChangedOn(teilnahmerTeilnahme.getCreatedOn().format(DateTimeFormatter.ISO_LOCAL_DATE));
            } else {
                seminarOverviewDto.setChangedOn(teilnahmerTeilnahme.getChangedOn().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
        return seminarOverviewDto;
    }

    @Override
    public void saveTeilnahme(TeilnahmeCreationDto teilnahmeCreationDto, Benutzer benutzer) {
        TeilnahmerTeilnahme tnTeilnahme = teilnahmerTeilnahmeRepository.findBySeminar_IdAndDatum(teilnahmeCreationDto.getSeminarId(), teilnahmeCreationDto.getDate());
        if (tnTeilnahme == null) {
            log.info("tnTeilnahme for seminar:{} and date:{} will be created", teilnahmeCreationDto.getSeminarId(), teilnahmeCreationDto.getDate());
            Optional<Seminar> seminar = seminarService.findById(teilnahmeCreationDto.getSeminarId());
            if(seminar.isEmpty())  {
                log.info("Seminar with id:{} not found", teilnahmeCreationDto.getSeminarId());
                return;
            }
            tnTeilnahme = new TeilnahmerTeilnahme();
            tnTeilnahme.setStatus(Status.ACTIVE);
            tnTeilnahme.setDatum(teilnahmeCreationDto.getDate());
            tnTeilnahme.setSeminar(seminar.get());
            tnTeilnahme.setCreatedBy(benutzer.getFirstName() + " " + benutzer.getLastName());
        } else {
            tnTeilnahme.setChangedBy(benutzer.getFirstName() + " " + benutzer.getLastName());
            tnTeilnahme.setChangedOn(getLocalDateNow());
        }
        tnTeilnahme = save(tnTeilnahme);

        for (TeilnahmerTeilnahmeCreationDto teilnehmerDto: teilnahmeCreationDto.getTeilnehmers()) {
            TeilnahmeStatus teilnahmeStatus = teilnahmeStatusService.findByTeilnehmerAndTeilnahme(teilnehmerDto.getTeilnahmerId(), tnTeilnahme.getId());
            if (teilnahmeStatus == null) {
                Optional<Teilnehmer> teilnehmer = teilnehmerService.findById(teilnehmerDto.getTeilnahmerId());
                if (teilnehmer.isEmpty()) {
                    log.info("Teilnehmer with id:{} not found", teilnehmerDto.getTeilnahmerId());
                    return;
                }
                teilnahmeStatus = new TeilnahmeStatus();
                teilnahmeStatus.setTeilnahmerTeilnahme(tnTeilnahme);
                teilnahmeStatus.setTeilnehmer(teilnehmer.get());
                teilnahmeStatus.setStatus(Status.ACTIVE);
            }
            TeilnahmeReason teilnahmeReason = teilnahmeReasonService.findByKuerzel(teilnehmerDto.getStatus());
            if(teilnahmeReason == null) {
                log.info("Teilnahme reason:{} not found", teilnehmerDto.getStatus());
                return;
            }
            teilnahmeStatus.setTeilnahmeReason(teilnahmeReason);
            teilnahmeStatus.setInfo(teilnehmerDto.getInfo());

            teilnahmeStatusService.save(teilnahmeStatus);
        }
    }
}