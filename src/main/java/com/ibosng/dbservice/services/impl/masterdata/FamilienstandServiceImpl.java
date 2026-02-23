package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Familienstand;
import com.ibosng.dbservice.repositories.masterdata.FamilienstandRepository;
import com.ibosng.dbservice.services.masterdata.FamilienstandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FamilienstandServiceImpl implements FamilienstandService {

    private final FamilienstandRepository familienstandRepository;

    public FamilienstandServiceImpl(FamilienstandRepository familienstandRepository) {
        this.familienstandRepository = familienstandRepository;
    }

    @Override
    public List<Familienstand> findAll() {
        return familienstandRepository.findAll();
    }

    @Override
    public Optional<Familienstand> findById(Integer id) {
        return familienstandRepository.findById(id);
    }

    @Override
    public Familienstand save(Familienstand object) {
        return familienstandRepository.save(object);
    }

    @Override
    public List<Familienstand> saveAll(List<Familienstand> objects) {
        return familienstandRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        familienstandRepository.deleteById(id);
    }

    @Override
    public List<Familienstand> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Familienstand findByName(String name) {
        return familienstandRepository.findByName(name);
    }
}
