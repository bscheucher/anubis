package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.repositories.masterdata.KVStufeRepository;
import com.ibosng.dbservice.services.masterdata.KVStufeService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
public class KVStufeServiceImpl implements KVStufeService {

    private final KVStufeRepository kvStufeRepository;

    public KVStufeServiceImpl(KVStufeRepository kvStufeRepository) {
        this.kvStufeRepository = kvStufeRepository;
    }

    @Override
    public List<KVStufe> findAll() {
        return kvStufeRepository.findAll();
    }

    @Override
    public Optional<KVStufe> findById(Integer id) {
        return kvStufeRepository.findById(id);
    }

    @Override
    public KVStufe save(KVStufe object) {
        return kvStufeRepository.save(object);
    }

    @Override
    public List<KVStufe> saveAll(List<KVStufe> objects) {
        return kvStufeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kvStufeRepository.deleteById(id);
    }

    @Override
    public List<KVStufe> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public KVStufe findByTotalMonths(int totalMonths) {
        return kvStufeRepository.findByTotalMonths(totalMonths);
    }

    @Override
    public KVStufe findAllByName(String name) {
        return findFirstObject(kvStufeRepository.findAllByName(name), new HashSet<>(List.of(name)), "KVStufe");
    }
}
