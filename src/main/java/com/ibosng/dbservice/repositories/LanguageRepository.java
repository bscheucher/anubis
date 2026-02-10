package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    Optional<Language> findByName(String name);
}
