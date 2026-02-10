package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeReason;
import com.ibosng.dbservice.repositories.TeilnahmeReasonRepository;
import com.ibosng.dbservice.services.TeilnahmeReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeilnahmeReasonServiceImpl implements TeilnahmeReasonService {

    private final TeilnahmeReasonRepository teilnahmeReasonRepository;

    @Override
    public List<TeilnahmeReason> findAll() {
        return teilnahmeReasonRepository.findAll();
    }

    @Override
    public Optional<TeilnahmeReason> findById(Integer id) {
        return teilnahmeReasonRepository.findById(id);
    }

    @Override
    public TeilnahmeReason save(TeilnahmeReason object) {
        return teilnahmeReasonRepository.save(object);
    }

    @Override
    public List<TeilnahmeReason> saveAll(List<TeilnahmeReason> objects) {
        return teilnahmeReasonRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnahmeReasonRepository.deleteById(id);
    }

    @Override
    public List<TeilnahmeReason> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public TeilnahmeReason findByKuerzel(String kuerzel){
        return teilnahmeReasonRepository.findByKuerzel(kuerzel);
    }
}
