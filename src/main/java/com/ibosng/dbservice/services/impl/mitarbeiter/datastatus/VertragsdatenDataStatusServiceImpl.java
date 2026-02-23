package com.ibosng.dbservice.services.impl.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VertragsdatenDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.datastatus.VertragsdatenDataStatusRepository;
import com.ibosng.dbservice.services.mitarbeiter.datastatus.VertragsdatenDataStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VertragsdatenDataStatusServiceImpl implements VertragsdatenDataStatusService {

    private final VertragsdatenDataStatusRepository vertragsdatenDataStatusRepository;

    public VertragsdatenDataStatusServiceImpl(VertragsdatenDataStatusRepository vertragsdatenDataStatusRepository) {
        this.vertragsdatenDataStatusRepository = vertragsdatenDataStatusRepository;
    }

    @Override
    public List<VertragsdatenDataStatus> findAll() {
        return vertragsdatenDataStatusRepository.findAll();
    }

    @Override
    public Optional<VertragsdatenDataStatus> findById(Integer id) {
        return vertragsdatenDataStatusRepository.findById(id);
    }

    @Override
    public VertragsdatenDataStatus save(VertragsdatenDataStatus object) {
        return vertragsdatenDataStatusRepository.save(object);
    }

    @Override
    public List<VertragsdatenDataStatus> saveAll(List<VertragsdatenDataStatus> objects) {
        return vertragsdatenDataStatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vertragsdatenDataStatusRepository.deleteById(id);
    }

    @Override
    public List<VertragsdatenDataStatus> findAllByIdentifier(String identifier) {
        return null;
    }
    
}
