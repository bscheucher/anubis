package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.telefon.Telefon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface TelefonRepository extends JpaRepository<Telefon, Integer> {

    Optional<Telefon> findByTelefonnummer(Long telefonnummer);
}
