package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.KollektivvertragIbos;
import com.ibosng.dbibosservice.repositories.KollektivvertragIbosRepository;
import com.ibosng.dbibosservice.services.KollektivvertragIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KollektivvertragIbosServiceImpl implements KollektivvertragIbosService {

    private final KollektivvertragIbosRepository kollektivvertragIbosRepository;

    public KollektivvertragIbosServiceImpl(KollektivvertragIbosRepository kollektivvertragIbosRepository) {
        this.kollektivvertragIbosRepository = kollektivvertragIbosRepository;
    }

    @Override
    public List<KollektivvertragIbos> findAll() {
        return kollektivvertragIbosRepository.findAll();
    }

    @Override
    public Optional<KollektivvertragIbos> findById(Integer id) {
        return kollektivvertragIbosRepository.findById(id);
    }

    @Override
    public KollektivvertragIbos save(KollektivvertragIbos object) {
        return kollektivvertragIbosRepository.save(object);
    }

    @Override
    public List<KollektivvertragIbos> saveAll(List<KollektivvertragIbos> objects) {
        return kollektivvertragIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kollektivvertragIbosRepository.deleteById(id);
    }


    @Override
    public KollektivvertragIbos findByBezeichnung(String bezeichnung) {
        return kollektivvertragIbosRepository.findByBezeichnung(bezeichnung);
    }
}