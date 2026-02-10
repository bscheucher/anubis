package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface IbisFirmaRepository extends JpaRepository<IbisFirma, Integer> {

    IbisFirma findByName(String name);

    IbisFirma findByShortName(String shortName);

    List<IbisFirma> findAllByStatus(Status status);

    IbisFirma findByBmdClient(Integer bmdClient);
}
