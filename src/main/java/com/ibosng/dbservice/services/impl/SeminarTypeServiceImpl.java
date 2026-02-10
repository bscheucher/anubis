package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.seminar.SeminarType;
import com.ibosng.dbservice.repositories.seminar.SeminarTypeRepository;
import com.ibosng.dbservice.services.SeminarTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarTypeServiceImpl implements SeminarTypeService {

    private final SeminarTypeRepository seminarTypeRepository;

    public SeminarTypeServiceImpl(SeminarTypeRepository seminarTypeRepository) {
        this.seminarTypeRepository = seminarTypeRepository;
    }

    @Override
    public Optional<SeminarType> findById(Integer id) {
        return seminarTypeRepository.findById(id);
    }

    @Override
    public List<SeminarType> findAll() {
        return seminarTypeRepository.findAll();
    }

    @Override
    public SeminarType save(SeminarType seminar) {
        return seminarTypeRepository.save(seminar);
    }

    @Override
    public void deleteById(Integer id) {
        seminarTypeRepository.deleteById(id);
    }

    @Override
    public List<SeminarType> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<SeminarType> saveAll(List<SeminarType> seminare) {
        return seminarTypeRepository.saveAll(seminare);
    }

    @Override
    public SeminarType findByName(String name) {
        return seminarTypeRepository.findByName(name);
    }

}
