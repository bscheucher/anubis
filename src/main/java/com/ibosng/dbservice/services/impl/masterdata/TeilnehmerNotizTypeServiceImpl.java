package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotizType;
import com.ibosng.dbservice.repositories.masterdata.TeilnehmerNotizTypeRepository;
import com.ibosng.dbservice.services.masterdata.TeilnehmerNotizTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerNotizTypeServiceImpl implements TeilnehmerNotizTypeService {

    private final TeilnehmerNotizTypeRepository teilnehmerNotizTypeRepository;

    public TeilnehmerNotizTypeServiceImpl(TeilnehmerNotizTypeRepository teilnehmerNotizTypeRepository) {
        this.teilnehmerNotizTypeRepository = teilnehmerNotizTypeRepository;
    }

    @Override
    public List<TeilnehmerNotizType> findAll() {
        return teilnehmerNotizTypeRepository.findAll();
    }

    @Override
    public Optional<TeilnehmerNotizType> findById(Integer id) {
        return teilnehmerNotizTypeRepository.findById(id);
    }

    @Override
    public TeilnehmerNotizType save(TeilnehmerNotizType object) {
        return teilnehmerNotizTypeRepository.save(object);
    }

    @Override
    public List<TeilnehmerNotizType> saveAll(List<TeilnehmerNotizType> objects) {
        return teilnehmerNotizTypeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerNotizTypeRepository.deleteById(id);
    }

    @Override
    public List<TeilnehmerNotizType> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public TeilnehmerNotizType findByName(String name) {
        return teilnehmerNotizTypeRepository.findByName(name);
    }
}
