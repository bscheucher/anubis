package com.ibosng.dbservice.services.changelog;

import com.ibosng.dbservice.dtos.changelog.ChangeLogDto;
import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EntityChangeLogService {

    List<ChangeLogDto> getHistoryChangeLogs(String historyTable, String entityIdColumn, Integer entityId, LocalDateTime changesAfter);

    List<FieldChangeDto> getChangeLogs(String tableName, Integer newId, Integer oldId);
}
