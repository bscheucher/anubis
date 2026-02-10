package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.SeminarbuchIbos;
import com.ibosng.dbibosservice.repositories.SeminarbuchIbosRepository;
import com.ibosng.dbibosservice.services.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeminarbuchIbosServiceImpl implements BaseService<SeminarbuchIbos> {

    private final SeminarbuchIbosRepository seminarbuchIbosRepository;

    public SeminarbuchIbosServiceImpl(SeminarbuchIbosRepository seminarbuchIbosRepository) {
        this.seminarbuchIbosRepository = seminarbuchIbosRepository;
    }

    @Override
    public List<SeminarbuchIbos> findAll() {
        return seminarbuchIbosRepository.findAll();
    }

    @Override
    public Optional<SeminarbuchIbos> findById(Integer id) {
        return seminarbuchIbosRepository.findById(id);
    }

    @Override
    public SeminarbuchIbos save(SeminarbuchIbos object) {
        return seminarbuchIbosRepository.save(object);
    }

    @Override
    public List<SeminarbuchIbos> saveAll(List<SeminarbuchIbos> objects) {
        return seminarbuchIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarbuchIbosRepository.deleteById(id);

    }
}
