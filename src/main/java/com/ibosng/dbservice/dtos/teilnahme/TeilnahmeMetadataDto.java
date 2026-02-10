package com.ibosng.dbservice.dtos.teilnahme;

import com.ibosng.dbservice.entities.Status;
import lombok.Data;

@Data
public class TeilnahmeMetadataDto {
    private Integer seminarId;
    private String bezeichnung;
    private String projekt;
    private Status status;
    private String changedOn;
    private String changedBy;
}
