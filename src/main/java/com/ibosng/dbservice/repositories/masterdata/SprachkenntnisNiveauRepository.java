package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.SprachkenntnisNiveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface SprachkenntnisNiveauRepository extends JpaRepository<SprachkenntnisNiveau, Integer> {

    SprachkenntnisNiveau findByName(String name);
}
