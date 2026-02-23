package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbibosservice.entities.pzleistung.PzleistungId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PzleistungService extends BaseService<Pzleistung> {
    Optional<Pzleistung> findById(PzleistungId pzleistungId);

    Page<Pzleistung> findByADadnrInPeriod(Integer adadnr, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Pzleistung> findByADadnrInPeriod(Integer adadnr, LocalDate startDate, LocalDate endDate);

    Page<Pzleistung> findByADadnrInPeriod(List<Integer> adadnrList, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Pzleistung> findByADadnrInChangePeriod(Integer adadnr, LocalDateTime startDate, LocalDateTime endDate);
}
