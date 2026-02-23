package com.ibosng.dbservice.repositories.zeitbuchung;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungstyp;
import com.ibosng.dbservice.entities.zeitbuchung.MoxisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface LeistungserfassungRepository extends JpaRepository<Leistungserfassung, Integer> {
    String OVERLAPPING_ENTRIES = """
            SELECT DISTINCT t1.leistungsdatum as "result"
                 FROM (select z.id as "id", leistungsdatum, von, bis, l.personalnummer FROM zeitbuchung z JOIN leistungserfassung l ON z.leistungserfassung = l.id) as t1
                 JOIN (select z.id as "id", leistungsdatum, von, bis, l.personalnummer FROM zeitbuchung z JOIN leistungserfassung l ON z.leistungserfassung = l.id) as t2 
                 	ON t1.leistungsdatum = t2.leistungsdatum
            		AND t1.leistungsdatum between :von and :bis
            		AND t1.personalnummer = t2.personalnummer
                    AND t1.von < t2.bis
                    AND t1.bis > t2.von
                    AND t1.id <> t2.id
                 WHERE t1.personalnummer = :personalnummerId 
                 ORDER BY 1
            """;

    String FIND_MOTNHS_TO_BE_SEND_TO_LHR = """
            SELECT l FROM Leistungserfassung l 
            WHERE l.personalnummer.id = :personalnummerId 
            AND l.leistungsdatum BETWEEN :leistungsdatumAfter AND :leistungsdatumBefore 
            AND (l.isLocked IS NULL OR l.isLocked = FALSE) 
            AND l.moxisStatus = :moxisStatus
            """;

    Optional<Leistungserfassung> findFirstByLeistungstypAndLeistungsdatumAndPersonalnummer(Leistungstyp leistungstyp, LocalDate leistungsdatum, Personalnummer personalnummer);

    List<Leistungserfassung> findByPersonalnummer_IdAndLeistungsdatumBetween(Integer personalnummerId, LocalDate leistungsdatumStart, LocalDate leistungsdatumEnd);

    List<Leistungserfassung> findByPersonalnummer_IdAndLeistungsdatum(Integer personalnummerId, LocalDate leistungsdatum);

    List<Leistungserfassung> findByPersonalnummer_IdAndLeistungsdatumAndLeistungstyp(Integer personalnummerId, LocalDate leistungsdatum, Leistungstyp leistungstyp);

    @Query(FIND_MOTNHS_TO_BE_SEND_TO_LHR)
    List<Leistungserfassung> findByPersonalNummerMonthsToBeSend(Integer personalnummerId, LocalDate leistungsdatumAfter, LocalDate leistungsdatumBefore, MoxisStatus moxisStatus);

    List<Leistungserfassung> findByPersonalnummer_IdAndLeistungsdatumBetweenAndIsSyncedWithLhrTrue(Integer personalnummerId, LocalDate leistungsdatumStart, LocalDate leistungsdatumEnd);

    List<Leistungserfassung> findByPersonalnummer_IdAndLeistungsdatumBetweenAndIsSyncedWithLhrNullOrIsSyncedWithLhrFalse(Integer personalnummerId, LocalDate leistungsdatumStart, LocalDate leistungsdatumEnd);

    @Query("SELECT l FROM Leistungserfassung l WHERE l.leistungsdatum BETWEEN :startDate AND :endDate AND (l.moxisStatus IS NULL OR l.moxisStatus <> :successStatus)")
    List<Leistungserfassung> findLeistungserfassungInPeriodAndNotInStatus(LocalDate startDate, LocalDate endDate, MoxisStatus successStatus);

    @Query(value = OVERLAPPING_ENTRIES, nativeQuery = true)
    List<java.sql.Date> findOverlapingInPeriod(Integer personalnummerId, LocalDate von, LocalDate bis);

    boolean existsByPersonalnummer_IdAndPersonalnummer_Firma_BmdClientAndLeistungsdatumBetweenAndMoxisStatusIn(Integer personalnummerId, Integer bmdClient, LocalDate leistungsdatumStart, LocalDate leistungsdatumEnd, Collection<MoxisStatus> moxisStatuses);

    boolean existsByPersonalnummer_IdAndPersonalnummer_Firma_BmdClientAndLeistungsdatumBetweenAndIsLocked(Integer personalnummerId, Integer bmdClient, LocalDate startDate, LocalDate endDate, boolean b);

    List<Leistungserfassung> findByPersonalnummerAndLeistungsdatumBetween(Personalnummer personalnummer, LocalDate leistungsdatumStart, LocalDate leistungsdatumEnd);

    List<Leistungserfassung> findByPersonalnummerAndLeistungsdatum(Personalnummer personalnummer, LocalDate leistungsdatum);

    List<Leistungserfassung> findByIsSyncedWithLhrNullOrIsSyncedWithLhrFalse();
}
