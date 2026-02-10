package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.DienstvertragIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.DienstvertragIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.DienstvertragIbosService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DienstvertragIbosServiceImpl implements DienstvertragIbosService {

    private final DienstvertragIbosRepository dienstvertragIbosRepository;

    public DienstvertragIbosServiceImpl(DienstvertragIbosRepository dienstvertragIbosRepository) {
        this.dienstvertragIbosRepository = dienstvertragIbosRepository;
    }

    @Override
    public List<DienstvertragIbos> findAll() {
        return dienstvertragIbosRepository.findAll();
    }

    @Override
    public Optional<DienstvertragIbos> findById(Integer id) {
        return dienstvertragIbosRepository.findById(id);
    }

    @Override
    public DienstvertragIbos save(DienstvertragIbos object) {
        return dienstvertragIbosRepository.save(object);
    }

    @Override
    public List<DienstvertragIbos> saveAll(List<DienstvertragIbos> objects) {
        return dienstvertragIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        dienstvertragIbosRepository.deleteById(id);
    }

    @Override
    public List<DienstvertragIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId) {
        return null;
    }

    @Override
    public List<DienstvertragIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after) {
        return dienstvertragIbosRepository.findAllByDvEruserAndDvErdaAfterOrDvEruserAndDvAedaAfter(createdBy, after, createdBy, after);
    }
}
