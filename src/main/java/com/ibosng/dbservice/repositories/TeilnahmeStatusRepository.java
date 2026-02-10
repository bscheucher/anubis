package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnahmeStatusRepository extends JpaRepository<TeilnahmeStatus, Integer> {
    List<TeilnahmeStatus> findByTeilnahmerTeilnahme_Id(Integer id);

    TeilnahmeStatus findByTeilnehmer_IdAndTeilnahmerTeilnahme_Id(Integer id, Integer id1);
}
