package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Dienstort;
import com.ibosng.dbservice.repositories.DienstortRepository;
import com.ibosng.dbservice.services.DienstortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DienstortServiceImpl implements DienstortService {

    private final DienstortRepository dienstortRepository;

    @Autowired
    public DienstortServiceImpl(DienstortRepository dienstortRepository) {
        this.dienstortRepository = dienstortRepository;
    }

    @Override
    public Optional<Dienstort> findById(Integer id) {
        return dienstortRepository.findById(id);
    }

    @Override
    public List<Dienstort> findAll() {
        return dienstortRepository.findAll();
    }

    @Override
    public Dienstort save(Dienstort Dienstort) {
        return dienstortRepository.save(Dienstort);
    }

    @Override
    public List<Dienstort> saveAll(List<Dienstort> Dienstorts) {
        return dienstortRepository.saveAll(Dienstorts);
    }

    @Override
    public void deleteById(Integer id) {
        this.dienstortRepository.deleteById(id);
    }

    @Override
    public List<Dienstort> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Dienstort> findAllByName(String name) {
        return dienstortRepository.findAllByName(name);
    }
}
