package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.BankDaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface BankDatenRepository extends JpaRepository<BankDaten, Integer> {

}
