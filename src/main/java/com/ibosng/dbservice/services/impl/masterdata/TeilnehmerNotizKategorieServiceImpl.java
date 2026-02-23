package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotizKategorie;
import com.ibosng.dbservice.repositories.masterdata.TeilnehmerNotizKategorieRepository;
import com.ibosng.dbservice.services.masterdata.TeilnehmerNotizKategorieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerNotizKategorieServiceImpl implements TeilnehmerNotizKategorieService {

    private final TeilnehmerNotizKategorieRepository teilnehmerNotizKategorieRepository;

    public TeilnehmerNotizKategorieServiceImpl(TeilnehmerNotizKategorieRepository teilnehmerNotizKategorieRepository) {
        this.teilnehmerNotizKategorieRepository = teilnehmerNotizKategorieRepository;
    }

    @Override
    public List<TeilnehmerNotizKategorie> findAll() {
        return teilnehmerNotizKategorieRepository.findAll();
    }

    @Override
    public Optional<TeilnehmerNotizKategorie> findById(Integer id) {
        return teilnehmerNotizKategorieRepository.findById(id);
    }

    @Override
    public TeilnehmerNotizKategorie save(TeilnehmerNotizKategorie object) {
        return teilnehmerNotizKategorieRepository.save(object);
    }

    @Override
    public List<TeilnehmerNotizKategorie> saveAll(List<TeilnehmerNotizKategorie> objects) {
        return teilnehmerNotizKategorieRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        teilnehmerNotizKategorieRepository.deleteById(id);
    }

    @Override
    public List<TeilnehmerNotizKategorie> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public TeilnehmerNotizKategorie findByName(String name) {
        return teilnehmerNotizKategorieRepository.findByName(name);
    }
}
