package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface VertragsdatenRepository extends JpaRepository<Vertragsdaten, Integer> {
    @Query("select ver.eintritt from Vertragsdaten ver where ver.personalnummer.personalnummer = :personalnummer and ver.eintritt IS NOT NULL")
    LocalDate findEintrittByPersonalnummer(String personalnummer);

    List<Vertragsdaten> findAllByCreatedOnAfterOrChangedOnAfter(LocalDateTime createdOn, LocalDateTime changedOn);

    Vertragsdaten findFirstByPersonalnummer_PersonalnummerOrderByCreatedOnDescChangedOnDesc(String personalnummer);

    List<Vertragsdaten> findByPersonalnummer_Personalnummer(String personalnummer);

    List<Vertragsdaten> findByPersonalnummer_PersonalnummerAndStatusIn(String personalnummer, Collection<MitarbeiterStatus> statuses);

    List<Vertragsdaten> findByPersonalnummer_IdAndStatusIn(Integer personalnummer, Collection<MitarbeiterStatus> statuses);

    List<Vertragsdaten> findAllByFuehrungskraftIsNullAndFuehrungskraftRefIsNotNull();

    List<Vertragsdaten> findAllByStartcoachIsNullAndStartcoachRefIsNotNull();

    @Query("Select v from Vertragsdaten v where v.personalnummer.personalnummer = :personalnummer and v.status in :statuses and v.id not in (select va.successor.id from Vertragsaenderung va WHERE va.successor IS NOT NULL)")
    List<Vertragsdaten> findAllByPNAndStatusesNotInVertragsdatenaenderungen(String personalnummer, List<MitarbeiterStatus> statuses);

    @Query("Select v from Vertragsdaten v where v.personalnummer = :personalnummer and v.status in :statuses and v.id not in (select va.successor.id from Vertragsaenderung va WHERE va.successor IS NOT NULL)")
    List<Vertragsdaten> findAllByPNAndStatusesNotInVertragsdatenaenderungen(Personalnummer personalnummer, List<MitarbeiterStatus> statuses);

    List<Vertragsdaten> findAllByPersonalnummer(Personalnummer personalnummer);
}
