package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Kategorie;
import com.ibosng.dbservice.repositories.masterdata.KategorieRepository;
import com.ibosng.dbservice.services.masterdata.KategorieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KategorieServiceImpl implements KategorieService {

    private final KategorieRepository kategorieRepository;

    public KategorieServiceImpl(KategorieRepository kategorieRepository) {
        this.kategorieRepository = kategorieRepository;
    }

    @Override
    public List<Kategorie> findAll() {
        return kategorieRepository.findAll();
    }

    @Override
    public Optional<Kategorie> findById(Integer id) {
        return kategorieRepository.findById(id);
    }

    @Override
    public Kategorie save(Kategorie object) {
        return kategorieRepository.save(object);
    }

    @Override
    public List<Kategorie> saveAll(List<Kategorie> objects) {
        return kategorieRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kategorieRepository.deleteById(id);
    }

    @Override
    public List<Kategorie> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Kategorie findByName(String name) {
        return kategorieRepository.findByName(name);
    }
}
