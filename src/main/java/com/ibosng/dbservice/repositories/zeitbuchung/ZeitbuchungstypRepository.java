package com.ibosng.dbservice.repositories.zeitbuchung;

import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchungstyp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface ZeitbuchungstypRepository extends JpaRepository<Zeitbuchungstyp, Integer> {
    Optional<Zeitbuchungstyp> findByType(String type);

    Optional<Zeitbuchungstyp> findFirstByTypeIgnoreCase(String type);
}
