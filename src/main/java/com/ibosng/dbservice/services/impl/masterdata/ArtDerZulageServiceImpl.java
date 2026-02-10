package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.ArtDerZulage;
import com.ibosng.dbservice.repositories.masterdata.ArtDerZulageRepository;
import com.ibosng.dbservice.services.masterdata.ArtDerZulageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtDerZulageServiceImpl implements ArtDerZulageService {

    private final ArtDerZulageRepository artDerZulageRepository;

    public ArtDerZulageServiceImpl(ArtDerZulageRepository artDerZulageRepository) {
        this.artDerZulageRepository = artDerZulageRepository;
    }

    @Override
    public List<ArtDerZulage> findAll() {
        return artDerZulageRepository.findAll();
    }

    @Override
    public Optional<ArtDerZulage> findById(Integer id) {
        return artDerZulageRepository.findById(id);
    }

    @Override
    public ArtDerZulage save(ArtDerZulage object) {
        return artDerZulageRepository.save(object);
    }

    @Override
    public List<ArtDerZulage> saveAll(List<ArtDerZulage> objects) {
        return artDerZulageRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        artDerZulageRepository.deleteById(id);
    }

    @Override
    public List<ArtDerZulage> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public ArtDerZulage findByName(String name) {
        return artDerZulageRepository.findByName(name);
    }
}
