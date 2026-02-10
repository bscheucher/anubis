package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.entities.lhr.Erreichbarkeit;
import com.ibosng.dbservice.repositories.lhr.ErreichbarkeitRepository;
import com.ibosng.dbservice.services.lhr.ErreichbarkeitService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
public class ErreichbarkeitServiceImpl implements ErreichbarkeitService {

    private final ErreichbarkeitRepository erreichbarkeitRepository;

    public ErreichbarkeitServiceImpl(ErreichbarkeitRepository erreichbarkeitRepository) {
        this.erreichbarkeitRepository = erreichbarkeitRepository;
    }

    @Override
    public List<Erreichbarkeit> findAll() {
        return erreichbarkeitRepository.findAll();
    }

    @Override
    public Optional<Erreichbarkeit> findById(Integer id) {
        return erreichbarkeitRepository.findById(id);
    }

    @Override
    public Erreichbarkeit save(Erreichbarkeit object) {
        return erreichbarkeitRepository.save(object);
    }

    @Override
    public List<Erreichbarkeit> saveAll(List<Erreichbarkeit> objects) {
        return erreichbarkeitRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        erreichbarkeitRepository.deleteById(id);
    }

    @Override
    public List<Erreichbarkeit> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Erreichbarkeit findByErreichbarkeitsart(String erreichbarkeitsart) {
        return findFirstObject(erreichbarkeitRepository.findAllByErreichbarkeitsart(erreichbarkeitsart), new HashSet<>(List.of(erreichbarkeitsart)), "Erreichbarkeit");
    }

    @Override
    public Erreichbarkeit findByKz(String kz) {
        return findFirstObject(erreichbarkeitRepository.findAllByKz(kz), new HashSet<>(List.of(kz)), "Erreichbarkeit");
    }
}
