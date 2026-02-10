package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.repositories.masterdata.VerwendungsgruppeRepository;
import com.ibosng.dbservice.services.masterdata.VerwendungsgruppeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VerwendungsgruppeServiceImpl implements VerwendungsgruppeService {

    private final VerwendungsgruppeRepository verwendungsgruppeRepository;

    public VerwendungsgruppeServiceImpl(VerwendungsgruppeRepository verwendungsgruppeRepository) {
        this.verwendungsgruppeRepository = verwendungsgruppeRepository;
    }

    @Override
    public List<Verwendungsgruppe> findAll() {
        return verwendungsgruppeRepository.findAll();
    }

    @Override
    public Optional<Verwendungsgruppe> findById(Integer id) {
        return verwendungsgruppeRepository.findById(id);
    }

    @Override
    public Verwendungsgruppe save(Verwendungsgruppe object) {
        return verwendungsgruppeRepository.save(object);
    }

    @Override
    public List<Verwendungsgruppe> saveAll(List<Verwendungsgruppe> objects) {
        return verwendungsgruppeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        verwendungsgruppeRepository.deleteById(id);
    }

    @Override
    public List<Verwendungsgruppe> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Verwendungsgruppe findByName(String name) {
        return verwendungsgruppeRepository.findByName(name);
    }

    @Override
    public List<Verwendungsgruppe> findAllByKollektivvertragName(String kollektivvertrag) {
        return verwendungsgruppeRepository.findAllByKollektivvertragName(kollektivvertrag);
    }
}
