package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.GrAdIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface GrAdIbosRepository extends JpaRepository<GrAdIbos, Integer> {

    GrAdIbos findAllByAdresseAdadnr(Integer adresseAdadnr);

}
