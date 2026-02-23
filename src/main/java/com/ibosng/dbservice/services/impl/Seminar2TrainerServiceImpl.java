package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.Seminar2TrainerDto;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.repositories.seminar.Seminar2TrainerRepository;
import com.ibosng.dbservice.services.Seminar2TrainerService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class Seminar2TrainerServiceImpl implements Seminar2TrainerService {

    private final Seminar2TrainerRepository seminar2TrainerRepository;

    public Seminar2TrainerServiceImpl(Seminar2TrainerRepository seminar2TrainerRepository) {
        this.seminar2TrainerRepository = seminar2TrainerRepository;
    }

    @Override
    public Optional<Seminar2Trainer> findByTrainerId(Integer trainerId) {
        return seminar2TrainerRepository.findSeminar2TrainerByTrainer_Id((trainerId));
    }

    @Override
    public Optional<Seminar2Trainer> findBySeminarId(Integer seminarId) {
        return seminar2TrainerRepository.findSeminar2TrainerBySeminar_Id(seminarId);
    }

    @Override
    public List<Seminar2Trainer> findAll() {
        return seminar2TrainerRepository.findAll();
    }

    @Override
    public Optional<Seminar2Trainer> findById(Integer id) {
        return seminar2TrainerRepository.findById(id);
    }

    @Override
    public Seminar2Trainer save(Seminar2Trainer object) {
        return seminar2TrainerRepository.save(object);
    }

    @Override
    public List<Seminar2Trainer> saveAll(List<Seminar2Trainer> objects) {
        return seminar2TrainerRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminar2TrainerRepository.deleteById(id);
    }

    @Override
    public List<Seminar2Trainer> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Seminar2Trainer> findAllByTrainerId(Integer trainerId) {
        return seminar2TrainerRepository.findByTrainer_Id(trainerId);
    }

    @Override
    public List<Seminar2Trainer> findAllBySeminarId(Integer seminarId) {
        return seminar2TrainerRepository.findBySeminar_Id(seminarId);
    }

    @Override
    public Seminar2TrainerDto map(Seminar2Trainer seminar2Trainer) {
        if (seminar2Trainer == null) {
            return null;
        }

        Seminar2TrainerDto seminar2TrainerDto = new Seminar2TrainerDto();

        if (seminar2Trainer.getSeminar() != null) {
            seminar2TrainerDto.setSeminarId(seminar2Trainer.getSeminar().getId());
            seminar2TrainerDto.setBezeichnung(seminar2Trainer.getSeminar().getBezeichnung());
            seminar2TrainerDto.setStandort(seminar2Trainer.getSeminar().getStandort());
            seminar2TrainerDto.setMasnahmennummer(seminar2Trainer.getSeminar().getMassnahmenNr());
        }

        if (seminar2Trainer.getStartDate() != null) {
            seminar2TrainerDto.setStartDate(seminar2Trainer.getStartDate().format(DateTimeFormatter.ISO_DATE));
        }

        if (seminar2Trainer.getEndDate() != null) {
            seminar2TrainerDto.setEndDate(seminar2Trainer.getEndDate().format(DateTimeFormatter.ISO_DATE));
        }

        seminar2TrainerDto.setRole(seminar2Trainer.getRole());

        return seminar2TrainerDto;
    }
}
