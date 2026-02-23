package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface KostenstelleRepository extends JpaRepository<Kostenstelle, Integer> {

    Kostenstelle findByBezeichnung(String name);

    Kostenstelle findByNummer(Integer nummer);

    List<Kostenstelle> findAllByStatus(Status status);
}
