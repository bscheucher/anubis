package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.betreuer.Betreuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface BetreuerRepository extends JpaRepository<Betreuer, Integer> {

    Betreuer findByVorname(String vorname);

    Betreuer findByNachname(String nachname);

    Betreuer findByVornameAndNachname(String vorname, String nachname);
}
