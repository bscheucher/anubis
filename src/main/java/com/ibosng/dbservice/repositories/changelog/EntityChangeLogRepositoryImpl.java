package com.ibosng.dbservice.repositories.changelog;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
@RequiredArgsConstructor
public class EntityChangeLogRepositoryImpl implements EntityChangeLogRepository {

    private final EntityManager entityManager;

    @Override
    public List<Object[]> findHistoryChangeLogs(String historyTable, String entityIdColumn, Integer entityId, LocalDateTime changesAfter) {

        String queryTemplate = """
                WITH history_comparison AS (
                    SELECT
                        h1.id AS current_id,
                        h2.id AS previous_id,
                        h1.action_timestamp AS changed_at,
                        h1.changed_by,
                        h1.%s AS entity_id,
                        curr.key AS field_name,
                        prev.key AS prev_field_name,
                        prev.value AS old_value,
                        curr.value AS new_value
                    FROM %s h1
                    LEFT JOIN %s h2
                        ON h1.%s = h2.%s
                        AND h1.action_timestamp > h2.action_timestamp
                        AND NOT EXISTS (
                            SELECT 1
                            FROM %s h3
                            WHERE h3.%s = h1.%s
                              AND h3.action_timestamp > h2.action_timestamp
                              AND h3.action_timestamp < h1.action_timestamp
                        )
                    CROSS JOIN LATERAL jsonb_each(to_jsonb(h1)) AS curr
                    CROSS JOIN LATERAL jsonb_each(to_jsonb(h2)) AS prev
                    WHERE h1.action = 'U'
                )
                SELECT
                    entity_id,
                    changed_by,
                    changed_at,
                    field_name,
                    old_value,
                    new_value
                FROM history_comparison
                WHERE field_name = prev_field_name
                  AND new_value IS DISTINCT FROM old_value
                  AND old_value IS NOT NULL AND old_value <> 'null' -- Exclude null values
                    AND field_name NOT IN ('id', 'status', 'changed_on', 'changed_by', 'action_timestamp', 'action', 'changed_on', 'lhr_kz', 'elda_code', 'land_code', 'is_in_eu_eea_ch', 'lhr_nr', 'abbreviation', 'nummer', 'lhr_gruppe', 'fuehrungskraft_ref', 'startcoach_ref') -- Exclude unwanted fields
                  AND changed_by IS NOT NULL
                  AND entity_id = :entityId
                  AND changed_at > :changesAfter
                ORDER BY entity_id, changed_at;
                """;

        String query = String.format(
                queryTemplate,
                entityIdColumn, // h1.%s
                historyTable,   // %s
                historyTable,   // %s
                entityIdColumn, // %s
                entityIdColumn, // %s
                historyTable,   // %s
                entityIdColumn, // %s
                entityIdColumn  // %s
        );

        return entityManager.createNativeQuery(query)
                .setParameter("entityId", entityId)
                .setParameter("changesAfter", changesAfter)
                .getResultList();
    }

    public List<Object[]> findChangeLogs(String tableName, Integer id1, Integer id2) {
        String queryTemplate = """
                WITH row_comparison AS (
                    SELECT
                        curr.key AS field_name,
                        prev.value AS old_value,
                        curr.value AS new_value
                    FROM (
                        SELECT * FROM %s WHERE id = :id1
                    ) r1
                    CROSS JOIN LATERAL jsonb_each(to_jsonb(r1)) AS curr(key, value)
                    CROSS JOIN (
                        SELECT * FROM %s WHERE id = :id2
                    ) r2
                    CROSS JOIN LATERAL jsonb_each(to_jsonb(r2)) AS prev(key, value)
                    WHERE curr.key = prev.key
                      AND curr.key NOT IN ('id','status', 'created_on', 'created_by', 'action_timestamp', 'action', 'changed_on', 'lhr_kz', 'elda_code', 'land_code', 'is_in_eu_eea_ch', 'lhr_nr', 'abbreviation', 'nummer', 'lhr_gruppe', 'fuehrungskraft_ref', 'startcoach_ref') -- Exclude unwanted fields)
                )
                SELECT
                    field_name,
                    old_value,
                    new_value
                FROM row_comparison
                WHERE old_value IS DISTINCT FROM new_value
                AND old_value IS NOT NULL AND old_value <> 'null' -- Exclude null values;
                """;

        String query = String.format(queryTemplate, tableName, tableName);

        return entityManager.createNativeQuery(query)
                .setParameter("id1", id1)
                .setParameter("id2", id2)
                .getResultList();

    }
}
