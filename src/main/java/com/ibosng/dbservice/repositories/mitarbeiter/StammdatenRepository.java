package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface StammdatenRepository extends JpaRepository<Stammdaten, Integer>, StammdatenRepositoryExtended, JpaSpecificationExecutor<Stammdaten> {

    Stammdaten findByPersonalnummer(Personalnummer personalnummer);

    Page<Stammdaten> findAll(Pageable pageable);

    List<Stammdaten> findAllByCreatedOnAfterOrChangedOnAfter(LocalDateTime createdOn, LocalDateTime changedOn);

    List<Stammdaten> findAllByVornameAndNachname(String vorname, String nachname);

    List<Stammdaten> findAllByEmail(String email);

    Stammdaten findByPersonalnummer_PersonalnummerAndStatusIn(String personalnummer, List<MitarbeiterStatus> status);

    Stammdaten findByPersonalnummerAndStatusIn(Personalnummer personalnummer, List<MitarbeiterStatus> status);

    Stammdaten findByPersonalnummer_Id(Integer personalnummerId);
}
