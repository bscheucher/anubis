package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface AbwesenheitRespository extends JpaRepository<Abwesenheit, Integer> {

    String FIND_DISTINCT_YEARS_BY_PERSONALNUMMER = "SELECT DISTINCT YEAR(a.von) FROM Abwesenheit a WHERE a.personalnummer.id = :personalnummerId " +
            "UNION " +
            "SELECT DISTINCT YEAR(a.bis) FROM Abwesenheit a WHERE a.personalnummer.id = :personalnummerId";

    List<Abwesenheit> findAllByFuehrungskraefte_Id(Integer userId);

    List<Abwesenheit> findAllByPersonalnummer_Id(Integer personalnummerId);

    Abwesenheit findFirstByPersonalnummer_IdAndVonAndBis(Integer personalnummerId, LocalDate von, LocalDate bis);

    long countByPersonalnummer_IdAndVonAfterAndBisBefore(Integer personalnummerId, LocalDate von, LocalDate bis);

    List<Abwesenheit> findAllByFuehrungskraefte_IdAndStatusIn(Integer id, List<AbwesenheitStatus> statusList);

    List<Abwesenheit> findAllByPersonalnummer_IdAndStatus(Integer personalnummerId, AbwesenheitStatus status);

    @Query("SELECT a FROM Abwesenheit a WHERE a.personalnummer.id = :personalnummerId AND (YEAR(a.von) = :year OR YEAR(a.bis) = :year)")
    List<Abwesenheit> findAllByPersonalnummerPersonalnummerAndVonYear(Integer personalnummerId, int year);

    @Query("SELECT a FROM Abwesenheit a WHERE a.personalnummer.id = :personalnummerId AND (YEAR(a.von) = :year OR YEAR(a.bis) = :year) AND a.status = :status")
    List<Abwesenheit> findAllByPersonalnummerPersonalnummerAndStatusAndVonYear(Integer personalnummerId, int year, AbwesenheitStatus status);

    @Query("SELECT a FROM Abwesenheit a WHERE a.personalnummer.id = :personalnummerId AND a.status NOT IN :excludedStatuses AND a.von <= :endDate AND a.bis >= :startDate")
    List<Abwesenheit> findAllByPersonalnummerAndStatusInPeriod(Integer personalnummerId, List<AbwesenheitStatus> excludedStatuses, LocalDate startDate, LocalDate endDate);

    @Query(FIND_DISTINCT_YEARS_BY_PERSONALNUMMER)
    List<Integer> findDistinctYearsByPersonalnummer(Integer personalnummerId);

    List<Abwesenheit> findByTageNullOrSaldoNullAndStatusIn(Collection<AbwesenheitStatus> statuses);

    @Query(value = "select a from Abwesenheit a join a.personalnummer pn where a.personalnummer.id = :personalnummerId and a.status in :statuses and a.grund in :grunde and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate))")
    List<Abwesenheit> findByDateRangeGrundAndStatus(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> statuses, List<String> grunde, Pageable pageable);

    @Deprecated
    @Query(value = "select a from Abwesenheit a join a.personalnummer pn where pn.id = :personalnummerId and a.status in :statuses and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate))")
    List<Abwesenheit> findByDateRangeAndStatus(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> statuses, Pageable pageable);

    @Query(value = "select a from Abwesenheit a join a.personalnummer pn where pn.id = :personalnummerId and pn.firma.bmdClient = :bmdClient and a.status in :statuses and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate))")
    List<Abwesenheit> findByDateRangeAndStatus(Integer personalnummerId, Integer bmdClient, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> statuses, Pageable pageable);

    @Query(value = "select a from Abwesenheit a join a.personalnummer pn where pn.id = :personalnummerId and pn.firma.bmdClient = :bmdClient and a.status in :statuses and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate))")
    List<Abwesenheit> findByDateRangeAndStatus(Integer personalnummerId, Integer bmdClient, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> statuses);

    @Query(value = "select a from Abwesenheit a join a.personalnummer pn where pn.id = :personalnummerId and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate))")
    List<Abwesenheit> findAbwesenheitBetweenDates(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate);

    @Query(value = "select a from Abwesenheit a join a.personalnummer pn where a.personalnummer.id = :personalnummerId and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate)) and a.status in :statuses")
    List<Abwesenheit> findAbwesenheitBetweenDatesAndStatusesIn(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> statuses);

    @Query(value = "select distinct(pn) from Abwesenheit a join a.personalnummer pn where a.von > :date and a.status in :statuses")
    List<Personalnummer> findAbwesenheitPersonalnummerFromDateAndStatusesIn(LocalDate date, List<AbwesenheitStatus> statuses);

    @Modifying
    @Query(value = "DELETE FROM abwesenheit_fuehrungskraft WHERE abwesenheit_id = :abwesenheitId", nativeQuery = true)
    void deleteFuehrungskraefteRelations(Integer abwesenheitId);

    @Query("select count(a) from Abwesenheit a join a.personalnummer pn where pn.id = :personalnummerId and a.status in :statuses and ((a.von between :vonDate and :bisDate) or (a.bis between :vonDate and :bisDate) or (a.von <= :vonDate and a.bis >= :bisDate))")
    long countByPersonalnummer_IdAndVonAfterAndBisBeforeAndStatusIn(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate, Collection<AbwesenheitStatus> statuses);

    @Query("select count(a) from Abwesenheit a join a.personalnummer pn where pn.id = :personalnummerId and a.status in :statuses and a.grund in :grunds and ((a.von between :von and :bis) or (a.bis between :von and :bis) or (a.von <= :von and a.bis >= :bis))")
    long countByPersonalnummer_IdAndVonAfterAndBisBeforeAndStatusInAndGrundIn(Integer personalnummerId, LocalDate von, LocalDate bis, Collection<AbwesenheitStatus> statuses, Collection<String> grunds);

    @Modifying
    @Query("UPDATE Abwesenheit a SET a.status = com.ibosng.dbservice.entities.lhr.AbwesenheitStatus.INVALID WHERE a.id = :abwesenheitId")
    void setStatusToInvalid(Integer abwesenheitId);
}
