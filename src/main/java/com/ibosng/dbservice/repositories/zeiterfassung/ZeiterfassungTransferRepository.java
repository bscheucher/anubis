package com.ibosng.dbservice.repositories.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ZeiterfassungTransferRepository extends JpaRepository<ZeiterfassungTransfer, Integer> {

    String FIND_OVERLAPPING_ENTRIES_FOR_SEMINAR = """
                SELECT zt
                FROM ZeiterfassungTransfer zt
                JOIN zt.seminars s
                WHERE s.id IN :seminarIds
                AND zt.datumVon <= :datumBis
                AND zt.datumBis >= :datumVon
                AND zt.status IN :statuses
            """;

    @Query(FIND_OVERLAPPING_ENTRIES_FOR_SEMINAR)
    List<ZeiterfassungTransfer> findOverlappingEntriesForSeminars(
            @Param("seminarIds") List<Integer> seminarIds,
            @Param("datumVon") LocalDate datumVon,
            @Param("datumBis") LocalDate datumBis,
            @Param("statuses") List<ZeiterfassungStatus> statuses
    );

    List<ZeiterfassungTransfer> findAllByStatus(ZeiterfassungStatus status);

    Page<ZeiterfassungTransfer> findAll(Pageable pageable);
}
