package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.PzMonat;
import com.ibosng.dbibosservice.entities.PzMonatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface PzMonatRepository extends JpaRepository<PzMonat, PzMonatId> {
    String GET_MOXIS_STATUS = """
            select state from laravel_moxis_leistung_processes
                        inner join laravel_moxis_process_status on laravel_moxis_process_status.id = laravel_moxis_leistung_processes.moxis_process_status_id
                        where constituent_adresse_id = :adAdnr and process_type = 'pep' and laravel_moxis_leistung_processes.year_month = :month 
            and (laravel_moxis_leistung_processes.replaced_by_moxis_leistung_process_id is null 
            or laravel_moxis_leistung_processes.replaced_by_moxis_leistung_process_id = '')
            """;

    PzMonat findById_AdAdnrAndId_JahrAndId_Monat(Integer adAdnr, Integer jahr, Integer monat);

    @Query(value = GET_MOXIS_STATUS, nativeQuery = true)
    String findStatusForMonat(Integer adAdnr, String month);
}
