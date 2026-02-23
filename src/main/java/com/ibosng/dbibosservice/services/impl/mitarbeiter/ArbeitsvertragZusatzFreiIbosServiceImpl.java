package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFreiIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragZusatzFreiIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzFreiIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsvertragZusatzFreiIbosServiceImpl implements ArbeitsvertragZusatzFreiIbosService {

    private final ArbeitsvertragZusatzFreiIbosRepository arbeitsvertragZusatzFreiIbosRepository;

    public ArbeitsvertragZusatzFreiIbosServiceImpl(ArbeitsvertragZusatzFreiIbosRepository arbeitsvertragZusatzFreiIbosRepository) {
        this.arbeitsvertragZusatzFreiIbosRepository = arbeitsvertragZusatzFreiIbosRepository;
    }

    @Override
    public List<ArbeitsvertragZusatzFreiIbos> findAll() {
        return arbeitsvertragZusatzFreiIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragZusatzFreiIbos> findById(Integer id) {
        return arbeitsvertragZusatzFreiIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragZusatzFreiIbos save(ArbeitsvertragZusatzFreiIbos object) {
        return arbeitsvertragZusatzFreiIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragZusatzFreiIbos> saveAll(List<ArbeitsvertragZusatzFreiIbos> objects) {
        return arbeitsvertragZusatzFreiIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragZusatzFreiIbosRepository.deleteById(id);
    }

    @Override
    public List<ArbeitsvertragZusatzFreiIbos> findAllByArbeitsvertragZusatzId(Integer arbeitstvertragzusatzId) {
        return arbeitsvertragZusatzFreiIbosRepository.findAllByArbeitsvertragZusatzId(arbeitstvertragzusatzId);
    }
}
