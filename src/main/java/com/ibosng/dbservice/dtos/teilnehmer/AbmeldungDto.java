package com.ibosng.dbservice.dtos.teilnehmer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AbmeldungDto {

    private Integer id;
    private Integer teilnehmerId;

    private String austrittsDatum;

    private String austrittsgrund;
    private String bemerkung;

    private String nachname;
    private String vorname;
    private String svNummer;
    private String kostenstelle;
    private String status;
}
