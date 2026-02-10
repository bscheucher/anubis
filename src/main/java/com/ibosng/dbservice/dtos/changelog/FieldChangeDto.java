package com.ibosng.dbservice.dtos.changelog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldChangeDto {
    private String fieldName;
    private String oldValue;
    private String newValue;
}
