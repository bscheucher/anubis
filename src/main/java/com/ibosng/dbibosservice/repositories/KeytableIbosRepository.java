package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.KeytableIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface KeytableIbosRepository extends JpaRepository<KeytableIbos, Integer> {
    List<KeytableIbos> findAllByKyNameAndKyValueT1(String kyName, String kyValuet1);
    List<KeytableIbos> findAllByKyNameAndKyNrOrderByKyIndexAsc(String kyName, int kyNr);
    Optional<KeytableIbos> findByKyNr(Integer kyNr);
}
