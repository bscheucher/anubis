package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Jobbeschreibung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface JobbeschreibungRepository extends JpaRepository<Jobbeschreibung, Integer> {

    Jobbeschreibung findByName(String name);
}
