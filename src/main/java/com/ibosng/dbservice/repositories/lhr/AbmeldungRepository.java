package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.Abmeldung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface AbmeldungRepository extends JpaRepository<Abmeldung, Integer> {

    Abmeldung findByPersonalnummerId(Integer personalnummer);

    Page<Abmeldung> findAll(Pageable pageable);

    Abmeldung findAllBySvNummer(String svNummer);
}
