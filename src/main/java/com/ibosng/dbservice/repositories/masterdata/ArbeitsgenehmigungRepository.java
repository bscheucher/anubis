package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Arbeitsgenehmigung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface ArbeitsgenehmigungRepository extends JpaRepository<Arbeitsgenehmigung, Integer> {

    Arbeitsgenehmigung findByName(String name);
}
