package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.BasePlz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {

    Optional<Adresse> findByPlzAndOrtAndStrasse(BasePlz plz, String ort, String strasse);
}
