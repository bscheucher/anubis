package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class ZeitspeicherWertDto {
    private int zeitspeicherNummer;
    private String zeitspeicherName;
    private int value;
    private String unit;
}
