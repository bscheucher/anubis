package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    Integer id;
    String reportName;
    String sourcePath;
    String mainReportFile;
    String dataSource;
    private List<ReportParameterDto> reportParameters = new ArrayList<>();

}
