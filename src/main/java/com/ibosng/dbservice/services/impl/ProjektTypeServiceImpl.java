package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.ProjektType;
import com.ibosng.dbservice.repositories.ProjektTypeRepository;
import com.ibosng.dbservice.services.ProjektTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjektTypeServiceImpl implements ProjektTypeService {

    private final ProjektTypeRepository projektTypeRepository;


    public ProjektTypeServiceImpl(ProjektTypeRepository projektTypeRepository) {
        this.projektTypeRepository = projektTypeRepository;
    }

    @Override
    public Optional<ProjektType> findById(Integer id) {
        return projektTypeRepository.findById(id);
    }

    @Override
    public List<ProjektType> findAll() {
        return projektTypeRepository.findAll();
    }

    @Override
    public ProjektType save(ProjektType projekt) {
        return projektTypeRepository.save(projekt);
    }

    @Override
    public void deleteById(Integer id) {
        projektTypeRepository.deleteById(id);
    }

    @Override
    public List<ProjektType> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<ProjektType> saveAll(List<ProjektType> projekte) {
        return projektTypeRepository.saveAll(projekte);
    }

    @Override
    public ProjektType findByName(String name) {
        return projektTypeRepository.findByName(name);
    }

}
