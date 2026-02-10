package com.ibosng.dbservice.dtos;

import com.ibosng.dbservice.entities.reports.ReportParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportParameterDto {

    private String name;
    private ReportParamType type;
    private String description;
    private String label;
    private Boolean required;
    private String value;
}
