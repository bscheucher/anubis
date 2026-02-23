package com.ibosng.dbservice.dtos.zeiterfassung.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PruefungXlsxDto {
    private String pruefungsstufe;
    private String titelVor;
    private String vorname;
    private String familienname;
    private String titelNach;
    private String geschlecht;
    private String geburtsOrt;
    private String geburtsLand;
    private String nationalitaet;
    private String geburtsDatum;
    private String prueferInNr1Schriftlich;
    private String prueferInNr2Schriftlich;
    private String prueferInNr1Muendlich;
    private String prueferInNr2Muendlich;
    private String modulLesen;
    private String modulHoeren;
    private String modulSchreiben;
    private String modulSprechen;
    // Used for file naming purposes
    private String niveauAndSeminar;
}
