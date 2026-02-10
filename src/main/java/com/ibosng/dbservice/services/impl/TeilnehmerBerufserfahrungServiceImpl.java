package com.ibosng.dbservice.services.impl;


import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.entities.teilnehmer.TnBerufserfahrung;
import com.ibosng.dbservice.repositories.TeilnehmerBerufserfahrungRepository;
import com.ibosng.dbservice.services.TeilnehmerBerufserfahrungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerBerufserfahrungServiceImpl implements TeilnehmerBerufserfahrungService {

    private final TeilnehmerBerufserfahrungRepository teilnehmerBerufserfahrungRepository;

    public TeilnehmerBerufserfahrungServiceImpl(TeilnehmerBerufserfahrungRepository teilnehmerBerufserfahrungRepository) {
        this.teilnehmerBerufserfahrungRepository = teilnehmerBerufserfahrungRepository;
    }

    @Override
    public List<TnBerufserfahrung> findAll() {
        return teilnehmerBerufserfahrungRepository.findAll();
    }

    @Override
    public Optional<TnBerufserfahrung> findById(Integer id) {
        return teilnehmerBerufserfahrungRepository.findById(id);
    }

    @Override
    public TnBerufserfahrung save(TnBerufserfahrung object) {
        return teilnehmerBerufserfahrungRepository.save(object);
    }

    @Override
    public List<TnBerufserfahrung> saveAll(List<TnBerufserfahrung> objects) {
        return teilnehmerBerufserfahrungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerBerufserfahrungRepository.deleteById(id);
    }

    @Override
    public List<TnBerufserfahrung> findAllByIdentifier(String identifier) {
        return null;
    }

    public List<TnBerufserfahrung> findAllByTeilnehmerId(Integer teilnehmerId) {
        return teilnehmerBerufserfahrungRepository.findAllByTeilnehmerId(teilnehmerId);
    }

    public TnBerufserfahrungDto mapTeilnehmerBerufserfahrungToDto(TnBerufserfahrung tnBerufserfahrung) {
        TnBerufserfahrungDto tnBerufserfahrungDto = new TnBerufserfahrungDto();
        if (tnBerufserfahrung != null) {
            tnBerufserfahrungDto.setId(tnBerufserfahrung.getId());
            tnBerufserfahrungDto.setBeruf(tnBerufserfahrung.getBeruf() != null ? tnBerufserfahrung.getBeruf().getName() : null);
            tnBerufserfahrungDto.setDauer(tnBerufserfahrung.getDauer());
        }
        return tnBerufserfahrungDto;
    }

    public TnBerufserfahrung findByTeilnehmerIdAndBeruf(Integer teilnehmerId, Integer berufId) {
        return teilnehmerBerufserfahrungRepository.findByTeilnehmer_IdAndBeruf_Id(teilnehmerId, berufId);
    }
}
