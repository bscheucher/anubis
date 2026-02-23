package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Teilnehmer2SeminarService extends BaseService<Teilnehmer2Seminar> {
    Optional<Teilnehmer2Seminar> findByTeilnehmerIdAndSeminarId(Integer teilnehmerId, Integer SeminarId);

    List<Teilnehmer2Seminar> findBySeminarId(int id);

    List<String> getAllMassnahmenummer();

    @Query(value = "select t2s from Teilnehmer2Seminar t2s where t2s.teilnehmer.id = :teilnehmerId")
    List<Teilnehmer2Seminar> findAllByTeilnehmer_Id(Integer teilnehmerId);
}