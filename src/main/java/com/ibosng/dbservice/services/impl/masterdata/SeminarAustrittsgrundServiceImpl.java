package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarAustrittsgrund;
import com.ibosng.dbservice.repositories.masterdata.SeminarAustrittsgrundRepository;
import com.ibosng.dbservice.services.masterdata.SeminarAustrittsgrundService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarAustrittsgrundServiceImpl implements SeminarAustrittsgrundService {

    private final SeminarAustrittsgrundRepository seminarAustrittsgrundRepository;

    public SeminarAustrittsgrundServiceImpl(SeminarAustrittsgrundRepository seminarAustrittsgrundRepository) {
        this.seminarAustrittsgrundRepository = seminarAustrittsgrundRepository;
    }

    @Override
    public List<SeminarAustrittsgrund> findAll() {
        return seminarAustrittsgrundRepository.findAll();
    }

    @Override
    public Optional<SeminarAustrittsgrund> findById(Integer id) {
        return seminarAustrittsgrundRepository.findById(id);
    }

    @Override
    public SeminarAustrittsgrund save(SeminarAustrittsgrund object) {
        return seminarAustrittsgrundRepository.save(object);
    }

    @Override
    public List<SeminarAustrittsgrund> saveAll(List<SeminarAustrittsgrund> objects) {
        return seminarAustrittsgrundRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarAustrittsgrundRepository.deleteById(id);
    }

    @Override
    public List<SeminarAustrittsgrund> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SeminarAustrittsgrund findByName(String name) {
        return seminarAustrittsgrundRepository.findByName(name);
    }
}
