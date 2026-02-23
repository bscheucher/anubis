package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Ausschreibung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface AusschreibungRepository extends JpaRepository<Ausschreibung, Integer> {

    Ausschreibung findByAusschreibungNummer(Integer ausschreibungNummer);

    Ausschreibung findByBezeichnung(String bezeichnung);
}
