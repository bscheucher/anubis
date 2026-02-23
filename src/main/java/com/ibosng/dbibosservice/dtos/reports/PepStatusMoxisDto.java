package com.ibosng.dbibosservice.dtos.reports;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PepStatusMoxisDto {

    private Long pepMaId;

    private String maName;

    private String maMail;

    private String pepStatus;

    private LocalDateTime abgeschlossenAm;

    private String abgeschlossenDurch;

    private Integer jahr;

    private Integer monat;

    private BigDecimal soll;

    private BigDecimal pmIst;

    private Integer urlaubKonsumiert;

    private BigDecimal zaStunden;

    private String kst;

    private LocalDateTime anMoxisVersendet;

    private Long moxisAuftragNr;

    private Integer aktuelleBeiId;

    private String aktuellBeiName;

    private String aktuellBeiMail;

    private String aktuellePosition;

    private String moxisStatus;

    private LocalDateTime letztesUpdateAm;

    private LocalDateTime abgeschlossenAmMoxis;

    private LocalDateTime abgelaufenAm;
}
