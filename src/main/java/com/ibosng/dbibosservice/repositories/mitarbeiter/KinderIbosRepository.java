package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.KinderIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface KinderIbosRepository extends JpaRepository<KinderIbos, Integer> {
    List<KinderIbos> findAllByAdresseAdnr(Integer adresseAdnr);
}
