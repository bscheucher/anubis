package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.masterdata.IbisFirma2Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface IbisFirma2KostenstelleRepository extends JpaRepository<IbisFirma2Kostenstelle, Integer> {

    @Query("SELECT k.kostenstelle FROM IbisFirma2Kostenstelle k WHERE k.ibisFirma.id = :ibisFirmaId")
    List<Kostenstelle> findKostenstelleByIbisFirmaId(Integer ibisFirmaId);

    @Query("SELECT k.kostenstelle FROM IbisFirma2Kostenstelle k WHERE k.ibisFirma.name = :ibisFirmaName")
    List<Kostenstelle> findKostenstelleByIbisFirmaName(String ibisFirmaName);

    @Query("SELECT k.kostenstelle FROM IbisFirma2Kostenstelle k WHERE k.ibisFirma.name = :ibisFirmaName and k.kostenstelle.bezeichnung = :kostenstelleBezeichnung")
    List<Kostenstelle> findKostenstelleByIbisFirmaNameAndKostenstelleBezeichnung(String ibisFirmaName, String kostenstelleBezeichnung);
}
