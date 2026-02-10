package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Transactional("mariaDbTransactionManager")
public interface SeminarIbosRepositoryExtended {
    List<SeminarIbos> findSeminarByBezeichnung1InAndDatumVonAndDatumBisAndZeitVon(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam);
}
