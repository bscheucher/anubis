package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Ausschreibung;
import com.ibosng.dbservice.repositories.AusschreibungRepository;
import com.ibosng.dbservice.services.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AusschreibungServiceImpl implements BaseService<Ausschreibung> {

    private final AusschreibungRepository ausschreibungRepository;

    public AusschreibungServiceImpl(AusschreibungRepository ausschreibungRepository) {
        this.ausschreibungRepository = ausschreibungRepository;
    }

    @Override
    public Optional<Ausschreibung> findById(Integer id) {
        return ausschreibungRepository.findById(id);
    }

    @Override
    public List<Ausschreibung> findAll() {
        return ausschreibungRepository.findAll();
    }

    @Override
    public Ausschreibung save(Ausschreibung ausschreibung) {
        return ausschreibungRepository.save(ausschreibung);
    }

    @Override
    public void deleteById(Integer id) {
        ausschreibungRepository.deleteById(id);
    }

    @Override
    public List<Ausschreibung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Ausschreibung> saveAll(List<Ausschreibung> ausschreibungen) {
        return ausschreibungRepository.saveAll(ausschreibungen);
    }

    public Ausschreibung findByAusschreibungNummer(Integer ausschreibungNummer) {
        return ausschreibungRepository.findByAusschreibungNummer(ausschreibungNummer);
    }

}
