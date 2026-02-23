package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ZeitausgleichService extends BaseService<Zeitausgleich> {
    ZeitausgleichDto map(Zeitausgleich zeitausgleich);

    Zeitausgleich map(ZeitausgleichDto zeitausgleichDto);

    AbwesenheitDto mapZeitausgleichToAbwesenheitDto(Zeitausgleich zeitausgleich);

    ZeitbuchungenDto mapZeitausgleichToZeitbuchungenDto(Zeitausgleich zeitausgleich);

    List<AbwesenheitDto> mapListZeitausgleichToListAbwesenheitDto(List<Zeitausgleich> zeitausgleich);

    List<ZeitbuchungenDto> mapListZeitausgleichToListZeitbuchungenDto(List<Zeitausgleich> zeitausgleich);

    boolean isExist(Integer personalnummerId, LocalDate datum, LocalTime timeVon, LocalTime timeBis);

    Zeitausgleich findByPersonalnummerAndDateTime(Integer personalnummerId, LocalDate datum, LocalTime timeVon, LocalTime timeBis);

    Zeitausgleich findByPersonalnummerAndDateTimeVon(Integer personalnummerId, LocalDate datum, LocalTime timeVon);

    List<Zeitausgleich> findByPersonalnummerAndDate(Integer personalnummerId, LocalDate date);

    Page<Zeitausgleich> findByPersonalnummerInPeriod(Integer personalnummerIdnummer, LocalDate dateVon, LocalDate dateBis, Pageable pageable);

    List<Zeitausgleich> findByPersonalnummerInPeriod(Integer personalnummerIdnummer, LocalDate dateVon, LocalDate dateBis);

    List<Zeitausgleich> findByPersonalnummerInPeriod(Integer personalnummerId, LocalDate dateVon, LocalDate dateBis, List<AbwesenheitStatus> abwesenheitStatuses);

    List<Zeitausgleich> findAllByFuehrungskraefteId(Integer id);

    List<Zeitausgleich> findAllByPersonalnummerPersonalnummer(Integer personalnummerId);

    /**
     * Searching all zeitausgleich in period by anyZeitausgleichId
     *
     * @param anyZeitausgleichId can be first/last/in between zeitausglech id
     *
     */
    List<Zeitausgleich> findAllZeitausgleichInPeriod(int anyZeitausgleichId);

    List<Zeitausgleich> findAllByIdIn(List<Integer> ids);

    // TODO - replace by a standard no-hack impl
    @Transactional(value = "postgresTransactionManager", propagation = Propagation.REQUIRES_NEW)
    Optional<Zeitausgleich> findByIdAndForceRefresh(Integer id);

    List<Zeitausgleich> findAllByFuehrungskraefteIdAndStatusIn(Integer id, List<AbwesenheitStatus> statusList);

    List<Zeitausgleich> findFilteredZeitausgleichByStatusAndYear(Integer personalnummerId, String status, Integer year);

    List<Zeitausgleich> findFilteredZeitausgleichByStatusInPeriod(Integer personalnummerId, List<AbwesenheitStatus> excludedStatuses, String startDate, String endDate);

    List<String> findDistinctYearsByPersonalnummer(Integer personalnummerId);
}
