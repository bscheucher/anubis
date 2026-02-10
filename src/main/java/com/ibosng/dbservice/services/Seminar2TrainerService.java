package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.Seminar2TrainerDto;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;

import java.util.List;
import java.util.Optional;

public interface Seminar2TrainerService extends BaseService<Seminar2Trainer> {
    @Deprecated(since = "trainer-2-seminar is many-to-many")
    Optional<Seminar2Trainer> findByTrainerId(Integer trainerId);

    @Deprecated(since = "trainer-2-seminar is many-to-many")
    Optional<Seminar2Trainer> findBySeminarId(Integer seminarId);

    List<Seminar2Trainer> findAllByTrainerId(Integer trainerId);

    List<Seminar2Trainer> findAllBySeminarId(Integer seminarId);

    Seminar2TrainerDto map(Seminar2Trainer seminar2Trainer);
}
