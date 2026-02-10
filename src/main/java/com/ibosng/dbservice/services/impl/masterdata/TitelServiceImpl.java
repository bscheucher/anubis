package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.Titel;
import com.ibosng.dbservice.repositories.masterdata.TitelRepository;
import com.ibosng.dbservice.services.masterdata.TitelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitelServiceImpl implements TitelService {

    private final TitelRepository titelRepository;

    public TitelServiceImpl(TitelRepository titelRepository) {
        this.titelRepository = titelRepository;
    }

    @Override
    public List<Titel> findAll() {
        return titelRepository.findAll();
    }

    @Override
    public Optional<Titel> findById(Integer id) {
        return titelRepository.findById(id);
    }

    @Override
    public Titel save(Titel object) {
        return titelRepository.save(object);
    }

    @Override
    public List<Titel> saveAll(List<Titel> objects) {
        return titelRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        titelRepository.deleteById(id);
    }

    @Override
    public List<Titel> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Titel findByName(String name) {
        List<Titel> titels = titelRepository.findAllByName(name);
        if(titels.size() > 1) {
            return titels.stream().filter(titel -> titel.getName().equals(name)).findFirst().orElse(null);
        } else if(titels.isEmpty()) {
            return null;
        }
        return titels.get(0);
    }
}
