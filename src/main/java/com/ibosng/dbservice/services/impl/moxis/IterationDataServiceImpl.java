package com.ibosng.dbservice.services.impl.moxis;

import com.ibosng.dbservice.entities.moxis.IterationData;
import com.ibosng.dbservice.repositories.moxis.IterationDataRepository;
import com.ibosng.dbservice.services.moxis.IterationDataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IterationDataServiceImpl implements IterationDataService {

    private final IterationDataRepository iterationDataRepository;

    public IterationDataServiceImpl(IterationDataRepository iterationDataRepository) {
        this.iterationDataRepository = iterationDataRepository;
    }

    @Override
    public List<IterationData> findAll() {
        return iterationDataRepository.findAll();
    }

    @Override
    public Optional<IterationData> findById(Integer id) {
        return iterationDataRepository.findById(id);
    }

    @Override
    public IterationData save(IterationData object) {
        return iterationDataRepository.save(object);
    }

    @Override
    public List<IterationData> saveAll(List<IterationData> objects) {
        return iterationDataRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        iterationDataRepository.deleteById(id);
    }

    @Override
    public List<IterationData> findAllByIdentifier(String identifier) {
        return null;
    }
}
