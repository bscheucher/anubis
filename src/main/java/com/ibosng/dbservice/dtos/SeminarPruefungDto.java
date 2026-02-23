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
public class SeminarPruefungDto {
    private Integer id;
    private String bezeichnung;
    private String pruefungArt;
    private String gegenstand;
    private String niveau;
    private String institut;
    private String begruendung;
    private Boolean antritt;
    private String ergebnis;
    private String ergebnisInProzent;
    private String pruefungAm;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}

