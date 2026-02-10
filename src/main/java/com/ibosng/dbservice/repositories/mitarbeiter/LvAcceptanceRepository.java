package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.LvAcceptance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface LvAcceptanceRepository extends JpaRepository<LvAcceptance, Integer> {

    List<LvAcceptance> findByPersonalnummer(Personalnummer personalnummer);
}
