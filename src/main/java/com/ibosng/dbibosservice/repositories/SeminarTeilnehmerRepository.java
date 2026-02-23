package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.entities.smtn.SmTnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface SeminarTeilnehmerRepository extends JpaRepository<SeminarTeilnehmerIbos, SmTnId> {

    String FIND_SEMINAR_IDS_FOR_ADRESSE = """ 
            Select * from SM_TN where ADRESSE_ADadnr = :adresseNr""";

    String FIND_SM_TN_FOR_ADRESSE_AND_SEMINAR = """ 
            Select * from SM_TN where ADRESSE_ADadnr = :adresseNr and SEMINAR_SMnr = :seminarNr""";

    @Query(value = FIND_SEMINAR_IDS_FOR_ADRESSE, nativeQuery = true)
    List<SeminarTeilnehmerIbos> findByAdresseNr(Integer adresseNr);

    @Query(value = FIND_SM_TN_FOR_ADRESSE_AND_SEMINAR, nativeQuery = true)
    SeminarTeilnehmerIbos findByAdresseAndSeminar(Integer adresseNr, Integer seminarNr);

    //List<AdresseIbos> findAllByAderuserAndAderdaAfterAndAdtypOrAderuserAndAdaedaAfterAndAdtyp(String eruser1, LocalDateTime timeErda, String adtyp1, String eruser2, LocalDateTime timeAeda, String adtyp2);

    List<SeminarTeilnehmerIbos> findAllByTaeruserAndTaerdaAfterOrTaeruserAndTaaeda(String taeruser, LocalDateTime taerdaAfter, String taeruser1, LocalDateTime taaeda);

}
