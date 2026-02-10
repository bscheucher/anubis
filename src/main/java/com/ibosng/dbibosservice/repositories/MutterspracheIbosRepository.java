package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.MutterspracheIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface MutterspracheIbosRepository extends JpaRepository<MutterspracheIbos, Integer> {
    MutterspracheIbos findByName(String name);
}
