package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Taetigkeit;
import com.ibosng.dbservice.repositories.masterdata.TaetigkeitRepository;
import com.ibosng.dbservice.services.masterdata.TaetigkeitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaetigkeitServiceImpl implements TaetigkeitService {

    private final TaetigkeitRepository taetigkeitRepository;

    public TaetigkeitServiceImpl(TaetigkeitRepository taetigkeitRepository) {
        this.taetigkeitRepository = taetigkeitRepository;
    }

    @Override
    public List<Taetigkeit> findAll() {
        return taetigkeitRepository.findAll();
    }

    @Override
    public Optional<Taetigkeit> findById(Integer id) {
        return taetigkeitRepository.findById(id);
    }

    @Override
    public Taetigkeit save(Taetigkeit object) {
        return taetigkeitRepository.save(object);
    }

    @Override
    public List<Taetigkeit> saveAll(List<Taetigkeit> objects) {
        return taetigkeitRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        taetigkeitRepository.deleteById(id);
    }

    @Override
    public List<Taetigkeit> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Taetigkeit findByName(String name) {
        return taetigkeitRepository.findByName(name);
    }
}
