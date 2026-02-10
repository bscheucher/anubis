package com.ibosng.dbservice.services.impl.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.repositories.mitarbeiter.GehaltInfoRepository;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GehaltInfoServiceImpl implements GehaltInfoService {

    private final GehaltInfoRepository gehaltInfoRepository;

    public GehaltInfoServiceImpl(GehaltInfoRepository gehaltInfoRepository) {
        this.gehaltInfoRepository = gehaltInfoRepository;
    }

    @Override
    public List<GehaltInfo> findAll() {
        return gehaltInfoRepository.findAll();
    }

    @Override
    public Optional<GehaltInfo> findById(Integer id) {
        return gehaltInfoRepository.findById(id);
    }

    @Override
    public GehaltInfo save(GehaltInfo object) {
        return gehaltInfoRepository.save(object);
    }

    @Override
    public List<GehaltInfo> saveAll(List<GehaltInfo> objects) {
        return gehaltInfoRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        gehaltInfoRepository.deleteById(id);
    }

    @Override
    public List<GehaltInfo> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public GehaltInfo findByVertragsdatenId(Integer vertragsdatenId) {
        return gehaltInfoRepository.findByVertragsdaten_Id(vertragsdatenId);
    }
}
