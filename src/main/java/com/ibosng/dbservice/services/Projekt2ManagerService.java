package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.Projekt2ManagerDto;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Projekt2Manager;

import java.util.List;

public interface Projekt2ManagerService extends BaseService<Projekt2Manager> {
    List<Projekt2Manager> findByProject(Projekt projekt);

    List<Projekt2Manager> findByProject(Integer projektId);

    List<Projekt2Manager> findByManager(Integer managerId);

    Projekt2ManagerDto map(Projekt2Manager projekt2Manager);
}
