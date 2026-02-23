package com.ibosng.dbservice.repositories.vereinbarung;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional("postgresTransactionManager")
public interface VereinbarungRepository extends JpaRepository<Vereinbarung, Integer>, JpaSpecificationExecutor<Vereinbarung> {

    List<Vereinbarung> findAllByPersonalnummer(Personalnummer personalnummer);

    Page<Vereinbarung> findAllByStatus(VereinbarungStatus status, Pageable pageable);

    Vereinbarung findVereinbarungByWorkflow_Id(Integer workflowId);
}
