package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.MutterspracheIbos;
import com.ibosng.dbibosservice.repositories.MutterspracheIbosRepository;
import com.ibosng.dbibosservice.services.MutterspracheIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MutterspracheIbosServiceImpl implements MutterspracheIbosService {

    private final MutterspracheIbosRepository mutterspracheIbosRepository;

    public MutterspracheIbosServiceImpl(MutterspracheIbosRepository mutterspracheIbosRepository) {
        this.mutterspracheIbosRepository = mutterspracheIbosRepository;
    }

    @Override
    public List<MutterspracheIbos> findAll() {
        return mutterspracheIbosRepository.findAll();
    }

    @Override
    public Optional<MutterspracheIbos> findById(Integer id) {
        return mutterspracheIbosRepository.findById(id);
    }

    @Override
    public MutterspracheIbos save(MutterspracheIbos object) {
        return mutterspracheIbosRepository.save(object);
    }

    @Override
    public List<MutterspracheIbos> saveAll(List<MutterspracheIbos> objects) {
        return mutterspracheIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        mutterspracheIbosRepository.deleteById(id);
    }


    @Override
    public MutterspracheIbos findByName(String name) {
        return mutterspracheIbosRepository.findByName(name);
    }
}