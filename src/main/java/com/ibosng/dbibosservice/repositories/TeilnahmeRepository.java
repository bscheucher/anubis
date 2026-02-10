package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;
import com.ibosng.dbibosservice.entities.teilnahme.TeilnahmeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface TeilnahmeRepository extends JpaRepository<Teilnahme, TeilnahmeId> {

    String TN_LEHRILINGE_TEILNAHME = """
            SELECT tn.* FROM ibos.TEILNAHME tn
                     inner join SM_TN on SM_TN_ADRESSE_ADadnr = SM_TN.ADRESSE_ADadnr and SM_TN_SEMINAR_SMnr = SM_TN.SEMINAR_SMnr
                     inner join ADRESSE on SM_TN_ADRESSE_ADadnr = ADadnr
                     inner join SEMINAR on SM_TN_SEMINAR_SMnr = SMnr
                     inner join PROJEKT on SEMINAR.PJnr = PROJEKT.PJnr
                     left join UEBA_WIEN_SMTN_ERWEITERUNG on SM_TN_ADRESSE_ADadnr = TeilnehmerId and PROJEKT.PJtyp = '7'
                     left join UEBA_TN_DETAIL on SM_TN_ADRESSE_ADadnr = tnd_k_knr and PROJEKT.PJtyp = '5'
                     and SM_TN_SEMINAR_SMnr = SeminarId
                     where TNdatum >= :datumVon
                     and TNdatum <= :datumBis  and SM_TN_SEMINAR_SMnr = :seminarSmnr and PROJEKT.PJtyp in (5,7,8,9,10);""";

    String TN_SEM_SUMMARY = """
            WITH SeminarCounts AS (
                SELECT SM_TN_SEMINAR_SMnr, COUNT(DISTINCT SM_TN_ADRESSE_ADadnr) AS distinct_count
                FROM TEILNAHME
                inner join KSTTNstatus on  KSTTNstatus.KSTTNkynr = TEILNAHME.KSTTNstatusV and DPWCode is not null
                inner join SM_TN on SM_TN_ADRESSE_ADadnr = SM_TN.ADRESSE_ADadnr and SM_TN_SEMINAR_SMnr = SM_TN.SEMINAR_SMnr
                inner join ADRESSE on SM_TN_ADRESSE_ADadnr = ADadnr
                inner join SEMINAR on SM_TN_SEMINAR_SMnr = SMnr
                inner join PROJEKT on SEMINAR.PJnr = PROJEKT.PJnr
                 left join UEBA_WIEN_SMTN_ERWEITERUNG on SM_TN_ADRESSE_ADadnr = TeilnehmerId and PROJEKT.PJtyp = '7'
                 left join UEBA_TN_DETAIL on SM_TN_ADRESSE_ADadnr = tnd_k_knr and PROJEKT.PJtyp = '5'
                WHERE TNdatum >= :datumVon
                  AND TNdatum <= :datumBis
                  AND SM_TN_SEMINAR_SMnr IN :seminars
                  and PROJEKT.PJtyp in (5,7,8,9,10)
                GROUP BY SM_TN_SEMINAR_SMnr
            )
            
            SELECT
                SUM(distinct_count) AS total_adadnr_count,
                GROUP_CONCAT(SM_TN_SEMINAR_SMnr ORDER BY SM_TN_SEMINAR_SMnr ASC) AS seminars
            FROM SeminarCounts;""";

    String TN_SEM_SUMMARY_V1 = """
            WITH SeminarCounts AS (
                SELECT SM_TN_SEMINAR_SMnr, COUNT(DISTINCT SM_TN_ADRESSE_ADadnr) AS distinct_count
                FROM TEILNAHME
                inner join KSTTNstatus on  KSTTNstatus.KSTTNkynr = TEILNAHME.KSTTNstatusV and KSTTNstatus.KSTTNkynr in :reasons
                inner join SM_TN on SM_TN_ADRESSE_ADadnr = SM_TN.ADRESSE_ADadnr and SM_TN_SEMINAR_SMnr = SM_TN.SEMINAR_SMnr
                inner join ADRESSE on SM_TN_ADRESSE_ADadnr = ADadnr
                inner join SEMINAR on SM_TN_SEMINAR_SMnr = SMnr
                inner join PROJEKT on SEMINAR.PJnr = PROJEKT.PJnr
                 left join UEBA_WIEN_SMTN_ERWEITERUNG on SM_TN_ADRESSE_ADadnr = TeilnehmerId and PROJEKT.PJtyp = '7'
                 left join UEBA_TN_DETAIL on SM_TN_ADRESSE_ADadnr = tnd_k_knr and PROJEKT.PJtyp = '5'
                WHERE TNdatum >= :datumVon
                  AND TNdatum <= :datumBis
                  AND SM_TN_SEMINAR_SMnr IN :seminars
                  and PROJEKT.PJtyp in (5,7,8,9,10)
                GROUP BY SM_TN_SEMINAR_SMnr
            )
            
            SELECT
                SUM(distinct_count) AS total_adadnr_count,
                GROUP_CONCAT(SM_TN_SEMINAR_SMnr ORDER BY SM_TN_SEMINAR_SMnr ASC) AS seminars
            FROM SeminarCounts;""";

    @Query(value = TN_SEM_SUMMARY, nativeQuery = true)
    List<Object[]> findTeilnehmerSeminarSummary(List<Integer> seminars, LocalDate datumVon, LocalDate datumBis);

    @Query(value = TN_SEM_SUMMARY_V1, nativeQuery = true)
    List<Object[]> findTeilnehmerSeminarSummaryV1(List<Integer> seminars, List<Integer> reasons, LocalDate datumVon, LocalDate datumBis);

    @Query(value = TN_LEHRILINGE_TEILNAHME, nativeQuery = true)
    List<Teilnahme> findByIdSeminarSmnrAndIdDatumBetween(Integer seminarSmnr, LocalDate datumVon, LocalDate datumBis);

    List<Teilnahme> findByIdAdresseAdnr(Integer adresseAdnr);

    List<Teilnahme> findByIdAdresseAdnrAndIdSeminarSmnr(Integer adresseAdnr, Integer seminarSmnr);

    Page<Teilnahme> findByIdSeminarSmnr(Integer seminarSmnr, Pageable pageable);

    List<Teilnahme> findByIdSeminarSmnrAndIdDatum(Integer seminarSmnr, Date datum);

    Optional<Teilnahme> findByIdSeminarSmnrAndIdDatumAndIdAdresseAdnr(Integer seminarSmnr, LocalDate datum, Integer adresseAdnr);
}
