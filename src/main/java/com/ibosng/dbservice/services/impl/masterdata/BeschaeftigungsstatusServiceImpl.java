package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsstatus;
import com.ibosng.dbservice.repositories.masterdata.BeschaeftigungsstatusRepository;
import com.ibosng.dbservice.services.masterdata.BeschaeftigungsstatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeschaeftigungsstatusServiceImpl implements BeschaeftigungsstatusService {

    private final BeschaeftigungsstatusRepository beschaeftigungsstatusRepository;

    public BeschaeftigungsstatusServiceImpl(BeschaeftigungsstatusRepository beschaeftigungsstatusRepository) {
        this.beschaeftigungsstatusRepository = beschaeftigungsstatusRepository;
    }

    @Override
    public List<Beschaeftigungsstatus> findAll() {
        return beschaeftigungsstatusRepository.findAll();
    }

    @Override
    public Optional<Beschaeftigungsstatus> findById(Integer id) {
        return beschaeftigungsstatusRepository.findById(id);
    }

    @Override
    public Beschaeftigungsstatus save(Beschaeftigungsstatus object) {
        return beschaeftigungsstatusRepository.save(object);
    }

    @Override
    public List<Beschaeftigungsstatus> saveAll(List<Beschaeftigungsstatus> objects) {
        return beschaeftigungsstatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        beschaeftigungsstatusRepository.deleteById(id);
    }

    @Override
    public List<Beschaeftigungsstatus> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Beschaeftigungsstatus findByName(String name) {
        return beschaeftigungsstatusRepository.findByName(name);
    }
}
