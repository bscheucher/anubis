package com.ibosng.dbservice.services.impl.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.Auszahlungsantrag;
import com.ibosng.dbservice.entities.zeiterfassung.AuszahlungsantragStatus;
import com.ibosng.dbservice.repositories.zeiterfassung.AuszahlungsantragRepository;
import com.ibosng.dbservice.services.zeiterfassung.AuszahlungsantragService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuszahlungsantragServiceImpl implements AuszahlungsantragService {

    private final AuszahlungsantragRepository auszahlungsantragRepository;

    public AuszahlungsantragServiceImpl(AuszahlungsantragRepository auszahlungsantragRepository) {
        this.auszahlungsantragRepository = auszahlungsantragRepository;
    }

    @Override
    public List<Auszahlungsantrag> findAll() {
        return auszahlungsantragRepository.findAll();
    }

    @Override
    public Optional<Auszahlungsantrag> findById(Integer id) {
        return auszahlungsantragRepository.findById(id);
    }

    @Override
    public Auszahlungsantrag save(Auszahlungsantrag auszahlungsantrag) {
        return auszahlungsantragRepository.save(auszahlungsantrag);
    }

    @Override
    public List<Auszahlungsantrag> saveAll(List<Auszahlungsantrag> auszahlungsantragList) {
        return auszahlungsantragRepository.saveAll(auszahlungsantragList);
    }

    @Override
    public void deleteById(Integer id) {
        auszahlungsantragRepository.deleteById(id);
    }

    @Override
    public List<Auszahlungsantrag> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Auszahlungsantrag> findByStatus(AuszahlungsantragStatus status) {
        return auszahlungsantragRepository.findByStatus(status);
    }

    @Override
    public Optional<Auszahlungsantrag> findByAnfrageNr(Integer anfrageNr) {
        return auszahlungsantragRepository.findByAnfrageNr(anfrageNr);
    }
}
