package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Vorwahl;
import com.ibosng.dbservice.repositories.VorwahlRepository;
import com.ibosng.dbservice.services.VorwahlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VorwahlServiceImpl implements VorwahlService {

    private final VorwahlRepository vorwahlRepository;

    @Autowired
    public VorwahlServiceImpl(VorwahlRepository vorwahlRepository) {
        this.vorwahlRepository = vorwahlRepository;
    }

    @Override
    public Optional<Vorwahl> findById(Integer id) {
        return vorwahlRepository.findById(id);
    }

    @Override
    public List<Vorwahl> findAll() {
        return vorwahlRepository.findAll();
    }

    @Override
    public Vorwahl save(Vorwahl Vorwahl) {
        return vorwahlRepository.save(Vorwahl);
    }

    @Override
    public List<Vorwahl> saveAll(List<Vorwahl> Vorwahls) {
        return vorwahlRepository.saveAll(Vorwahls);
    }

    @Override
    public void deleteById(Integer id) {
        this.vorwahlRepository.deleteById(id);
    }

    @Override
    public List<Vorwahl> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Integer> findAllVorwahlByLaender(Land land) {
        return vorwahlRepository.findAllVorwahlByLaender(land);
    }

    @Override
    public Vorwahl findByVorwahl(Integer vorwahl) {
        return vorwahlRepository.findByVorwahl(vorwahl);
    }
}
