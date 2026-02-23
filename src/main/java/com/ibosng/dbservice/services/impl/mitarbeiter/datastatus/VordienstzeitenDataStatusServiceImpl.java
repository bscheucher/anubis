package com.ibosng.dbservice.services.impl.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VordienstzeitenDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.datastatus.VordienstzeitenDataStatusRepository;
import com.ibosng.dbservice.services.mitarbeiter.datastatus.VordienstzeitenDataStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VordienstzeitenDataStatusServiceImpl implements VordienstzeitenDataStatusService {

    private final VordienstzeitenDataStatusRepository vordienstzeitenDataStatusRepository;

    public VordienstzeitenDataStatusServiceImpl(VordienstzeitenDataStatusRepository vordienstzeitenDataStatusRepository) {
        this.vordienstzeitenDataStatusRepository = vordienstzeitenDataStatusRepository;
    }

    @Override
    public List<VordienstzeitenDataStatus> findAll() {
        return vordienstzeitenDataStatusRepository.findAll();
    }

    @Override
    public Optional<VordienstzeitenDataStatus> findById(Integer id) {
        return vordienstzeitenDataStatusRepository.findById(id);
    }

    @Override
    public VordienstzeitenDataStatus save(VordienstzeitenDataStatus object) {
        return vordienstzeitenDataStatusRepository.save(object);
    }

    @Override
    public List<VordienstzeitenDataStatus> saveAll(List<VordienstzeitenDataStatus> objects) {
        return vordienstzeitenDataStatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vordienstzeitenDataStatusRepository.deleteById(id);
    }

    @Override
    public List<VordienstzeitenDataStatus> findAllByIdentifier(String identifier) {
        return null;
    }
    
}
