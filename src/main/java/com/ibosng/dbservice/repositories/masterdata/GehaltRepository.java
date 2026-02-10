package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Gehalt;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface GehaltRepository extends JpaRepository<Gehalt, Integer> {

    List<Gehalt> findAllByKvStufeAndVerwendungsgruppe(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe);
    List<Gehalt> findAllByKvStufeAndVerwendungsgruppeAndStatus(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe, Status status);

    @Query("SELECT g FROM Gehalt g " +
            "WHERE g.kvStufe = :kvStufe " +
            "AND g.verwendungsgruppe = :verwendungsgruppe " +
            "AND g.status = :status " +
            "AND g.gueltigAb <= :date " +
            "AND g.gueltigBis >= :date")
    Gehalt findCurrentByKvStufeAndVerwendungsgruppeAndStatus(KVStufe kvStufe,Verwendungsgruppe verwendungsgruppe, Status status, LocalDate date);
}
