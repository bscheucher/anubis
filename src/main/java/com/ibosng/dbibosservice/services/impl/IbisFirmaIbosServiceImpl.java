package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.IbisFirmaIbos;
import com.ibosng.dbibosservice.repositories.IbisFirmaIbosRepository;
import com.ibosng.dbibosservice.services.IbisFirmaIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IbisFirmaIbosServiceImpl implements IbisFirmaIbosService {

    private final IbisFirmaIbosRepository ibisFirmaIbosRepository;

    public IbisFirmaIbosServiceImpl(IbisFirmaIbosRepository ibisFirmaIbosRepository) {
        this.ibisFirmaIbosRepository = ibisFirmaIbosRepository;
    }

    @Override
    public List<IbisFirmaIbos> findAll() {
        return ibisFirmaIbosRepository.findAll();
    }

    @Override
    public Optional<IbisFirmaIbos> findById(Integer id) {
        return ibisFirmaIbosRepository.findById(id);
    }

    @Override
    public IbisFirmaIbos save(IbisFirmaIbos object) {
        return ibisFirmaIbosRepository.save(object);
    }

    @Override
    public List<IbisFirmaIbos> saveAll(List<IbisFirmaIbos> objects) {
        return ibisFirmaIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        ibisFirmaIbosRepository.deleteById(id);
    }


    @Override
    public IbisFirmaIbos findByBmdKlientIdAndLhrKzAndLhrNr(Integer bmdKlientId, String lhrKz, Integer lhrNr) {
        return ibisFirmaIbosRepository.findByBmdKlientIdAndLhrKzAndLhrNr(bmdKlientId, lhrKz, lhrNr);
    }
}