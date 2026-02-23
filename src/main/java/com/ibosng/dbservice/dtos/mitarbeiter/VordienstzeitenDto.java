package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class VordienstzeitenDto {

    private Integer id;

    private String personalnummer;

    private String vertragsart;

    private String firma;

    private String vordienstzeitenVon;

    private String vordienstzeitenBis;

    private String vWochenstunden;

    private boolean anrechenbar;

    private String nachweis;

    private String nachweisFilename;

    private double facheinschlaegig;

    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}
