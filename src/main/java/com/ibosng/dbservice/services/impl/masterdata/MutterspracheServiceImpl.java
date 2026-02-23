package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Muttersprache;
import com.ibosng.dbservice.repositories.masterdata.MutterspracheRepository;
import com.ibosng.dbservice.services.masterdata.MutterspracheService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MutterspracheServiceImpl implements MutterspracheService {

    private final MutterspracheRepository mutterspracheRepository;


    public MutterspracheServiceImpl(MutterspracheRepository mutterspracheRepository) {
        this.mutterspracheRepository = mutterspracheRepository;
    }

    @Override
    public List<Muttersprache> findAll() {
        return mutterspracheRepository.findAll();
    }

    @Override
    public Optional<Muttersprache> findById(Integer id) {
        return mutterspracheRepository.findById(id);
    }

    @Override
    public Muttersprache save(Muttersprache object) {
        return mutterspracheRepository.save(object);
    }

    @Override
    public List<Muttersprache> saveAll(List<Muttersprache> objects) {
        return mutterspracheRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        mutterspracheRepository.deleteById(id);
    }

    @Override
    public List<Muttersprache> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Muttersprache findByName(String name) {
        return mutterspracheRepository.findByName(name);
    }
}
