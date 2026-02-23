package com.ibosng.dbservice.services.impl.mitarbeiter;



import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import com.ibosng.dbservice.repositories.mitarbeiter.ArbeitszeitenRepository;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitszeitenServiceImpl implements ArbeitszeitenService {

    private final ArbeitszeitenRepository arbeitszeitenRepository;

    public ArbeitszeitenServiceImpl(ArbeitszeitenRepository arbeitszeitenRepository) {
        this.arbeitszeitenRepository = arbeitszeitenRepository;
    }

    @Override
    public List<Arbeitszeiten> findAll() {
        return arbeitszeitenRepository.findAll();
    }

    @Override
    public Optional<Arbeitszeiten> findById(Integer id) {
        return arbeitszeitenRepository.findById(id);
    }

    @Override
    public Arbeitszeiten save(Arbeitszeiten object) {
        return arbeitszeitenRepository.save(object);
    }

    @Override
    public List<Arbeitszeiten> saveAll(List<Arbeitszeiten> objects) {
        return arbeitszeitenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitszeitenRepository.deleteById(id);
    }

    @Override
    public List<Arbeitszeiten> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Arbeitszeiten> findByArbAndArbeitszeitenInfoId(Integer arbeitszeiteninfoId) {
        return arbeitszeitenRepository.findByArbAndArbeitszeitenInfoId(arbeitszeiteninfoId);
    }
}
