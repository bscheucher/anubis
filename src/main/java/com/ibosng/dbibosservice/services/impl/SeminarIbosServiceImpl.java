package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.dtos.SeminarDto;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.repositories.SeminarIbosRepository;
import com.ibosng.dbibosservice.services.BaseService;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional("mariaDbTransactionManager")
public class SeminarIbosServiceImpl implements BaseService<SeminarIbos>, SeminarIbosService {

    private final SeminarIbosRepository seminarIbosRepository;

    public SeminarIbosServiceImpl(SeminarIbosRepository seminarIbosRepository) {
        this.seminarIbosRepository = seminarIbosRepository;
    }

    @Override
    public List<SeminarIbos> findAll() {
        return seminarIbosRepository.findAll();
    }

    @Override
    public Optional<SeminarIbos> findById(Integer id) {
        return seminarIbosRepository.findById(id);
    }

    @Override
    public SeminarIbos save(SeminarIbos object) {
        return seminarIbosRepository.save(object);
    }

    @Override
    public List<SeminarIbos> saveAll(List<SeminarIbos> objects) {
        return seminarIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarIbosRepository.deleteById(id);
    }

    public List<SeminarDto> getSeminarDataRaw(String user) {
        List<Object[]> rawData = seminarIbosRepository.getSeminarDataRaw(user);
        return rawData.stream()
                .map(this::mapSeminarDto)
                .toList();
    }

    public List<SeminarIbos> findSeminarByDetails(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam) {
        return seminarIbosRepository.findSeminarByBezeichnung1InAndDatumVonAndDatumBisAndZeitVon(identifiers, datumVonParam, datumBisParam, zeitVonParam);
    }

    private SeminarDto mapSeminarDto(Object[] row) {
        return new SeminarDto(
                (Integer) row[0],
                (Integer) row[1],
                (String) row[2],
                (String) row[3],
                mapLocalDateRow(row[4]),
                mapLocalDateRow(row[5]),
                timeToString(row[6]) + " - " + timeToString(row[7]));
    }

    private LocalDate mapLocalDateRow(Object row) {
        return row != null ? ((Date) row).toLocalDate() : null;
    }

    private String timeToString(Object time) {
        return time != null ? ((java.sql.Time) time).toLocalTime().toString() : "";
    }

    public List<SeminarIbos> findAllChangedAfter(LocalDateTime after){
        return seminarIbosRepository.findAllByErdaAfterOrAedaAfter(after, after);
    }

    public Page<SeminarIbos> findAllChangedAfterPageable(LocalDateTime after, Pageable pageable){
        return seminarIbosRepository.findAllByErdaAfterOrAedaAfter(after, after, pageable);
    }

    public Page<SeminarIbos> findAllByEruserAndErdaAfterOrEruserAndAedaAfter(LocalDateTime after, Pageable pageable, String createdBy){
        return seminarIbosRepository.findAllByEruserAndErdaAfterOrEruserAndAedaAfter(createdBy,after, createdBy, after, pageable);
    }

    @Override
    public List<SeminarIbos> findAllByAedaAfterAndDatumBisGreaterThanEqualAndType(LocalDateTime aeda, LocalDate datumBis, Integer type) {
        return seminarIbosRepository.findAllByAedaAfterAndDatumBisGreaterThanEqualAndType(aeda, datumBis, type);
    }

    @Override
    public List<SeminarIbos> fingAllActiveSeminarIbos() {
        return seminarIbosRepository.fingAllActiveSeminarIbos();
    }

    @Override
    public List<SeminarIbos> findAllChangedAndActiveSeminars(LocalDateTime date, LocalDate seminarBis, Integer seminarType) {
        return seminarIbosRepository.findAllChangedAndActiveSeminars(date, seminarBis, seminarType);
    }

}
