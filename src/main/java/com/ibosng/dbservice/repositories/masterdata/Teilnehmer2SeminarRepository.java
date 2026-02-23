package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface Teilnehmer2SeminarRepository extends JpaRepository<Teilnehmer2Seminar, Integer> {

    Optional<Teilnehmer2Seminar> findByTeilnehmer_IdAndSeminar_Id(Integer teilnehmerId, Integer seminarId);

    @Query(value = "select distinct t2s.massnahmennummer from Teilnehmer2Seminar t2s where t2s.massnahmennummer is not null")
    List<String> getAllMassnahmenummer();

    List<Teilnehmer2Seminar> findBySeminar_Id(Integer id);

    List<Teilnehmer2Seminar> findAllByTeilnehmer_Id(Integer teilnehmerId);
}
