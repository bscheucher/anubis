package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.smad.SmAd;
import com.ibosng.dbibosservice.entities.smad.SmAdId;
import com.ibosng.dbibosservice.repositories.SmAdRepository;
import com.ibosng.dbibosservice.services.SmAdService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmAdServiceImpl implements SmAdService {

    private final SmAdRepository smAdRepository;

    public SmAdServiceImpl(SmAdRepository smAdRepository) {
        this.smAdRepository = smAdRepository;
    }

    @Override
    public List<SmAd> findAll() {
        return smAdRepository.findAll();
    }

    @Override
    public Optional<SmAd> findById(Integer id) {
        return Optional.empty();
    }

    public Optional<SmAd> findById(SmAdId id) {
        return smAdRepository.findById(id);
    }

    @Override
    public SmAd save(SmAd object) {
        return smAdRepository.save(object);
    }

    @Override
    public List<SmAd> saveAll(List<SmAd> objects) {
        return smAdRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
    }

    public void deleteById(SmAdId id) {
        smAdRepository.deleteById(id);
    }

    @Override
    public List<SmAd> findAllBySeminarNummer(Integer seminarNummer) {
        return smAdRepository.findAllBySmAdId_SeminarSmnr(seminarNummer);
    }
}
