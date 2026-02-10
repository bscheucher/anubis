package com.ibosng.dbservice.dtos.teilnahme;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TeilnahmerTeilnahmeCreationDto {
    @JsonProperty("id")
    private int teilnahmerId;
    private String status;
    private String info;
}
