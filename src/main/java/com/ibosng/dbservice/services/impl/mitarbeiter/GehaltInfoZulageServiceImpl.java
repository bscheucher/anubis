package com.ibosng.dbservice.services.impl.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.ZulageDto;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfoZulage;
import com.ibosng.dbservice.repositories.mitarbeiter.GehaltInfoZulageRepository;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoZulageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GehaltInfoZulageServiceImpl implements GehaltInfoZulageService {

    private final GehaltInfoZulageRepository gehaltInfoZulageRepository;

    public GehaltInfoZulageServiceImpl(GehaltInfoZulageRepository gehaltInfoZulageRepository) {
        this.gehaltInfoZulageRepository = gehaltInfoZulageRepository;
    }

    @Override
    public List<GehaltInfoZulage> findAll() {
        return gehaltInfoZulageRepository.findAll();
    }

    @Override
    public Optional<GehaltInfoZulage> findById(Integer id) {
        return gehaltInfoZulageRepository.findById(id);
    }

    @Override
    public GehaltInfoZulage save(GehaltInfoZulage object) {
        return gehaltInfoZulageRepository.save(object);
    }

    @Override
    public List<GehaltInfoZulage> saveAll(List<GehaltInfoZulage> objects) {
        return gehaltInfoZulageRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        gehaltInfoZulageRepository.deleteById(id);
    }

    @Override
    public List<GehaltInfoZulage> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<GehaltInfoZulage> findAllByGehaltInfoId(Integer gehaltInfoId) {
        return gehaltInfoZulageRepository.findAllByGehaltInfoId(gehaltInfoId);
    }

    @Override
    public ZulageDto mapGehaltInfoZulageToDto(GehaltInfoZulage gehaltInfoZulage) {

        ZulageDto zulageDto = new ZulageDto();
        zulageDto.setId(gehaltInfoZulage.getId());
        zulageDto.setArtDerZulage(gehaltInfoZulage.getArtDerZulage());
        zulageDto.setZulageInEuro(String.valueOf(gehaltInfoZulage.getZulageInEuro()));
        zulageDto.setGehaltInfoId(String.valueOf(gehaltInfoZulage.getGehaltInfo().getId()));
        return zulageDto;
    }

    @Override
    public Optional<GehaltInfoZulage> findByArtDerZulageAndGehaltInfoId(String artDerZulage, Integer gehaltInfoId) {
        return gehaltInfoZulageRepository.findByArtDerZulageAndGehaltInfoId(artDerZulage, gehaltInfoId);
    }

    @Override
    public void deleteByArtDerZulageAndGehaltInfoId(String artDerZulage, Integer gehaltInfoId) {
        gehaltInfoZulageRepository.deleteByArtDerZulageAndGehaltInfoId(artDerZulage, gehaltInfoId);
    }
}
