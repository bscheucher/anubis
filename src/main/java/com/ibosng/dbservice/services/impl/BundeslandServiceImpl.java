package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Bundesland;
import com.ibosng.dbservice.repositories.BundeslandRepository;
import com.ibosng.dbservice.services.BundeslandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BundeslandServiceImpl implements BundeslandService {

    private final BundeslandRepository bundeslandRepository;

    public BundeslandServiceImpl(BundeslandRepository bundeslandRepository) {
        this.bundeslandRepository = bundeslandRepository;
    }

    @Override
    public List<Bundesland> findAll() {
        return bundeslandRepository.findAll();
    }

    @Override
    public Optional<Bundesland> findById(Integer id) {
        return bundeslandRepository.findById(id);
    }

    @Override
    public Bundesland save(Bundesland object) {
        return bundeslandRepository.save(object);
    }

    @Override
    public List<Bundesland> saveAll(List<Bundesland> objects) {
        return bundeslandRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        bundeslandRepository.deleteById(id);
    }

    @Override
    public List<Bundesland> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Bundesland findByPlzId(Integer plzId) {
        return bundeslandRepository.findByPlzId(plzId);
    }
    
}
