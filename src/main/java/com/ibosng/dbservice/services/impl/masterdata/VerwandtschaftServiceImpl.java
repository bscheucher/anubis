package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Verwandtschaft;
import com.ibosng.dbservice.repositories.masterdata.VerwandtschaftRepository;
import com.ibosng.dbservice.services.masterdata.VerwandtschaftService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VerwandtschaftServiceImpl implements VerwandtschaftService {

    private final VerwandtschaftRepository verwandtschaftRepository;

    public VerwandtschaftServiceImpl(VerwandtschaftRepository verwandtschaftRepository) {
        this.verwandtschaftRepository = verwandtschaftRepository;
    }

    @Override
    public List<Verwandtschaft> findAll() {
        return verwandtschaftRepository.findAll();
    }

    @Override
    public Optional<Verwandtschaft> findById(Integer id) {
        return verwandtschaftRepository.findById(id);
    }

    @Override
    public Verwandtschaft save(Verwandtschaft object) {
        return verwandtschaftRepository.save(object);
    }

    @Override
    public List<Verwandtschaft> saveAll(List<Verwandtschaft> objects) {
        return verwandtschaftRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        verwandtschaftRepository.deleteById(id);
    }

    @Override
    public List<Verwandtschaft> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Verwandtschaft findByName(String name) {
        return verwandtschaftRepository.findByName(name);
    }
}
