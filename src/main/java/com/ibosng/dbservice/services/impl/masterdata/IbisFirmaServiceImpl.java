package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.repositories.masterdata.IbisFirmaRepository;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IbisFirmaServiceImpl implements IbisFirmaService {

    private final IbisFirmaRepository ibisFirmaRepository;

    public IbisFirmaServiceImpl(IbisFirmaRepository ibisFirmaRepository) {
        this.ibisFirmaRepository = ibisFirmaRepository;
    }

    @Override
    public List<IbisFirma> findAll() {
        return ibisFirmaRepository.findAll();
    }

    @Override
    public Optional<IbisFirma> findById(Integer id) {
        return ibisFirmaRepository.findById(id);
    }

    @Override
    public IbisFirma save(IbisFirma object) {
        return ibisFirmaRepository.save(object);
    }

    @Override
    public List<IbisFirma> saveAll(List<IbisFirma> objects) {
        return ibisFirmaRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        ibisFirmaRepository.deleteById(id);
    }

    @Override
    public List<IbisFirma> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public IbisFirma findByName(String name) {
        return ibisFirmaRepository.findByName(name);
    }

    @Override
    public IbisFirma findByShortName(String shortName) {
        return ibisFirmaRepository.findByShortName(shortName);
    }

    @Override
    public List<IbisFirma> findAllByStatus(Status status) {
        return ibisFirmaRepository.findAllByStatus(status);
    }

    @Override
    public IbisFirma findByBmdClient(Integer bmdClient) {
        return ibisFirmaRepository.findByBmdClient(bmdClient);
    }
}
