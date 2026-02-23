package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Anrede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface AnredeRepository extends JpaRepository<Anrede, Integer> {

    Anrede findByName(String name);
}
