package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.teilnehmer.TnAusbildungType;
import com.ibosng.dbservice.repositories.masterdata.TeilnehmerAusbildungTypeRepository;
import com.ibosng.dbservice.services.masterdata.TeilnehmerAusbildungTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerAusbildungTypeServiceImpl implements TeilnehmerAusbildungTypeService {

    private final TeilnehmerAusbildungTypeRepository teilnehmerAusbildungTypeRepository;

    public TeilnehmerAusbildungTypeServiceImpl(TeilnehmerAusbildungTypeRepository teilnehmerAusbildungTypeRepository) {
        this.teilnehmerAusbildungTypeRepository = teilnehmerAusbildungTypeRepository;
    }

    @Override
    public List<TnAusbildungType> findAll() {
        return teilnehmerAusbildungTypeRepository.findAll();
    }

    @Override
    public Optional<TnAusbildungType> findById(Integer id) {
        return teilnehmerAusbildungTypeRepository.findById(id);
    }

    @Override
    public TnAusbildungType save(TnAusbildungType object) {
        return teilnehmerAusbildungTypeRepository.save(object);
    }

    @Override
    public List<TnAusbildungType> saveAll(List<TnAusbildungType> objects) {
        return teilnehmerAusbildungTypeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerAusbildungTypeRepository.deleteById(id);
    }

    @Override
    public List<TnAusbildungType> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public TnAusbildungType findByName(String name) {
        return teilnehmerAusbildungTypeRepository.findByName(name);
    }
}
