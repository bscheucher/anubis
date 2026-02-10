package com.ibosng.dbservice.repositories.urlaub;

import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlaubsdatenRepository extends JpaRepository<Urlaubsdaten, Integer> {
    boolean existsByPersonalnummer_IdAndAnspruchType_LhrIdAndMonthAndFromAndNextAnspruch(Integer personalnummer, Integer lhrId, LocalDate month, LocalDate from, LocalDate nextAnspruch);

    Urlaubsdaten findFirstByPersonalnummer_IdAndAnspruchType_LhrIdAndMonthAndFromAndNextAnspruch(Integer personalnummer, Integer lhrId, LocalDate month, LocalDate from, LocalDate nextAnspruch);

    List<Urlaubsdaten> findByPersonalnummer_IdAndMonth(Integer personalnummer, LocalDate month);

    List<Urlaubsdaten> findByPersonalnummer_IdAndMonthBetweenOrderByMonthDesc(Integer personalnummer, LocalDate monthStart, LocalDate monthEnd);

    List<Urlaubsdaten> findByPersonalnummer_IdAndMonthBetweenAndAnspruchType_BezeichnungOrderByMonthDesc(Integer personalnummer, LocalDate month, LocalDate monthEnd, String anspruch);

    Optional<Urlaubsdaten> findFirstByPersonalnummer_IdAndMonthOrderByAnspruchType_IdAsc(Integer personalnummerId, LocalDate month);
}
