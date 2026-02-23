package com.ibosng.dbservice.services.urlaub;

import com.ibosng.dbservice.dtos.UrlaubsdatenDto;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UrlaubsdatenService extends BaseService<Urlaubsdaten> {
    boolean isExists(Integer personalnummerId, Integer anspuruchLhr, LocalDate month, LocalDate from, LocalDate nextAnspuruch);

    Urlaubsdaten findUrlaubsdaten(Integer personalnummerId, Integer anspuruchLhr, LocalDate month, LocalDate from, LocalDate nextAnspuruch);

    List<Urlaubsdaten> findByMonth(Integer personalnummerId, LocalDate month);

    List<Urlaubsdaten> findByPersonalnummerInPeriod(Integer personalnummerId, LocalDate monthStart, LocalDate monthEnd);

    List<Urlaubsdaten> findByPersonalnummerInPeriodAndAnspruchType(Integer personalnummerId, LocalDate month, LocalDate monthEnd, String anspruch);

    UrlaubsdatenDto mapToDto(Urlaubsdaten urlaubsdaten);

    Optional<Urlaubsdaten> findUrlaubsdatenByPersonalnummerMonth(Integer personalnummerId, LocalDate month);
}
