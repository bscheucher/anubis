package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.FKostenstelleIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface FKostenstelleIbosRepository extends JpaRepository<FKostenstelleIbos, Integer> {
    List<FKostenstelleIbos> findAllByIdKstKstGr(Integer kstgr);

    List<FKostenstelleIbos> findAllByIdKstKstNr(Integer kstnr);
}
