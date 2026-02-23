package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.teilnahme.TeilnehmerTeilnahmeOverviewDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmerTeilnahme;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.repositories.TeilnahmeStatusRepository;
import com.ibosng.dbservice.services.TeilnahmeStatusService;
import com.ibosng.dbservice.services.Teilnehmer2SeminarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeilnahmeStatusServiceImpl implements TeilnahmeStatusService {
    private final TeilnahmeStatusRepository teilnahmeStatusRepository;
    private final Teilnehmer2SeminarService teilnehmer2SeminarService;

    @Override
    public List<TeilnahmeStatus> findAll() {
        return teilnahmeStatusRepository.findAll();
    }

    @Override
    public Optional<TeilnahmeStatus> findById(Integer id) {
        return teilnahmeStatusRepository.findById(id);
    }

    @Override
    public TeilnahmeStatus save(TeilnahmeStatus object) {
        return teilnahmeStatusRepository.save(object);
    }

    @Override
    public List<TeilnahmeStatus> saveAll(List<TeilnahmeStatus> objects) {
        return teilnahmeStatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnahmeStatusRepository.deleteById(id);
    }

    @Override
    public List<TeilnahmeStatus> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public List<TeilnahmeStatus> formByTnTeilnahme(TeilnahmerTeilnahme teilnahmerTeilnahme) {
        List<TeilnahmeStatus> teilnahmeStatuses = new ArrayList<>();
        if (teilnahmerTeilnahme.getId() != null) {
            teilnahmeStatuses = teilnahmeStatusRepository.findByTeilnahmerTeilnahme_Id(teilnahmerTeilnahme.getId());
        }
        List<Teilnehmer> teilnehmers = teilnehmer2SeminarService.findBySeminarId(teilnahmerTeilnahme.getSeminar().getId()).stream().map(Teilnehmer2Seminar::getTeilnehmer).toList();

        for (Teilnehmer teilnehmer : teilnehmers) {
            boolean isTnExists = teilnahmeStatuses.stream().anyMatch(tnStatus -> Objects.equals(tnStatus.getTeilnehmer().getId(), teilnehmer.getId()));
            if (!isTnExists) {
                TeilnahmeStatus teilnehmeStatus = new TeilnahmeStatus();
                teilnehmeStatus.setTeilnahmerTeilnahme(teilnahmerTeilnahme);
                teilnehmeStatus.setTeilnehmer(teilnehmer);
                teilnahmeStatuses.add(teilnehmeStatus);
            }
        }
        return teilnahmeStatuses;
    }

    @Override
    public List<TeilnahmeStatus> findByTnTeilnahme(int id) {
        return teilnahmeStatusRepository.findByTeilnahmerTeilnahme_Id(id);
    }

    @Override
    public TeilnehmerTeilnahmeOverviewDto map(TeilnahmeStatus teilnahmeStatus) {
        final String anwesenheit = "X"; //default value, tn-anwesend

        TeilnehmerTeilnahmeOverviewDto overview = new TeilnehmerTeilnahmeOverviewDto();
        if (teilnahmeStatus != null) {
            if (teilnahmeStatus.getTeilnehmer() != null) {
                Teilnehmer teilnehmer = teilnahmeStatus.getTeilnehmer();
                overview.setId(teilnehmer.getId());
                overview.setNachname(teilnehmer.getNachname());
                overview.setVorname(teilnehmer.getVorname());
                if (teilnehmer.getGeburtsdatum() != null) {
                    overview.setGeburtsdatum(teilnehmer.getGeburtsdatum().format(DateTimeFormatter.ISO_LOCAL_DATE));
                }
            }
            if (teilnahmeStatus.getTeilnahmeReason() != null) {
                overview.setStatus(teilnahmeStatus.getTeilnahmeReason().getKuerzel());
            } else {
                overview.setStatus(anwesenheit);
            }
            overview.setInfo(teilnahmeStatus.getInfo());
        }
        return overview;
    }

    @Override
    public TeilnahmeStatus findByTeilnehmerAndTeilnahme(int teilnahmerId, int tnTeilnahmeId) {
        return teilnahmeStatusRepository.findByTeilnehmer_IdAndTeilnahmerTeilnahme_Id(teilnahmerId, tnTeilnahmeId);
    }
}
