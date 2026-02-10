package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.Projekt2ManagerDto;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Projekt2Manager;
import com.ibosng.dbservice.repositories.Projekt2ManagerRepository;
import com.ibosng.dbservice.services.Projekt2ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Projekt2ManagerServiceImpl implements Projekt2ManagerService {
    private final Projekt2ManagerRepository projekt2ManagerRepository;

    @Override
    public List<Projekt2Manager> findAll() {
        return projekt2ManagerRepository.findAll();
    }

    @Override
    public Optional<Projekt2Manager> findById(Integer id) {
        return projekt2ManagerRepository.findById(id);
    }

    @Override
    public Projekt2Manager save(Projekt2Manager object) {
        return projekt2ManagerRepository.save(object);
    }

    @Override
    public List<Projekt2Manager> saveAll(List<Projekt2Manager> objects) {
        return projekt2ManagerRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        projekt2ManagerRepository.deleteById(id);
    }

    @Override
    public List<Projekt2Manager> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public List<Projekt2Manager> findByProject(Projekt projekt) {
        return projekt2ManagerRepository.findByProjekt(projekt);
    }

    @Override
    public List<Projekt2Manager> findByProject(Integer projektId) {
        return projekt2ManagerRepository.findByProjekt_Id(projektId);
    }

    @Override
    public List<Projekt2Manager> findByManager(Integer managerId) {
        return projekt2ManagerRepository.findByManager_Id(managerId);
    }

    @Override
    public Projekt2ManagerDto map(Projekt2Manager projekt2Manager) {
        Projekt2ManagerDto projekt2ManagerDto = new Projekt2ManagerDto();
        if (projekt2Manager.getProjekt() != null) {
            projekt2ManagerDto.setProjectId(projekt2Manager.getProjekt().getId());
            projekt2ManagerDto.setBezeichnung(projekt2Manager.getProjekt().getBezeichnung());
        }
        if (projekt2Manager.getEndDate() != null) {
            projekt2ManagerDto.setEndDate(projekt2Manager.getEndDate().format(DateTimeFormatter.ISO_DATE));
        }
        if (projekt2Manager.getStartDate() != null) {
            projekt2ManagerDto.setStartDate(projekt2Manager.getStartDate().format(DateTimeFormatter.ISO_DATE));
        }
        return projekt2ManagerDto;
    }
}
