package com.ibosng.dbservice.dtos;

import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ZeitbuchungenDto implements Serializable {
    private Integer jahr;
    private Integer monat;
    private String leistungstyp;
    private String leistungsdatum;
    private String von;
    private String bis;
    private String pauseVon;
    private String pauseBis;
    private Double dauerStd;
    private String taetigkeit;
    private ZeitbuchungenType anAbwesenheit;
    private String leistungsort;
    private String personalnummer;
    private Integer bmdClient;
    private BasicSeminarDto seminar;
    private Integer kostenstellenummer;
    private String kostenstelle;
    private String kostentraeger;
    private boolean hasError;
}
