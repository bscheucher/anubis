package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Arbeitszeitmodell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface ArbeitszeitmodellRepository extends JpaRepository<Arbeitszeitmodell, Integer> {

    Arbeitszeitmodell findByName(String name);
}
