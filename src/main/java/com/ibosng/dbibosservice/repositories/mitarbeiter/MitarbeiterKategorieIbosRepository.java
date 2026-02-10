package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.MitarbeiterKategorieIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface MitarbeiterKategorieIbosRepository extends JpaRepository<MitarbeiterKategorieIbos, Integer> {
    List<MitarbeiterKategorieIbos> findAllByBezeichnung(String bezeichnung);
}
