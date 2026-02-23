package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.ProjektTypenIbos;
import com.ibosng.dbibosservice.repositories.ProjektTypenIbosRepository;
import com.ibosng.dbibosservice.services.ProjektTypenIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjektTypenIbosServiceImpl implements ProjektTypenIbosService {

    private final ProjektTypenIbosRepository projektTypenIbosRepository;

    public ProjektTypenIbosServiceImpl(ProjektTypenIbosRepository projektTypenIbosRepository) {
        this.projektTypenIbosRepository = projektTypenIbosRepository;
    }

    @Override
    public List<ProjektTypenIbos> findAll() {
        return projektTypenIbosRepository.findAll();
    }

    @Override
    public Optional<ProjektTypenIbos> findById(Integer id) {
        return projektTypenIbosRepository.findById(id);
    }

    @Override
    public ProjektTypenIbos save(ProjektTypenIbos object) {
        return projektTypenIbosRepository.save(object);
    }

    @Override
    public List<ProjektTypenIbos> saveAll(List<ProjektTypenIbos> objects) {
        return projektTypenIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        projektTypenIbosRepository.deleteById(id);
    }

    @Override
    public ProjektTypenIbos findByBezeichnung(String bezeichnung) {
        return projektTypenIbosRepository.findByBezeichnung(bezeichnung);
    }
}