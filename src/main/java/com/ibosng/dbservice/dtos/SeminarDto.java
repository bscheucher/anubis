package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarDto {
    private int id;
    private int projektId;
    private String projektName;
    private String buchungsstatus;
    private String anmerkung;
    private String geplant;
    private String eintritt;
    private String austritt;
    private String massnahmennummer;
    private String veranstaltungsnummer;
    private String zubuchung;
    private String rgs;
    private String rgsBezeichnung;
    private String betreuerTitel;
    private String betreuerVorname;
    private String betreuerNachname;
    private String seminarBezeichnung;
    private Integer seminarNumber;
    private String kostentraegerDisplayName;
    private String standort;
    private String schieneUhrzeit;
    private String austrittsgrund;
    private String begehrenBis;
    private String zusaetzlicheUnterstuetzung;
    private String lernfortschritt;
    private String fruehwarnung;
    private String anteilAnwesenheit;
    private List<TrainerDto> trainer;
    private List<SeminarTeilnehmerAbwesenheitDto> abwesenheiten;
    private List<SeminarTeilnehmerAnwesenheitDto> anwesenheiten;
    private String gesamtbeurteilungTyp;
    private String gesamtbeurteilungErgebnis;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
    private String kursDatumVon;
    private String kursDatumBis;
}
