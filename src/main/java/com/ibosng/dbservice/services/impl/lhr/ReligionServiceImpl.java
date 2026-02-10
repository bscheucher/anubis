package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.entities.lhr.Religion;
import com.ibosng.dbservice.repositories.lhr.ReligionRepository;
import com.ibosng.dbservice.services.lhr.ReligionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReligionServiceImpl implements ReligionService {

    private final ReligionRepository religionRepository;

    public ReligionServiceImpl(ReligionRepository religionRepository) {
        this.religionRepository = religionRepository;
    }

    @Override
    public List<Religion> findAll() {
        return religionRepository.findAll();
    }

    @Override
    public Optional<Religion> findById(Integer id) {
        return religionRepository.findById(id);
    }

    @Override
    public Religion save(Religion object) {
        return religionRepository.save(object);
    }

    @Override
    public List<Religion> saveAll(List<Religion> objects) {
        return religionRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        religionRepository.deleteById(id);
    }

    @Override
    public List<Religion> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Optional<Religion> findByName(String name) {
        return religionRepository.findByName(name);
    }
}
