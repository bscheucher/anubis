package com.ibosng.dbservice.repositories.ai;

import com.ibosng.dbservice.entities.ai.OpenaiAssistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface OpenaiAssistantRepository extends JpaRepository<OpenaiAssistant, Integer> {
    OpenaiAssistant findByAssistantId(String assistantId);

    OpenaiAssistant findByAssistantName(String assistantName);
}
