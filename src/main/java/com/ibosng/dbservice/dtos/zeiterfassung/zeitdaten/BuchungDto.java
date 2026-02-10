package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class BuchungDto {
    private String effectiveTime;
    private String originalTime;
    private int zeitspeicherNummer;
    private String zeitspeicherName;
    private boolean zeitende;
}
