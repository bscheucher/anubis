package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.StaatenIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface StaatenIbosRepository extends JpaRepository<StaatenIbos, Integer> {
    List<StaatenIbos> findAllByAlpha2AndAlpha3(String alpha2, String alpha3);
}
