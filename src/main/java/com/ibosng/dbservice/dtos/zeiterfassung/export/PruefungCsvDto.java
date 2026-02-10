package com.ibosng.dbservice.dtos.zeiterfassung.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PruefungCsvDto {
    private String geschlecht;
    private String nachname;
    private String vorname;
    private String strasse;
    private String plz;
    private String ort;
    private String geburtsdatum;
    private String telefon;
    private String email;
    private String staat;
    private String herkunft;
    private String art;
    private String abrechnung;
    // Used for file naming purposes
    private String niveauAndSeminar;

}
