package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.ksttn.KsttnId;
import com.ibosng.dbibosservice.entities.ksttn.KsttnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface KsttnStatusRepository extends JpaRepository<KsttnStatus, KsttnId> {
    String FIND_KSTTN_STATUS_BY_TEILNEHME =
            """
                    select `KSTTNkynr`, KSTTNstatus.`TEILNAHMESTATUS_BEREICH_id`, `KSTTNkyindex`, `KSTTNkuerzel`, `KSTTNbez`, `KSTTNloek`, `KSTTNaeda`, `KSTTNaeuser`, `KSTTNerda`, `KSTTNeruser`, `DPWCode` 
                    FROM ibos.KSTTNstatus INNER JOIN `TEILNAHME` on KSTTNkynr = KSTTNstatusV 
                    inner join SEMINAR on SM_TN_SEMINAR_SMnr = SMnr 
                    inner join PROJEKT on SEMINAR.PJnr = PROJEKT.PJnr and KSTTNstatus.TEILNAHMESTATUS_BEREICH_id = PROJEKT.TEILNAHMESTATUS_BEREICH_id
                    where SM_TN_ADRESSE_ADadnr=:adadnr and SM_TN_SEMINAR_SMnr=:smnr and TNdatum=:datum ;
                    """;

    @Query(value = FIND_KSTTN_STATUS_BY_TEILNEHME, nativeQuery = true)
    Optional<KsttnStatus> findByKsttnkynrAndTeilnahmestatusBereichId(Integer adadnr, Integer smnr, LocalDate datum);

    KsttnStatus findAllByIdKsttnkynr(Integer idKsttnkynr);

}
