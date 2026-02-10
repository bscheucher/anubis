package com.ibosng.dbservice.repositories.changelog;

import java.time.LocalDateTime;
import java.util.List;

public interface EntityChangeLogRepository {
    List<Object[]> findHistoryChangeLogs(String historyTable, String entityIdColumn, Integer entityId, LocalDateTime changesAfter);

    List<Object[]> findChangeLogs(String tableName, Integer id1, Integer id2);
}