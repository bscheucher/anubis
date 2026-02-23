package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungInstitut;
import com.ibosng.dbservice.repositories.masterdata.SeminarPruefungInstitutRepository;
import com.ibosng.dbservice.services.masterdata.SeminarPruefungInstitutService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungInstitutServiceImpl implements SeminarPruefungInstitutService {

    private final SeminarPruefungInstitutRepository seminarPruefungInstitutRepository;

    public SeminarPruefungInstitutServiceImpl(SeminarPruefungInstitutRepository seminarPruefungInstitutRepository) {
        this.seminarPruefungInstitutRepository = seminarPruefungInstitutRepository;
    }

    @Override
    public List<SeminarPruefungInstitut> findAll() {
        return seminarPruefungInstitutRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefungInstitut> findById(Integer id) {
        return seminarPruefungInstitutRepository.findById(id);
    }

    @Override
    public SeminarPruefungInstitut save(SeminarPruefungInstitut object) {
        return seminarPruefungInstitutRepository.save(object);
    }

    @Override
    public List<SeminarPruefungInstitut> saveAll(List<SeminarPruefungInstitut> objects) {
        return seminarPruefungInstitutRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungInstitutRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefungInstitut> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarPruefungInstitut findByName(String name) {
        return seminarPruefungInstitutRepository.findByName(name);
    }
}
