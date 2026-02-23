package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Klasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface KlasseRepository extends JpaRepository<Klasse, Integer> {

    Klasse findByName(String name);
}
