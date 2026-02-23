package com.ibosng.dbservice.services.impl;


import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotiz;
import com.ibosng.dbservice.repositories.TeilnehmerNotizRepository;
import com.ibosng.dbservice.services.TeilnehmerNotizService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerNotizServiceImpl implements TeilnehmerNotizService {

    private final TeilnehmerNotizRepository teilnehmerNotizRepository;

    public TeilnehmerNotizServiceImpl(TeilnehmerNotizRepository teilnehmerNotizRepository) {
        this.teilnehmerNotizRepository = teilnehmerNotizRepository;
    }

    @Override
    public List<TeilnehmerNotiz> findAll() {
        return teilnehmerNotizRepository.findAll();
    }

    @Override
    public Optional<TeilnehmerNotiz> findById(Integer id) {
        return teilnehmerNotizRepository.findById(id);
    }

    @Override
    public TeilnehmerNotiz save(TeilnehmerNotiz object) {
        return teilnehmerNotizRepository.save(object);
    }

    @Override
    public List<TeilnehmerNotiz> saveAll(List<TeilnehmerNotiz> objects) {
        return teilnehmerNotizRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerNotizRepository.deleteById(id);
    }

    @Override
    public List<TeilnehmerNotiz> findAllByIdentifier(String identifier) {
        return null;
    }

    public List<TeilnehmerNotiz> findAllByTeilnehmerId(Integer teilnehmerId) {
        return teilnehmerNotizRepository.findAllByTeilnehmerId(teilnehmerId);
    }

    public TeilnehmerNotizDto mapTeilnehmerNotizToDto(TeilnehmerNotiz teilnehmerNotiz) {
        TeilnehmerNotizDto teilnehmerNotizDto = new TeilnehmerNotizDto();
        if (teilnehmerNotiz != null) {
            teilnehmerNotizDto.setId(teilnehmerNotiz.getId());
            teilnehmerNotizDto.setNotiz(teilnehmerNotiz.getNotiz());
            teilnehmerNotizDto.setKategorie(teilnehmerNotiz.getKategorie() != null ? teilnehmerNotiz.getKategorie().getName() : null);
            teilnehmerNotizDto.setType(teilnehmerNotiz.getType() != null ? teilnehmerNotiz.getType().getName() : null);
            teilnehmerNotizDto.setCreatedOn(teilnehmerNotiz.getCreatedOn() != null ? teilnehmerNotiz.getCreatedOn().toLocalDate().toString() : null);
            teilnehmerNotizDto.setCreatedBy(teilnehmerNotiz.getCreatedBy());

        }
        return teilnehmerNotizDto;
    }
}
