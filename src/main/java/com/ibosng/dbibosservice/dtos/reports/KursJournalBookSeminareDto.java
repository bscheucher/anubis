package com.ibosng.dbibosservice.dtos.reports;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class KursJournalBookSeminareDto {

    private String fuerdasProjekt;

    private String projektname;

    private String seminar;

    private String projektzeitraum;

    private String kostentraeger;

    private String trainer;

    private String fach;

    private BigDecimal gesamt_Seminar;

    private BigDecimal gesamt_Fach_EC;

    private Integer kalenderwoche;

    private Integer kurswoche;
}


