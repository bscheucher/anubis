package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Rgs;
import com.ibosng.dbservice.repositories.RgsRepository;
import com.ibosng.dbservice.services.RgsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RgsServiceImpl implements RgsService {

    private final RgsRepository rgsRepository;

    public RgsServiceImpl(RgsRepository rgsRepository) {
        this.rgsRepository = rgsRepository;
    }

    @Override
    public List<Rgs> findAll() {
        return rgsRepository.findAll();
    }

    @Override
    public Optional<Rgs> findById(Integer id) {
        return rgsRepository.findById(id);
    }

    @Override
    public Rgs save(Rgs object) {
        return rgsRepository.save(object);
    }

    @Override
    public List<Rgs> saveAll(List<Rgs> objects) {
        return rgsRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        rgsRepository.deleteById(id);
    }

    @Override
    public List<Rgs> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Integer> getAllRgs() {
        return rgsRepository.getAllRgs();
    }

    @Override
    public Rgs findByRgs(Integer rgs) {
        return rgsRepository.findByRgs(rgs);
    }
}
