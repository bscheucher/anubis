package com.ibosng.dbibosservice.dtos.reports;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Data
@Builder
public class KursJournalDto {

    private String fuerDasProjekt;

    private String projektname;

    private String projektzeitraum;

    private String kostentraeger;

    private String trainer;

    private String fach;

    private BigDecimal gesamtSeminar;

    private BigDecimal gesamtEcFach;

    private String stundentyp;

    private Date datum;

    private Time uhrzeitVon;

    private Time uhrzeitBis;

    private Time pauseVon;

    private Time pauseBis;

    private String inhalt;

    private String bemerkung;

    private BigDecimal dauer;

    private Long dauerPause;

    private Integer kalenderwoche;

    private Integer wochentag;
}
