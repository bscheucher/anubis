package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.dtos.TeilnahmeBasicDto;
import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TeilnahmeService extends BaseService<Teilnahme> {

    List<Teilnahme> findBySeminarSmnrAndInPeriod(Integer seminarSmnr, LocalDate datumBis, LocalDate datumVon);

    Page<Teilnahme> findBySeminarSmnr(Integer seminarSmnr, Pageable pageable);

    List<Teilnahme> findBySeminarSmnrAndDatum(Integer seminarSmnr, Date datum);

    Optional<Teilnahme> findBySeminarSmnrAndDatumAndAdresseAdnr(Integer seminarSmnr, LocalDate datum, Integer adresseAdnr);

    List<Teilnahme> findByAdresseAdnr(Integer adresseAdnr);

    List<Teilnahme> findByAdresseAdnrAndSeminarSmnr(Integer adresseAdnr, Integer seminarSmnr);

    TeilnahmeBasicDto getTeilnehmerSeminarSummary(List<Integer> seminars, LocalDate datumVon, LocalDate datumBis);

    TeilnahmeBasicDto getTeilnehmerSeminarSummary(List<Integer> seminars, List<Integer> reasons, LocalDate datumVon, LocalDate datumBis);
}
