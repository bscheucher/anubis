package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbibosservice.entities.pzleistung.PzleistungId;
import com.ibosng.dbibosservice.repositories.PzleistungRepository;
import com.ibosng.dbibosservice.services.PzleistungService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PzleistungServiceImpl implements PzleistungService {

    private final static String LEISTUNG = "l";

    private final PzleistungRepository pzleistungRepository;


    @Override
    public List<Pzleistung> findAll() {
        return pzleistungRepository.findAll();
    }

    @Override
    public Optional<Pzleistung> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Pzleistung save(Pzleistung object) {
        return pzleistungRepository.save(object);
    }

    @Override
    public List<Pzleistung> saveAll(List<Pzleistung> objects) {
        return pzleistungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public Optional<Pzleistung> findById(PzleistungId pzleistungId) {
        return pzleistungRepository.findById(pzleistungId);
    }

    @Override
    public Page<Pzleistung> findByADadnrInPeriod(Integer adadnr, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return pzleistungRepository.findById_ADadnrAndLzdatumBetweenAndLztyp(adadnr, startDate, endDate, LEISTUNG, pageable);
    }

    @Override
    public List<Pzleistung> findByADadnrInPeriod(Integer adadnr, LocalDate startDate, LocalDate endDate) {
        return pzleistungRepository.findById_ADadnrAndLzdatumBetweenAndLztyp(adadnr, startDate, endDate, LEISTUNG);
    }

    @Override
    public Page<Pzleistung> findByADadnrInPeriod(List<Integer> adadnrList, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return pzleistungRepository.findById_ADadnrInAndLzdatumBetweenAndLztyp(adadnrList, startDate, endDate, LEISTUNG, pageable);
    }

    @Override
    public List<Pzleistung> findByADadnrInChangePeriod(Integer adadnr, LocalDateTime startDate, LocalDateTime endDate) {
        return pzleistungRepository.findById_ADadnrAndLzaedaBetweenOrLzerdaBetween(adadnr, startDate, endDate);
    }
}
