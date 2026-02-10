package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.GrAdIbos;
import com.ibosng.dbibosservice.repositories.GrAdIbosRepository;
import com.ibosng.dbibosservice.services.GrAdIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrAdIbosServiceImpl implements GrAdIbosService {

    private final GrAdIbosRepository grAdIbosRepository;

    public GrAdIbosServiceImpl(GrAdIbosRepository GrAdIbosRepository) {
        this.grAdIbosRepository = GrAdIbosRepository;
    }

    @Override
    public List<GrAdIbos> findAll() {
        return grAdIbosRepository.findAll();
    }

    @Override
    public Optional<GrAdIbos> findById(Integer id) {
        return grAdIbosRepository.findById(id);
    }

    @Override
    public GrAdIbos save(GrAdIbos object) {
        return grAdIbosRepository.save(object);
    }

    @Override
    public List<GrAdIbos> saveAll(List<GrAdIbos> objects) {
        return grAdIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        grAdIbosRepository.deleteById(id);
    }

    @Override
    public GrAdIbos findAllByAdresseAdadnr(Integer adresseAdadnr) {
        return grAdIbosRepository.findAllByAdresseAdadnr(adresseAdadnr);
    }
}