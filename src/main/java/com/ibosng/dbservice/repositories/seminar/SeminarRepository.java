package com.ibosng.dbservice.repositories.seminar;

import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface SeminarRepository extends JpaRepository<Seminar, Integer>, SeminarRepositoryExtended {

    String FIND_ALL_SEMINARS_FROM_PROJECT_AND_ACTIVE = """
                        SELECT sem.bezeichnung
                        FROM seminar sem
                        LEFT JOIN projekt pr ON sem.project = pr.id
                        LEFT JOIN seminar_2_trainer s2t ON s2t.seminar_id = sem.id
                        LEFT JOIN benutzer ben ON s2t.trainer_id = ben.id
                        LEFT JOIN teilnehmer_2_seminar t2s on sem.id = t2s.seminar_id
                        LEFT JOIN teilnehmer tn on t2s.teilnehmer_id = tn.id
                        WHERE (:projectName IS NULL OR LOWER(pr.bezeichnung) = LOWER(:projectName))
                          AND (:isActive IS NULL OR sem.status = CASE WHEN :isActive THEN 1 ELSE 2 END)
                          AND (
                              (:isActive IS NULL)
                              OR (:isActive = true AND current_date BETWEEN sem.start_date AND sem.end_date)
                              OR (:isActive = false AND sem.start_date < current_date AND sem.end_date > current_date)
                          )
                          AND (:benutzerId IS NULL OR ben.id = :benutzerId)
                        AND (:isKorrigieren IS NULL OR tn.status = 2)
                        GROUP BY sem.seminar_nummer, sem.bezeichnung
                          ORDER BY sem.bezeichnung, sem.seminar_nummer\
            """;

    List<Seminar> findAllByIdentifier(String identifier);

    Seminar findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTimeAndEndTime(String identifier, SeminarType seminarType, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);

    Seminar findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTime(String identifier, SeminarType seminarType, LocalDate startDate, LocalDate endDate, LocalTime startTime);

    List<Seminar> findByBezeichnung(String bezeichnung);

    List<Seminar> findAllBySeminarNummer(Integer seminarNummer);

    Page<Seminar> findAllByStatus(Status status, Pageable pageable);

    Integer getCountSeminarByStatus(Status status);

    Page<Seminar> findAll(Pageable pageable);

    @Query("SELECT s FROM Seminar s WHERE (s.status = 1) and (:currentDate BETWEEN s.startDate AND s.endDate)")
    List<Seminar> findAllActive(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Seminar s WHERE (s.status = 2) and (:currentDate NOT BETWEEN s.startDate AND s.endDate)")
    List<Seminar> findAllInactive(@Param("currentDate") LocalDate currentDate);

    List<Seminar> findAllByProjectBezeichnung(String bezeichnung);

    @Query("SELECT s FROM Seminar s WHERE (s.status = 1) and (:currentDate BETWEEN s.startDate AND s.endDate) " +
            "and (s.project.bezeichnung = :bezeichnung)")
    List<Seminar> findAllActiveByProjektBezeichnung(@Param("currentDate") LocalDate currentDate, @Param("bezeichnung") String bezeichnung);

    @Query("SELECT s FROM Seminar s WHERE (s.status = 2) and (:currentDate NOT BETWEEN s.startDate AND s.endDate) " +
            "and (s.project.bezeichnung = :bezeichnung)")
    List<Seminar> findAllInactiveByProjektBezeichnung(@Param("currentDate") LocalDate currentDate, @Param("bezeichnung") String bezeichnung);

    @Query("SELECT new com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto(s.id, s.seminarNummer,  s.bezeichnung) FROM Seminar s WHERE s.project.projektType.name IN :projektTypeNames")
    Page<BasicSeminarDto> findAllSeminarsFromProjectAndIsUeba(Pageable pageable, List<String> projektTypeNames);

    @Query("SELECT new com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto(s.id, s.seminarNummer,  s.bezeichnung) FROM Seminar s")
    Page<BasicSeminarDto> findAllBasicSeminarDtos(Pageable pageable);

    @Query(nativeQuery = true, value = FIND_ALL_SEMINARS_FROM_PROJECT_AND_ACTIVE)
    List<String> findAllSeminarsFromProjectAndActive(String projectName, Boolean isActive, Boolean isKorrigieren, Integer benutzerId);

    @Query("SELECT s FROM Seminar s WHERE s.trainerSeminars IS EMPTY")
    List<Seminar> findSeminarsWithoutTrainers();
}
