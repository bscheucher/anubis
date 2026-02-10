package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.AusschreibungIbos;
import com.ibosng.dbibosservice.repositories.AusschreibungIbosRepository;
import com.ibosng.dbibosservice.services.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AusschreibungIbosServiceImpl implements BaseService<AusschreibungIbos> {

    private final AusschreibungIbosRepository ausschreibungIbosRepository;

    public AusschreibungIbosServiceImpl(AusschreibungIbosRepository ausschreibungIbosRepository) {
        this.ausschreibungIbosRepository = ausschreibungIbosRepository;
    }

    @Override
    public List<AusschreibungIbos> findAll() {
        return ausschreibungIbosRepository.findAll();
    }

    @Override
    public Optional<AusschreibungIbos> findById(Integer id) {
        return ausschreibungIbosRepository.findById(id);
    }

    @Override
    public AusschreibungIbos save(AusschreibungIbos object) {
        return ausschreibungIbosRepository.save(object);
    }

    @Override
    public List<AusschreibungIbos> saveAll(List<AusschreibungIbos> objects) {
        return ausschreibungIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        ausschreibungIbosRepository.deleteById(id);
    }

}