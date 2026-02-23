package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.repositories.masterdata.Teilnehmer2SeminarRepository;
import com.ibosng.dbservice.services.Teilnehmer2SeminarService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Teilnehmer2SeminarServiceImpl implements Teilnehmer2SeminarService {

    private final Teilnehmer2SeminarRepository teilnehmer2SeminarRepository;

    public Teilnehmer2SeminarServiceImpl(Teilnehmer2SeminarRepository teilnehmer2SeminarRepository) {
        this.teilnehmer2SeminarRepository = teilnehmer2SeminarRepository;
    }

    @Override
    public List<Teilnehmer2Seminar> findAll() {
        return teilnehmer2SeminarRepository.findAll();
    }

    @Override
    public Optional<Teilnehmer2Seminar> findById(Integer id) {
        return teilnehmer2SeminarRepository.findById(id);
    }

    @Override
    public Teilnehmer2Seminar save(Teilnehmer2Seminar object) {
        return teilnehmer2SeminarRepository.save(object);
    }

    @Override
    public List<Teilnehmer2Seminar> saveAll(List<Teilnehmer2Seminar> objects) {
        return teilnehmer2SeminarRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmer2SeminarRepository.deleteById(id);
    }

    @Override
    public List<Teilnehmer2Seminar> findAllByIdentifier(String identifier) {
        return null;
    }

    public Optional<Teilnehmer2Seminar> findByTeilnehmerIdAndSeminarId(Integer teilnehmerId, Integer seminarId) {
        return teilnehmer2SeminarRepository.findByTeilnehmer_IdAndSeminar_Id(teilnehmerId, seminarId);
    }

    @Override
    public List<Teilnehmer2Seminar> findBySeminarId(int id) {
        return teilnehmer2SeminarRepository.findBySeminar_Id(id);
    }

    @Override
    public List<String> getAllMassnahmenummer() {
        return teilnehmer2SeminarRepository.getAllMassnahmenummer();
    }

    @Override
    public List<Teilnehmer2Seminar> findAllByTeilnehmer_Id(Integer teilnehmerId) {
        return teilnehmer2SeminarRepository.findAllByTeilnehmer_Id(teilnehmerId);
    }
}
