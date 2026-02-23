package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.entities.smtn.SmTnId;
import com.ibosng.dbibosservice.repositories.SeminarTeilnehmerRepository;
import com.ibosng.dbibosservice.services.SeminarTeilnehmerIbosService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeminarTeilnehmerIbosServiceImpl implements SeminarTeilnehmerIbosService {

    private final SeminarTeilnehmerRepository seminarTeilnehmerRepository;

    public SeminarTeilnehmerIbosServiceImpl(SeminarTeilnehmerRepository seminarTeilnehmerRepository) {
        this.seminarTeilnehmerRepository = seminarTeilnehmerRepository;
    }


    @Override
    public List<SeminarTeilnehmerIbos> findAll() {
        return seminarTeilnehmerRepository.findAll();
    }

    @Override
    public Optional<SeminarTeilnehmerIbos> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<SeminarTeilnehmerIbos> findById(SmTnId id) {
        return seminarTeilnehmerRepository.findById(id);
    }

    @Override
    public SeminarTeilnehmerIbos findByAdresseAndSeminar(Integer adresse, Integer seminar) {
        return seminarTeilnehmerRepository.findByAdresseAndSeminar(adresse, seminar);
    }

    @Override
    public List<SeminarTeilnehmerIbos> findAllByTaeruserAndTaerdaAfterOrTaeruserAndTaaeda(String taeruser, LocalDateTime taerdaAfter, String taeruser1, LocalDateTime taaeda) {
        return seminarTeilnehmerRepository.findAllByTaeruserAndTaerdaAfterOrTaeruserAndTaaeda(taeruser, taerdaAfter, taeruser1, taaeda);
    }

    @Override
    public SeminarTeilnehmerIbos save(SeminarTeilnehmerIbos object) {
        return seminarTeilnehmerRepository.save(object);
    }

    @Override
    public List<SeminarTeilnehmerIbos> saveAll(List<SeminarTeilnehmerIbos> objects) {
        return seminarTeilnehmerRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void deleteById(SmTnId id) {
        seminarTeilnehmerRepository.deleteById(id);
    }

    @Override
    public List<SeminarTeilnehmerIbos> findByAdresseNr(Integer adresseNr) {
        return seminarTeilnehmerRepository.findByAdresseNr(adresseNr);
    }
}
