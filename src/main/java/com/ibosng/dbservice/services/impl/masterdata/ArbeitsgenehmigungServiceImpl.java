package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Arbeitsgenehmigung;
import com.ibosng.dbservice.repositories.masterdata.ArbeitsgenehmigungRepository;
import com.ibosng.dbservice.services.masterdata.ArbeitsgenehmigungService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsgenehmigungServiceImpl implements ArbeitsgenehmigungService {

    private final ArbeitsgenehmigungRepository arbeitsgenehmigungRepository;

    public ArbeitsgenehmigungServiceImpl(ArbeitsgenehmigungRepository arbeitsgenehmigungRepository) {
        this.arbeitsgenehmigungRepository = arbeitsgenehmigungRepository;
    }

    @Override
    public List<Arbeitsgenehmigung> findAll() {
        return arbeitsgenehmigungRepository.findAll();
    }

    @Override
    public Optional<Arbeitsgenehmigung> findById(Integer id) {
        return arbeitsgenehmigungRepository.findById(id);
    }

    @Override
    public Arbeitsgenehmigung save(Arbeitsgenehmigung object) {
        return arbeitsgenehmigungRepository.save(object);
    }

    @Override
    public List<Arbeitsgenehmigung> saveAll(List<Arbeitsgenehmigung> objects) {
        return arbeitsgenehmigungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsgenehmigungRepository.deleteById(id);
    }

    @Override
    public List<Arbeitsgenehmigung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Arbeitsgenehmigung findByName(String name) {
        return arbeitsgenehmigungRepository.findByName(name);
    }
}
