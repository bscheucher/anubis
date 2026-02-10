package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.dtos.TeilnahmeBasicDto;
import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;
import com.ibosng.dbibosservice.repositories.TeilnahmeRepository;
import com.ibosng.dbibosservice.services.TeilnahmeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeilnahmeServiceImpl implements TeilnahmeService {

    private final TeilnahmeRepository teilnahmeRepository;

    @Override
    public List<Teilnahme> findBySeminarSmnrAndInPeriod(Integer seminarSmnr, LocalDate datumVon, LocalDate datumBis) {
        return teilnahmeRepository.findByIdSeminarSmnrAndIdDatumBetween(seminarSmnr, datumVon, datumBis);
    }

    @Override
    public Page<Teilnahme> findBySeminarSmnr(Integer seminarSmnr, Pageable pageable) {
        return teilnahmeRepository.findByIdSeminarSmnr(seminarSmnr, pageable);
    }

    @Override
    public List<Teilnahme> findBySeminarSmnrAndDatum(Integer seminarSmnr, Date datum) {
        return teilnahmeRepository.findByIdSeminarSmnrAndIdDatum(seminarSmnr, datum);
    }

    @Override
    public Optional<Teilnahme> findBySeminarSmnrAndDatumAndAdresseAdnr(Integer seminarSmnr, LocalDate datum, Integer adresseAdnr) {
        return teilnahmeRepository.findByIdSeminarSmnrAndIdDatumAndIdAdresseAdnr(seminarSmnr, datum, adresseAdnr);
    }

    @Override
    public List<Teilnahme> findByAdresseAdnr(Integer adresseAdnr) {
        return teilnahmeRepository.findByIdAdresseAdnr(adresseAdnr);
    }

    @Override
    public List<Teilnahme> findByAdresseAdnrAndSeminarSmnr(Integer adresseAdnr, Integer seminarSmnr) {
        return teilnahmeRepository.findByIdAdresseAdnrAndIdSeminarSmnr(adresseAdnr, seminarSmnr);
    }

    @Override
    public List<Teilnahme> findAll() {
        return teilnahmeRepository.findAll();
    }

    @Override
    public Optional<Teilnahme> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Teilnahme save(Teilnahme object) {
        return teilnahmeRepository.save(object);
    }

    @Override
    public List<Teilnahme> saveAll(List<Teilnahme> objects) {
        return teilnahmeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public TeilnahmeBasicDto getTeilnehmerSeminarSummary(List<Integer> seminars, LocalDate datumVon, LocalDate datumBis) {
        List<Object[]> result = teilnahmeRepository.findTeilnehmerSeminarSummary(seminars, datumVon, datumBis);

        if (result.isEmpty()) {
            return new TeilnahmeBasicDto(); // Return empty DTO if no result
        }

        return getTeilnehmerSeminarSummary(result);
    }

    @Override
    public TeilnahmeBasicDto getTeilnehmerSeminarSummary(List<Integer> seminars, List<Integer> reasons, LocalDate datumVon, LocalDate datumBis) {
        List<Object[]> result = teilnahmeRepository.findTeilnehmerSeminarSummaryV1(seminars, reasons, datumVon, datumBis);

        if (result.isEmpty()) {
            return new TeilnahmeBasicDto(); // Return empty DTO if no result
        }

        return getTeilnehmerSeminarSummary(result);
    }

    private TeilnahmeBasicDto getTeilnehmerSeminarSummary(List<Object[]> result) {
        Object[] row = result.get(0); // There should be only one row due to aggregation

        TeilnahmeBasicDto dto = new TeilnahmeBasicDto();

        // Assuming the first column is total_adadnr_count and the second is seminars
        if(row[0] != null) {
            dto.setTeilnehmerNumber(((Number) row[0]).intValue());
        }
        // Split the comma-separated seminars and convert to a list of integers
        if(row[1] != null) {
            String seminarIds = (String) row[1];
            List<Integer> seminarsFound = Arrays.stream(seminarIds.split(","))
                    .map(Integer::parseInt)
                    .toList();
            dto.setSeminars(seminarsFound);
        }
        return dto;
    }
}
