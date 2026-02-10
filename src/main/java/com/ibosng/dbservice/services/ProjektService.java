package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Projekt;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjektService extends BaseService<Projekt> {
    // Add custom methods specific to Projekt entity
    Projekt findByProjektNummer(Integer projektNummer);

    Projekt findByAuftragNummer(Integer auftragNummer);

    List<String> findByStatus(Boolean isActive, Boolean isKorrigieren, Integer benutzerId);

    List<Projekt> findProjektsWithoutManagers();

    List<Projekt> findAllActive(@Param("currentDate") LocalDate currentDate);

    List<Projekt> findAllByProjektNummer(Integer projektNummer);
}
