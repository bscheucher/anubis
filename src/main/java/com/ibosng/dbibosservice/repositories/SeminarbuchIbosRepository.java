package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.SeminarbuchIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface SeminarbuchIbosRepository extends JpaRepository<SeminarbuchIbos, Integer> {

}
