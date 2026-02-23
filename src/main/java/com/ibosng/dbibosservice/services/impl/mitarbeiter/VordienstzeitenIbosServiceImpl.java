package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.VordienstzeitenIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.VordienstzeitenIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.VordienstzeitenIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VordienstzeitenIbosServiceImpl implements VordienstzeitenIbosService {

    private final VordienstzeitenIbosRepository vordienstzeitenIbosRepository;

    public VordienstzeitenIbosServiceImpl(VordienstzeitenIbosRepository vordienstzeitenIbosRepository) {
        this.vordienstzeitenIbosRepository = vordienstzeitenIbosRepository;
    }

    @Override
    public List<VordienstzeitenIbos> findAll() {
        return vordienstzeitenIbosRepository.findAll();
    }

    @Override
    public Optional<VordienstzeitenIbos> findById(Integer id) {
        return vordienstzeitenIbosRepository.findById(id);
    }

    @Override
    public VordienstzeitenIbos save(VordienstzeitenIbos object) {
        return vordienstzeitenIbosRepository.save(object);
    }

    @Override
    public List<VordienstzeitenIbos> saveAll(List<VordienstzeitenIbos> objects) {
        return vordienstzeitenIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vordienstzeitenIbosRepository.deleteById(id);
    }


    @Override
    public List<VordienstzeitenIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId) {
        return vordienstzeitenIbosRepository.findAllByArbeitsvertragId(arbeitsvertragId);
    }

    @Override
    public List<VordienstzeitenIbos> findAllByPersonalbogenId(Integer personalbogenId) {
        return vordienstzeitenIbosRepository.findAllByPersonalbogenId(personalbogenId);
    }
}
