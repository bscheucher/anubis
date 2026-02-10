package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Verwandtschaft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface VerwandtschaftRepository extends JpaRepository<Verwandtschaft, Integer> {

    Verwandtschaft findByName(String name);
}
