package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.SprachkenntnisNiveau;
import com.ibosng.dbservice.repositories.masterdata.SprachkenntnisNiveauRepository;
import com.ibosng.dbservice.services.masterdata.SprachkenntnisNiveauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SprachkenntnisNiveauServiceImpl implements SprachkenntnisNiveauService {

    private final SprachkenntnisNiveauRepository sprachkenntnisNiveauRepository;

    @Override
    public List<SprachkenntnisNiveau> findAll() {
        return sprachkenntnisNiveauRepository.findAll();
    }

    @Override
    public Optional<SprachkenntnisNiveau> findById(Integer id) {
        return sprachkenntnisNiveauRepository.findById(id);
    }

    @Override
    public SprachkenntnisNiveau save(SprachkenntnisNiveau object) {
        return sprachkenntnisNiveauRepository.save(object);
    }

    @Override
    public List<SprachkenntnisNiveau> saveAll(List<SprachkenntnisNiveau> objects) {
        return sprachkenntnisNiveauRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        sprachkenntnisNiveauRepository.deleteById(id);
    }

    @Override
    public List<SprachkenntnisNiveau> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public SprachkenntnisNiveau findByName(String name) {
        return sprachkenntnisNiveauRepository.findByName(name);
    }
}
