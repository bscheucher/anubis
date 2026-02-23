package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.KollektivvertragIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface KollektivvertragIbosRepository extends JpaRepository<KollektivvertragIbos, Integer> {
    KollektivvertragIbos findByBezeichnung(String bezeichnung);
}
