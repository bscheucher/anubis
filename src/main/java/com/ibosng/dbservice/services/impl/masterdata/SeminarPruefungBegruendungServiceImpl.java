package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungBegruendung;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungBegruendungRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungBegruendungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungBegruendungServiceImpl implements SeminarPruefungBegruendungService {

    private final SeminarPruefungBegruendungRepository seminarPruefungBegruendungRepository;

    public SeminarPruefungBegruendungServiceImpl(SeminarPruefungBegruendungRepository seminarPruefungBegruendungRepository) {
        this.seminarPruefungBegruendungRepository = seminarPruefungBegruendungRepository;
    }

    @Override
    public List<SeminarPruefungBegruendung> findAll() {
        return seminarPruefungBegruendungRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungBegruendung> findById(Integer id) {
        return seminarPruefungBegruendungRepository.findById(id);
    }

    @Override
    public SeminarPruefungBegruendung save(SeminarPruefungBegruendung object) {
        return seminarPruefungBegruendungRepository.save(object);
    }

    @Override
    public List<SeminarPruefungBegruendung> saveAll(List<SeminarPruefungBegruendung> objects) {
        return seminarPruefungBegruendungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungBegruendungRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungBegruendung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungBegruendung findByName(String name) {
        return seminarPruefungBegruendungRepository.findByName(name);
    }
}
