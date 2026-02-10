package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.BasePlz;
import com.ibosng.dbservice.repositories.AdresseRepository;
import com.ibosng.dbservice.services.AdresseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdresseServiceImpl implements AdresseService {

    private final AdresseRepository adresseRepository;

    public AdresseServiceImpl(AdresseRepository adresseRepository) {
        this.adresseRepository = adresseRepository;
    }

    @Override
    public List<Adresse> findAll() {
        return adresseRepository.findAll();
    }

    @Override
    public Optional<Adresse> findById(Integer id) {
        return adresseRepository.findById(id);
    }

    @Override
    public Adresse save(Adresse object) {
        return adresseRepository.save(object);
    }

    @Override
    public List<Adresse> saveAll(List<Adresse> objects) {
        return adresseRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        adresseRepository.deleteById(id);
    }

    @Override
    public List<Adresse> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Optional<Adresse> findByPlzAndOrtAndStrasse(BasePlz plz, String ort, String strasse) {
        return adresseRepository.findByPlzAndOrtAndStrasse(plz, ort, strasse);
    }
}
