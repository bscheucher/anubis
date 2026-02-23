package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.AbgeltungUeberstunden;
import com.ibosng.dbservice.repositories.masterdata.AbgeltungUeberstundenRepository;
import com.ibosng.dbservice.services.masterdata.AbgeltungUeberstundenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbgeltungUeberstundenServiceImpl implements AbgeltungUeberstundenService {

    private final AbgeltungUeberstundenRepository abgeltungUeberstundenRepository;

    public AbgeltungUeberstundenServiceImpl(AbgeltungUeberstundenRepository abgeltungUeberstundenRepository) {
        this.abgeltungUeberstundenRepository = abgeltungUeberstundenRepository;
    }

    @Override
    public List<AbgeltungUeberstunden> findAll() {
        return abgeltungUeberstundenRepository.findAll();
    }

    @Override
    public Optional<AbgeltungUeberstunden> findById(Integer id) {
        return abgeltungUeberstundenRepository.findById(id);
    }

    @Override
    public AbgeltungUeberstunden save(AbgeltungUeberstunden object) {
        return abgeltungUeberstundenRepository.save(object);
    }

    @Override
    public List<AbgeltungUeberstunden> saveAll(List<AbgeltungUeberstunden> objects) {
        return abgeltungUeberstundenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        abgeltungUeberstundenRepository.deleteById(id);
    }

    @Override
    public List<AbgeltungUeberstunden> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public AbgeltungUeberstunden findByName(String name) {
        return abgeltungUeberstundenRepository.findByName(name);
    }
}
