package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.KvStufeIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface KvStufeIbosRepository extends JpaRepository<KvStufeIbos, Integer> {
    List<KvStufeIbos> findAllByBezeichnungAndKvVerwendungsgruppeId(String bezeichnung, Integer verwendungsgruppe);
}
