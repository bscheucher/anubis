package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StammdatenDto {
    private int id;
    private String personalnummer;
    private String firma;
    private String anrede;
    private String titel;
    private String titel2;
    private String nachname;
    private String vorname;
    private String geburtsname;
    private String svnr;
    private String ecard;
    private String geschlecht;
    private String familienstand;
    private String geburtsDatum;
    private String alter;
    private String staatsbuergerschaft;
    private String muttersprache;
    private String strasse;
    private String land;
    private String plz;
    private String ort;
    private String aStrasse;
    private String aLand;
    private String aPlz;
    private String aOrt;
    private String email;
    private String mobilnummer;
    private Boolean handySignatur;
    private String bank;
    private String iban;
    private String bic;
    private String bankcard;
    private Boolean burgenland;
    private Boolean kaernten;
    private Boolean niederoesterreich;
    private Boolean oberoesterreich;
    private Boolean salzburg;
    private Boolean steiermark;
    private Boolean tirol;
    private Boolean vorarlberg;
    private Boolean wien;
    private String arbeitsgenehmigungDok;
    private String gueltigBis;
    private String arbeitsgenehmigung;
    private String foto;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}
