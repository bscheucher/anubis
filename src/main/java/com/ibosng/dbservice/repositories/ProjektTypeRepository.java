package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.ProjektType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface ProjektTypeRepository extends JpaRepository<ProjektType, Integer> {
    ProjektType findByName(String name);
}
