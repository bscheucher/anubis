package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungErgebnisType;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungErgebnisTypeRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungErgebnisTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungErgebnisTypeServiceImpl implements SeminarPruefungErgebnisTypeService {

    private final SeminarPruefungErgebnisTypeRepository seminarPruefungErgebnisTypeRepository;

    public SeminarPruefungErgebnisTypeServiceImpl(SeminarPruefungErgebnisTypeRepository seminarPruefungErgebnisTypeRepository) {
        this.seminarPruefungErgebnisTypeRepository = seminarPruefungErgebnisTypeRepository;
    }

    @Override
    public List<SeminarPruefungErgebnisType> findAll() {
        return seminarPruefungErgebnisTypeRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungErgebnisType> findById(Integer id) {
        return seminarPruefungErgebnisTypeRepository.findById(id);
    }

    @Override
    public SeminarPruefungErgebnisType save(SeminarPruefungErgebnisType object) {
        return seminarPruefungErgebnisTypeRepository.save(object);
    }

    @Override
    public List<SeminarPruefungErgebnisType> saveAll(List<SeminarPruefungErgebnisType> objects) {
        return seminarPruefungErgebnisTypeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungErgebnisTypeRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungErgebnisType> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungErgebnisType findByName(String name) {
        return seminarPruefungErgebnisTypeRepository.findByName(name);
    }
}
