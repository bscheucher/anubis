package com.ibosng.teilnehmerportal.repository;

import com.ibosng.teilnehmerportal.entity.TeilnehmerAbwesenheitsbestaetigung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerAbwesenheitsbestaetigungRepository extends JpaRepository<TeilnehmerAbwesenheitsbestaetigung, Integer> {
}
