package com.ibosng.dbservice.services.impl.rollen;

import com.ibosng.dbservice.entities.rollen.Funktionen;
import com.ibosng.dbservice.repositories.rollen.FunktionenRepository;
import com.ibosng.dbservice.services.rollen.FunktionenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FunktionenServiceImpl implements FunktionenService {

    private final FunktionenRepository funktionenRepository;

    public FunktionenServiceImpl(FunktionenRepository funktionenRepository) {
        this.funktionenRepository = funktionenRepository;
    }

    @Override
    public Optional<Funktionen> findById(Integer id) {
        return funktionenRepository.findById(id);
    }

    @Override
    public List<Funktionen> findAll() {
        return funktionenRepository.findAll();
    }

    @Override
    public Funktionen save(Funktionen funktion) {
        return funktionenRepository.save(funktion);
    }

    @Override
    public void deleteById(Integer id) {
        this.funktionenRepository.deleteById(id);
    }

    @Override
    public List<Funktionen> saveAll(List<Funktionen> funktionen) {
        return funktionenRepository.saveAll(funktionen);
    }

    @Override
    public List<Funktionen> findAllByIdentifier(String identifier) {
        return null;
    }
}
