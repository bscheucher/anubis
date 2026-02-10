package com.ibosng.dbservice.repositories.ai;


import com.ibosng.dbservice.entities.ai.OpenaiAssistantAnweisung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional("postgresTransactionManager")
public interface OpenaiAssistantAnweisungRepository extends JpaRepository<OpenaiAssistantAnweisung, Integer> {

    @Query("SELECT MAX(o.version) FROM OpenaiAssistantAnweisung o WHERE o.openaiAssistantId.assistantName = :assistantName")
    Integer findMaxVersionByOpenaiAssistantId_AssistantName(String assistantName);

    OpenaiAssistantAnweisung findByOpenaiAssistantId_AssistantNameAndVersion(String assistantName, int version);

}
