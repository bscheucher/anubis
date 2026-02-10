package com.ibosng.dbservice.dtos.changelog;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChangeLogDto {
    private String type;
    private String changedBy;
    private LocalDateTime changedAt;
    private List<FieldChangeDto> fieldChanges = new ArrayList<>();
}
