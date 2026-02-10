package com.ibosng.dbservice.services.history.impl;

import com.ibosng.dbservice.entities.history.TeilnehmerHistory;
import com.ibosng.dbservice.repositories.history.TeilnehmerHistoryRepository;
import com.ibosng.dbservice.services.history.TeilnehmerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerHistoryServiceImpl implements TeilnehmerHistoryService {

    private final TeilnehmerHistoryRepository teilnehmerHistoryRepository;

    @Autowired
    public TeilnehmerHistoryServiceImpl(TeilnehmerHistoryRepository teilnehmerHistoryRepository) {
        this.teilnehmerHistoryRepository = teilnehmerHistoryRepository;
    }

    @Override
    public List<TeilnehmerHistory> findAll() {
        return null;
    }

    @Override
    public Optional<TeilnehmerHistory> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public TeilnehmerHistory save(TeilnehmerHistory object) {
        return null;
    }

    @Override
    public List<TeilnehmerHistory> saveAll(List<TeilnehmerHistory> objects) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<TeilnehmerHistory> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<TeilnehmerHistory> getAllForActionAfter(Character action, LocalDateTime actionTimestamp) {
        return teilnehmerHistoryRepository.findAllByActionAndActionTimestampAfter(action,actionTimestamp);
    }
}
