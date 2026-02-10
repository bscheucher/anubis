package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungGegenstand;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungGegenstandRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungGegenstandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungGegenstandServiceImpl implements SeminarPruefungGegenstandService {

    private final SeminarPruefungGegenstandRepository seminarPruefungGegenstandRepository;

    public SeminarPruefungGegenstandServiceImpl(SeminarPruefungGegenstandRepository seminarPruefungGegenstandRepository) {
        this.seminarPruefungGegenstandRepository = seminarPruefungGegenstandRepository;
    }

    @Override
    public List<SeminarPruefungGegenstand> findAll() {
        return seminarPruefungGegenstandRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungGegenstand> findById(Integer id) {
        return seminarPruefungGegenstandRepository.findById(id);
    }

    @Override
    public SeminarPruefungGegenstand save(SeminarPruefungGegenstand object) {
        return seminarPruefungGegenstandRepository.save(object);
    }

    @Override
    public List<SeminarPruefungGegenstand> saveAll(List<SeminarPruefungGegenstand> objects) {
        return seminarPruefungGegenstandRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungGegenstandRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungGegenstand> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungGegenstand findByName(String name) {
        return seminarPruefungGegenstandRepository.findByName(name);
    }
}
