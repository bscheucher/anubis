package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

@Data
public class VertretungsplanungMetaDataDto {
    private Integer mitarbeiterId;
    private String vorname;
    private String nachname;
    private String fromDate;
    private String toDate;
    private String reason;
}
