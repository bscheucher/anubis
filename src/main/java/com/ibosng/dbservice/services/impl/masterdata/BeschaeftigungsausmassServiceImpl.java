package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsausmass;
import com.ibosng.dbservice.repositories.masterdata.BeschaeftigungsausmassRepository;
import com.ibosng.dbservice.services.masterdata.BeschaeftigungsausmassService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeschaeftigungsausmassServiceImpl implements BeschaeftigungsausmassService {

    private final BeschaeftigungsausmassRepository beschaeftigungsausmassRepository;

    public BeschaeftigungsausmassServiceImpl(BeschaeftigungsausmassRepository beschaeftigungsausmassRepository) {
        this.beschaeftigungsausmassRepository = beschaeftigungsausmassRepository;
    }

    @Override
    public List<Beschaeftigungsausmass> findAll() {
        return beschaeftigungsausmassRepository.findAll();
    }

    @Override
    public Optional<Beschaeftigungsausmass> findById(Integer id) {
        return beschaeftigungsausmassRepository.findById(id);
    }

    @Override
    public Beschaeftigungsausmass save(Beschaeftigungsausmass object) {
        return beschaeftigungsausmassRepository.save(object);
    }

    @Override
    public List<Beschaeftigungsausmass> saveAll(List<Beschaeftigungsausmass> objects) {
        return beschaeftigungsausmassRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        beschaeftigungsausmassRepository.deleteById(id);
    }

    @Override
    public List<Beschaeftigungsausmass> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Beschaeftigungsausmass findByName(String name) {
        return beschaeftigungsausmassRepository.findByName(name);
    }
}
