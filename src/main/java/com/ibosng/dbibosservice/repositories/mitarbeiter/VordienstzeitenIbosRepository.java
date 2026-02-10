package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.VordienstzeitenIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface VordienstzeitenIbosRepository extends JpaRepository<VordienstzeitenIbos, Integer> {
    List<VordienstzeitenIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
    List<VordienstzeitenIbos> findAllByPersonalbogenId(Integer personalbogenId);
}
