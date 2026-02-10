package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.AuftraggeberBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface AuftraggeberBankRepository extends JpaRepository<AuftraggeberBank, Integer> {

    Optional<AuftraggeberBank> findByFirmenbankName(String firmenbankName);
}
