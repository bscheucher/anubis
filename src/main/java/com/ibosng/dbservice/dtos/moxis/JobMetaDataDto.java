package com.ibosng.dbservice.dtos.moxis;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class JobMetaDataDto {

    private String description;
    private String category;
    private OffsetDateTime expirationDate;
    private String referenceId;
    private Map<String, String> customMap;
}
