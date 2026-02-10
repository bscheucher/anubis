package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.KeytableIbos;
import com.ibosng.dbibosservice.repositories.KeytableIbosRepository;
import com.ibosng.dbibosservice.services.KeytableIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeytableIbosServiceImpl implements KeytableIbosService {

    private final KeytableIbosRepository keytableIbosRepository;

    public KeytableIbosServiceImpl(KeytableIbosRepository keytableIbosRepository) {
        this.keytableIbosRepository = keytableIbosRepository;
    }

    @Override
    public List<KeytableIbos> findAll() {
        return keytableIbosRepository.findAll();
    }

    @Override
    public Optional<KeytableIbos> findById(Integer id) {
        return keytableIbosRepository.findById(id);
    }

    @Override
    public KeytableIbos save(KeytableIbos object) {
        return keytableIbosRepository.save(object);
    }

    @Override
    public List<KeytableIbos> saveAll(List<KeytableIbos> objects) {
        return keytableIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        keytableIbosRepository.deleteById(id);
    }

    @Override
    public List<KeytableIbos> findAllByKyNameAndKyValueT1(String kyName, String kyValuet1) {
        return keytableIbosRepository.findAllByKyNameAndKyValueT1(kyName, kyValuet1);
    }

    @Override
    public List<KeytableIbos> findAllByKyNameAndKyNrOrderByKyIndex(String kyName, int kyNr) {
        return keytableIbosRepository.findAllByKyNameAndKyNrOrderByKyIndexAsc(kyName, kyNr);
    }

    @Override
    public KeytableIbos findFirstByKyNameAndKyNrOrderByKyIndex(String kyName, int kyNr) {
        return keytableIbosRepository.findAllByKyNameAndKyNrOrderByKyIndexAsc(kyName, kyNr).stream().findFirst().orElse(null);
    }

    @Override
    public Optional<KeytableIbos> findByKyNr(Integer kyNr) {
        return keytableIbosRepository.findByKyNr(kyNr);
    }
}