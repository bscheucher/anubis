package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Projekt2Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface Projekt2ManagerRepository extends JpaRepository<Projekt2Manager, Integer> {
    List<Projekt2Manager> findByProjekt(Projekt projekt);

    List<Projekt2Manager> findByProjekt_Id(Integer id);

    List<Projekt2Manager> findByManager_Id(Integer id);
}
