package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.KvStufeIbos;
import com.ibosng.dbibosservice.repositories.KvStufeIbosRepository;
import com.ibosng.dbibosservice.services.KvStufeIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KvStufeIbosServiceImpl implements KvStufeIbosService {

    private final KvStufeIbosRepository kvStufeIbosRepository;

    public KvStufeIbosServiceImpl(KvStufeIbosRepository kvStufeIbosRepository) {
        this.kvStufeIbosRepository = kvStufeIbosRepository;
    }

    @Override
    public List<KvStufeIbos> findAll() {
        return kvStufeIbosRepository.findAll();
    }

    @Override
    public Optional<KvStufeIbos> findById(Integer id) {
        return kvStufeIbosRepository.findById(id);
    }

    @Override
    public KvStufeIbos save(KvStufeIbos object) {
        return kvStufeIbosRepository.save(object);
    }

    @Override
    public List<KvStufeIbos> saveAll(List<KvStufeIbos> objects) {
        return kvStufeIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kvStufeIbosRepository.deleteById(id);
    }


    @Override
    public List<KvStufeIbos> findAllByBezeichnungAndKvVerwendungsgruppeId(String bezeichnung, Integer verwendungsgruppe) {
        return kvStufeIbosRepository.findAllByBezeichnungAndKvVerwendungsgruppeId(bezeichnung, verwendungsgruppe);
    }
}