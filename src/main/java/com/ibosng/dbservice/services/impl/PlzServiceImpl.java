package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.repositories.PlzRepository;
import com.ibosng.dbservice.services.PlzService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlzServiceImpl implements PlzService {

    private final PlzRepository plzRepository;

    public PlzServiceImpl(PlzRepository plzRepository) {
        this.plzRepository = plzRepository;
    }

    @Override
    public List<Plz> findAll() {
        return plzRepository.findAll();
    }

    @Override
    public Optional<Plz> findById(Integer id) {
        return plzRepository.findById(id);
    }

    @Override
    public Plz save(Plz object) {
        return plzRepository.save(object);
    }

    @Override
    public List<Plz> saveAll(List<Plz> objects) {
        return plzRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        plzRepository.deleteById(id);
    }

    @Override
    public List<Plz> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Integer> getAllPlz() {
        return plzRepository.getAllPlz();
    }

    @Override
    public List<String> getAllOrt() {
        return plzRepository.getAllOrt();
    }

    @Override
    public List<Plz> findByPlz(Integer Plz) {
        return plzRepository.findByPlz(Plz);
    }

    @Override
    public List<String> findOrtByPlz(Integer plz) {
        return plzRepository.findOrtByPlz(plz);
    }

    @Override
    public Integer findPlzByOrt(String ort) {
        return plzRepository.findPlzByOrt(ort);
    }
}
