package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.teilnehmer.TeilnahmerTeilnahme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnahmerTeilnahmeRepository extends JpaRepository<TeilnahmerTeilnahme, Integer> {
    TeilnahmerTeilnahme findBySeminar_IdAndDatum(Integer id, LocalDate datum);
}
