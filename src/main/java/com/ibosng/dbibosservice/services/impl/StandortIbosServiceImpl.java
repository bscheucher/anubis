package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.StandortIbos;
import com.ibosng.dbibosservice.repositories.StandortIbosRepository;
import com.ibosng.dbibosservice.services.StandortIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StandortIbosServiceImpl implements StandortIbosService {

    private final StandortIbosRepository standortIbosRepository;

    public StandortIbosServiceImpl(StandortIbosRepository standortIbosRepository) {
        this.standortIbosRepository = standortIbosRepository;
    }

    @Override
    public List<StandortIbos> findAll() {
        return standortIbosRepository.findAll();
    }

    @Override
    public Optional<StandortIbos> findById(Integer id) {
        return standortIbosRepository.findById(id);
    }

    @Override
    public StandortIbos save(StandortIbos object) {
        return standortIbosRepository.save(object);
    }

    @Override
    public List<StandortIbos> saveAll(List<StandortIbos> objects) {
        return standortIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        standortIbosRepository.deleteById(id);

    }

    @Override
    public Integer getLandIdFromLandCode(String landcode) {
        return standortIbosRepository.getLandIdFromLandCode(landcode);
    }

    @Override
    public Integer getRGS(String rgs) {
        return standortIbosRepository.getRGS(rgs);
    }

    @Override
    public Integer getAnredeKeyValueFromAbbreviation(String geschlecht) {
        return standortIbosRepository.getAnredeKeyValueFromAbbreviation(geschlecht);
    }

    @Override
    public Integer getAnredeKeyValueFromAbbreviationAndName(String geschlecht, String name) {
        return standortIbosRepository.getAnredeKeyValueFromAbbreviationAndName(geschlecht, name);
    }

    @Override
    public String getAnredeStringValue(Integer anredeId) {
        return standortIbosRepository.getAnredeStringValue(anredeId);
    }

    @Override
    public String getLandIdFromId(Integer id) {
        return standortIbosRepository.getLandIdFromId(id);
    }

    @Override
    public Integer getRGSNummer(Integer id) {
        return standortIbosRepository.getRGSNummer(id);
    }

    @Override
    public Integer getFamilienstandKey(String familienstand) {
        return standortIbosRepository.getFamilienstandKey(familienstand);
    }
}
