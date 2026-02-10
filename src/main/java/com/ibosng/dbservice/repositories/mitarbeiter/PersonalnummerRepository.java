package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface PersonalnummerRepository extends JpaRepository<Personalnummer, Integer> {

    @Query("SELECT MAX(p.nummer) FROM Personalnummer p WHERE p.firma.id = :firmaId")
    Integer findMaxNummerByFirmaId(Integer firmaId);

    Personalnummer findByPersonalnummer(String personalnummer);

    List<Personalnummer> findAllByMitarbeiterType(MitarbeiterType mitarbeiterType);

    List<Personalnummer> findAllByMitarbeiterTypeAndIsIbosngOnboarded(MitarbeiterType mitarbeiterType, Boolean isIbosngOnboarded);

    Personalnummer findByPersonalnummerAndFirma_BmdClient(String personalnummer, Integer bmdClient);
}
