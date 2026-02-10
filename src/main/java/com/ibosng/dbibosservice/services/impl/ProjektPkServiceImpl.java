package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.ProjektPk;
import com.ibosng.dbibosservice.repositories.ProjektPkRepository;
import com.ibosng.dbibosservice.services.ProjektPkService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjektPkServiceImpl implements ProjektPkService {
    private final ProjektPkRepository projektPkRepository;

    @Override
    public List<ProjektPk>  findByProjectId(Integer projektId) {
        return projektPkRepository.findByProjektId(projektId);
    }

    @Override
    public List<ProjektPk> findAll() {
        return projektPkRepository.findAll();
    }

    @Override
    public Optional<ProjektPk> findById(Integer id) {
        return projektPkRepository.findById(id);
    }

    @Override
    public ProjektPk save(ProjektPk object) {
        return projektPkRepository.save(object);
    }

    @Override
    public List<ProjektPk> saveAll(List<ProjektPk> objects) {
        return projektPkRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        projektPkRepository.deleteById(id);
    }
}
