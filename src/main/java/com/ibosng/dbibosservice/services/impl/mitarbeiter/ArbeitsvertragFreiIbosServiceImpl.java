package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragFreiIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragFreiIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragFreiIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsvertragFreiIbosServiceImpl implements ArbeitsvertragFreiIbosService {

    private final ArbeitsvertragFreiIbosRepository arbeitsvertragFreiIbosRepository;

    public ArbeitsvertragFreiIbosServiceImpl(ArbeitsvertragFreiIbosRepository arbeitsvertragFreiIbosRepository) {
        this.arbeitsvertragFreiIbosRepository = arbeitsvertragFreiIbosRepository;
    }

    @Override
    public List<ArbeitsvertragFreiIbos> findAll() {
        return arbeitsvertragFreiIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragFreiIbos> findById(Integer id) {
        return arbeitsvertragFreiIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragFreiIbos save(ArbeitsvertragFreiIbos object) {
        return arbeitsvertragFreiIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragFreiIbos> saveAll(List<ArbeitsvertragFreiIbos> objects) {
        return arbeitsvertragFreiIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragFreiIbosRepository.deleteById(id);
    }

    @Override
    public List<ArbeitsvertragFreiIbos> findAllByArbeitsvertragId(Integer arbeitstvertragzusatzId) {
        return arbeitsvertragFreiIbosRepository.findAllByArbeitsvertragId(arbeitstvertragzusatzId);
    }
}
