package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungBezeichnung;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungBezeichnungRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungBezeichnungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungBezeichnungServiceImpl implements SeminarPruefungBezeichnungService {

    private final SeminarPruefungBezeichnungRepository seminarPruefungBezeichnungRepository;

    public SeminarPruefungBezeichnungServiceImpl(SeminarPruefungBezeichnungRepository seminarPruefungBezeichnungRepository) {
        this.seminarPruefungBezeichnungRepository = seminarPruefungBezeichnungRepository;
    }

    @Override
    public List<SeminarPruefungBezeichnung> findAll() {
        return seminarPruefungBezeichnungRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungBezeichnung> findById(Integer id) {
        return seminarPruefungBezeichnungRepository.findById(id);
    }

    @Override
    public SeminarPruefungBezeichnung save(SeminarPruefungBezeichnung object) {
        return seminarPruefungBezeichnungRepository.save(object);
    }

    @Override
    public List<SeminarPruefungBezeichnung> saveAll(List<SeminarPruefungBezeichnung> objects) {
        return seminarPruefungBezeichnungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungBezeichnungRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungBezeichnung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungBezeichnung findByName(String name) {
        return seminarPruefungBezeichnungRepository.findByName(name);
    }
}
