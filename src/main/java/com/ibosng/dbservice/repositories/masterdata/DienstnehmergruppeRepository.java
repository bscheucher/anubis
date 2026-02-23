package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.masterdata.Dienstnehmergruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface DienstnehmergruppeRepository extends JpaRepository<Dienstnehmergruppe, Integer> {

    Optional<Dienstnehmergruppe> findByAbbreviation(String abbreviation);

    List<Dienstnehmergruppe> findByBezeichnung(String abbreviation);
    List<Dienstnehmergruppe> findAllByAbbreviationAndBezeichnung(String abbreviation, String bezeichnung);

}
