package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import lombok.Data;

@Data
public class ZeitspeicherDataDto {

    Integer zeitspeicherNummer;
    String name;
    String abbreviation;
    String comment;
}
