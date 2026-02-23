package com.ibosng.dbservice.dtos.zeiterfassung.umbuchung;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@JsonInclude()
@NoArgsConstructor
@AllArgsConstructor
public class UmbuchungMetadataDto {
    private String date;
    private Boolean isEligible;
    private String reason;
}
