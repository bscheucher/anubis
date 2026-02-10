package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.repositories.TelefonRepository;
import com.ibosng.dbservice.services.TelefonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelefonServiceImpl implements TelefonService {

    private final TelefonRepository telefonRepository;

    @Autowired
    public TelefonServiceImpl(TelefonRepository telefonRepository) {
        this.telefonRepository = telefonRepository;
    }

    @Override
    public Optional<Telefon> findById(Integer id) {
        return telefonRepository.findById(id);
    }

    @Override
    public List<Telefon> findAll() {
        return telefonRepository.findAll();
    }

    @Override
    public Telefon save(Telefon telefon) {
        return telefonRepository.save(telefon);
    }

    @Override
    public List<Telefon> saveAll(List<Telefon> telefons) {
        return telefonRepository.saveAll(telefons);
    }

    @Override
    public void deleteById(Integer id) {
        this.telefonRepository.deleteById(id);
    }

    @Override
    public List<Telefon> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Optional<Telefon> findByTelefonnummer(Long telefonnummer) {
        return telefonRepository.findByTelefonnummer(telefonnummer);
    }
}
