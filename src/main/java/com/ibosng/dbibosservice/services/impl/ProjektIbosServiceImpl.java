package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.ProjektIbos;
import com.ibosng.dbibosservice.repositories.ProjektIbosRepository;
import com.ibosng.dbibosservice.services.ProjektIbosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjektIbosServiceImpl implements ProjektIbosService {

    private final ProjektIbosRepository projektIbosRepository;

    public ProjektIbosServiceImpl(ProjektIbosRepository projektIbosRepository) {
        this.projektIbosRepository = projektIbosRepository;
    }

    @Override
    public List<ProjektIbos> findAll() {
        return projektIbosRepository.findAll();
    }

    @Override
    public Optional<ProjektIbos> findById(Integer id) {
        return projektIbosRepository.findById(id);
    }

    @Override
    public ProjektIbos save(ProjektIbos object) {
        return projektIbosRepository.save(object);
    }

    @Override
    public List<ProjektIbos> saveAll(List<ProjektIbos> objects) {
        return projektIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        projektIbosRepository.deleteById(id);
    }

    @Override
    public Page<ProjektIbos> findAllChangedAfterPageable(LocalDateTime after, Pageable pageable) {
        return projektIbosRepository.findAllByPjErdaAfterOrPjAedaAfter(after, after, pageable);
    }

    @Override
    public Page<ProjektIbos> findAllCreatedByAndChangedAfterPageable(String createdBy, LocalDateTime after, Pageable pageable) {
        return projektIbosRepository.findAllByPjEruserAndPjErdaOrPjEruserAndPjAeda(createdBy, after, createdBy, after, pageable);
    }

    @Override
    public List<ProjektIbos> fingAllActiveProjektIbos() {
        return projektIbosRepository.fingAllActiveProjektIbos();
    }
}