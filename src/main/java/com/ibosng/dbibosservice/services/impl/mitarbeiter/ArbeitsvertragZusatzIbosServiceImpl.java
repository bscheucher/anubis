package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragZusatzIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzIbosService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsvertragZusatzIbosServiceImpl implements ArbeitsvertragZusatzIbosService {

    private final ArbeitsvertragZusatzIbosRepository arbeitsvertragZusatzIbosRepository;

    public ArbeitsvertragZusatzIbosServiceImpl(ArbeitsvertragZusatzIbosRepository arbeitsvertragZusatzIbosRepository) {
        this.arbeitsvertragZusatzIbosRepository = arbeitsvertragZusatzIbosRepository;
    }

    @Override
    public List<ArbeitsvertragZusatzIbos> findAll() {
        return arbeitsvertragZusatzIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragZusatzIbos> findById(Integer id) {
        return arbeitsvertragZusatzIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragZusatzIbos save(ArbeitsvertragZusatzIbos object) {
        return arbeitsvertragZusatzIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragZusatzIbos> saveAll(List<ArbeitsvertragZusatzIbos> objects) {
        return arbeitsvertragZusatzIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragZusatzIbosRepository.deleteById(id);
    }

    @Override
    public List<ArbeitsvertragZusatzIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId) {
        return arbeitsvertragZusatzIbosRepository.findAllByArbeitsvertragId(arbeitsvertragId);
    }

    @Override
    public List<ArbeitsvertragZusatzIbos> findAllByPersnr(String personalnummer) {
        return arbeitsvertragZusatzIbosRepository.findAllByPersnr(personalnummer);
    }

    @Override
    public List<ArbeitsvertragZusatzIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after) {
        return arbeitsvertragZusatzIbosRepository.findAllByEruserAndErdaAfterOrEruserAndAedaAfter(createdBy, after, createdBy, after);
    }
}
