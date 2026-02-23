package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projekt2ManagerDto {
    private Integer projectId;
    private String bezeichnung;
    private String startDate;
    private String endDate;
}
