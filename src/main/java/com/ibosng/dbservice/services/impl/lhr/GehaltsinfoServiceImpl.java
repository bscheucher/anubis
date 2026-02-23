package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.entities.lhr.Gehaltsinfo;
import com.ibosng.dbservice.repositories.lhr.GehaltsinfoRepository;
import com.ibosng.dbservice.services.lhr.GehaltsinfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GehaltsinfoServiceImpl implements GehaltsinfoService {

    private final GehaltsinfoRepository gehaltsinfoRepository;

    public GehaltsinfoServiceImpl(GehaltsinfoRepository gehaltsinfoRepository) {
        this.gehaltsinfoRepository = gehaltsinfoRepository;
    }

    @Override
    public List<Gehaltsinfo> findAll() {
        return gehaltsinfoRepository.findAll();
    }

    @Override
    public Optional<Gehaltsinfo> findById(Integer id) {
        return gehaltsinfoRepository.findById(id);
    }

    @Override
    public Gehaltsinfo save(Gehaltsinfo object) {
        return gehaltsinfoRepository.save(object);
    }

    @Override
    public List<Gehaltsinfo> saveAll(List<Gehaltsinfo> objects) {
        return gehaltsinfoRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        gehaltsinfoRepository.deleteById(id);
    }

    @Override
    public List<Gehaltsinfo> findAllByIdentifier(String identifier) {
        return null;
    }
}
