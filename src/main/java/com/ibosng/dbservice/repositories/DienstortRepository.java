package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Dienstort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface DienstortRepository extends JpaRepository<Dienstort, Integer> {
    List<Dienstort> findAllByName(String name);
}
