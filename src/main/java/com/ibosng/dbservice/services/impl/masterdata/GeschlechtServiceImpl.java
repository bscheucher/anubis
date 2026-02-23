package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.repositories.masterdata.GeschlechtRepository;
import com.ibosng.dbservice.services.masterdata.GeschlechtService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeschlechtServiceImpl implements GeschlechtService {

    private final GeschlechtRepository geschlechtRepository;

    public GeschlechtServiceImpl(GeschlechtRepository geschlechtRepository) {
        this.geschlechtRepository = geschlechtRepository;
    }

    @Override
    public List<Geschlecht> findAll() {
        return geschlechtRepository.findAll();
    }

    @Override
    public Optional<Geschlecht> findById(Integer id) {
        return geschlechtRepository.findById(id);
    }

    @Override
    public Geschlecht save(Geschlecht object) {
        return geschlechtRepository.save(object);
    }

    @Override
    public List<Geschlecht> saveAll(List<Geschlecht> objects) {
        return geschlechtRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        geschlechtRepository.deleteById(id);
    }

    @Override
    public List<Geschlecht> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Geschlecht findByName(String name) {
        return geschlechtRepository.findByName(name);
    }

    @Override
    public Geschlecht findByAbbreviation(String abbreviation) {
        return geschlechtRepository.findByAbbreviation(abbreviation);
    }
}
