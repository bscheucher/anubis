package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface SeminarIbosRepository extends JpaRepository<SeminarIbos, Integer>, SeminarIbosRepositoryExtended {

    String FIND_SEMINAR_DATA_QUERY_PK =
            "select sem.PJnr as projektNummer, " +
                    "sem.SMnr as seminarNummer, " +
                    "so.SObezeichnung as ort, " +
                    "sem.SMbezeichnung1 as seminar, " +
                    "sem.SMdatumVon as von, " +
                    "sem.SMdatumBis as bis, " +
                    "sem.SMzeitVon as uhrzeitVon, " +
                    "sem.SMzeitBis as uhrzeitBis " +
                    "from BENUTZER as bn " +
                    "left join SM_AD sm on sm.ADRESSE_ADadnr = bn.bnadnr " +
                    "inner join SEMINAR sem on sm.SEMINAR_SMnr = sem.SMnr and sm.ADRESSE_ADadnr = bn.BNadnr   " +
                    "join STANDORT so on so.soStandortId = sem.SMSOstandortid " +
                    "where sem.SMdatumBis > current_date and bn.bnadSaman = :user";

    @Query(value = FIND_SEMINAR_DATA_QUERY_PK, nativeQuery = true)
    List<Object[]> getSeminarDataRaw(String user);

    List<SeminarIbos> findAllByErdaAfterOrAedaAfter(LocalDateTime timeErda, LocalDateTime timeAeda);

    Page<SeminarIbos> findAllByErdaAfterOrAedaAfter(LocalDateTime timeErda, LocalDateTime timeAeda, Pageable pageable);
    Page<SeminarIbos> findAllByEruserAndErdaAfterOrEruserAndAedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda, Pageable pageable);

    @Query("""
        SELECT s FROM SeminarIbos s
        JOIN ProjektIbos p ON s.pjnr = p.id
        WHERE s.aeda > :aeda
        AND s.datumBis >= :datumBis
        AND (:type IS NULL OR s.type = :type)
    """)
    List<SeminarIbos> findAllByAedaAfterAndDatumBisGreaterThanEqualAndType(
            @Param("aeda") LocalDateTime aeda,
            @Param("datumBis") LocalDate datumBis,
            @Param("type") Integer type
    );

    @Query(value = "SELECT sem FROM SeminarIbos sem where sem.datumVon <= current_date and sem.datumBis >= current_date")
    List<SeminarIbos> fingAllActiveSeminarIbos();

    @Query("SELECT sem FROM SeminarIbos sem where (sem.aeda > :date or sem.erda > :date) and sem.type = :seminarType and sem.datumBis >= :seminarBis")
    List<SeminarIbos> findAllChangedAndActiveSeminars(LocalDateTime date, LocalDate seminarBis,  Integer seminarType);

}
