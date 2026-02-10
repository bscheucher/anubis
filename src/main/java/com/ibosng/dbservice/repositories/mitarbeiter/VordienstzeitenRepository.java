package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface VordienstzeitenRepository extends JpaRepository<Vordienstzeiten, Integer> {
    String FIND_BY_PERSONALNUMMER = "SELECT vdz FROM Vordienstzeiten vdz LEFT JOIN vdz.vertragsdaten v LEFT JOIN v.personalnummer p where p.personalnummer = :personalnummer order by vdz.changedOn desc, vdz.createdOn desc limit 1";

    List<Vordienstzeiten> findAllByVertragsdatenId(Integer vertragsdatenId);

    List<Vordienstzeiten> findAllByCreatedOnAfterOrChangedOnAfter(LocalDateTime createdOn, LocalDateTime changedOn);

    @Query(FIND_BY_PERSONALNUMMER)
    Optional<Vordienstzeiten> findByPersonalnummer(String personalnummer);
}
