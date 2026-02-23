package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragZusatzFixIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzFixIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsvertragZusatzFixIbosServiceImpl implements ArbeitsvertragZusatzFixIbosService {

    private final ArbeitsvertragZusatzFixIbosRepository arbeitsvertragZusatzFixIbosRepository;

    public ArbeitsvertragZusatzFixIbosServiceImpl(ArbeitsvertragZusatzFixIbosRepository arbeitsvertragZusatzFixIbosRepository) {
        this.arbeitsvertragZusatzFixIbosRepository = arbeitsvertragZusatzFixIbosRepository;
    }

    @Override
    public List<ArbeitsvertragZusatzFixIbos> findAll() {
        return arbeitsvertragZusatzFixIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragZusatzFixIbos> findById(Integer id) {
        return arbeitsvertragZusatzFixIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragZusatzFixIbos save(ArbeitsvertragZusatzFixIbos object) {
        return arbeitsvertragZusatzFixIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragZusatzFixIbos> saveAll(List<ArbeitsvertragZusatzFixIbos> objects) {
        return arbeitsvertragZusatzFixIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragZusatzFixIbosRepository.deleteById(id);
    }

    @Override
    public List<ArbeitsvertragZusatzFixIbos> findAllByArbeitsvertragZusatzId(Integer arbeitstvertragzusatzId) {
        return arbeitsvertragZusatzFixIbosRepository.findAllByArbeitsvertragZusatzId(arbeitstvertragzusatzId);
    }
}
