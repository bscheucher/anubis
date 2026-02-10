package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.ProjektPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ProjektPkRepository extends JpaRepository<ProjektPk, Integer> {
    List<ProjektPk> findByProjektId(Integer projektId);
}
