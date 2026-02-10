package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.FKostenstelleIbos;
import com.ibosng.dbibosservice.repositories.FKostenstelleIbosRepository;
import com.ibosng.dbibosservice.services.FKostenstelleIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FKostenstelleIbosServiceImpl implements FKostenstelleIbosService {

    private final FKostenstelleIbosRepository fKostenstelleIbosRepository;

    public FKostenstelleIbosServiceImpl(FKostenstelleIbosRepository fKostenstelleIbosRepository) {
        this.fKostenstelleIbosRepository = fKostenstelleIbosRepository;
    }

    @Override
    public List<FKostenstelleIbos> findAll() {
        return fKostenstelleIbosRepository.findAll();
    }

    @Override
    public Optional<FKostenstelleIbos> findById(Integer id) {
        return fKostenstelleIbosRepository.findById(id);
    }

    @Override
    public FKostenstelleIbos save(FKostenstelleIbos object) {
        return fKostenstelleIbosRepository.save(object);
    }

    @Override
    public List<FKostenstelleIbos> saveAll(List<FKostenstelleIbos> objects) {
        return fKostenstelleIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        fKostenstelleIbosRepository.deleteById(id);
    }

    @Override
    public List<FKostenstelleIbos> findAllByIdKstKstGr(Integer kstgr) {
        return fKostenstelleIbosRepository.findAllByIdKstKstGr(kstgr);
    }

    @Override
    public List<FKostenstelleIbos> findAllByIdKstKstNr(Integer kstnr) {
        return fKostenstelleIbosRepository.findAllByIdKstKstNr(kstnr);
    }
}