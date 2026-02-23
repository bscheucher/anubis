package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.IbosReference;
import com.ibosng.dbservice.repositories.IbosReferenceRepository;
import com.ibosng.dbservice.services.IbosReferenceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IbosReferenceServiceImpl implements IbosReferenceService {

    private final IbosReferenceRepository ibosReferenceRepository;

    public IbosReferenceServiceImpl(IbosReferenceRepository ibosReferenceRepository) {
        this.ibosReferenceRepository = ibosReferenceRepository;
    }

    @Override
    public List<IbosReference> findAll() {
        return ibosReferenceRepository.findAll();
    }

    @Override
    public Optional<IbosReference> findById(Integer id) {
        return ibosReferenceRepository.findById(id);
    }

    @Override
    public IbosReference save(IbosReference object) {
        return ibosReferenceRepository.save(object);
    }

    @Override
    public List<IbosReference> saveAll(List<IbosReference> objects) {
        return ibosReferenceRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        ibosReferenceRepository.deleteById(id);
    }

    @Override
    public List<IbosReference> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<IbosReference> findAllByIbosId(Integer ibosId) {
        return ibosReferenceRepository.findAllByIbosId(ibosId);
    }

    @Override
    public List<IbosReference> findAllByIbosngId(Integer ibosngId) {
        return ibosReferenceRepository.findAllByIbosngId(ibosngId);
    }

    @Override
    public List<IbosReference> findAllByIbosIdAndData(Integer ibosId, String data) {
        return ibosReferenceRepository.findAllByIbosIdAndData(ibosId, data);
    }

    @Override
    public List<IbosReference> findAllByIbosngIdAndData(Integer ibosngId, String data) {
        return ibosReferenceRepository.findAllByIbosngIdAndData(ibosngId, data);
    }

    @Override
    public List<IbosReference> findAllByData(String data) {
        return ibosReferenceRepository.findAllByData(data);
    }

    @Override
    public List<IbosReference> findAllByDataStartingWith(String data) {
        return ibosReferenceRepository.findAllByDataStartingWith(data);
    }
}
