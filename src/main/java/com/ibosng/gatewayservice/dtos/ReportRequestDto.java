package com.ibosng.gatewayservice.dtos;

import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {

    String personalnummer;
    String gueltigAb;
    String gueltigBis;
    Integer reportId;
    String reportName;
    String outputFormat;
    String createdBy;
    Boolean isPreview;
    List<ReportParameterDto> reportParameters;
}
