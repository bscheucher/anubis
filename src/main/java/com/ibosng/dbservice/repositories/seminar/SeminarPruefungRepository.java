package com.ibosng.dbservice.repositories.seminar;


import com.ibosng.dbservice.entities.seminar.SeminarPruefung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface SeminarPruefungRepository extends JpaRepository<SeminarPruefung, Integer> {
    List<SeminarPruefung> findByTeilnehmer2Seminar_Id(Integer teilnehmer2SeminarId);
}
