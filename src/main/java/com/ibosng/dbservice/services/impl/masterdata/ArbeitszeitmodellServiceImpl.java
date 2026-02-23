package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Arbeitszeitmodell;
import com.ibosng.dbservice.repositories.masterdata.ArbeitszeitmodellRepository;
import com.ibosng.dbservice.services.masterdata.ArbeitszeitmodellService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitszeitmodellServiceImpl implements ArbeitszeitmodellService {

    private final ArbeitszeitmodellRepository arbeitszeitmodellRepository;

    public ArbeitszeitmodellServiceImpl(ArbeitszeitmodellRepository arbeitszeitmodellRepository) {
        this.arbeitszeitmodellRepository = arbeitszeitmodellRepository;
    }

    @Override
    public List<Arbeitszeitmodell> findAll() {
        return arbeitszeitmodellRepository.findAll();
    }

    @Override
    public Optional<Arbeitszeitmodell> findById(Integer id) {
        return arbeitszeitmodellRepository.findById(id);
    }

    @Override
    public Arbeitszeitmodell save(Arbeitszeitmodell object) {
        return arbeitszeitmodellRepository.save(object);
    }

    @Override
    public List<Arbeitszeitmodell> saveAll(List<Arbeitszeitmodell> objects) {
        return arbeitszeitmodellRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitszeitmodellRepository.deleteById(id);
    }

    @Override
    public List<Arbeitszeitmodell> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Arbeitszeitmodell findByName(String name) {
        return arbeitszeitmodellRepository.findByName(name);
    }
}
