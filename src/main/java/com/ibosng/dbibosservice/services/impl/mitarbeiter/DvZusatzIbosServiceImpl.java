package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.DvZusatzIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.DvZusatzIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.DvZusatzIbosService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DvZusatzIbosServiceImpl implements DvZusatzIbosService {

    private final DvZusatzIbosRepository dvZusatzIbosRepository;

    public DvZusatzIbosServiceImpl(DvZusatzIbosRepository dvZusatzIbosRepository) {
        this.dvZusatzIbosRepository = dvZusatzIbosRepository;
    }

    @Override
    public List<DvZusatzIbos> findAll() {
        return dvZusatzIbosRepository.findAll();
    }

    @Override
    public Optional<DvZusatzIbos> findById(Integer id) {
        return dvZusatzIbosRepository.findById(id);
    }

    @Override
    public DvZusatzIbos save(DvZusatzIbos object) {
        return dvZusatzIbosRepository.save(object);
    }

    @Override
    public List<DvZusatzIbos> saveAll(List<DvZusatzIbos> objects) {
        return dvZusatzIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        dvZusatzIbosRepository.deleteById(id);
    }


    @Override
    public List<DvZusatzIbos> findAllByAdAdnr(Integer adAdnr) {
        return dvZusatzIbosRepository.findAllByAdAdnr(adAdnr);
    }

    @Override
    public List<DvZusatzIbos> findAllByAdAdnrAndDvNr(Integer adAdnr, Integer dvNr) {
        return dvZusatzIbosRepository.findAllByAdAdnrAndDvNr(adAdnr, dvNr);
    }

    @Override
    public List<DvZusatzIbos> findAllByDvNr(Integer dvNr) {
        return dvZusatzIbosRepository.findAllByDvNr(dvNr);
    }

    @Override
    public List<DvZusatzIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after) {
        return dvZusatzIbosRepository.findAllByDzEruserAndDzErdaAfterOrDzEruserAndDzAedaAfter(createdBy, after, createdBy, after);
    }
}
