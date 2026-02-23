package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.masterdata.Kommunalsteuergemeinde;
import com.ibosng.dbservice.repositories.masterdata.KommunalsteuergemeindeRepository;
import com.ibosng.dbservice.services.masterdata.KommunalsteuergemeindeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KommunalsteuergemeindeServiceImpl implements KommunalsteuergemeindeService {

    private final KommunalsteuergemeindeRepository kommunalsteuergemeindeRepository;

    public KommunalsteuergemeindeServiceImpl(KommunalsteuergemeindeRepository kommunalsteuergemeindeRepository) {
        this.kommunalsteuergemeindeRepository = kommunalsteuergemeindeRepository;
    }

    @Override
    public List<Kommunalsteuergemeinde> findAll() {
        return kommunalsteuergemeindeRepository.findAll();
    }

    @Override
    public Optional<Kommunalsteuergemeinde> findById(Integer id) {
        return kommunalsteuergemeindeRepository.findById(id);
    }

    @Override
    public Kommunalsteuergemeinde save(Kommunalsteuergemeinde object) {
        return kommunalsteuergemeindeRepository.save(object);
    }

    @Override
    public List<Kommunalsteuergemeinde> saveAll(List<Kommunalsteuergemeinde> objects) {
        return kommunalsteuergemeindeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kommunalsteuergemeindeRepository.deleteById(id);
    }

    @Override
    public List<Kommunalsteuergemeinde> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Kommunalsteuergemeinde findByDienstortPlz(Integer dienstortPlz) {
        return kommunalsteuergemeindeRepository.findByDienstortPlz(dienstortPlz);
    }
}
