package com.ibosng.dbservice.services.changelog.impl;

import com.ibosng.dbservice.dtos.changelog.ChangeLogDto;
import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.repositories.changelog.EntityChangeLogRepository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.changelog.EntityChangeLogService;
import com.ibosng.dbservice.utils.FieldNameMapper;
import com.ibosng.dbservice.utils.ForeignKeyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EntityChangeLogServiceImpl implements EntityChangeLogService {

    private static final List<String> benutzerFieldNames = Arrays.asList("fuehrungskraft", "startcoach");
    private final EntityChangeLogRepository entityChangeLogRepository;
    private final BenutzerService benutzerService;

    @Override
    public List<ChangeLogDto> getHistoryChangeLogs(String historyTable, String entityIdColumn, Integer entityId, LocalDateTime changesAfter) {
        // Fetch raw history log data
        List<Object[]> rows = entityChangeLogRepository.findHistoryChangeLogs(historyTable, entityIdColumn, entityId, changesAfter);

        // Initialize the list to hold all change logs
        List<ChangeLogDto> changeLogs = new ArrayList<>();

        // Process each row in the result
        for (Object[] row : rows) {
            //Integer entityIdDto = (Integer) row[0];
            String changedBy = (String) row[1];
            LocalDateTime changedAt = ((Timestamp) row[2]).toLocalDateTime();
            String fieldName = (String) row[3];
            String oldValue = String.valueOf(row[4]).replace("\"", "");
            String newValue = String.valueOf(row[5]).replace("\"", "");

            // Create a new ChangeLogDto or find an existing one for grouping
            ChangeLogDto changeLog = findOrCreateChangeLog(changeLogs, changedBy, changedAt, historyTable);

            // Detect if the field is a foreign key
            String referencedTable = ForeignKeyResolver.getReferencedTable(fieldName);
            if (referencedTable != null) {
                try {
                    // Attempt to parse old and new values as numeric IDs
                    Integer oldId = oldValue.matches("\\d+") ? Integer.parseInt(oldValue) : null;
                    Integer newId = newValue.matches("\\d+") ? Integer.parseInt(newValue) : null;

                    if (oldId != null && newId != null) {
                        // Recursively fetch changes for the foreign key values
                        List<FieldChangeDto> foreignKeyChanges = new ArrayList<>();
                        if (benutzerFieldNames.contains(fieldName)) {
                            List<FieldChangeDto> benutzerFieldChanges = getChangeLogs(referencedTable, newId, oldId);
                            foreignKeyChanges.add(manageBenutzerChanges(benutzerFieldChanges, fieldName));
                        } else {
                            foreignKeyChanges = getChangeLogs(referencedTable, newId, oldId);
                            // Prefix the fieldName from the main table to the foreign key changes
                        }

                        changeLog.getFieldChanges().addAll(foreignKeyChanges);
                    }
                } catch (NumberFormatException e) {
                    log.error("Invalid foreign key value for field: {}", fieldName);
                }
            } else {
                // Add the field change if it's not a foreign key
                FieldChangeDto fieldChange = new FieldChangeDto(fieldName, oldValue, newValue);
                changeLog.getFieldChanges().add(fieldChange);
            }
        }

        return changeLogs.stream()
                .filter(changeLog -> !changeLog.getFieldChanges().isEmpty())
                .toList();
    }

    private FieldChangeDto manageBenutzerChanges(List<FieldChangeDto> benutzerChanges, String fieldName) {
        Map<String, FieldChangeDto> changeMap = benutzerChanges.stream()
                .collect(Collectors.toMap(FieldChangeDto::getFieldName, Function.identity()));

        FieldChangeDto fieldChangeDto = new FieldChangeDto();
        fieldChangeDto.setFieldName(fieldName);

        String emailOld = Optional.ofNullable(changeMap.get("email"))
                .map(FieldChangeDto::getOldValue)
                .orElse("");
        String emailNew = Optional.ofNullable(changeMap.get("email"))
                .map(FieldChangeDto::getNewValue)
                .orElse("");

        Benutzer benutzerOld = benutzerService.findByEmail(emailOld);

        Benutzer benutzerNew = benutzerService.findByEmail(emailNew);

        fieldChangeDto.setOldValue(benutzerOld.getFirstName() + " " + benutzerOld.getLastName());
        fieldChangeDto.setNewValue(benutzerNew.getFirstName() + " " + benutzerNew.getLastName());

        return fieldChangeDto;
    }

    private ChangeLogDto findOrCreateChangeLog(List<ChangeLogDto> changeLogs, String changedBy, LocalDateTime changedAt, String type) {
        // Check if there's already a ChangeLogDto with the same changedBy and changedAt
        for (ChangeLogDto log : changeLogs) {
            if (log.getChangedBy().equals(changedBy) && log.getChangedAt().equals(changedAt)) {
                return log;
            }
        }
        Benutzer benutzer = benutzerService.findByEmail(changedBy);

        // Create a new ChangeLogDto if none exists
        ChangeLogDto newLog = new ChangeLogDto();
        if (benutzer != null) {
            newLog.setChangedBy(benutzer.getFirstName() + " " + benutzer.getLastName());
        } else {
            newLog.setChangedBy(changedBy);
        }
        newLog.setChangedAt(changedAt);
        //formatting name of the table and assigning it to a type so FE can see in the payload what kind of data they
        // are getting. (at this state it is either stammdaten or vertragsdaten)
        newLog.setType(type.replace("_history", ""));
        changeLogs.add(newLog);
        return newLog;
    }

    @Override
    public List<FieldChangeDto> getChangeLogs(String tableName, Integer id1, Integer id2) {
        List<Object[]> results = entityChangeLogRepository.findChangeLogs(tableName, id1, id2);

        List<FieldChangeDto> changes = new ArrayList<>();
        for (Object[] row : results) {
            String fieldName = (String) row[0];
            String oldValue = String.valueOf(row[1]).replace("\"", "");
            String newValue = String.valueOf(row[2]).replace("\"", "");

            // Check if the field is a foreign key
            String referencedTable = ForeignKeyResolver.getReferencedTable(fieldName);
            if (referencedTable != null) {
                try {
                    // Attempt to parse old and new values as numeric IDs
                    Integer oldId = oldValue.matches("\\d+") ? Integer.parseInt(oldValue) : null;
                    Integer newId = newValue.matches("\\d+") ? Integer.parseInt(newValue) : null;

                    if (oldId != null && newId != null) {
                        List<FieldChangeDto> foreignKeyChanges = new ArrayList<>();
                        if (benutzerFieldNames.contains(fieldName)) {
                            List<FieldChangeDto> benutzerFieldChanges = getChangeLogs(referencedTable, newId, oldId);
                            foreignKeyChanges.add(manageBenutzerChanges(benutzerFieldChanges, fieldName));
                        } else {
                            // Recursively fetch changes for the foreign key values
                            foreignKeyChanges = getChangeLogs(referencedTable, newId, oldId);
                            // Prefix the fieldName from the main table to the foreign key changes
                            for (FieldChangeDto foreignKeyChange : foreignKeyChanges) {
                                foreignKeyChange.setFieldName(getCorrectFieldName(foreignKeyChange.getFieldName(), referencedTable));
                            }
                        }
                        changes.addAll(foreignKeyChanges);
                    }
                } catch (NumberFormatException e) {
                    // Log and skip invalid foreign key values
                    log.error("Invalid foreign key value for field: {}", fieldName);
                }
            } else {
                // Add the change if it's not a foreign key
                changes.add(new FieldChangeDto(getCorrectFieldName(fieldName, tableName), oldValue, newValue));
            }
        }
        return changes;
    }

    private String getCorrectFieldName(String fieldName, String tableName) {
        if ("name".equals(fieldName)) {
            return FieldNameMapper.getDtoFieldName(tableName + "." + fieldName);
        } else {
            return FieldNameMapper.getDtoFieldName(fieldName);
        }
    }
}
