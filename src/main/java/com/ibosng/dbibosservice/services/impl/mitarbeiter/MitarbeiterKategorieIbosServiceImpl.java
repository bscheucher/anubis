package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.MitarbeiterKategorieIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.MitarbeiterKategorieIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.MitarbeiterKategorieIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MitarbeiterKategorieIbosServiceImpl implements MitarbeiterKategorieIbosService {

    private final MitarbeiterKategorieIbosRepository mitarbeiterKategorieIbosRepository;

    public MitarbeiterKategorieIbosServiceImpl(MitarbeiterKategorieIbosRepository mitarbeiterKategorieIbosRepository) {
        this.mitarbeiterKategorieIbosRepository = mitarbeiterKategorieIbosRepository;
    }

    @Override
    public List<MitarbeiterKategorieIbos> findAll() {
        return mitarbeiterKategorieIbosRepository.findAll();
    }

    @Override
    public Optional<MitarbeiterKategorieIbos> findById(Integer id) {
        return mitarbeiterKategorieIbosRepository.findById(id);
    }

    @Override
    public MitarbeiterKategorieIbos save(MitarbeiterKategorieIbos object) {
        return mitarbeiterKategorieIbosRepository.save(object);
    }

    @Override
    public List<MitarbeiterKategorieIbos> saveAll(List<MitarbeiterKategorieIbos> objects) {
        return mitarbeiterKategorieIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        mitarbeiterKategorieIbosRepository.deleteById(id);
    }

    @Override
    public List<MitarbeiterKategorieIbos> findAllByBezeichnung(String bezeichnung) {
        return mitarbeiterKategorieIbosRepository.findAllByBezeichnung(bezeichnung);
    }
}
