package com.ibosng.dbservice.dtos.teilnahme;

import lombok.Data;

@Data
public class TeilnehmerTeilnahmeOverviewDto {
    private Integer id;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String status;
    private Double kursAnwesendPercent; //todo will be implemented later
    private String info;
}
