package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.betreuer.Betreuer;
import com.ibosng.dbservice.repositories.BetreuerRepository;
import com.ibosng.dbservice.services.BetreuerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BetreuerServiceImpl implements BetreuerService {


    private final BetreuerRepository betreuerRepository;

    @Autowired
    public BetreuerServiceImpl(BetreuerRepository betreuerRepository) {
        this.betreuerRepository = betreuerRepository;
    }

    @Override
    public Optional<Betreuer> findById(Integer id) {
        return betreuerRepository.findById(id);
    }

    @Override
    public List<Betreuer> findAll() {
        return betreuerRepository.findAll();
    }

    @Override
    public Betreuer save(Betreuer betreuer) {
        return betreuerRepository.save(betreuer);
    }

    @Override
    public List<Betreuer> saveAll(List<Betreuer> betreuers) {
        return betreuerRepository.saveAll(betreuers);
    }

    @Override
    public void deleteById(Integer id) {
        this.betreuerRepository.deleteById(id);
    }

    @Override
    public List<Betreuer> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Betreuer findByVorname(String vorname) {
        return betreuerRepository.findByVorname(vorname);
    }

    @Override
    public Betreuer findByNachname(String nachname) {
        return betreuerRepository.findByNachname(nachname);
    }

    @Override
    public Betreuer findByVornameAndNachname(String vorname, String nachname) {
        return betreuerRepository.findByVornameAndNachname(vorname, nachname);
    }
}
