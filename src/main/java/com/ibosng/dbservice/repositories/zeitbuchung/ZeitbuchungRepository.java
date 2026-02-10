package com.ibosng.dbservice.repositories.zeitbuchung;

import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungsort;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface ZeitbuchungRepository extends JpaRepository<Zeitbuchung, Integer> {
    List<Zeitbuchung> findAllByLeistungserfassung(Leistungserfassung leistungserfassung);

    Optional<Zeitbuchung> findFirstByVonAndBisAndAnAbwesenheitAndLeistungsortAndSeminar_IdAndZeitbuchungstyp_IdAndLeistungserfassung_IdAndKostenstelle_Id(LocalTime von, LocalTime bis, Boolean anAbwesenheit, Leistungsort leistungsort, Integer seminarId, Integer zeitbuchuntypId, Integer leistungserfassungId, Integer kostenstelleId);


    @Query(value = """
            SELECT zet FROM Zeitbuchung zet
            JOIN zet.leistungserfassung ls
            JOIN ls.personalnummer pn
            WHERE pn.id = :personalnummerId
                        AND zet.anAbwesenheit = :isAnAbwesenheit
            AND ls.leistungsdatum BETWEEN :startDate AND :endDate
            """)
    List<Zeitbuchung> findZeitbuchungenInPeriodAndAnAbwesenheit(Integer personalnummerId, LocalDate startDate, LocalDate endDate, Boolean isAnAbwesenheit);
}
