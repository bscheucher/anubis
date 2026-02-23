package com.ibosng.dbservice.services.impl;


import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.entities.teilnehmer.TnAusbildung;
import com.ibosng.dbservice.repositories.TeilnehmerAusbildungRepository;
import com.ibosng.dbservice.services.TeilnehmerAusbildungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerAusbildungServiceImpl implements TeilnehmerAusbildungService {

    private final TeilnehmerAusbildungRepository teilnehmerAusbildungRepository;

    public TeilnehmerAusbildungServiceImpl(TeilnehmerAusbildungRepository teilnehmerAusbildungRepository) {
        this.teilnehmerAusbildungRepository = teilnehmerAusbildungRepository;
    }

    @Override
    public List<TnAusbildung> findAll() {
        return teilnehmerAusbildungRepository.findAll();
    }

    @Override
    public Optional<TnAusbildung> findById(Integer id) {
        return teilnehmerAusbildungRepository.findById(id);
    }

    @Override
    public TnAusbildung save(TnAusbildung object) {
        return teilnehmerAusbildungRepository.save(object);
    }

    @Override
    public List<TnAusbildung> saveAll(List<TnAusbildung> objects) {
        return teilnehmerAusbildungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerAusbildungRepository.deleteById(id);
    }

    @Override
    public List<TnAusbildung> findAllByIdentifier(String identifier) {
        return null;
    }

    public List<TnAusbildung> findAllByTeilnehmerId(Integer teilnehmerId) {
        return teilnehmerAusbildungRepository.findAllByTeilnehmerId(teilnehmerId);
    }

    public TnAusbildungDto mapTeilnehmerAusbildungToDto(TnAusbildung tnAusbildung) {
        TnAusbildungDto tnAusbildungDto = new TnAusbildungDto();
        if (tnAusbildung != null) {
            tnAusbildungDto.setId(tnAusbildung.getId());
            tnAusbildungDto.setAusbildungstyp(tnAusbildung.getAusbildungType() != null ? tnAusbildung.getAusbildungType().getName() : null);
            tnAusbildungDto.setHoechsterAbschluss(tnAusbildung.getHoechsterAbschluss());
            tnAusbildungDto.setErkanntInAt(tnAusbildung.getErkanntInAt());
        }
        return tnAusbildungDto;
    }
}
