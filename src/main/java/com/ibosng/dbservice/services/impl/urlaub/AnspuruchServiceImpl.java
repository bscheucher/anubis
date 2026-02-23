package com.ibosng.dbservice.services.impl.urlaub;

import com.ibosng.dbservice.entities.urlaub.Anspruch;
import com.ibosng.dbservice.repositories.urlaub.AnspuruchRepository;
import com.ibosng.dbservice.services.urlaub.AnspuruchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnspuruchServiceImpl implements AnspuruchService {

    private final AnspuruchRepository anspuruchRepository;

    @Override
    public List<Anspruch> findAll() {
        return anspuruchRepository.findAll();
    }

    @Override
    public Optional<Anspruch> findById(Integer id) {
        return anspuruchRepository.findById(id);
    }

    @Override
    public Anspruch save(Anspruch object) {
        return anspuruchRepository.save(object);
    }

    @Override
    public List<Anspruch> saveAll(List<Anspruch> objects) {
        return anspuruchRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        anspuruchRepository.deleteById(id);
    }

    @Override
    public List<Anspruch> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public Anspruch findByLhrId(Integer lhrId) {
        return anspuruchRepository.findByLhrId(lhrId);
    }

    @Override
    public Anspruch findByBezeichnung(String bezeichnung) {
        return anspuruchRepository.findByBezeichnung(bezeichnung);
    }
}
