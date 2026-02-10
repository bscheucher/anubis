package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.EmailTemplate;
import com.ibosng.dbservice.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {

    List<EmailTemplate> findAllByIdentifierAndLanguage(String identifier, Language language);
}
