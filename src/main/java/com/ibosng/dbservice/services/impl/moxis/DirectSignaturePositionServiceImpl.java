package com.ibosng.dbservice.services.impl.moxis;

import com.ibosng.dbservice.entities.moxis.DirectSignaturePosition;
import com.ibosng.dbservice.repositories.moxis.DirectSignaturePositionRepository;
import com.ibosng.dbservice.services.moxis.DirectSignaturePositionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectSignaturePositionServiceImpl implements DirectSignaturePositionService {

    private final DirectSignaturePositionRepository directSignaturePositionRepository;

    public DirectSignaturePositionServiceImpl(DirectSignaturePositionRepository directSignaturePositionRepository) {
        this.directSignaturePositionRepository = directSignaturePositionRepository;
    }

    @Override
    public List<DirectSignaturePosition> findAll() {
        return directSignaturePositionRepository.findAll();
    }

    @Override
    public Optional<DirectSignaturePosition> findById(Integer id) {
        return directSignaturePositionRepository.findById(id);
    }

    @Override
    public DirectSignaturePosition save(DirectSignaturePosition object) {
        return directSignaturePositionRepository.save(object);
    }

    @Override
    public List<DirectSignaturePosition> saveAll(List<DirectSignaturePosition> objects) {
        return directSignaturePositionRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        directSignaturePositionRepository.deleteById(id);
    }

    @Override
    public List<DirectSignaturePosition> findAllByIdentifier(String identifier) {
        return null;
    }
}
