package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.InternationalPlz;
import com.ibosng.dbservice.repositories.InternationalPlzRepository;
import com.ibosng.dbservice.services.InternationalPlzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternationalPlzServiceImpl implements InternationalPlzService {

    private final InternationalPlzRepository internationalPlzRepository;

    @Override
    public List<String> getAllPlz() {
        return internationalPlzRepository.getAllPlz();
    }

    @Override
    public List<String> getAllOrt() {
        return internationalPlzRepository.getAllOrt();
    }

    @Override
    public List<InternationalPlz> findByPlz(String plz) {
        return internationalPlzRepository.findByPlz(plz);
    }

    @Override
    public List<String> findOrtByPlz(String plz) {
        return internationalPlzRepository.findOrtByPlz(plz);
    }

    @Override
    public String findPlzByOrt(String ort) {
        return internationalPlzRepository.findPlzByOrt(ort);
    }

    @Override
    public Optional<InternationalPlz> findPlzByPlzOrtLand(String plz, String ort, Integer land) {
        return internationalPlzRepository.findPlzByPlzOrtLand(plz, ort, land);
    }

    @Override
    public List<InternationalPlz> findAll() {
        return internationalPlzRepository.findAll();
    }

    @Override
    public Optional<InternationalPlz> findById(Integer id) {
        return internationalPlzRepository.findById(id);
    }

    @Override
    public InternationalPlz save(InternationalPlz object) {
        return internationalPlzRepository.save(object);
    }

    @Override
    public List<InternationalPlz> saveAll(List<InternationalPlz> objects) {
        return internationalPlzRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        internationalPlzRepository.deleteById(id);
    }

    @Override
    public List<InternationalPlz> findAllByIdentifier(String identifier) {
        return List.of();
    }
}
