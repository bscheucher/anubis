package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface GeschlechtRepository extends JpaRepository<Geschlecht, Integer> {

    Geschlecht findByName(String name);
    Geschlecht findByAbbreviation(String abbreviation);
}
