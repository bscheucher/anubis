package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Projekt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface ProjektRepository extends JpaRepository<Projekt, Integer> {

    String PROJEKT_FIND_ALL_SWITCHABLE = """
            SELECT p.bezeichnung
            FROM projekt p
                     LEFT JOIN projekt_2_manager pm on pm.projekt = p.id
                     LEFT JOIN seminar sem on p.id = sem.project
                     LEFT JOIN teilnehmer_2_seminar t2s on sem.id = t2s.seminar_id
                     LEFT JOIN teilnehmer tn on t2s.teilnehmer_id = tn.id
            --nullable isActive check, yes it works
            WHERE ((:isActive is null) or ((:isActive and (now() BETWEEN p.start_date AND p.end_date)) or
                                           ((not :isActive) and (now() not BETWEEN p.start_date AND p.end_date))))
              --nullable benutzer
              and ((:benutzerId is null) or (pm.manager = :benutzerId))
              AND (:isKorrigieren IS NULL OR tn.status = 2)
              GROUP BY p.projekt_nummer, p.bezeichnung
            ORDER BY p.bezeichnung, p.projekt_nummer;""";

    Projekt findByProjektNummer(Integer projektNummer);

    Projekt findByAuftragNummer(Integer auftragNummer);

    Projekt findByBezeichnung(String bezeichnung);

    @Query(value = PROJEKT_FIND_ALL_SWITCHABLE, nativeQuery = true)
    List<String> findAllByIsActiveAndBenutzerId(@Nullable Boolean isActive, Boolean isKorrigieren, @Nullable Integer benutzerId);

    @Query("SELECT p FROM Projekt p WHERE p.trainerSeminars IS EMPTY")
    List<Projekt> findProjektsWithoutManagers();

    @Query("SELECT pr FROM Projekt pr WHERE (:currentDate BETWEEN pr.startDate AND pr.endDate)")
    List<Projekt> findAllActive(@Param("currentDate") LocalDate currentDate);

    List<Projekt> findAllByProjektNummer(Integer projektNummer);
}
