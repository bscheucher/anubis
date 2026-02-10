package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Kollektivvertrag;
import com.ibosng.dbservice.repositories.masterdata.KollektivvertragRepository;
import com.ibosng.dbservice.services.masterdata.KollektivvertragService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KollektivvertragServiceImpl implements KollektivvertragService {

    private final KollektivvertragRepository kollektivvertragRepository;

    public KollektivvertragServiceImpl(KollektivvertragRepository kollektivvertragRepository) {
        this.kollektivvertragRepository = kollektivvertragRepository;
    }

    @Override
    public List<Kollektivvertrag> findAll() {
        return kollektivvertragRepository.findAll();
    }

    @Override
    public Optional<Kollektivvertrag> findById(Integer id) {
        return kollektivvertragRepository.findById(id);
    }

    @Override
    public Kollektivvertrag save(Kollektivvertrag object) {
        return kollektivvertragRepository.save(object);
    }

    @Override
    public List<Kollektivvertrag> saveAll(List<Kollektivvertrag> objects) {
        return kollektivvertragRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kollektivvertragRepository.deleteById(id);
    }

    @Override
    public List<Kollektivvertrag> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Kollektivvertrag findByName(String name) {
        return kollektivvertragRepository.findByName(name);
    }
}
