package com.ibosng.gatewayservice.dtos.response;

import com.ibosng.gatewayservice.enums.ReportOutputFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private byte[] reportBytes;
    private String reportName;
    private ReportOutputFormat outputFormat;
}
