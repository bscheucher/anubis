package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Anrede;
import com.ibosng.dbservice.repositories.masterdata.AnredeRepository;
import com.ibosng.dbservice.services.masterdata.AnredeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnredeServiceImpl implements AnredeService {

    private final AnredeRepository anredeRepository;

    public AnredeServiceImpl(AnredeRepository anredeRepository) {
        this.anredeRepository = anredeRepository;
    }

    @Override
    public List<Anrede> findAll() {
        return anredeRepository.findAll();
    }

    @Override
    public Optional<Anrede> findById(Integer id) {
        return anredeRepository.findById(id);
    }

    @Override
    public Anrede save(Anrede object) {
        return anredeRepository.save(object);
    }

    @Override
    public List<Anrede> saveAll(List<Anrede> objects) {
        return anredeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        anredeRepository.deleteById(id);
    }

    @Override
    public List<Anrede> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Anrede findByName(String name) {
        return anredeRepository.findByName(name);
    }
}
