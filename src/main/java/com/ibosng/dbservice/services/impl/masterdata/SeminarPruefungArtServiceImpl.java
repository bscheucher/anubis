package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungArt;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungArtRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungArtService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungArtServiceImpl implements SeminarPruefungArtService {

    private final SeminarPruefungArtRepository seminarPruefungArtRepository;

    public SeminarPruefungArtServiceImpl(SeminarPruefungArtRepository seminarPruefungArtRepository) {
        this.seminarPruefungArtRepository = seminarPruefungArtRepository;
    }

    @Override
    public List<SeminarPruefungArt> findAll() {
        return seminarPruefungArtRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungArt> findById(Integer id) {
        return seminarPruefungArtRepository.findById(id);
    }

    @Override
    public SeminarPruefungArt save(SeminarPruefungArt object) {
        return seminarPruefungArtRepository.save(object);
    }

    @Override
    public List<SeminarPruefungArt> saveAll(List<SeminarPruefungArt> objects) {
        return seminarPruefungArtRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungArtRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungArt> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungArt findByName(String name) {
        return seminarPruefungArtRepository.findByName(name);
    }
}
