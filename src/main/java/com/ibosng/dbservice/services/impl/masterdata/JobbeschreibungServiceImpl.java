package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Jobbeschreibung;
import com.ibosng.dbservice.repositories.masterdata.JobbeschreibungRepository;
import com.ibosng.dbservice.services.masterdata.JobbeschreibungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobbeschreibungServiceImpl implements JobbeschreibungService {

    private final JobbeschreibungRepository jobbeschreibungRepository;

    public JobbeschreibungServiceImpl(JobbeschreibungRepository jobbeschreibungRepository) {
        this.jobbeschreibungRepository = jobbeschreibungRepository;
    }

    @Override
    public List<Jobbeschreibung> findAll() {
        return jobbeschreibungRepository.findAll();
    }

    @Override
    public Optional<Jobbeschreibung> findById(Integer id) {
        return jobbeschreibungRepository.findById(id);
    }

    @Override
    public Jobbeschreibung save(Jobbeschreibung object) {
        return jobbeschreibungRepository.save(object);
    }

    @Override
    public List<Jobbeschreibung> saveAll(List<Jobbeschreibung> objects) {
        return jobbeschreibungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        jobbeschreibungRepository.deleteById(id);
    }

    @Override
    public List<Jobbeschreibung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Jobbeschreibung findByName(String name) {
        return jobbeschreibungRepository.findByName(name);
    }
}
