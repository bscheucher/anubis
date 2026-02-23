package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.PzMonat;
import com.ibosng.dbibosservice.entities.PzMonatId;
import com.ibosng.dbibosservice.repositories.PzMonatRepository;
import com.ibosng.dbibosservice.services.PzMonatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PzMonatServiceImpl implements PzMonatService {
    private final PzMonatRepository pzMonatRepository;

    @Override
    public List<PzMonat> findAll() {
        return pzMonatRepository.findAll();
    }

    @Override
    public Optional<PzMonat> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public PzMonat save(PzMonat object) {
        return pzMonatRepository.save(object);
    }

    @Override
    public List<PzMonat> saveAll(List<PzMonat> objects) {
        return pzMonatRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public Optional<PzMonat> findById(PzMonatId id) {
        return pzMonatRepository.findById(id);
    }

    @Override
    public PzMonat findByAdAdnrJahrAndMonat(Integer adAdnr, Integer jahr, Integer monat) {
        return pzMonatRepository.findById_AdAdnrAndId_JahrAndId_Monat(adAdnr, jahr, monat);
    }

    @Override
    public String getMoxisStatus(Integer adAdnr, LocalDate month) {
        if (month == null) {
            return null;
        }
        String monat = month.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return pzMonatRepository.findStatusForMonat(adAdnr, monat);
    }
}
