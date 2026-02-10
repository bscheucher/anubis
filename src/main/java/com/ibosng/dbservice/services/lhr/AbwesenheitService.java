package com.ibosng.dbservice.services.lhr;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.urlaubsdaten.AbwesenheitDetailedDto;
import com.ibosng.dbservice.dtos.urlaubsdaten.AbwesenheitOverviewDto;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.services.BaseService;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AbwesenheitService extends BaseService<Abwesenheit> {
    Optional<Abwesenheit> findByIdAndForceRefresh(Integer id);

    AbwesenheitDto mapToAbwesenheitDto(Abwesenheit abwesenheit);

    List<Abwesenheit> findAllByPersonalnummer(Integer personalnummerId);

    List<Abwesenheit> findAllByFuehrungskraefteId(Integer benutzerId);

    Abwesenheit findByPersonalnummerVonAndBis(Integer personalnummerId, LocalDate von, LocalDate bis);

    AbwesenheitOverviewDto formUrlaubOverview(Integer personalnummerId, LocalDate von, LocalDate bis, Pageable pageable);

    long countByPersonalnummerBeweenVonAndBis(Integer personalnummerId, LocalDate von, LocalDate bis);

    long countByPersonalnummerBetweenVonAndBisAndStatus(Integer personalnummerId, LocalDate von, LocalDate bis, List<AbwesenheitStatus> status);

    long countByPersonalnummerBetweenVonAndBisStatusAndGrunde(Integer personalnummerId, LocalDate von, LocalDate bis, List<AbwesenheitStatus> status, List<String> grunde);

    AbwesenheitDetailedDto mapToAbwesenheitDetailedDto(Abwesenheit abwesenheit);

    Abwesenheit map(AbwesenheitDto abwesenheitDto, AbwesenheitStatus status);

    Abwesenheit map(AbwesenheitDto abwesenheitDto);

    List<Abwesenheit> findAllByFuehrungskraefteIdAndStatusIn(Integer id, List<AbwesenheitStatus> statusList);

    List<Abwesenheit> findFilteredAbwesenheitenByStatusAndYear(Integer personalnummerId, String status, Integer year);

    List<Abwesenheit> findFilteredAbwesenheitenByStatusInPeriod(Integer personalnummerId, List<AbwesenheitStatus> excludedStatuses, String startDate, String endDate);

    List<String> findDistinctYearsByPersonalnummer(Integer personalnummerId);

    List<Abwesenheit> findAllUncalculatedAbwesenheit();

    long calculateBusinessDays(LocalDate startDate, LocalDate endDate, String country);

    List<Abwesenheit> findAbwesenheitBetweenDates(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate);

    List<Abwesenheit> findAbwesenheitBetweenDatesAndStatuses(Integer personalnummer, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> abwesenheitStatuses);

    List<Personalnummer> findAbwesenheitPersonalnummerFromDateAndStatusesIn(LocalDate date, List<AbwesenheitStatus> statuses);
}
