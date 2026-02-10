package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface BenutzerRepository extends JpaRepository<Benutzer, Integer> {
    List<Benutzer> getBenutzerByAzureId(String azureId);

    @Query("SELECT b FROM Benutzer b WHERE b.email ILIKE :email")
    List<Benutzer> findByEmailIgnoreCase(String email);

    List<Benutzer> findAllByFirstNameAndLastName(String firstName, String lastName);

    Benutzer findByPersonalnummer_Personalnummer(String personalnummer);

    Benutzer findByUpnIgnoreCase(String upn);
    Benutzer findByUpnContainingIgnoreCase(String upn);

    Benutzer findByAzureId(String azureId);

    Optional<Benutzer> findFirstByPersonalnummer_PersonalnummerAndPersonalnummer_Firma_BmdClientOrderByCreatedOnDesc(String personalnummer, Integer firma);

    @Query(value = "SELECT * FROM benutzer WHERE LOWER(SPLIT_PART(email, '@', 1)) = :samIbos", nativeQuery = true)
    List<Benutzer> findAllBySamIbosName(String samIbos);

    List<Benutzer> findByPersonalnummer(Personalnummer personalnummer);

    Benutzer findByPersonalnummer_Id(Integer personalnummerId);
}
