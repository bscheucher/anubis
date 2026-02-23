package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface ZeitausgleichRepository extends JpaRepository<Zeitausgleich, Integer> {

    String FIND_LAST_ZA = """
             SELECT lz.* 
                    FROM zeitausgleich lz 
                    LEFT JOIN zeitausgleich rz 
                        ON lz.datum = rz.datum - INTERVAL '1 day'
                        AND rz.personalnummer = lz.personalnummer 
                    WHERE lz.datum >= (SELECT datum FROM zeitausgleich WHERE id = :id) 
                      AND lz.personalnummer = (SELECT personalnummer FROM zeitausgleich WHERE id = :id) 
                      AND rz.datum IS NULL 
            		  AND lz."comment" = (SELECT "comment" FROM zeitausgleich WHERE id = :id) 
            		  AND lz.status = (SELECT status FROM zeitausgleich WHERE id = :id) 
                    ORDER BY lz.datum ASC 
                    LIMIT 1;
            """;

    String FIND_FIRST_ZA = """
            SELECT lz.* 
                  FROM zeitausgleich lz 
                  LEFT JOIN zeitausgleich rz 
                      ON lz.datum = rz.datum + INTERVAL '1 day'
                      AND rz.personalnummer = lz.personalnummer 
                  WHERE lz.datum <= (SELECT datum FROM zeitausgleich WHERE id = :id) 
                    AND lz.personalnummer = (SELECT personalnummer FROM zeitausgleich WHERE id = :id) 
                    AND rz.datum IS NULL 
              AND lz."comment" = (SELECT "comment" FROM zeitausgleich WHERE id = :id) 
              AND lz.status = (SELECT status FROM zeitausgleich WHERE id = :id) 
                  ORDER BY lz.datum DESC 
                  LIMIT 1;
            """;

    String GET_PERIOD = """
             select * from zeitausgleich za 
             		where za.datum >= (select z1.datum from zeitausgleich z1 where z1.id = :startId) 
             			and za.datum <= (select z2.datum from zeitausgleich z2 where z2.id = :lastId)
             			and za.personalnummer = (select z1.personalnummer from zeitausgleich z1 where z1.id = :startId)  
             			and za.status = (select z1.status from zeitausgleich z1 where z1.id = :startId);
            """;

    String FIND_DISTINCT_YEARS_BY_PERSONALNUMMER = "SELECT DISTINCT YEAR(z.datum) FROM Zeitausgleich z WHERE z.personalnummer.id = :personalnummerId ";

    boolean existsByPersonalnummer_IdAndDatumAndTimeVonAndTimeBis(Integer personalnummerId, LocalDate datum, LocalTime timeVon, LocalTime timeBis);

    Zeitausgleich findFirstByPersonalnummer_IdAndDatumAndTimeVonAndTimeBis(Integer personalnummerId, LocalDate datum, LocalTime timeVon, LocalTime timeBis);

    Zeitausgleich findByPersonalnummer_IdAndDatumAndTimeVon(Integer personalnummerId, LocalDate datum, LocalTime timeVon);

    List<Zeitausgleich> findByPersonalnummer_IdAndDatum(Integer personalnummer, LocalDate datum);

    Page<Zeitausgleich> findByPersonalnummer_IdAndDatumBetweenOrderByDatumDescCreatedOnDesc(Integer personalnummerId, LocalDate datumStart, LocalDate datumEnd, Pageable pageable);

    List<Zeitausgleich> findByPersonalnummer_IdAndDatumBetweenOrderByDatumDescCreatedOnDesc(Integer personalnummerId, LocalDate datumStart, LocalDate datumEnd);

    List<Zeitausgleich> findAllByFuehrungskraefte_Id(Integer id);

    List<Zeitausgleich> findAllByFuehrungskraefte_IdAndStatusIn(Integer id, List<AbwesenheitStatus> statusList);

    List<Zeitausgleich> findAllByPersonalnummer_Id(Integer personalnummer);

    @Query(value = FIND_FIRST_ZA, nativeQuery = true)
    Zeitausgleich findFirstDayInZeitausgleich(@Param("id") Integer id);

    @Query(value = FIND_LAST_ZA, nativeQuery = true)
    Zeitausgleich findLastDayInZeitausgleich(@Param("id") Integer id);

    @Query(value = GET_PERIOD, nativeQuery = true)
    List<Zeitausgleich> findZeitausgleichInPeriod(int startId, int lastId);

    List<Zeitausgleich> findByPersonalnummer_IdAndDatumBetweenAndStatusIn(Integer personalnummer, LocalDate datumStart, LocalDate datumEnd, Collection<AbwesenheitStatus> statuses);

    List<Zeitausgleich> findAllByIdIn(List<Integer> ids);

    List<Zeitausgleich> findAllByPersonalnummer_IdAndStatus(Integer personalnummer, AbwesenheitStatus status);

    @Query("SELECT z FROM Zeitausgleich z WHERE z.personalnummer.id = :personalnummerId AND YEAR(z.datum) = :year")
    List<Zeitausgleich> findAllByPersonalnummerPersonalnummerAndVonYear(Integer personalnummerId, int year);

    @Query("SELECT z FROM Zeitausgleich z WHERE z.personalnummer.id = :personalnummerId AND YEAR(z.datum) = :year AND z.status = :status")
    List<Zeitausgleich> findAllByPersonalnummerPersonalnummerAndStatusAndVonYear(Integer personalnummerId, int year, AbwesenheitStatus status);

    @Query("SELECT z FROM Zeitausgleich z WHERE z.personalnummer.id = :personalnummerId AND z.status NOT IN :excludedStatuses AND z.datum <= :endDate AND z.datum >= :startDate")
    List<Zeitausgleich> findAllByPersonalnummerAndStatusInPeriod(Integer personalnummerId, List<AbwesenheitStatus> excludedStatuses, LocalDate startDate, LocalDate endDate);

    @Query(FIND_DISTINCT_YEARS_BY_PERSONALNUMMER)
    List<Integer> findDistinctYearsByPersonalnummer(Integer personalnummerId);
}
