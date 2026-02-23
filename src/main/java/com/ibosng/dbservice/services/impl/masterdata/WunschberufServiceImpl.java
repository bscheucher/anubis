package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.teilnehmer.Beruf;
import com.ibosng.dbservice.repositories.masterdata.WunschberufRepository;
import com.ibosng.dbservice.services.masterdata.BerufService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WunschberufServiceImpl implements BerufService {

    private final WunschberufRepository wunschberufRepository;

    public WunschberufServiceImpl(WunschberufRepository WunschberufRepository) {
        this.wunschberufRepository = WunschberufRepository;
    }

    @Override
    public List<Beruf> findAll() {
        return wunschberufRepository.findAll();
    }

    @Override
    public Optional<Beruf> findById(Integer id) {
        return wunschberufRepository.findById(id);
    }

    @Override
    public Beruf save(Beruf object) {
        return wunschberufRepository.save(object);
    }

    @Override
    public List<Beruf> saveAll(List<Beruf> objects) {
        return wunschberufRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        return;
    }

    @Override
    public List<Beruf> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Beruf findByName(String name) {
        return wunschberufRepository.findByName(name);
    }
}
