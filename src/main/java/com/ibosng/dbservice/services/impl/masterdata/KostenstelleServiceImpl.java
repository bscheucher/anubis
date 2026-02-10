package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.repositories.masterdata.KostenstelleRepository;
import com.ibosng.dbservice.services.masterdata.KostenstelleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KostenstelleServiceImpl implements KostenstelleService {

    private final KostenstelleRepository kostenstelleRepository;

    public KostenstelleServiceImpl(KostenstelleRepository kostenstelleRepository) {
        this.kostenstelleRepository = kostenstelleRepository;
    }

    @Override
    public List<Kostenstelle> findAll() {
        return kostenstelleRepository.findAll();
    }

    @Override
    public Optional<Kostenstelle> findById(Integer id) {
        return kostenstelleRepository.findById(id);
    }

    @Override
    public Kostenstelle save(Kostenstelle object) {
        return kostenstelleRepository.save(object);
    }

    @Override
    public List<Kostenstelle> saveAll(List<Kostenstelle> objects) {
        return kostenstelleRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kostenstelleRepository.deleteById(id);
    }

    @Override
    public List<Kostenstelle> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Kostenstelle findByBezeichnung(String name) {
        return kostenstelleRepository.findByBezeichnung(name);
    }

    @Override
    public Kostenstelle findByNummer(Integer id) {
        return kostenstelleRepository.findByNummer(id);
    }

    @Override
    public List<Kostenstelle> findAllByStatus(Status status) {
        return kostenstelleRepository.findAllByStatus(status);
    }
}
