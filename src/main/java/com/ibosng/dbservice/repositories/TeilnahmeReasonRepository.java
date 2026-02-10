package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnahmeReasonRepository extends JpaRepository<TeilnahmeReason, Integer> {
    TeilnahmeReason findByKuerzel(String kuerzel);
}
