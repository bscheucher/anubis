package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragFixIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ArbeitsvertragFixIbosRepository extends JpaRepository<ArbeitsvertragFixIbos, Integer> {

    // Define the query string as a constant
    String FIND_SUPERVISORS_QUERY = """
            SELECT DISTINCT ad.ADemail2 AS email
            FROM ARBEITSVERTRAG_FIX avf
            JOIN ADRESSE ad ON ad.ADadnr = avf.vorgesetzter_id
            JOIN ARBEITSVERTRAG_ZUSATZ abzus ON avf.ARBEITSVERTRAG_id = abzus.ARBEITSVERTRAG_id
            WHERE abzus.persnr = :persnr
            """;

    String FIND_SUPERVISORS_QUERY_UPNS = """
            SELECT DISTINCT ben.BNupn AS email
            FROM ARBEITSVERTRAG_FIX avf
            JOIN ADRESSE ad ON ad.ADadnr = avf.vorgesetzter_id
            JOIN BENUTZER ben on ben.BNadnr = ad.ADadnr
            JOIN ARBEITSVERTRAG_ZUSATZ abzus ON avf.ARBEITSVERTRAG_id = abzus.ARBEITSVERTRAG_id
            WHERE abzus.persnr = :persnr
            """;

    // Custom query to retrieve supervisor emails (Führungskraft)
    @Query(value = FIND_SUPERVISORS_QUERY, nativeQuery = true)
    List<String> findFuehrungskraftByPersnr(String persnr);

    @Query(value = FIND_SUPERVISORS_QUERY_UPNS, nativeQuery = true)
    List<String> findFuehrungskraftUPNsByPersnr(String persnr);

    List<ArbeitsvertragFixIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
}
