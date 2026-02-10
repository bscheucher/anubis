package com.ibosng.dbservice.services.impl.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.datastatus.UnterhaltsberechtigteDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.datastatus.UnterhaltsberechtigteDataStatusRepository;
import com.ibosng.dbservice.services.mitarbeiter.datastatus.UnterhaltsberechtigteDataStatusService;

import java.util.List;
import java.util.Optional;

public class UnterhaltsberechtigteDataStatusServiceImpl implements UnterhaltsberechtigteDataStatusService {

    private final UnterhaltsberechtigteDataStatusRepository unterhaltsberechtigteDataStatusRepository;

    public UnterhaltsberechtigteDataStatusServiceImpl(UnterhaltsberechtigteDataStatusRepository unterhaltsberechtigteDataStatusRepository) {
        this.unterhaltsberechtigteDataStatusRepository = unterhaltsberechtigteDataStatusRepository;
    }


    @Override
    public List<UnterhaltsberechtigteDataStatus> findAll() {
        return unterhaltsberechtigteDataStatusRepository.findAll();
    }

    @Override
    public Optional<UnterhaltsberechtigteDataStatus> findById(Integer id) {
        return unterhaltsberechtigteDataStatusRepository.findById(id);
    }

    @Override
    public UnterhaltsberechtigteDataStatus save(UnterhaltsberechtigteDataStatus object) {
        return unterhaltsberechtigteDataStatusRepository.save(object);
    }

    @Override
    public List<UnterhaltsberechtigteDataStatus> saveAll(List<UnterhaltsberechtigteDataStatus> objects) {
        return unterhaltsberechtigteDataStatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        unterhaltsberechtigteDataStatusRepository.deleteById(id);
    }

    @Override
    public List<UnterhaltsberechtigteDataStatus> findAllByIdentifier(String identifier) {
        return null;
    }
}
