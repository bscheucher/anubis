package com.ibosng.dbservice.services.impl.mitarbeiter.datastatus;




import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.datastatus.StammdatenDataStatusRepository;
import com.ibosng.dbservice.services.mitarbeiter.datastatus.StammdatenDataStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StammdatenDataStatusServiceImpl implements StammdatenDataStatusService {

    private final StammdatenDataStatusRepository stammdatenDataStatusRepository;

    public StammdatenDataStatusServiceImpl(StammdatenDataStatusRepository stammdatenDataStatusRepository) {
        this.stammdatenDataStatusRepository = stammdatenDataStatusRepository;
    }

    @Override
    public List<StammdatenDataStatus> findAll() {
        return stammdatenDataStatusRepository.findAll();
    }

    @Override
    public Optional<StammdatenDataStatus> findById(Integer id) {
        return stammdatenDataStatusRepository.findById(id);
    }

    @Override
    public StammdatenDataStatus save(StammdatenDataStatus object) {
        return stammdatenDataStatusRepository.save(object);
    }

    @Override
    public List<StammdatenDataStatus> saveAll(List<StammdatenDataStatus> objects) {
        return stammdatenDataStatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        stammdatenDataStatusRepository.deleteById(id);
    }

    @Override
    public List<StammdatenDataStatus> findAllByIdentifier(String identifier) {
        return null;
    }

}
