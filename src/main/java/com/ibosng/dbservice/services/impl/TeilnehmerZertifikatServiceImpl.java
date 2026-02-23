package com.ibosng.dbservice.services.impl;


import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.entities.teilnehmer.TnZertifikat;
import com.ibosng.dbservice.repositories.TeilnehmerZertifikatRepository;
import com.ibosng.dbservice.services.TeilnehmerZertifikatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerZertifikatServiceImpl implements TeilnehmerZertifikatService {

    private final TeilnehmerZertifikatRepository teilnehmerZertifikatRepository;

    public TeilnehmerZertifikatServiceImpl(TeilnehmerZertifikatRepository teilnehmerZertifikatRepository) {
        this.teilnehmerZertifikatRepository = teilnehmerZertifikatRepository;
    }

    @Override
    public List<TnZertifikat> findAll() {
        return teilnehmerZertifikatRepository.findAll();
    }

    @Override
    public Optional<TnZertifikat> findById(Integer id) {
        return teilnehmerZertifikatRepository.findById(id);
    }

    @Override
    public TnZertifikat save(TnZertifikat object) {
        return teilnehmerZertifikatRepository.save(object);
    }

    @Override
    public List<TnZertifikat> saveAll(List<TnZertifikat> objects) {
        return teilnehmerZertifikatRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerZertifikatRepository.deleteById(id);
    }

    @Override
    public List<TnZertifikat> findAllByIdentifier(String identifier) {
        return null;
    }

    public List<TnZertifikat> findAllByTeilnehmerId(Integer teilnehmerId) {
        return teilnehmerZertifikatRepository.findAllByTeilnehmerId(teilnehmerId);
    }

    public TnZertifikatDto mapTeilnehmerZertifikatToDto(TnZertifikat teilnehmerZertifikat) {
        TnZertifikatDto tnZertifikatDto = new TnZertifikatDto();
        if (teilnehmerZertifikat != null) {
            tnZertifikatDto.setId(teilnehmerZertifikat.getId());
            tnZertifikatDto.setBezeichnung(teilnehmerZertifikat.getBezeichnung());
        }
        return tnZertifikatDto;
    }
}
