package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragFixIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragFixIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragFixIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsvertragFixIbosServiceImpl implements ArbeitsvertragFixIbosService {

    private final ArbeitsvertragFixIbosRepository arbeitsvertragFixIbosRepository;

    public ArbeitsvertragFixIbosServiceImpl(ArbeitsvertragFixIbosRepository arbeitsvertragFixIbosRepository) {
        this.arbeitsvertragFixIbosRepository = arbeitsvertragFixIbosRepository;
    }

    @Override
    public List<ArbeitsvertragFixIbos> findAll() {
        return arbeitsvertragFixIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragFixIbos> findById(Integer id) {
        return arbeitsvertragFixIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragFixIbos save(ArbeitsvertragFixIbos object) {
        return arbeitsvertragFixIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragFixIbos> saveAll(List<ArbeitsvertragFixIbos> objects) {
        return arbeitsvertragFixIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragFixIbosRepository.deleteById(id);
    }

    @Override
    public List<ArbeitsvertragFixIbos> findAllByArbeitsvertragId(Integer arbeitstvertragzusatzId) {
        return arbeitsvertragFixIbosRepository.findAllByArbeitsvertragId(arbeitstvertragzusatzId);
    }

    @Override
    public List<String> findFuehrungskraftByPersnr(String persnr) {
        return arbeitsvertragFixIbosRepository.findFuehrungskraftByPersnr(persnr);
    }

    @Override
    public List<String> findFuehrungskraftUPNsByPersnr(String persnr) {
        return arbeitsvertragFixIbosRepository.findFuehrungskraftUPNsByPersnr(persnr);
    }
}
