package com.ibosng.dbservice.repositories.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.Auszahlungsantrag;
import com.ibosng.dbservice.entities.zeiterfassung.AuszahlungsantragStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface AuszahlungsantragRepository  extends JpaRepository<Auszahlungsantrag, Integer> {

    List<Auszahlungsantrag> findByStatus(AuszahlungsantragStatus status);

    Optional<Auszahlungsantrag> findByAnfrageNr(Integer anfrageNr);
}
