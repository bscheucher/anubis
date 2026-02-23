package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFreiIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ArbeitsvertragZusatzFreiIbosRepository extends JpaRepository<ArbeitsvertragZusatzFreiIbos, Integer> {
    List<ArbeitsvertragZusatzFreiIbos> findAllByArbeitsvertragZusatzId(Integer arbeitstvertragzusatzId);
}
