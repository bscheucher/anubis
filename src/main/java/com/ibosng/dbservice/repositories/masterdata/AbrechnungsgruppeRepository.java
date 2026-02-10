package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.masterdata.Abrechnungsgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface AbrechnungsgruppeRepository extends JpaRepository<Abrechnungsgruppe, Integer> {

    Optional<Abrechnungsgruppe> findByAbbreviation(String abbreviation);

    List<Abrechnungsgruppe> findByBezeichnung(String abbreviation);
    List<Abrechnungsgruppe> findAllByAbbreviationAndBezeichnung(String abbreviation, String bezeichnung);

}
