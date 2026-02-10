package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.StaatenIbos;
import com.ibosng.dbibosservice.repositories.StaatenIbosRepository;
import com.ibosng.dbibosservice.services.StaatenIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaatenIbosServiceImpl implements StaatenIbosService {

    private final StaatenIbosRepository staatenIbosRepository;

    public StaatenIbosServiceImpl(StaatenIbosRepository staatenIbosRepository) {
        this.staatenIbosRepository = staatenIbosRepository;
    }

    @Override
    public List<StaatenIbos> findAll() {
        return staatenIbosRepository.findAll();
    }

    @Override
    public Optional<StaatenIbos> findById(Integer id) {
        return staatenIbosRepository.findById(id);
    }

    @Override
    public StaatenIbos save(StaatenIbos object) {
        return staatenIbosRepository.save(object);
    }

    @Override
    public List<StaatenIbos> saveAll(List<StaatenIbos> objects) {
        return staatenIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        staatenIbosRepository.deleteById(id);
    }


    @Override
    public List<StaatenIbos> findAllByAlpha2AndAlpha3(String alpha2, String alpha3) {
        return staatenIbosRepository.findAllByAlpha2AndAlpha3(alpha2, alpha3);
    }
}