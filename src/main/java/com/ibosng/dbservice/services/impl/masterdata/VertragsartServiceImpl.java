package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Vertragsart;
import com.ibosng.dbservice.repositories.masterdata.VertragsartRepository;
import com.ibosng.dbservice.services.masterdata.VertragsartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VertragsartServiceImpl implements VertragsartService {

    private final VertragsartRepository vertragsartRepository;

    public VertragsartServiceImpl(VertragsartRepository vertragsartRepository) {
        this.vertragsartRepository = vertragsartRepository;
    }

    @Override
    public List<Vertragsart> findAll() {
        return vertragsartRepository.findAll();
    }

    @Override
    public Optional<Vertragsart> findById(Integer id) {
        return vertragsartRepository.findById(id);
    }

    @Override
    public Vertragsart save(Vertragsart object) {
        return vertragsartRepository.save(object);
    }

    @Override
    public List<Vertragsart> saveAll(List<Vertragsart> objects) {
        return vertragsartRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vertragsartRepository.deleteById(id);
    }

    @Override
    public List<Vertragsart> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Vertragsart findByName(String name) {
        return vertragsartRepository.findByName(name);
    }
}
