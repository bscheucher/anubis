package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.AusschreibungIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface AusschreibungIbosRepository extends JpaRepository<AusschreibungIbos, Integer> {

}

