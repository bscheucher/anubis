package com.ibosng.dbservice.dtos;


import com.ibosng.dbservice.entities.reports.ReportParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VereinbarungParameterDto {
    private Integer id;
    private Integer vereinbarungId;
    private String name;
    private String value;
    private ReportParamType type;
}
