package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungNiveau;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungNiveauRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungNiveauService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungNiveauServiceImpl implements SeminarPruefungNiveauService {

    private final SeminarPruefungNiveauRepository seminarPruefungNiveauRepository;

    public SeminarPruefungNiveauServiceImpl(SeminarPruefungNiveauRepository seminarPruefungNiveauRepository) {
        this.seminarPruefungNiveauRepository = seminarPruefungNiveauRepository;
    }

    @Override
    public List<SeminarPruefungNiveau> findAll() {
        return seminarPruefungNiveauRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungNiveau> findById(Integer id) {
        return seminarPruefungNiveauRepository.findById(id);
    }

    @Override
    public SeminarPruefungNiveau save(SeminarPruefungNiveau object) {
        return seminarPruefungNiveauRepository.save(object);
    }

    @Override
    public List<SeminarPruefungNiveau> saveAll(List<SeminarPruefungNiveau> objects) {
        return seminarPruefungNiveauRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungNiveauRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungNiveau> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungNiveau findByName(String name) {
        return seminarPruefungNiveauRepository.findByName(name);
    }
}
