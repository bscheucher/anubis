package com.ibosng.dbservice.repositories.seminar;

import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface Seminar2TrainerRepository extends JpaRepository<Seminar2Trainer, Integer> {

    Optional<Seminar2Trainer> findSeminar2TrainerByTrainer_Id(Integer trainerId);

    Optional<Seminar2Trainer> findSeminar2TrainerBySeminar_Id(Integer seminarId);

    List<Seminar2Trainer> findByTrainer_Id(Integer id);

    List<Seminar2Trainer> findBySeminar_Id(Integer id);
}
