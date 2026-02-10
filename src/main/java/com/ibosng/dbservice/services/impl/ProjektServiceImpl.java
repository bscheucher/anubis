package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.repositories.ProjektRepository;
import com.ibosng.dbservice.services.ProjektService;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProjektServiceImpl implements ProjektService {

    private final ProjektRepository projektRepository;

    private Collator germanCollator;

    public ProjektServiceImpl(ProjektRepository projektRepository) {
        this.projektRepository = projektRepository;
        this.germanCollator = Collator.getInstance(Locale.GERMAN);
    }

    @Override
    public Optional<Projekt> findById(Integer id) {
        return projektRepository.findById(id);
    }

    @Override
    public List<Projekt> findAll() {
        return projektRepository.findAll();
    }

    @Override
    public Projekt save(Projekt projekt) {
        return projektRepository.save(projekt);
    }

    @Override
    public void deleteById(Integer id) {
        projektRepository.deleteById(id);
    }

    @Override
    public List<Projekt> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Projekt> saveAll(List<Projekt> projekte) {
        return projektRepository.saveAll(projekte);
    }

    // Add custom methods specific to Projekt entity
    @Override
    public Projekt findByProjektNummer(Integer projektNummer) {
        return projektRepository.findByProjektNummer(projektNummer);
    }

    @Override
    public Projekt findByAuftragNummer(Integer auftragNummer) {
        return projektRepository.findByAuftragNummer(auftragNummer);
    }

    @Override
    public List<String> findByStatus(Boolean isActive, Boolean isKorrigieren, Integer benutzerId) {
        return projektRepository.findAllByIsActiveAndBenutzerId(isActive, isKorrigieren, benutzerId);
    }

    @Override
    public List<Projekt> findProjektsWithoutManagers() {
        return projektRepository.findProjektsWithoutManagers();
    }

    @Override
    public List<Projekt> findAllActive(LocalDate currentDate) {
        return projektRepository.findAllActive(currentDate);
    }

    @Override
    public List<Projekt> findAllByProjektNummer(Integer projektNummer) {
        return  projektRepository.findAllByProjektNummer(projektNummer);
    }
}
