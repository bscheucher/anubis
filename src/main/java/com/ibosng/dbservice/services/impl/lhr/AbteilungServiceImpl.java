package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.entities.lhr.Abteilung;
import com.ibosng.dbservice.repositories.lhr.AbteilungRepository;
import com.ibosng.dbservice.services.lhr.AbteilungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbteilungServiceImpl implements AbteilungService {

    private final AbteilungRepository abteilungRepository;

    public AbteilungServiceImpl(AbteilungRepository abteilungRepository) {
        this.abteilungRepository = abteilungRepository;
    }

    @Override
    public List<Abteilung> findAll() {
        return abteilungRepository.findAll();
    }

    @Override
    public Optional<Abteilung> findById(Integer id) {
        return abteilungRepository.findById(id);
    }

    @Override
    public Abteilung save(Abteilung object) {
        return abteilungRepository.save(object);
    }

    @Override
    public List<Abteilung> saveAll(List<Abteilung> objects) {
        return abteilungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        abteilungRepository.deleteById(id);
    }

    @Override
    public List<Abteilung> findAllByIdentifier(String identifier) {
        return null;
    }
}
