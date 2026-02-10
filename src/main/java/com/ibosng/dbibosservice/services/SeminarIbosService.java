package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.dtos.SeminarDto;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SeminarIbosService {
    List<SeminarIbos> findAll();

    Optional<SeminarIbos> findById(Integer id);

    SeminarIbos save(SeminarIbos object);

    List<SeminarIbos> saveAll(List<SeminarIbos> objects);

    void deleteById(Integer id);

    List<SeminarDto> getSeminarDataRaw(String user);

    List<SeminarIbos> findSeminarByDetails(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam);

    List<SeminarIbos> findAllChangedAfter(LocalDateTime after);

    Page<SeminarIbos> findAllChangedAfterPageable(LocalDateTime after, Pageable pageable);

    Page<SeminarIbos> findAllByEruserAndErdaAfterOrEruserAndAedaAfter(LocalDateTime after, Pageable pageable, String createdBy);

    List<SeminarIbos> findAllByAedaAfterAndDatumBisGreaterThanEqualAndType(LocalDateTime aeda, LocalDate datumBis, Integer type);

    List<SeminarIbos> fingAllActiveSeminarIbos();

    List<SeminarIbos> findAllChangedAndActiveSeminars(LocalDateTime date, LocalDate seminarBis, Integer seminarType);
}

