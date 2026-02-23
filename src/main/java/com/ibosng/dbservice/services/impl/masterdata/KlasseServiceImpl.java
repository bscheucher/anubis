package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Klasse;
import com.ibosng.dbservice.repositories.masterdata.KlasseRepository;
import com.ibosng.dbservice.services.masterdata.KlasseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KlasseServiceImpl implements KlasseService {

    private final KlasseRepository klasseRepository;

    public KlasseServiceImpl(KlasseRepository klasseRepository) {
        this.klasseRepository = klasseRepository;
    }

    @Override
    public List<Klasse> findAll() {
        return klasseRepository.findAll();
    }

    @Override
    public Optional<Klasse> findById(Integer id) {
        return klasseRepository.findById(id);
    }

    @Override
    public Klasse save(Klasse object) {
        return klasseRepository.save(object);
    }

    @Override
    public List<Klasse> saveAll(List<Klasse> objects) {
        return klasseRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        klasseRepository.deleteById(id);
    }

    @Override
    public List<Klasse> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Klasse findByName(String name) {
        return klasseRepository.findByName(name);
    }
}
