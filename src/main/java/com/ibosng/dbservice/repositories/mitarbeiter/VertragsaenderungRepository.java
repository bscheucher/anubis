package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsaenderung;
import com.ibosng.dbservice.entities.mitarbeiter.VertragsaenderungStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface VertragsaenderungRepository extends JpaRepository<Vertragsaenderung, Integer>, VertragsaenderungRepositoryExtended {
    List<Vertragsaenderung> findAllByPersonalnummer_Personalnummer(String personalnummer);

    List<Vertragsaenderung> findAllByPersonalnummer_PersonalnummerAndStatus(String personalnummer, MitarbeiterStatus status);

    List<Vertragsaenderung> findAllBySuccessor_Id(Integer successorId);

    List<Vertragsaenderung> findAllByPredecessor_Id(Integer predecessorId);

    Vertragsaenderung findBySuccessor_IdAndPredecessor_Id(Integer successorId, Integer predecessorId);

    List<Vertragsaenderung> findByPersonalnummer_PersonalnummerAndStatus(String personalnummer, VertragsaenderungStatus status);
}
