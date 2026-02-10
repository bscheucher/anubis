package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

import java.util.List;

@Data
public class VertretungsplanungDataDto {
    private VertretungsplanungMetaDataDto vertretungsplanungMetaData;
    private List<AISeminarDto> vertretungsplanungTable;
}