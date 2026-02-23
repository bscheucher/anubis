package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.KinderIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.KinderIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.KinderIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KinderIbosServiceImpl implements KinderIbosService {

    private final KinderIbosRepository kinderIbosRepository;

    public KinderIbosServiceImpl(KinderIbosRepository kinderIbosRepository) {
        this.kinderIbosRepository = kinderIbosRepository;
    }

    @Override
    public List<KinderIbos> findAll() {
        return kinderIbosRepository.findAll();
    }

    @Override
    public Optional<KinderIbos> findById(Integer id) {
        return kinderIbosRepository.findById(id);
    }

    @Override
    public KinderIbos save(KinderIbos object) {
        return kinderIbosRepository.save(object);
    }

    @Override
    public List<KinderIbos> saveAll(List<KinderIbos> objects) {
        return kinderIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kinderIbosRepository.deleteById(id);
    }

    @Override
    public List<KinderIbos> findAllByAdresseAdnr(Integer adresseAdnr) {
        return kinderIbosRepository.findAllByAdresseAdnr(adresseAdnr);
    }
}
