package com.ibosng.dbservice.dtos.teilnehmer;

import lombok.Data;

@Data
public class SimpleTNDto {
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String svn;
}
